package com.seomse.system.server;

import com.seomse.api.server.ApiServer;
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
 * server
 * @author macle
 */
public class Server {

	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private static Server instance = null;


	/**
	 * new singleton instance
	 * @param serverId String server id
	 * @return Server singleton instance
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
	 * @return Server singleton instance
	 */
	public static Server getInstance (){
		return instance;
	}
	
	
	public enum OsType{
		WINDOWS //윈도우즈 계열
		, UNIX //유닉스계열
	}

	
	private final String serverId;

	private OsType osType = null;

	private ServerTimeDno timeDno = null;
	
	
	private InetAddress inetAddress;
	
	/**
	 * 생성자
	 * @param serverId String server id
	 */
	private Server(final String serverId){
		this.serverId = serverId;

		ServerDno serverDno = JdbcNaming.getObj(ServerDno.class, "SERVER_ID='" + serverId + "' AND DEL_FG='N'");

		if(serverDno == null){
			
			logger.error("server not reg server id " +  serverId);
			System.exit(-1);
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
			ApiServer apiServer = new ApiServer(serverDno.getAPI_PORT_NO(), "com.seomse.system.server.api");
			if(inetAddress != null)
				apiServer.setInetAddress(inetAddress);
			apiServer.start();

			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			System.exit(-1);
			return ;
		}

		//noinspection AnonymousHasLambdaAlternative
		new Thread(){
			@Override
			public void run(){
				try{

					String initializerPackage = getConfig("server.initializer.package");

					if(initializerPackage == null){
						initializerPackage = Config.getConfig("default.package", "com.seomse");
					}
					String [] initPackages = initializerPackage.split(",");

					List<ServerInitializer> initializerList = new ArrayList<>();

					for(String initPackage : initPackages) {
						//0.9.10
						Reflections ref = new Reflections(new ConfigurationBuilder()
								.setScanners(new SubTypesScanner(false), new ResourcesScanner())
								.setUrls(ClasspathHelper.forPackage(initPackage))
								.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(initPackage))));

//						Reflections ref = new Reflections(initPackage);

						for (Class<?> cl : ref.getSubTypesOf(ServerInitializer.class)) {
							try{

								ServerInitializer initializer = (ServerInitializer)cl.newInstance();
								initializerList.add(initializer);
							}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
						}
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

	/**
	 * start complete
	 * time update
	 */
	private void startComplete(){
		long dataTime = Database.getDateTime();
		
		timeDno = new ServerTimeDno();
		timeDno.setSERVER_ID(serverId);
		timeDno.setSTART_DT(dataTime);
		timeDno.setSTOP_DT(null);
		JdbcNaming.update(timeDno, true);
		logger.info("Server start complete!");
	}

	/**
	 * end time update
	 */
	public void updateEndTime(){
		long dataTime = Database.getDateTime();
		timeDno.setSTOP_DT(dataTime);
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
	 * @return String ServerId
	 */
	public String getServerId(){
		return serverId;
	}
	

	/**
	 * InetAddress 얻기
	 * @return InetAddress Server InetAddress
	 */
	public InetAddress getInetAddress() {
		return inetAddress;
	}

	/**
	 * config get
	 * database server direct
	 * @param key String config key
	 * @return String config value
	 */
	public String getConfig(String key){

		//서버설정 검색
		String value = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_SERVER_CONFIG WHERE SERVER_ID='" + serverId +"' AND CONFIG_KEY='" + key + "' AND DEL_FG='N'");
		if(value != null){
			return value;
		}

		//공통설정검색
		value = JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_COMMON_CONFIG WHERE CONFIG_KEY='" + key + "' AND DEL_FG='N'");
		if(value != null){
			return value;
		}

		//파일설정 검색
		return Config.getConfig(key);
	}

	/**
	 * config get
	 * database server direct
	 * @param serverId String server id
	 * @param key String config key
	 * @return String config value
	 */
	public static String getServerDbConfig(String serverId, String key){
		return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_SYSTEM_SERVER_CONFIG WHERE SERVER_ID='" + serverId +"'"
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
