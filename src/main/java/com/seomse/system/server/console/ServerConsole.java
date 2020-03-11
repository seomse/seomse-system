package com.seomse.system.server.console;

import com.seomse.api.ApiRequests;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.objects.JdbcObjects;
import com.seomse.system.commons.PingApi;
import com.seomse.system.commons.SystemMessageType;

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
		if(serverConnect == null){
			return false;
		}

		return PingApi.ping(serverConnect.hostAddress, serverConnect.port);
   }

	
	/**
	 * 서버종료
	 * @param serverId serverId
	 * @return receive message
	 */
	public static String serverStop(String serverId){
		return sendToReceiveMessage(serverId, "ServerStopApi", "");
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
			return SystemMessageType.FAIL +" server null" ;
		}
		return ApiRequests.sendToReceiveMessage(serverConnect.hostAddress, serverConnect.port, packageName, code, message);
	}

	/**
	 * InetAddress 용 ip address 얻기
	 * @param serverId server id
	 * @return ip address
	 */
	public static String getIpAddress(String serverId){
		return getServerConfig(serverId, "ip.address");
	}


	/**
	 * 서버 설정 얻기
	 * @param serverId 서버 아이디
	 * @param configKey 설정키
	 * @return 설정값
	 */
	public static String getServerConfig(String serverId, String configKey){
		return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_SERVER_CONFIG WHERE SERVER_ID='" + serverId +"' AND CONFIG_KEY ='" + configKey + "' AND DEL_FG='N' ");
	}

	//외부 접속 host address 얻기

	/**
	 * 외부 접속용 host address 얻기
	 * @param serverId 서버 아이디
	 * @return 외부 접속용 host address
	 */
	public static String getHostAddressOut(String serverId){
		String address = getServerConfig(serverId,"host.address.out");
		if(address == null){
			address = JdbcQuery.getResultOne("SELECT HOST_ADDR FROM T_SYSTEM_SERVER WHERE SERVER_ID='" + serverId +"' AND DEL_FG ='N'");

		}
		return address;
	}

}
