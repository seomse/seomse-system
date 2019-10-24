package com.seomse.system.server;

import com.seomse.api.server.ApiServer;
import com.seomse.commons.annotation.Priority;
import com.seomse.commons.code.ExitCode;
import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigSet;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.NetworkUtil;
import com.seomse.commons.utils.sort.QuickSortArray;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.server.initializer.ServerInitializer;
import com.seomse.system.server.vo.ServerConfigVo;
import com.seomse.system.server.vo.ServerInfoVo;
import com.seomse.system.server.vo.ServerTimeUpdateVo;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  파 일 명 : Server.java
 *  설    명 : Server
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.24
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class Server {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private static Server instance = null;
	
	
	/**
	 * 싱글턴객체생성
	 * 반드시 메인에서 실행해서 실행
	 * 관련시나리오로 동기화 작성하지 않음
	 * @param serverId serverId
	 */
	public static Server newInstance(String serverId){
		if(instance != null){
			logger.error("already server " + instance.serverId);
			return instance;
		}
		
		instance = new Server(serverId);
		
		return instance;
	}
	
	/**
	 * 싱글인스턴스 얻기
	 * 반드시 메인에서 실행해서 실행
	 * 관련시나리오로 동기화 작성하지 않음
	 * @return Server Instance
	 */
	public static Server getInstance (){
		return instance;
	}
	
	
	public enum OsType{
		WINDOWS //윈도우즈 계열
		, UNIX //유닉스계열
	}
	
	
	private Map<String, String> serverConfigMap;
	
	private String serverId;
	
	
	private OsType osType = null;

	private ServerTimeUpdateVo timeVo = null;
	
	
	private InetAddress inetAddress;
	
	/**
	 * 생성자
	 * @param serverId
	 */
	private Server(final String serverId){
		this.serverId = serverId;

		ServerInfoVo serverInfoVo = JdbcNaming.getObj(ServerInfoVo.class, "SERVER_ID='" + serverId + "' AND IS_DELETED='N'");
		
		serverConfigMap = new HashMap<>();
		
		List<ServerConfigVo> configList = JdbcNaming.getObjList(ServerConfigVo.class, "SERVER_ID='" + serverId +"'");
		
		if(configList.size() >0){
			for(ServerConfigVo configVo : configList){
				serverConfigMap.put(configVo.getCONFIG_KEY(), configVo.getCONFIG_VALUE());
			}

			configList.clear();
		}

		if(serverInfoVo == null){
			
			logger.error("server not reg server id " +  serverId);
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}
		
		if(!updateNetworkInfo(serverInfoVo)){
			logger.error("server network info not set server id: " +  serverId);
			System.exit(ExitCode.ERROR.getCodeNum());
			return;
		}
		osType = OsType.valueOf(serverInfoVo.getOS_TYPE());
		
		try{
			inetAddress = NetworkUtil.getInetAddress(serverInfoVo.getHOST_ADDRESS());
			if(inetAddress == null){
				logger.error("server host address error server code: " +  serverId);
				System.exit(ExitCode.ERROR.getCodeNum());
				return ;
			}

			ApiServer apiServer = new ApiServer(serverInfoVo.getAPI_PORT(), "com.seomse.system.server.api");
			apiServer.setInetAddress(inetAddress);
			apiServer.start();

			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}
		
		
		new Thread(){
			@Override
			public void run(){
				try{


					String initializerPackage = Config.getConfig("server.initializer.package", "com.seomse.system");


					Reflections ref = new Reflections(initializerPackage);
					List<ServerInitializer> initializerList = new ArrayList<>();
					for (Class<?> cl : ref.getSubTypesOf(ServerInitializer.class)) {
						try{
							ServerInitializer initializer = (ServerInitializer)cl.newInstance();
							initializerList.add(initializer);
						}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
					}
					
					if(initializerList.size() == 0){
						startComplete();
						return;
					}
					
					
					
					ServerInitializer[] initializerArray = initializerList.toArray(new ServerInitializer[0]);
					
					int [] numArray = new int[initializerArray.length];
					
					for(int i=0 ; i<numArray.length ; i++){
						
						int seq ;
						try{
							
							Priority priority =initializerArray[i].getClass().getAnnotation(Priority.class);
							if(priority != null){
								seq = priority.seq();
							}else{
								seq = 1000;
							}
							
							
							
						}catch(Exception e){
							seq = 1000;
							logger.error(ExceptionUtil.getStackTrace(e));
						}
						
						
						numArray[i] = seq;
						
					}
					
					
					QuickSortArray<ServerInitializer> sort = new QuickSortArray<>(initializerArray);
					sort.sortAsc(numArray);
					for (int i=0; i<initializerArray.length; i++) {
						try{
							initializerArray[i].init();
						}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
					}
					
					
				}catch(Exception e){
					logger.error(ExceptionUtil.getStackTrace(e));
				}
				
				startComplete();
			}
			
		}.start();
	}
	
	
	private void startComplete(){
		long dataTime = Database.getDateTime();
		
		timeVo = new ServerTimeUpdateVo();
		timeVo.setSERVER_ID(serverId);
		timeVo.setSTART_DATE(dataTime);
		timeVo.setEND_DATE(null);
		JdbcNaming.update(timeVo, true);
		
		logger.info("Server start complete!");
	}
	

	
	
	
	
	/**
	 * 서버 설정 정보 얻기
	 * @return
	 */
	public String getServerConfig(String key, String defaultValue){
		
		String value = serverConfigMap.get(key);
		if(value == null){

			value = Config.getConfig(key);

			if(value == null)
				return defaultValue;
		}
		
		return value;
		
	}
	
	/**
	 * 서버 시작종료 정보Vo얻기
	 * @return
	 */
	public ServerTimeUpdateVo getTimeVo(){
		return timeVo;
	}
	
	/**
	 * 운영체제 유형얻기
	 * @return
	 */
	public OsType getOsType(){
		return osType;
	}
	
	/**
	 * 서버ID 얻기
	 * @return
	 */
	public String getServerId(){
		return serverId;
	}
	
	
	
	/**
	 * InetAddress 얻기
	 * @return
	 */
	public InetAddress getInetAddress() {
		return inetAddress;
	}


	/**
	 * DB서버의 설정 정보 바로 얻기
	 * 메모리 동기화 오차방지용
	 * 실시간 반영 중요한부분만 사용
	 * 잦은사용시 속도저하 심각.
	 * @param serverId
	 * @param key
	 * @return
	 */
	public static String getServerDbConfig(String serverId, String key){
		return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM SERVER_CONFIG WHERE SERVER_ID='" + serverId +"'"
				+ " AND CONFIG_KEY='" + key +"'");
	}
	
	public static void main(String [] args){
		if(args == null){
			logger.error("args is null, server code in");
			return ;
		}

		if(args.length != 2){
			logger.error("args is server code, config path");
			return;
		}


		//noinspection ConstantConditions
		if(args.length >= 3){
			ConfigSet.LOG_BACK_PATH = args[2];
		}

		ConfigSet.CONFIG_PATH = args[1];

		//서버 인스턴스 생성
		newInstance(args[0]);

	}
	
}
