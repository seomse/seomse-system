package com.seomse.system.server.api;

import com.seomse.api.ApiMessage;
import com.seomse.commons.utils.ExceptionUtil;

import java.io.File;

/**
 * <pre>
 *  파 일 명 : GetEngineLibPathApi.java
 *  설    명 : engine jar를 얻기위한 경로 코드
 *  작 성 자 : macle
 *  작 성 일 : 2018.04
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
public class GetEngineLibPathApi extends ApiMessage {
	
	@Override
	public void receive(String message) {
		try{
			File file = new File(message);
			File pFile = file.getParentFile();
			sendMessage(ServerApiMessageType.SUCCESS + pFile.getAbsolutePath() + "/lib/");
		}catch(Exception e){
			sendMessage(ServerApiMessageType.FAIL + ExceptionUtil.getStackTrace(e));
		}
	}

}