package com.seomse.system.engine;

import com.seomse.api.ApiRequest;
import com.seomse.api.SocketAddress;
import com.seomse.commons.communication.StringPush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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

	private String engineId;
	private SocketAddress socketAddress;
	private SocketAddress pushSocketAddress;
	
	/**
	 * 생성자
	 */
	public EngineCommunication(String engineId){
		this.engineId = engineId;
		socketAddress = EngineUtil.getApiSocketAddress(engineId);
		pushSocketAddress = socketAddress;
	}
	
	/**
	 * 푸시용 ip port설정
	 * @param pushSocketAddress
	 */
	public void setPushIpPort(SocketAddress pushSocketAddress){
		this.pushSocketAddress = pushSocketAddress;
	}
	
	/**
	 * 메시지 전송
	 * @param message
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void push(String message) throws UnknownHostException, IOException{
		Socket socket = new Socket(pushSocketAddress.getHostAddress(), pushSocketAddress.getPort());
		StringPush push = new StringPush(socket);
		push.sendMessage(message);
		push.disConnect();
	}
	
	/**
	 * 메시지 전송, 메시지 받기
	 * @param message
	 * @return
	 */
	public String sendToReceiveMessage(String packageName, String message){
		ApiRequest request = new ApiRequest(socketAddress.getHostAddress(), socketAddress.getPort());
		String result = request.sendToReceiveMessage(packageName, message);
		request.disConnect();
		
		return result;
		
	}
	
	/**
	 * 메시지를 배열단위로 끊어서 전송 
	 * 
	 * @param messageArray
	 * @return
	 */
	public boolean sendMessage(String packageName, String [] messageArray){
		ApiRequest request = new ApiRequest(socketAddress.getHostAddress(), socketAddress.getPort());
		boolean isResult = true;
		for(String message: messageArray){
			String result = request.sendToReceiveMessage(packageName, message);
			if(!result.startsWith(ServerApiMessageType.SUCCESS)){
				logger.error(result);
				isResult = false;
			}
		}

		request.disConnect();

		
		return isResult;
	}
	
	
	/**
	 * 엔진ID 얻기
	 * @return 엔진ID
	 */
	public String getEngineId() {
		return engineId;
	}
	
	/**
	 * 아이피 주소 얻기
	 * @return Host Address
	 */
	public String getHostAddress(){
		return socketAddress.getHostAddress();
	}

		
}
