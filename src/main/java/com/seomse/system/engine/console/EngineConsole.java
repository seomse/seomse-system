package com.seomse.system.engine.console;

import com.seomse.api.ApiRequests;
import com.seomse.commons.communication.HostAddrPort;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.system.commons.PingApi;
import com.seomse.system.commons.SystemMessageType;
import com.seomse.system.server.console.ServerConsole;

import java.util.Map;

/**
 * <pre>
 *  파 일 명 : EngineConsole.java
 *  설    명 : 엔진콘솔 이벤트 관련 (서버가아닌 콘솔에서 클라이언트 이벤트용)
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public final class EngineConsole {
	
	/**
	 * 엔진시작
	 * 서버에 엔진시작 명령을 전달
	 * @param engineId engine id
	 * @return 엔진시작 결과
	 */
	public static String engineStart(String engineId){
		
		if(ping(engineId)){
			return "engine already start";
		}
		String serverId = getServerId(engineId);
		return ServerConsole.sendToReceiveMessage(serverId, "EngineRunApi", engineId);
	}

	/**
	 * 엔진종료
	 * 엔진은 설정에서 패키지를 변경하여 시작할 수 있으므로 엔진연관 api 패키지를 지정하여 명령 전송
	 * @param engineId engine id
	 * @return receive message
	 */
	public static String engineStop(String engineId){
		return sendToReceiveMessage(engineId,"com.seomse.system.engine.api", "EngineStopApi", "");
	}


	/**
	 * ping
	 * @param engineId engine id
	 * @return is ping success
	 */
	public static boolean ping(String engineId) {
		HostAddrPort hostAddrPort = getHostAddrPort(engineId);
		if(hostAddrPort == null){
			return false;
		}
		return PingApi.ping(hostAddrPort.getHostAddress(), hostAddrPort.getPort());
	}


	/**
	 * 서바 아이디 얻기
	 * @param engineId engine id
	 * @return 서버아이디
	 */
	public static String getServerId(String engineId){
		return JdbcQuery.getResultOne("SELECT SERVER_ID FROM T_SYSTEM_ENGINE WHERE ENGINE_ID='" + engineId+ "' AND DEL_FG='N'");
	}

	/**
	 * 엔진 host address 얻기
	 * @param engineId engine id
	 * @return host address
	 */
	public static String getHostAddress(String engineId){

		String sql ="SELECT S.HOST_ADDR AS HOST_ADDR FROM T_SYSTEM_SERVER S, T_SYSTEM_ENGINE E\n" +
				" WHERE S.SERVER_ID = E.SERVER_ID\n" +
				" AND S.DEL_FG='N' AND E.DEL_FG='N'\n" +
				" AND E.ENGINE_ID ='" + engineId + "'";

		return JdbcQuery.getResultOne(sql);
	}

	/**
	 * 접속정보 얻기
	 * @param engineId engine id
	 * @return 접속정보 host address, port
	 */
	public static HostAddrPort getHostAddrPort(String engineId){
		String sql = "SELECT S.HOST_ADDR AS HOST_ADDR, E.API_PORT_NB AS PORT_NB FROM T_SYSTEM_SERVER S, T_SYSTEM_ENGINE E\n" +
				" WHERE S.SERVER_ID = E.SERVER_ID\n" +
				" AND S.DEL_FG='N' AND E.DEL_FG='N'\n" +
				" AND E.ENGINE_ID ='" + engineId + "'";

		Map<String, String> data = JdbcQuery.getMapString(sql);
		if(data == null){
			return null;
		}

		HostAddrPort hostAddrPort = new HostAddrPort();
		hostAddrPort.setHostAddress(data.get("HOST_ADDR"));
		hostAddrPort.setPort(Integer.parseInt(data.get("PORT_NB")));
		return hostAddrPort;
	}

	/**
	 * 메시지 전송후 결과얻기
	 * @param engineId engine id
	 * @param code api code
	 * @param message send message
	 * @return receive message
	 */
	public static String sendToReceiveMessage(String engineId, String code, String message){
		return sendToReceiveMessage(engineId, null, code, message);
	}

	/**
	 * 메시지 전송후 결과얻기
	 * @param engineId engine id
	 * @param packageName code package name
	 * @param code api code
	 * @param message send message
	 * @return receive message
	 */
	public static String sendToReceiveMessage(String engineId, String packageName, String code, String message){
		HostAddrPort hostAddrPort = getHostAddrPort(engineId);

		if(hostAddrPort == null){
			return SystemMessageType.FAIL +" engine null" ;
		}
		return ApiRequests.sendToReceiveMessage(hostAddrPort.getHostAddress(), hostAddrPort.getPort(), packageName, code, message);
	}



	/**
	 * 엔진 설정 얻기
	 * @param engineId 엔진 아이디
	 * @param configKey 설정키
	 * @return 설정값
	 */
	public static String getEngineConfig(String engineId, String configKey){
		return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId +"' AND CONFIG_KEY ='" + configKey + "' AND DEL_FG='N' ");
	}


	public static void main(String[] args) {
		System.out.println(getEngineConfig("E58","crawling.active.priority"));
	}


}
