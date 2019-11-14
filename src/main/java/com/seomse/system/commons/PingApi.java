package com.seomse.system.commons;

import com.seomse.api.ApiMessage;
import com.seomse.api.ApiRequest;

/**
 * <pre>
 *  파 일 명 : PingApi.java
 *  설    명 : 응답체크
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 ㈜섬세한사람들. All right reserved.
 */
public class PingApi extends ApiMessage {

	@Override
	public void receive(String message) {
	
		try{
			sendMessage(SystemMessageType.SUCCESS);
		}catch(Exception e){
			sendMessage(SystemMessageType.FAIL);
		}
	
	}

	/**
	 * ping
	 * @param hostAddress host address
	 * @param port port
	 * @return is ping success
	 */
	public static boolean ping(String hostAddress, int port){
		ApiRequest request = new ApiRequest(hostAddress, port);
		request.setNotLog();
		//최대 5초대기
		request.setConnectTimeOut(5000);
		request.setPackageName("com.seomse.system.commons");
		request.setConnectErrorLog(false);
		request.connect();
		String result = request.sendToReceiveMessage("PingApi", "");
		request.disConnect();
		return result.startsWith(SystemMessageType.SUCCESS);
	}
	
}