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

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  파 일 명 : EngineRunWindows.java
 *  설    명 : windows계열용 엔진실행
 *  작 성 자 : macle
 *  작 성 일 : 2018.03
 *  버    전 : 1.1
 *  수정이력 :  2019.05
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 ~ 2019by ㈜섬세한사람들. All right reserved.
 */
public class EngineRunWindows implements EngineRun{

	private final static Logger logger = LoggerFactory.getLogger(EngineRunWindows.class);
	
	@Override
	public String start(String engineId) {
		
		Server server = Server.getInstance();
		final EngineRunVo engineRunVo = JdbcNaming.getObj(EngineRunVo.class , "ENGINE_ID ='" + engineId +"' AND IS_DELETED ='N' AND SERVER_ID ='"
		+ server.getServerId() + "'" );
		if(engineRunVo == null){
			return "FAIL : ENGINES -> ENGINE_CODE Check: " + engineId;
		}

		String logbackXmlPath = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM ENGINE_CONFIG WHERE ENGINE_ID='" + engineId +"' AND CONFIG_KEY='" + ConfigProperty.PROP_LOGBACK_XML_PATH + "' AND IS_DELETED='N'");

		String pathValue;
		if(logbackXmlPath == null){
			pathValue = engineRunVo.getCONFIG_FILE_PATH();
		}else{
			pathValue = engineRunVo.getCONFIG_FILE_PATH() + Engine.PATH_SPLIT + logbackXmlPath;
		}


		List<String> commandList =  new ArrayList<>();
		commandList.add("cmd");
		commandList.add("/c");


		commandList.add(engineRunVo.getEXE_FILE_PATH() );
		commandList.add(engineId);
		commandList.add(pathValue);
		commandList.add(Integer.toString(engineRunVo.getMEMORY_MB_MIN_VALUE()));
		commandList.add(Integer.toString(engineRunVo.getMEMORY_MB_MAX_VALUE()));
		try{
			ProcessBuilder builder = new ProcessBuilder(commandList);
			builder.redirectError(Redirect.INHERIT);
			builder.redirectOutput(Redirect.INHERIT);
			builder.start();

			return ServerApiMessageType.SUCCESS;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return "FAIL : " + e.getMessage();
		}
	}

}
