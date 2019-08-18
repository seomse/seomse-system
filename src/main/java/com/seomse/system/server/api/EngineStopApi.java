package com.seomse.system.server.api;

import com.seomse.api.ApiMessage;
import com.seomse.commons.code.ExitCode;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.server.Server;
import com.seomse.system.server.vo.ServerTimeUpdateVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  <pre>
 *  파 일 명 : EngineStopApi.java
 *  설    명 : 서버종료
 *

 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ㈜섬세한사람들. All right reserved.
 */
public class EngineStopApi extends ApiMessage {
	
	private static final Logger logger = LoggerFactory.getLogger(EngineStopApi.class);
	
	@Override
	public void receive(String message) {
		try{
			Server server = Server.getInstance();
			ServerTimeUpdateVo serverTimeUpdateVo = server.getTimeVo();
			long time = Database.getDateTime();
			serverTimeUpdateVo.setEND_DATE(time);
			JdbcNaming.update(serverTimeUpdateVo, false);
			
			sendMessage(ServerApiMessageType.SUCCESS);
			System.exit(ExitCode.EXIT_ORDER.getCodeNum());
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			sendMessage(ServerApiMessageType.FAIL + ExceptionUtil.getStackTrace(e));
		}
	}

}