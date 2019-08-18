package com.seomse.system.server.run;

/**
 * <pre>
 *  파 일 명 : EngineRun.java
 *  설    명 : 엔진 실행
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2016 by ㈜섬세한사람들. All right reserved.
 */
public interface EngineRun {

	/**
	 * 엔진 시작
	 * @param engineId 엔진ID
	 * @return
	 */
	String start(String engineId);
	
}