/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.system.engine;

import com.seomse.api.server.ApiServer;
import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigSet;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.NetworkUtil;
import com.seomse.commons.utils.PriorityUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.sync.SynchronizerManager;
import com.seomse.system.commons.CommonConfigData;
import com.seomse.system.engine.console.EngineConsole;
import com.seomse.system.engine.dno.EngineStartDno;
import com.seomse.system.engine.dno.EngineTimeDno;
import com.seomse.system.server.console.ServerConsole;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * engine
 * @author macle
 */
public class Engine {

	private static final Logger logger = LoggerFactory.getLogger(Engine.class);
	
	private static Engine instance = null;

	/**
	 * new singleton instance
	 * @param engineId String engine id
	 * @return Engine singleton instance
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
	 * singleton instance get
	 * @return Engine singleton instance
	 */
	public static Engine getInstance (){
		return instance;
	}
	
	private final String engineId;
	
	private final EngineTimeDno timeDno;

	private EngineConfigData engineConfigData;

	private CommonConfigData commonConfigData;

	/**
	 * 생성자
	 * @param engineId String engine id
	 */
	private Engine(String engineId){
		this.engineId = engineId;
		timeDno = new EngineTimeDno();
		timeDno.setENGINE_ID(engineId);

		EngineStartDno engineStartDno= JdbcNaming.getObj(EngineStartDno.class,"ENGINE_ID='" + engineId +"'");
		if(engineStartDno == null){
			
			logger.error("engine not reg engine id: " + engineId);
			System.exit(-1);
			return ;
		}
		
		String hostAddress = EngineConsole.getHostAddress(engineId);
		if(hostAddress == null){
			
			logger.error("engine host address null engine id: " + engineId + ", server id: " + engineStartDno.getSERVER_ID());
			System.exit(-1);
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

			ApiServer apiServer = new ApiServer(engineStartDno.getAPI_PORT_NO(), Config.getConfig("engine.api.package","com.seomse.system.engine.api"));
			if(inetAddress != null)
				apiServer.setInetAddress(inetAddress);
			apiServer.start();

			//스프링 부트는 시스템을 이용하는 다른프로젝트에서 EngineInitializer 를 이용하여 구동시키는 방향으로 진행
			//이니셜라이저 실행
			//noinspection AnonymousHasLambdaAlternative
			new Thread(){
				@Override
				public void run(){
					try{

						String initializerPackage = Config.getConfig("engine.initializer.package");

						if(initializerPackage == null){
							initializerPackage = Config.getConfig("default.package", "com.seomse");
						}

						String [] initPackages = initializerPackage.split(",");

						List<EngineInitializer> initializerList = new ArrayList<>();
						for(String initPackage : initPackages) {
							//0.9.10
							Reflections ref = new Reflections(new ConfigurationBuilder()
									.setScanners(new SubTypesScanner(false), new ResourcesScanner())
									.setUrls(ClasspathHelper.forPackage(initPackage))
									.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(initPackage))));

//							Reflections ref = new Reflections(initPackage);

							for (Class<?> cl : ref.getSubTypesOf(EngineInitializer.class)) {
								try{
									EngineInitializer initializer = (EngineInitializer)cl.newInstance();
									initializerList.add(initializer);
								}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
							}
						}



						if(initializerList.size() == 0){
							startComplete();
							return;
						}

						EngineInitializer[] initializerArray = initializerList.toArray(new EngineInitializer[0]);

						Arrays.sort(initializerArray, PriorityUtil.PRIORITY_SORT);

						//순서 정보가 꼭맞아야하는 정보라 fori 구문 사용 확실한 인지를위해
						//noinspection ForLoopReplaceableByForEach
						for (int i=0 ; i < initializerArray.length ; i++) {
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

		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			System.exit(-1);
		}
		

	}

	/**
	 * start complete
	 * time update
	 */
	private void startComplete(){
		long dataTime = Database.getDateTime();

		timeDno.setSTART_DT(dataTime);
		timeDno.setSTOP_DT( null);
		JdbcNaming.update(timeDno, true);

		logger.info("Engine start complete!");
	}

	/**
	 * stio time update
	 */
	public void updateStopTime(){
		long time = Database.getDateTime();
		timeDno.setSTOP_DT(time);
		JdbcNaming.update(timeDno, false);
	}

	/**
	 * @return String engine id
	 */
	public String getId() {
		return engineId;
	}

	/**
	 * engine config get
	 * memory data
	 * @param key String config key
	 * @return String engine config value
	 */
	public String getEngineConfig(String key){
		return engineConfigData.getConfig(key);
	}

	/**
	 * common config get
	 * memory data
	 * @param key String config key
	 * @return String common config value
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
		if(args.length >= 3){
			ConfigSet.LOG_BACK_PATH = args[2];
		}else{
			//length 2
			File file =new File(args[1]);
			String logbackPath = file.getParentFile().getPath()+"/logback.xml";
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
