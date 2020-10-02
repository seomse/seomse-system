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
package com.seomse.system.server.console;

import com.seomse.api.ApiRequests;
import com.seomse.api.Messages;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.objects.JdbcObjects;
import com.seomse.system.commons.PingApi;

/**
 * server console
 * @author macle
 */
public class ServerConsole {

	/**
	 * ping
	 * @param serverId String server id
	 * @return boolean is ping
	 */
	public static boolean ping(String serverId){

		ServerConnect serverConnect = JdbcObjects.getObj(ServerConnect.class, "SERVER_ID='" + serverId +"' AND DEL_FG='N'");
		if(serverConnect == null){
			return false;
		}

		return PingApi.ping(serverConnect.hostAddress, serverConnect.port);
   }

	
	/**
	 * server stop
	 * @param serverId String serverId
	 * @return String receive message
	 */
	public static String serverStop(String serverId){
		return sendToReceiveMessage(serverId, "ServerStopApi", "");
	}
	
	/**
	 * 메시지 전송 후 결과 얻기
	 * @param serverId String server id
	 * @param code String api code
	 * @param message String send message
	 * @return String receive message
	 */
	public static String sendToReceiveMessage(String serverId, String code, String message){
		return sendToReceiveMessage(serverId, null, code, message);
	}

	/**
	 * 메시지 전송 후 결과 얻기
	 * @param serverId String server id
	 * @param packageName String package name
	 * @param code String code
	 * @param message String send message
	 * @return String receive message
	 */
	public static String sendToReceiveMessage(String serverId, String packageName, String code, String message){
		ServerConnect serverConnect = JdbcObjects.getObj(ServerConnect.class, "SERVER_ID='" + serverId +"' AND DEL_FG='N'");

		if(serverConnect == null){
			return Messages.FAIL +" server null" ;
		}
		return ApiRequests.sendToReceiveMessage(serverConnect.hostAddress, serverConnect.port, packageName, code, message);
	}

	/**
	 * inetaddress 에서 사용할 ip 얻기
	 * 네트워크 카드선택용
	 * @param serverId String server id
	 * @return String ip address
	 */
	public static String getIpAddress(String serverId){
		return getServerConfig(serverId, "ip.address");
	}


	/**
	 * 서버 설정 얻기
	 * @param serverId String server id
	 * @param configKey String config key
	 * @return String config value
	 */
	public static String getServerConfig(String serverId, String configKey){
		return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_SERVER_CONFIG WHERE SERVER_ID='" + serverId +"' AND CONFIG_KEY ='" + configKey + "' AND DEL_FG='N' ");
	}

	/**
	 * host address get
	 * 외부에서 접속할 수 있는 host address
	 * 외부망에서도 접속이 필요한 경우
	 * @param serverId String server id
	 * @return String host address
	 */
	public static String getHostAddressOut(String serverId){
		String address = getServerConfig(serverId,"host.address.out");
		if(address == null){
			address = JdbcQuery.getResultOne("SELECT HOST_ADDR FROM T_SYSTEM_SERVER WHERE SERVER_ID='" + serverId +"' AND DEL_FG ='N'");

		}
		return address;
	}

}
