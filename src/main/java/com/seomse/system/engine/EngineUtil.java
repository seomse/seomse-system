package com.seomse.system.engine;

import com.seomse.api.ApiRequest;
import com.seomse.api.SocketAddress;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.server.api.ServerApiMessageType;
import com.seomse.system.server.vo.ServerConnectVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <pre>
 *  파 일 명 : EngineUtil.java
 *  설    명 : 엔진용 유틸성 클래스
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.1
 *  수정이력 : 2018.06, 2019.05
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ~ 2019 by ㈜섬세한사람들. All right reserved.
 */
public class EngineUtil {
	private static final Logger logger = LoggerFactory.getLogger(EngineUtil.class);
	/**
	 * 엔진의 서버아이피 얻기
	 * @param engineId
	 * @return
	 */
	public static String getServerIp(String engineId){
		String query ="SELECT HOST_ADDRESS FROM SERVER "
				+ " WHERE SERVER_ID IN (SELECT SERVER_ID FROM ENGINE WHERE ENGINE_ID='" + engineId+"' AND IS_DELETED='N') AND IS_DELETED='N'";
		
		return JdbcQuery.getResultOne(query);
	}
	
	/**
	 * 엔진에 접속할 수 있는 아이피 포트정보 얻기
	 * @param engineId
	 * @return
	 */
	public static SocketAddress getApiSocketAddress(String engineId){
		Map<String, String> engineInfoMap = JdbcQuery.getMapString("SELECT SERVER_ID, API_PORT, OUT_API_PORT FROM ENGINE WHERE ENGINE_ID='" + engineId+"' AND IS_DELETED='N'");
		 
		String serverId = engineInfoMap.get("SERVER_ID");
		ServerConnectVo serverConnectVo =  JdbcNaming.getObj(ServerConnectVo.class, "SERVER_ID='" + serverId+"'");
		if(serverConnectVo.getCLIENT_CONNECT_TYPE().equals("O")){
			//외부이면
			return new SocketAddress(serverConnectVo.getOUT_HOST_ADDRESS(), Integer.valueOf(engineInfoMap.get("OUT_API_PORT")));
			
		}else{
			//내부이면
			return new SocketAddress(serverConnectVo.getHOST_ADDRESS(), Integer.valueOf(engineInfoMap.get("API_PORT")));
		}
	}
	
	/**
	 * 엔진에 해당하는 서버ID 얻기
	 * @param engineId 엔진ID
	 * @return 서버ID
	 */
	public static String getserverId(String engineId){
		String serverId = JdbcQuery.getResultOne("SELECT SERVER_ID FROM ENGINE WHERE ENGINE_ID='" + engineId+"' AND IS_DELETED='N'");
		
		return serverId;
	}
	
	/**
	 * 엔진응답결과 얻기
	 * @param engineId
	 * @return
	 */
	public static boolean ping(String engineId){
		try{
			SocketAddress socketAddress = getApiSocketAddress(engineId);
			return ping(engineId, socketAddress);
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return false;
		}
	}
	/**
	 * 엔진 응답결과 얻기
	 * @param engineId
	 * @param socketAddress
	 * @return
	 */
	public static boolean ping(String engineId, SocketAddress socketAddress){
		try{
			//최대 5초대기
			ApiRequest request = new ApiRequest(socketAddress.getHostAddress(), socketAddress.getPort());
			request.setConnectTimeOut(5000);
			request.setConnectErrorLog(true);
		
			String result = request.sendToReceiveMessage("com.seomse.system.server.api", "EngeineRunCheckApi" + engineId);
	    	request.disConnect();
	    	
	  
	    	if(result.equals(ServerApiMessageType.SUCCESS)){
	    		return true;
	    	}else{
//	    		logger.error(result);
	     	}
	     	return false;
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return false;
		}
	}
	
	/**
	 * 엔진에 메시지 전송후 결과얻기
	 * @param engineId
	 * @param message
	 * @return
	 */
	public static  String sendToReceiveMessage(String engineId, String message){
		return sendToReceiveMessage(engineId , null , message);
	}
	
	
	/**
	 * 엔진에 메시지 전송후 결과얻기
	 * @param engineId
	 * @param packageName
	 * @param message
	 * @return
	 */
	public static  String sendToReceiveMessage(String engineId, String packageName, String message){
		SocketAddress socketAddress = EngineUtil.getApiSocketAddress(engineId);
		ApiRequest request = new ApiRequest(socketAddress.getHostAddress(), socketAddress.getPort());
		String result = request.sendToReceiveMessage(packageName, message);
		request.disConnect();
		return result;
	}


}
