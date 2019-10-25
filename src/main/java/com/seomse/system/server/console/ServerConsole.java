package com.seomse.system.server.console;

import com.seomse.api.ApiRequest;
import com.seomse.jdbc.objects.JdbcObjects;
import com.seomse.system.server.api.ServerApiMessageType;

/**
 * <pre>
 *  파 일 명 : ServerConsole.java
 *  설    명 : 서버콘솔 이벤트 관련 (서버가아닌 콘솔에서 클라이언트 이벤트용)
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class ServerConsole {
	
	/**
	 * ping
	 * @param serverId 서버아이디
	 * @return is ping success
	 */
	public static boolean ping(String serverId){

		ServerConnect serverConnect = JdbcObjects.getObj(ServerConnect.class, "SERVER_ID='" + serverId +"' AND DEL_FG='N'");
		//최대 5초대기
		if(serverConnect == null){
			return false;
		}

		ApiRequest request = new ApiRequest(serverConnect.hostAddress, serverConnect.port);
		request.setConnectTimeOut(5000);
		request.setConnectErrorLog(true);
		String result = request.sendToReceiveMessage("ServerPingApi", null);
		request.disConnect();

		return result.startsWith(ServerApiMessageType.SUCCESS);

   }

	
	/**
	 * 서버종료
	 * @param serverId serverId
	 * @return receive message
	 */
	public static String serverStop(String serverId){
		return sendToReceiveMessage(serverId, "EngineStopApi", null);
	}
	
	/**
	 * 메시지 전송후 결과얻기
	 * @param serverId server id
	 * @param code api code
	 * @param message send message
	 * @return receive message
	 */
	public static String sendToReceiveMessage(String serverId, String code, String message){
		return sendToReceiveMessage(serverId, null, code, message);
	}
	
	/**
	 * 메시지 전송후 결과얻기
	 * @param serverId server id
	 * @param packageName code package name
	 * @param code api code
	 * @param message send message
	 * @return receive message
	 */
	public static String sendToReceiveMessage(String serverId, String packageName, String code, String message){
		ServerConnect serverConnect = JdbcObjects.getObj(ServerConnect.class, "SERVER_ID='" + serverId +"' AND DEL_FG='N'");

		if(serverConnect == null){
			return ServerApiMessageType.FAIL +" server null" ;
		}

		ApiRequest request = new ApiRequest(serverConnect.hostAddress, serverConnect.port);
		if(packageName != null){
			request.setPackageName(packageName);
		}
		request.connect();
		String result = request.sendToReceiveMessage(code, message);
		request.disConnect();
		
		return result;
	}


}
