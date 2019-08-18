package com.seomse.system.server.api;

import com.seomse.api.ApiMessage;

/**
 * <pre>
 *  파 일 명 : PingTestApi.java
 *  설    명 : 서버가 살아있는지 테스트한다.
 서버 응답테스트
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ㈜섬세한사람들. All right reserved.
 */
public class PingTestApi extends ApiMessage {

	@Override
	public void receive(String message) {
	
		try{
			sendMessage(ServerApiMessageType.SUCCESS);
		}catch(Exception e){
			sendMessage(ServerApiMessageType.FAIL);
		}
	
	}
	
}