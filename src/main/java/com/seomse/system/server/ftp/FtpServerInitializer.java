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
package com.seomse.system.server.ftp;

import com.seomse.system.server.Server;
import com.seomse.system.server.ServerInitializer;
import org.moara.ftp.server.FtpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ftp 기능
 * @author macle
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
