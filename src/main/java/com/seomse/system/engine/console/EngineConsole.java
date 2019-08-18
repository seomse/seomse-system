package com.seomse.system.engine.console;

import com.seomse.system.engine.EngineUtil;
import com.seomse.system.server.console.ServerConsole;

/**
 * <pre>
 *  파 일 명 : EngineConsole.java
 *  설    명 : 엔진콘솔 이벤트 관련 (서버가아닌 콘솔에서 클라이언트 이벤트용)
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.1
 *  수정이력 :  2018.03
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017, 2018 by ㈜섬세한사람들. All right reserved.
 */
public final class EngineConsole {
	
	/**
	 * 엔진시작
	 * @param engineId 엔진ID
	 * @return 엔진시작 결과
	 */
	public static String engineStart(String engineId){
		
		if(ping(engineId)){
			return "engine already start";
		}

		String serverId = EngineUtil.getserverId(engineId);
		
		return ServerConsole.sendToReceiveMessage(serverId, "EngineRunApi" + engineId);
	}
	
	/**
	 * 엔진종료
	 * @param engineId 엔진ID
	 * @return 엔진중지 결과
	 */
	public static String engineStop(String engineId){
		return EngineUtil.sendToReceiveMessage(engineId, "EngineStopApi");
	}
	
	
	/**
	 * 엔진응답결과 얻기
	 * @param engineId 엔진ID
	 * @return 엔진응답 결과
	 */
	public static boolean ping(String engineId){
		return EngineUtil.ping(engineId);
	}
	
}
