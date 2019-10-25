package com.seomse.system.server.run;

import com.seomse.system.server.Server.OsType;
import com.seomse.system.server.exception.NotSupportOsTypeException;

/**
 * <pre>
 *  파 일 명 : EngineRunFactory.java
 *  설    명 : EngineRun 생성
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.1
 *  수정이력 : 2018.03
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017, 2018 by ㈜섬세한사람들. All right reserved.
 */
public class EngineRunFactory {
	
	/**
	 * EngineRun 생성
	 * @param osType osType
	 * @return EngineRun
	 */
	public static EngineRun newEngineRun(OsType osType){
		switch(osType){
		case UNIX:
			return new EngineRunUnix();
		case WINDOWS:
			return new EngineRunWindows();
		default:
			throw new NotSupportOsTypeException(osType.toString());
		}
	}

}
