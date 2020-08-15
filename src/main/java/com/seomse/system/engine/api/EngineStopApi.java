package com.seomse.system.engine.api;

import com.seomse.api.ApiMessage;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.system.commons.SystemMessageType;
import com.seomse.system.engine.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *  파 일 명 : EngineStopApi.java
 *  설    명 : 엔진 종료
 *

 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 ㈜섬세한사람들. All right reserved.
 */
public class EngineStopApi extends ApiMessage {
	
	private static final Logger logger = LoggerFactory.getLogger(EngineStopApi.class);
	
	@Override
	public void receive(String message) {
		try{
			Engine engine = Engine.getInstance();
			engine.updateEndTime();

			sendMessage(SystemMessageType.SUCCESS);
			System.exit(-1);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			sendMessage(SystemMessageType.FAIL + ExceptionUtil.getStackTrace(e));
		}
	}

}