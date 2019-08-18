package com.seomse.system.engine;

import com.seomse.api.server.ApiServer;
import com.seomse.commons.code.ExitCode;
import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigSet;
import com.seomse.commons.hardware.HardWare;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcDataType;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.jdbc.naming.PrepareStatementData;
import com.seomse.system.engine.vo.EngineConfigVo;
import com.seomse.system.engine.vo.EngineInfoVo;
import com.seomse.system.engine.vo.EngineStartEndTimeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  파 일 명 : Engine.java
 *  설    명 : Engine
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.2
 *  수정이력 :  2018.04, 2018.06
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017, 2018 by ㈜섬세한사람들. All right reserved.
 */
public class Engine {
	private static final Logger logger = LoggerFactory.getLogger(Engine.class);
	
	private static Engine instance = null;


	public static final String PATH_SPLIT = ".CONFIG.LOG.SPLIT.";

	/**
	 * 싱글턴객체생성
	 * 반드시 메인에서 실행해서 실행
	 * 관련시나리오로 동기화 작성하지 않음
	 * @param engineId 엔진ID
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
	
	private EngineStartEndTimeVo startEndTimeVo;
	
	private long configTime = 0L;
	private Map<String,String> configMap = null;
	
	/**
	 * 생성자
	 * @param engineId 엔진ID
	 */
	private Engine(String engineId){
		this.engineId = engineId;
		
		EngineInfoVo engineInfoVo = JdbcNaming.getObj(EngineInfoVo.class, "ENGINE_ID='" + engineId + "' AND IS_DELETED='N'");
		if(engineInfoVo == null){
			
			logger.error("engine not reg engine code: " + engineId);
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}
		
		String ipAddress = JdbcQuery.getResultOne("SELECT HOST_ADDRESS FROM SERVER WHERE SERVER_ID='"
				+engineInfoVo.getSERVER_ID() +"' AND IS_DELETED='N'");
		if(ipAddress == null){
			
			logger.error("server ip is null server code: " +  engineInfoVo.getSERVER_ID());
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}
		
		Config.getConfig("");
		configMap = new HashMap<>();

		memoryUpdate();

		try{
			
			InetAddress inetAddress = HardWare.getInetAddress(ipAddress);
			if(inetAddress == null){
				logger.error("InetAddress error ip address: " +  ipAddress);
				System.exit(ExitCode.ERROR.getCodeNum());
				return ;
			}

			ApiServer apiServer = new ApiServer(engineInfoVo.getAPI_PORT(), "com.seomse.system.server.api");
			apiServer.setInetAddress(inetAddress);
			apiServer.start();

		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}
		
		long dataTime = Database.getDateTime();
		startEndTimeVo = new EngineStartEndTimeVo();
		startEndTimeVo.setENGINE_ID(engineId);
		startEndTimeVo.setSTART_DATE(dataTime);
		startEndTimeVo.setEND_DATE( null);
		JdbcNaming.update(startEndTimeVo, true);

		logger.info("Engine start complete!");
	}

	
	public void memoryUpdate(){
		List<EngineConfigVo> configList;
		if (configTime == 0L) {
			configList = JdbcNaming.getObjList(EngineConfigVo.class, "ENGINE_ID='" + engineId + "'");
		} else {
			Map<Integer, PrepareStatementData> prepareStatementDataMap = new HashMap<>();
			PrepareStatementData prepareStatementData = new PrepareStatementData();
			prepareStatementData.setData(configTime);
			prepareStatementData.setType(JdbcDataType.DATE_TIME);
			prepareStatementDataMap.put(1, prepareStatementData);
			configList = JdbcNaming.getObjList(EngineConfigVo.class, "ENGINE_ID='" + engineId + "' AND LAST_UPDATE_DATE > ? ", prepareStatementDataMap);
		}
		
		
		if(configList.size() > 0){
			logger.debug("ENGINE_CONFIG update: " + configList.size());
			
			Map<String, String> changeMap = new HashMap<>();
			for(EngineConfigVo configVo :configList){
				
				if(configVo.getLAST_UPDATE_TIME() > configTime){
					configTime = configVo.getLAST_UPDATE_TIME();
				}
				if(configVo.getIS_DELETED().equals("Y")){
					if(!configMap.containsKey(configVo.getCONFIG_KEY())){
						continue;
					}
					configMap.remove(configVo.getCONFIG_KEY());
				}else{
					
					String lastValue = configMap.get(configVo.getCONFIG_KEY());
					configMap.put(configVo.getCONFIG_KEY(), configVo.getCONFIG_VALUE());
					
					if(lastValue == null){
						changeMap.put(configVo.getCONFIG_KEY(), configVo.getCONFIG_VALUE());
					}else{
						if(configVo.getCONFIG_VALUE() != null && lastValue.equals(configVo.getCONFIG_VALUE())){
							continue;
						}
						changeMap.put(configVo.getCONFIG_KEY(), configVo.getCONFIG_VALUE());
					}
					
					
					
				}
				if(changeMap.size()>0){

					for (Map.Entry<String, String> entry : changeMap.entrySet()) {
						Config.setConfig(entry.getKey(), entry.getValue());
					}

					changeMap.clear();
					
				}
			}
			configList.clear();
		}
	
	}
	
	/**
	 * 종료시점에 호출
	 */
	public void updateEndTime(){
		long time = Database.getDateTime();
		startEndTimeVo.setEND_DATE(time);
		JdbcNaming.update(startEndTimeVo, false);
	}
	
	
	/**
	 * 엔진ID 얻기
	 * @return 엔진ID
	 */
	public String getId() {
		return engineId;
	}
	
	/**
	 * 엔진설정맵얻기
	 * @return 엔진 설정 맵
	 */
	public Map<String, String> getConfigMap() {
		return configMap;
	}

	/**
	 * 엔진 전용설정얻기
	 * @param key 설정 Key
	 * @return 설정값
	 */
	public String getConfig(String key){
		return configMap.get(key);
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
		try{

			int index = args[1].indexOf(Engine.PATH_SPLIT);

			if(index == -1){
				ConfigSet.CONFIG_PATH = args[1];
			}else{

				ConfigSet.LOG_BACK_PATH = args[1].substring(index+ + Engine.PATH_SPLIT.length());

				ConfigSet.CONFIG_PATH = args[1].substring(0, index);
			}



			//서버 인스턴스 생성
			newInstance(args[0]);
			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	}
	
}
