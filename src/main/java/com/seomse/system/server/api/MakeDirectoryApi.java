package com.seomse.system.server.api;

import com.seomse.api.ApiMessage;
import com.seomse.commons.utils.ExceptionUtil;

import java.io.File;

/**  <pre>
 *  파 일 명 : MakeDirectoryApi.java
 *  설    명 : 폴더생성
 *

 *  작 성 자 : macle
 *  작 성 일 : 2017.11
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ㈜섬세한사람들. All right reserved.
 */
public class MakeDirectoryApi extends ApiMessage {
	
	@Override
	public void receive(String message) {
		try{
			File file = new File(message);
			if(file.mkdirs()){
				sendMessage(ServerApiMessageType.SUCCESS);
			}else{
				sendMessage(ServerApiMessageType.FAIL + " "  + message + " make fail");
			}
			
		}catch(Exception e){
			sendMessage(ServerApiMessageType.FAIL + ExceptionUtil.getStackTrace(e));
		}
	}

}