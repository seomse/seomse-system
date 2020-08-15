package com.seomse.system.engine;

import com.seomse.api.ApiRequest;
import com.seomse.api.communication.HostAddrPort;
import com.seomse.api.communication.StringPush;
import com.seomse.system.commons.SystemMessageType;
import com.seomse.system.engine.console.EngineConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * <pre>
 *  파 일 명 : EngineCommunication.java
 *  설    명 : 엔진 통신
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class EngineCommunication {
	
	private static final Logger logger = LoggerFactory.getLogger(EngineCommunication.class);

	private final String engineId;
	private final HostAddrPort apiHostAddrPort;
	private HostAddrPort pushHostAddrPort;

	/**
	 * 생성자
	 * @param engineId engine id
	 */
	public EngineCommunication(String engineId){
		this.engineId = engineId;
		apiHostAddrPort = EngineConsole.getHostAddrPort(engineId);
	}
	
	/**
	 * 푸시용 host address port 설정
	 * @param pushHostAddrPort push host address port info
	 */
	public void setPushIpPort(HostAddrPort pushHostAddrPort){
		this.pushHostAddrPort = pushHostAddrPort;
	}
	
	/**
	 * 메시지 전송
	 * @param message message
	 */
	public void push(String message) throws IOException{
		Socket socket = new Socket(pushHostAddrPort.getHostAddress(), pushHostAddrPort.getPort());
		StringPush push = new StringPush(socket);
		push.sendMessage(message);
		push.disConnect();
	}
	
	/**
	 * 메시지 전송, 메시지 받기
	 * @param message message
	 * @return receive message
	 */
	public String sendToReceiveMessage(String packageName, String message){
		ApiRequest request = new ApiRequest(apiHostAddrPort.getHostAddress(), apiHostAddrPort.getPort());
		String result = request.sendToReceiveMessage(packageName, message);
		request.disConnect();
		return result;
	}
	
	/**
	 * 메시지를 배열단위로 끊어서 전송
	 * @param messageArray message array
	 * @return is message all success
	 */
	public boolean sendMessage(String packageName, String [] messageArray){
		ApiRequest request = new ApiRequest(apiHostAddrPort.getHostAddress(), apiHostAddrPort.getPort());
		boolean isResult = true;
		for(String message: messageArray){
			String result = request.sendToReceiveMessage(packageName, message);
			if(!result.startsWith(SystemMessageType.SUCCESS)){
				logger.error(result);
				isResult = false;
			}
		}

		request.disConnect();
		return isResult;
	}
	
	
	/**
	 * @return engine id
	 */
	public String getEngineId() {
		return engineId;
	}
	
	/**
	 * @return host address
	 */
	public String getHostAddress(){
		return apiHostAddrPort.getHostAddress();
	}

		
}
