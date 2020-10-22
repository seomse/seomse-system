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
package com.seomse.system.server.dno;

import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * 서버 정보
 * @author macle
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
@Table(name="T_SYSTEM_SERVER")
public class ServerDno {

    @PrimaryKey(seq = 1)
    private String SERVER_ID;
    private String SERVER_NM;
    private String HOST_ADDR;
    private Integer API_PORT_NO;
    @SuppressWarnings("FieldMayBeFinal")
    private String OS_TP = "UNIX" ;

    /**
     *
     * @return String server id
     */
    public String getSERVER_ID() {
        return SERVER_ID;
    }

    /**
     *
     * @param SERVER_ID String server id
     */
    public void setSERVER_ID(String SERVER_ID) {
        this.SERVER_ID = SERVER_ID;
    }

    /**
     *
     * @return String host address
     */
    public String getHOST_ADDR() {
        return HOST_ADDR;
    }

    /**
     *
     * @return Integer port number
     */
    public Integer getAPI_PORT_NO() {
        return API_PORT_NO;
    }

    /**
     *
     * @return String os type
     */
    public String getOS_TP() {
        return OS_TP;
    }

    /**
     *
     * @return String server name
     */
    public String getSERVER_NM() {
        return SERVER_NM;
    }

}
