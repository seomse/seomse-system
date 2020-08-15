package com.seomse.system.server.api;

import com.seomse.api.ApiMessage;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.system.commons.SystemMessageType;
import com.seomse.system.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *  파 일 명 : ServerStopApi.java
 *  설    명 : 서버종료
 *

 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 ㈜섬세한사람들. All right reserved.
 */
public class ServerStopApi extends ApiMessage {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerStopApi.class);
	
	@Override
	public void receive(String message) {
		try{
			Server server = Server.getInstance();
			server.updateEndTime();

			sendMessage(SystemMessageType.SUCCESS);
			System.exit(-1);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			sendMessage(SystemMessageType.FAIL + ExceptionUtil.getStackTrace(e));
		}
	}

}