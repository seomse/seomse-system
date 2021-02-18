/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.system.server.run;

import com.seomse.api.Messages;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.server.Server;
import com.seomse.system.server.dno.EngineRunDno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

/**
 * unix계열용 엔진실행
 * @author macle
 */
public class EngineRunUnix implements EngineRun{
	
	private final static Logger logger = LoggerFactory.getLogger(EngineRunUnix.class);

	private boolean isMessageComplete = false;
	
	@Override
	public String start(String engineId) {
		
		Server server = Server.getInstance();
		final EngineRunDno engineRunDno = JdbcNaming.getObj(EngineRunDno.class , "ENGINE_ID ='" + engineId +"' AND DEL_FG='N' AND SERVER_ID='"
		+ server.getServerId() + "'" );
		if(engineRunDno == null){
			return Messages.FAIL + " ENGINE_ID Check: " + engineId;
		}



		List<String> commandList =  new ArrayList<>();
		commandList.add("sh");
		commandList.add(engineRunDno.getEXE_FILE_PATH() );
		commandList.add("start");
		commandList.add(engineId);
		String configPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId + "' AND CONFIG_KEY='" + EngineRun.CONFIG_PATH_KEY +"' AND DEL_FG='N'");
		if(configPath == null){
			configPath = "../config/seomse_config.xml";
		}
		commandList.add(configPath);
		commandList.add(Integer.toString(engineRunDno.getMIN_MEMORY_MB()));
		commandList.add(Integer.toString(engineRunDno.getMAX_MEMORY_MB()));

		String logbackXmlPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId + "' AND CONFIG_KEY='logback.xml.path' AND DEL_FG='N'");
		if(logbackXmlPath != null){
			commandList.add(logbackXmlPath);
		}

		try{
			final StringBuilder messageBuilder = new StringBuilder();
			ProcessBuilder builder = new ProcessBuilder(commandList);
			builder.redirectError(Redirect.INHERIT);
			final Process process = builder.start();
			new Thread(() -> {
				try (BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) )) {
					String line;

					while ((line = reader.readLine()) != null) {
						messageBuilder.append(line);
					}

				}catch(Exception e){
					logger.error(ExceptionUtil.getStackTrace(e));
				}

				isMessageComplete = true;
			}).start();
	
			synchronized (process) {
				process.wait();
				process.destroy();
			}

			while (!isMessageComplete) {
				//noinspection BusyWait
				Thread.sleep(200);
			}
			
			String message = messageBuilder.toString();

			messageBuilder.setLength(0);
			if(message.contains("engine start")){
				return Messages.SUCCESS;
			}else{
				return Messages.FAIL + " "+ message;
			}
			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return Messages.FAIL + "\n" + ExceptionUtil.getStackTrace(e);
		}
	}

}
