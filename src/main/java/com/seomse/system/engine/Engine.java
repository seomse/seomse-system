package com.seomse.system.engine;

import com.seomse.api.server.ApiServer;
import com.seomse.commons.code.ExitCode;
import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigSet;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.NetworkUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.sync.SynchronizerManager;
import com.seomse.system.commons.CommonConfigData;
import com.seomse.system.engine.console.EngineConsole;
import com.seomse.system.engine.dno.EngineStartDno;
import com.seomse.system.engine.dno.EngineTimeDno;
import com.seomse.system.server.console.ServerConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;

/**
 * <pre>
 *  파 일 명 : Engine.java
 *  설    명 : Engine
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class Engine {

	private static final Logger logger = LoggerFactory.getLogger(Engine.class);
	
	private static Engine instance = null;

	/**
	 * 싱글턴객체생성
	 * 반드시 메인에서 실행해서 실행
	 * 관련시나리오로 동기화 작성하지 않음
	 * @param engineId engine id
	 * @return 엔진 인스턴스
	 */
	public static Engine newInstance(final String engineId){
		if(instance != null){
			logger.error("already server " + instance.engineId);
			return instance;
		}
		
		instance = new Engine(engineId);
		return instance;
	}
	
	/**
	 * 싱글인스턴스 얻기
	 * 반드시 메인에서 실행해서 실행
	 * 관련시나리오로 동기화 작성하지 않음
	 * @return 엔진 인스턴스
	 */
	public static Engine getInstance (){
		return instance;
	}
	
	private String engineId;
	
	private EngineTimeDno timeDno;
	
	private long configTime = 0L;

	private EngineConfigData engineConfigData;

	private CommonConfigData commonConfigData;

	/**
	 * 생성자
	 * @param engineId engine id
	 */
	private Engine(String engineId){
		this.engineId = engineId;

		EngineStartDno engineStartDno= JdbcNaming.getObj(EngineStartDno.class,"ENGINE_ID='" + engineId +"'");
		if(engineStartDno == null){
			
			logger.error("engine not reg engine id: " + engineId);
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}
		
		String hostAddress = EngineConsole.getHostAddress(engineId);
		if(hostAddress == null){
			
			logger.error("engine host address null engine id: " + engineId + ", server id: " + engineStartDno.getSERVER_ID());
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}


		//설정정보 세팅하기
		//엔진설정 등록
		engineConfigData = new EngineConfigData(engineId);
		engineConfigData.sync();
		Config.addConfigData(engineConfigData);
		SynchronizerManager.getInstance().add(engineConfigData);

		//공통설정등록
		commonConfigData = new CommonConfigData();
		commonConfigData.sync();
		Config.addConfigData(commonConfigData);
		SynchronizerManager.getInstance().add(commonConfigData);

		try{
			//ip설정이 있는지 체크
			String ipAddress = ServerConsole.getIpAddress(engineStartDno.getSERVER_ID());
			InetAddress inetAddress;
			if(ipAddress == null) {
				inetAddress = NetworkUtil.getInetAddress(hostAddress);
			}else{
				inetAddress = NetworkUtil.getInetAddress(ipAddress);
				if(inetAddress == null){
					inetAddress = NetworkUtil.getInetAddress(hostAddress);
				}
			}


			ApiServer apiServer = new ApiServer(engineStartDno.getAPI_PORT_NB(), Config.getConfig("system.engine.api.package","com.seomse.system.engine.api"));
			if(inetAddress != null)
				apiServer.setInetAddress(inetAddress);
			apiServer.start();
			//스프링부트 실행


			//싱크서비스 실행
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}
		
		long dataTime = Database.getDateTime();
		timeDno = new EngineTimeDno();
		timeDno.setENGINE_ID(engineId);
		timeDno.setSTART_DT(dataTime);
		timeDno.setEND_DT( null);
		JdbcNaming.update(timeDno, true);

		logger.info("Engine start complete!");
	}

	/**
	 * 종료시점에 호출
	 */
	public void updateEndTime(){
		long time = Database.getDateTime();
		timeDno.setEND_DT(time);
		JdbcNaming.update(timeDno, false);
	}

	/**
	 * @return engine id
	 */
	public String getId() {
		return engineId;
	}

	/**
	 * 엔진 전용설정얻기
	 * @param key config Key
	 * @return config value
	 */
	public String getEngineConfig(String key){
		return engineConfigData.getConfig(key);
	}

	/**
	 * 공통설정에 등록된 정보 얻기
	 * @param key config Key
	 * @return config value
	 */
	public String getCommonConfig(String key){
		return commonConfigData.getConfig(key);
	}
	
	public static void main(String [] args){
		if(args == null){
			logger.error("args is null, engine code in");
			return ;
		}
		
		if(args.length < 2){
			logger.error("args is engine code, config path");
			return;
		}
		//noinspection
		if(args.length >= 3){
			ConfigSet.LOG_BACK_PATH = args[2];
		}else{
			//length 2
			File file =new File(args[1]);
			String logbackPath = file.getPath()+"/logback.xml";
			File logbackFile = new File(logbackPath);
			if(logbackFile.isFile()) {
				ConfigSet.LOG_BACK_PATH = logbackPath;
			}
		}
		ConfigSet.CONFIG_PATH = args[1];

		//서버 인스턴스 생성
		newInstance(args[0]);
	}
	
}
