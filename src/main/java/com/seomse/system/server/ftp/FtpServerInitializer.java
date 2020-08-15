package com.seomse.system.server.ftp;

import com.seomse.ftp.server.FtpServer;
import com.seomse.system.server.Server;
import com.seomse.system.server.ServerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *  파 일 명 : FtpServerInitializer.java
 *  설    명 : Ftp Server Initializer
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class FtpServerInitializer implements ServerInitializer {

    private static final Logger logger = LoggerFactory.getLogger(FtpServerInitializer.class);


    @Override
    public void init() {
        Server server = Server.getInstance();

        String flag = server.getConfig("ftp.use.flag");

        if(flag != null && flag.toUpperCase().equals("Y")){
            int portNumber = Integer.parseInt(server.getConfig("ftp.port"));

            int bufferSize;
            String bufferValue = server.getConfig("ftp.read.buffer.arr.size");

            if(bufferValue == null) {
                bufferSize = 10240;
            }else{
                bufferSize = Integer.parseInt(bufferValue);
            }
            FtpServer ftpServer = new FtpServer(portNumber);
            ftpServer.setInetAddress(server.getInetAddress());
            ftpServer.setBufferArrSize(bufferSize);

            if(!ftpServer.newServerSocket()){
                logger.error("ftp server start fail: " +  server.getServerId());
                System.exit(-1);
                return ;
            }
            ftpServer.start();
        }

    }
}
