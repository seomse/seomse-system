package com.seomse.system.server.api;

import com.seomse.api.ApiMessage;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.system.engine.Engine;

/**
 * <pre>
 *  파 일 명 : EngeineRunCheckApi.java
 *  설    명 : 엔진이 동작하고 있나 체크

 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ㈜섬세한사람들. All right reserved.
 */
public class EngeineRunCheckApi extends ApiMessage {

	@Override
	public void receive(String message) {
	
		try{
			
			Engine engine = Engine.getInstance();
			if(engine == null){
				communication.sendMessage(ServerApiMessageType.FAIL +" engine null");
				return;
			}
			
			if(engine.getId().equals(message)){
				sendMessage(ServerApiMessageType.SUCCESS);
			}else{
				sendMessage(ServerApiMessageType.FAIL +" engine code  difference , receive code: " + message +" ready code: " + engine.getId());
			}

		}catch(Exception e){
			sendMessage(ServerApiMessageType.FAIL + ExceptionUtil.getStackTrace(e));
		}
	
	}
	
}