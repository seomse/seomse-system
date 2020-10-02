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
package com.seomse.system.engine;

import com.seomse.api.ApiRequest;
import com.seomse.api.Messages;
import com.seomse.api.communication.HostAddrPort;
import com.seomse.api.communication.StringPush;
import com.seomse.system.engine.console.EngineConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * engine communication
 * @author macle
 */
public class EngineCommunication {
	
	private static final Logger logger = LoggerFactory.getLogger(EngineCommunication.class);

	private final String engineId;
	private final HostAddrPort apiHostAddrPort;
	private HostAddrPort pushHostAddrPort;

	/**
	 * 생성자
	 * @param engineId String engine id
	 */
	public EngineCommunication(String engineId){
		this.engineId = engineId;
		apiHostAddrPort = EngineConsole.getHostAddrPort(engineId);
	}
	
	/**
	 * 푸시용 host address port 설정
	 * @param pushHostAddrPort HostAddrPort push host address port info
	 */
	public void setPushIpPort(HostAddrPort pushHostAddrPort){
		this.pushHostAddrPort = pushHostAddrPort;
	}

	/**
	 *  메시지 전송
	 * @param message String message
	 * @throws IOException IOException
	 */
	public void push(String message) throws IOException{
		Socket socket = new Socket(pushHostAddrPort.getHostAddress(), pushHostAddrPort.getPort());
		StringPush push = new StringPush(socket);
		push.sendMessage(message);
		push.disConnect();
	}
	
	/**
	 * 메시지 전송, 메시지 받기
	 * @param message String request message
	 * @return String response message
	 */
	public String sendToReceiveMessage(String packageName, String message){
		ApiRequest request = new ApiRequest(apiHostAddrPort.getHostAddress(), apiHostAddrPort.getPort());
		String result = request.sendToReceiveMessage(packageName, message);
		request.disConnect();
		return result;
	}

	/**
	 * 메시지를 배열단위로 끊어서 전송
	 * @param packageName String package name
	 * @param messageArray String [] message array
	 * @return boolean is message all success
	 */
	public boolean sendMessage(String packageName, String [] messageArray){
		ApiRequest request = new ApiRequest(apiHostAddrPort.getHostAddress(), apiHostAddrPort.getPort());
		boolean isResult = true;
		for(String message: messageArray){
			String result = request.sendToReceiveMessage(packageName, message);
			if(!result.startsWith(Messages.SUCCESS)){
				logger.error(result);
				isResult = false;
			}
		}

		request.disConnect();
		return isResult;
	}

	/**
	 * @return String engine id
	 */
	public String getEngineId() {
		return engineId;
	}
	
	/**
	 * @return String host address
	 */
	public String getHostAddress(){
		return apiHostAddrPort.getHostAddress();
	}

		
}
