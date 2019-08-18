package com.seomse.system.server.console;

import com.seomse.api.ApiRequest;
import com.seomse.api.SocketAddress;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.server.vo.ServerConnectVo;

/**
 * <pre>
 *  파 일 명 : ServerConsole.java
 *  설    명 : 서버콘솔 이벤트 관련 (서버가아닌 콘솔에서 클라이언트 이벤트용)
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.11
 *  버    전 : 1.2
 *  수정이력 :  2018.03, 2018.06
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017, 2018 by ㈜섬세한사람들. All right reserved.
 */
public class ServerConsole {
	
	/**
	 * 서버 ping test
	 * @param serverId
	 * @return
	 */
	public static final String ping(String serverId){

		SocketAddress socketAddress = getApiConnectInfo(serverId);
		//최대 5초대기

		ApiRequest request = new ApiRequest(socketAddress.getHostAddress(), socketAddress.getPort());
		request.setConnectTimeOut(5000);
		request.setConnectErrorLog(true);

		String result = request.sendToReceiveMessage("com.seomse.system.server.api", "PingTestApi");
		request.disConnect();
  
        return result;
	}

	/**
	 * 서버 폴덧 생성
	 * @param serverId
	 * @param dirPath
	 * @return
	 */
	public static final String mkdirs(String serverId, String dirPath){
     	return sendToReceiveMessage(serverId, "MakeDirectoryApi" + dirPath);
	}
	
	
	/**
	 * 서버종료
	 * @param serverId
	 * @return
	 */
	public static final String serverStop(String serverId){
		return sendToReceiveMessage(serverId, "EngineStopApi");
	}
	
	
	/**
	 * 메시지 전송후 결과얻기
	 * @param serverId
	 * @param message
	 * @return
	 */
	public static final String sendToReceiveMessage(String serverId, String message){
		SocketAddress socketAddress = getApiConnectInfo(serverId);
		ApiRequest request = new ApiRequest(socketAddress.getHostAddress(), socketAddress.getPort());
		String result = request.sendToReceiveMessage("com.seomse.system.server.api", message);
		request.disConnect();
		return result;
	}
	
	/**
	 * 메시지 전송후 결과얻기
	 * @param serverId
	 * @param code
	 * @param message
	 * @return
	 */
	public static final String sendToReceiveMessage(String serverId, String code, String message){
		return sendToReceiveMessage(serverId, null, code, message);
	}
	
	/**
	 * 메시지 전송후 결과얻기
	 * @param serverId
	 * @param packageName
	 * @param code
	 * @param message
	 * @return
	 */
	public static final String sendToReceiveMessage(String serverId, String packageName, String code, String message){
		SocketAddress socketAddress = getApiConnectInfo(serverId);
		ApiRequest request = new ApiRequest(socketAddress.getHostAddress(), socketAddress.getPort());
		if(packageName != null){
			request.setPackageName(packageName);
		}
		request.connect();
		String result = request.sendToReceiveMessage(code, message);
		request.disConnect();
		
		return result;
	}
	
	/**
	 * 서버 api접속정보 얻기
	 * @param serverId
	 * @return
	 */
	public static final SocketAddress getApiConnectInfo(String serverId){
		ServerConnectVo serverConnectVo =  JdbcNaming.getObj(ServerConnectVo.class, "SERVER_ID='" + serverId+"'");
		if(serverConnectVo.getCLIENT_CONNECT_TYPE().equals("O")){
			//외부이면
			return new SocketAddress(serverConnectVo.getOUT_HOST_ADDRESS(), serverConnectVo.getOUT_API_PORT());
			
		}else{
			//내부이면
			return new SocketAddress(serverConnectVo.getHOST_ADDRESS(), serverConnectVo.getAPI_PORT());
			
		}
		
	}
	

}
