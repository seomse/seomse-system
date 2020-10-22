/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.system.engine.console;

import com.seomse.api.ApiRequests;
import com.seomse.api.Messages;
import com.seomse.api.communication.HostAddrPort;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.system.commons.PingApi;
import com.seomse.system.server.console.ServerConsole;

import java.util.Map;

/**
 * engine console
 * @author macle
 */
public final class EngineConsole {

	/**
	 * engine start
	 * @param engineId String engine id
	 * @return String receive message
	 */
	public static String engineStart(String engineId){
		
		if(ping(engineId)){
			return "engine already start";
		}
		String serverId = getServerId(engineId);
		return ServerConsole.sendToReceiveMessage(serverId, "EngineRunApi", engineId);
	}

	/**
	 * engine stop
	 * @param engineId String engine id
	 * @return String receive message
	 */
	public static String engineStop(String engineId){
		return sendToReceiveMessage(engineId,"com.seomse.system.engine.api", "EngineStopApi", "");
	}


	/**
	 * ping
	 * @param engineId String engine iod
	 * @return boolean
	 */
	public static boolean ping(String engineId) {
		HostAddrPort hostAddrPort = getHostAddrPort(engineId);
		if(hostAddrPort == null){
			return false;
		}
		return PingApi.ping(hostAddrPort.getHostAddress(), hostAddrPort.getPort());
	}

	/**
	 * server id get
	 * @param engineId String engine id
	 * @return String server id
	 */
	public static String getServerId(String engineId){
		return JdbcQuery.getResultOne("SELECT SERVER_ID FROM T_SYSTEM_ENGINE WHERE ENGINE_ID='" + engineId+ "' AND DEL_FG='N'");
	}

	/**
	 * host address get
	 * @param engineId String engine id
	 * @return String host address
	 */
	public static String getHostAddress(String engineId){

		String sql ="SELECT S.HOST_ADDR AS HOST_ADDR FROM T_SYSTEM_SERVER S, T_SYSTEM_ENGINE E\n" +
				" WHERE S.SERVER_ID = E.SERVER_ID\n" +
				" AND S.DEL_FG='N' AND E.DEL_FG='N'\n" +
				" AND E.ENGINE_ID ='" + engineId + "'";

		return JdbcQuery.getResultOne(sql);
	}

	/**
	 * HostAddrPort get
	 * @param engineId String engine id
	 * @return HostAddrPort host adder and port
	 */
	public static HostAddrPort getHostAddrPort(String engineId){
		String sql = "SELECT S.HOST_ADDR AS HOST_ADDR, E.API_PORT_NO AS PORT_NB FROM T_SYSTEM_SERVER S, T_SYSTEM_ENGINE E\n" +
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
	 * 메시지 전송후 결과 얻기
	 * @param engineId String engine id
	 * @param code String code
	 * @param message String send message
	 * @return String receive message
	 */
	public static String sendToReceiveMessage(String engineId, String code, String message){
		return sendToReceiveMessage(engineId, null, code, message);
	}



	/**
	 *  메시지 전송후 결과 얻기
	 * @param engineId String engine id
	 * @param packageName String package name
	 * @param code String code
	 * @param message String send message
	 * @return String receive message
	 */
	public static String sendToReceiveMessage(String engineId, String packageName, String code, String message){
		HostAddrPort hostAddrPort = getHostAddrPort(engineId);

		if(hostAddrPort == null){
			return Messages.FAIL +" engine null" ;
		}
		return ApiRequests.sendToReceiveMessage(hostAddrPort.getHostAddress(), hostAddrPort.getPort(), packageName, code, message);
	}


	/**
	 * engine config get
	 * data base direct
	 * @param engineId String engine id
	 * @param configKey String config key
	 * @return String config value
	 */
	public static String getEngineConfig(String engineId, String configKey){
		return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_ENGINE_CONFIG WHERE ENGINE_ID='" + engineId +"' AND CONFIG_KEY ='" + configKey + "' AND DEL_FG='N' ");
	}

}
