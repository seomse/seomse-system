package com.seomse.system.server.run;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.commons.SystemMessageType;
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
 * <pre>
 *  파 일 명 : EngineRunUnix.java
 *  설    명 : unix계열용 엔진실행
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
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
			return SystemMessageType.FAIL + " ENGINE_ID Check: " + engineId;
		}



		List<String> commandList =  new ArrayList<>();
		commandList.add("sh");
		commandList.add(engineRunDno.getEXE_FILE_PATH() );
		commandList.add("start");
		commandList.add(engineId);
		String configPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM TB_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId + "' AND CONFIG_KEY='" + EngineRun.CONFIG_PATH_KEY +"' AND DEL_FG='N'");
		if(configPath == null){
			configPath = "../config/seomse_config.xml";
		}
		commandList.add(configPath);
		commandList.add(Integer.toString(engineRunDno.getMIN_MEMORY_MB()));
		commandList.add(Integer.toString(engineRunDno.getMAX_MEMORY_MB()));

		String logbackXmlPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM TB_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId + "' AND CONFIG_KEY='logback.xml.path' AND DEL_FG='N'");
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
				Thread.sleep(200);
			}
			
			String message = messageBuilder.toString();

			messageBuilder.setLength(0);
			if(message.contains("engine start")){
				return SystemMessageType.SUCCESS;
			}else{
				return SystemMessageType.FAIL + " "+ message;
			}
			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return SystemMessageType.FAIL + "\n" + ExceptionUtil.getStackTrace(e);
		}
	}

}
