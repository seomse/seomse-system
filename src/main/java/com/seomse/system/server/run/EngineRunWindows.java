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

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

/**
 * windows 계열용 엔진실행
 * @author macle
 */
public class EngineRunWindows implements EngineRun{

	private final static Logger logger = LoggerFactory.getLogger(EngineRunWindows.class);
	
	@Override
	public String start(String engineId) {
		
		Server server = Server.getInstance();
		final EngineRunDno engineRunDno = JdbcNaming.getObj(EngineRunDno.class , "ENGINE_ID ='" + engineId +"' AND DEL_FG='N' AND SERVER_ID='"
				+ server.getServerId() + "'" );

		if(engineRunDno == null){
			return Messages.FAIL + " ENGINE_ID Check: " + engineId;
		}


		List<String> commandList =  new ArrayList<>();
		commandList.add("cmd");
		commandList.add("/c");
		commandList.add(engineRunDno.getEXE_FILE_PATH() );
		commandList.add(engineId);

		String configPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId + "' AND CONFIG_KEY='" + EngineRun.CONFIG_PATH_KEY +"' AND DEL_FG='N'");
		if(configPath == null){
			configPath = "..\\config\\seomse_config.xml";
		}

		commandList.add(configPath);
		commandList.add(Integer.toString(engineRunDno.getMIN_MEMORY_MB()));
		commandList.add(Integer.toString(engineRunDno.getMAX_MEMORY_MB()));

		String logbackXmlPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId + "' AND CONFIG_KEY='" + EngineRun.LOG_CONFIG_PATH_KEY +"' AND DEL_FG='N'");
		if(logbackXmlPath != null){
			commandList.add(logbackXmlPath);
		}

		try{
			ProcessBuilder builder = new ProcessBuilder(commandList);
			builder.redirectError(Redirect.INHERIT);
			builder.redirectOutput(Redirect.INHERIT);
			builder.start();

			return Messages.SUCCESS;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return  Messages.FAIL + "\n" + ExceptionUtil.getStackTrace(e);
		}
	}

}
