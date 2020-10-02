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
package com.seomse.system.commons;

import com.seomse.api.ApiMessage;
import com.seomse.api.ApiRequest;
import com.seomse.api.Messages;
import com.seomse.commons.config.Config;

/**
 * ping check
 * @author macle
 */
public class PingApi extends ApiMessage {

	@Override
	public void receive(String message) {
		try{
			sendMessage(Messages.SUCCESS);
		}catch(Exception e){
			sendMessage(Messages.FAIL);
		}
	}

	/**
	 * ping
	 * @param hostAddress String host address
	 * @param port int port
	 * @return boolean is ping
	 */
	public static boolean ping(String hostAddress, int port){
		ApiRequest request = new ApiRequest(hostAddress, port);
		request.setConnectErrorLog(false);
		//최대 5초대기
		request.setConnectTimeOut(Config.getInteger("ping.wait.time", 5000));
		request.setPackageName("com.seomse.system.commons");
		request.setConnectErrorLog(false);
		request.connect();
		String result = request.sendToReceiveMessage("PingApi", "");
		request.disConnect();
		return result.startsWith(Messages.SUCCESS);
	}
	
}