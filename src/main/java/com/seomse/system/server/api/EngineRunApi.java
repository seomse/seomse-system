package com.seomse.system.server.api;

import com.seomse.api.ApiMessage;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.system.server.ServerProcessor;
import com.seomse.system.server.run.EngineRun;
import com.seomse.system.server.run.EngineRunFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *  파 일 명 : EngineRunApi.java
 *  설    명 :  엔진시작
 *             엔진ID 전송받기
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ㈜섬세한사람들. All right reserved.
 */
public class EngineRunApi extends ApiMessage {

	private static final Logger logger = LoggerFactory.getLogger(EngineRunApi.class);
	
	@Override
	public void receive(String message) {

		try{
			ServerProcessor serverProcessor = ServerProcessor.getInstance();
			EngineRun engineRun = EngineRunFactory.newEngineRun(serverProcessor.getOsType());
			sendMessage(ServerApiMessageType.SUCCESS + engineRun.start(message));
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			sendMessage(ServerApiMessageType.FAIL + ExceptionUtil.getStackTrace(e));
		}
		
	}

}
