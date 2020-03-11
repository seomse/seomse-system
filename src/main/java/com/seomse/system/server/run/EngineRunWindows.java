package com.seomse.system.server.run;

import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.commons.SystemMessageType;
import com.seomse.system.server.Server;
import com.seomse.system.server.dno.EngineRunDno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  파 일 명 : EngineRunWindows.java
 *  설    명 : windows계열용 엔진실행
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019by ㈜섬세한사람들. All right reserved.
 */
public class EngineRunWindows implements EngineRun{

	private final static Logger logger = LoggerFactory.getLogger(EngineRunWindows.class);
	
	@Override
	public String start(String engineId) {
		
		Server server = Server.getInstance();
		final EngineRunDno engineRunDno = JdbcNaming.getObj(EngineRunDno.class , "ENGINE_ID ='" + engineId +"' AND DEL_FG='N' AND SERVER_ID='"
				+ server.getServerId() + "'" );

		if(engineRunDno == null){
			return SystemMessageType.FAIL + " ENGINE_ID Check: " + engineId;
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

			return SystemMessageType.SUCCESS;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return  SystemMessageType.FAIL + "\n" + ExceptionUtil.getStackTrace(e);
		}
	}

}
