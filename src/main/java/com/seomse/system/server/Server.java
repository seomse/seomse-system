package com.seomse.system.server;

import com.seomse.api.server.ApiServer;
import com.seomse.commons.code.ExitCode;
import com.seomse.commons.config.Config;
import com.seomse.commons.config.ConfigSet;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.NetworkUtil;
import com.seomse.commons.utils.PriorityUtil;
import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.naming.JdbcNaming;
import com.seomse.system.server.console.ServerConsole;
import com.seomse.system.server.dno.ServerDno;
import com.seomse.system.server.dno.ServerTimeDno;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 *  파 일 명 : Server.java
 *  설    명 : Server
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
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

	
	private String serverId;

	private OsType osType = null;

	private ServerTimeDno timeDno = null;
	
	
	private InetAddress inetAddress;
	
	/**
	 * 생성자
	 * @param serverId serverId
	 */
	private Server(final String serverId){
		this.serverId = serverId;

		ServerDno serverDno = JdbcNaming.getObj(ServerDno.class, "SERVER_ID='" + serverId + "' AND DEL_FG='N'");

		if(serverDno == null){
			
			logger.error("server not reg server id " +  serverId);
			System.exit(ExitCode.ERROR.getCodeNum());
			return ;
		}

		osType = OsType.valueOf(serverDno.getOS_TP());
		
		try{
			//서버 아이피주소 얻기
			//host address 와 ip address 가 다른경우 (네트워크 카드 설정용)

			String ipAddress = ServerConsole.getIpAddress(serverId);
			if(ipAddress == null) {
				inetAddress = NetworkUtil.getInetAddress(serverDno.getHOST_ADDR());
			}else{
				inetAddress = NetworkUtil.getInetAddress(ipAddress);
				if(inetAddress == null){
					inetAddress = NetworkUtil.getInetAddress(serverDno.getHOST_ADDR());
				}
			}
			ApiServer apiServer = new ApiServer(serverDno.getAPI_PORT_NB(), "com.seomse.system.server.api");
			if(inetAddress != null)
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

					String initializerPackage = getConfig("server.initializer.package");

					if(initializerPackage == null){
						initializerPackage = "com.seomse";
					}

					Reflections ref = new Reflections(initializerPackage);
					List<ServerInitializer> initializerList = new ArrayList<>();
					for (Class<?> cl : ref.getSubTypesOf(ServerInitializer.class)) {
						try{
							//noinspection deprecation
							ServerInitializer initializer = (ServerInitializer)cl.newInstance();
							initializerList.add(initializer);
						}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
					}
					
					if(initializerList.size() == 0){
						startComplete();
						return;
					}

					ServerInitializer[] initializerArray = initializerList.toArray(new ServerInitializer[0]);

					Arrays.sort(initializerArray, PriorityUtil.PRIORITY_SORT);

                    //순서 정보가 꼭맞아야하는 정보라 이전 for문 사용 확실한 인지를위해
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
	}
	
	
	private void startComplete(){
		long dataTime = Database.getDateTime();
		
		timeDno = new ServerTimeDno();
		timeDno.setSERVER_ID(serverId);
		timeDno.setSTART_DT(dataTime);
		timeDno.setEND_DT(null);
		JdbcNaming.update(timeDno, true);
		logger.info("Server start complete!");
	}

	/**
	 * 서버 종료시간 업데이트
	 */
	public void updateEndTime(){
		long dataTime = Database.getDateTime();
		timeDno.setEND_DT(dataTime);
		JdbcNaming.update(timeDno, false);
	}


	/**
	 * 운영체제 유형얻기
	 * @return OsType
	 */
	public OsType getOsType(){
		return osType;
	}
	
	/**
	 * 서버ID 얻기
	 * @return ServerId
	 */
	public String getServerId(){
		return serverId;
	}
	

	/**
	 * InetAddress 얻기
	 * @return Server InetAddress
	 */
	public InetAddress getInetAddress() {
		return inetAddress;
	}

	/**
	 * 설정얻기
	 * @param key 설정키
	 * @return 설정값
	 */
	public String getConfig(String key){

		//서버설정 검색
		String value = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM TB_SYSTEM_SERVER_CONFIG WHERE SERVER_ID='" + serverId +"' AND CONFIG_KEY='" + key + "' AND DEL_FG='N'");
		if(value != null){
			return value;
		}

		//공통설정검색
		value = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM TB_COMMON_CONFIG WHERE CONFIG_KEY='" + key + "' AND DEL_FG='N'");
		if(value != null){
			return value;
		}

		//파일설정 검색
		return Config.getConfig(key);
	}

	public static String getServerDbConfig(String serverId, String key){
		return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM SERVER_CONFIG WHERE SERVER_ID='" + serverId +"'"
				+ " AND CONFIG_KEY='" + key +"'");
	}
	
	public static void main(String [] args){
		if(args == null){
			logger.error("args is null, server code in");
			return ;
		}

		if(args.length < 2){
			logger.error("args is server code, config path");
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
