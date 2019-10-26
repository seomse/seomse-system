package com.seomse.system.server.exception;

/**
 * <pre>
 *  파 일 명 : NotSupportOsTypeException.java
 *  설    명 : 지원하지않는 운영체제
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class NotSupportOsTypeException extends RuntimeException{

	private static final long serialVersionUID = -7937577737311154941L;

	public NotSupportOsTypeException(String oyType){
		super("not support os type: " + oyType);
	}

}
