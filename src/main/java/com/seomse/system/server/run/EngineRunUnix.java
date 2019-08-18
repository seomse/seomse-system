package com.seomse.system.server.run;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.config.ConfigProperty;
import com.seomse.system.engine.Engine;
import com.seomse.system.server.Server;
import com.seomse.system.server.api.ServerApiMessageType;
import com.seomse.system.server.vo.EngineRunVo;
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
 *  작 성 일 : 2017.10
 *  버    전 : 1.1
 *  수정이력 :  2019.05
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017, 2019 by ㈜섬세한사람들. All right reserved.
 */
public class EngineRunUnix implements EngineRun{
	
	private final static Logger logger = LoggerFactory.getLogger(EngineRunUnix.class);

	private boolean isMessageComplete = false;
	
	@Override
	public String start(String engineId) {
		
		Server server = Server.getInstance();
		final EngineRunVo engineRunVo = JdbcNaming.getObj(EngineRunVo.class , "ENGINE_ID ='" + engineId +"' AND IS_DELETED='N' AND SERVER_ID='"
		+ server.getServerId() + "'" );
		if(engineRunVo == null){
			return "FAIL : ENGINE -> ENGINE_ID Check: " + engineId;
		}

		String logbackXmlPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM ENGINE_CONFIG WHERE ENGINE_ID='" + engineId +"' AND CONFIG_KEY='" + ConfigProperty.PROP_LOGBACK_XML_PATH + "' AND IS_DELETED='N'");

		String pathValue = engineRunVo.getCONFIG_FILE_PATH() + Engine.PATH_SPLIT + logbackXmlPath;

		List<String> commandList =  new ArrayList<>();
		commandList.add("sh");
		commandList.add(engineRunVo.getEXE_FILE_PATH() );
		commandList.add("start");
		commandList.add(engineId);
		commandList.add(pathValue);
		commandList.add(Integer.toString(engineRunVo.getMEMORY_MB_MIN_VALUE()));
		commandList.add(Integer.toString(engineRunVo.getMEMORY_MB_MAX_VALUE()));
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
				return ServerApiMessageType.SUCCESS;
			}else{
				return "FAIL : " + message;
			}
			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return "FAIL : " + e.getMessage();
		}
	}

}
