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
package com.seomse.system.engine.dno;

import com.seomse.jdbc.annotation.Table;

/**
 * engine connect info
 * @author macle
 */
@Table(name="T_SYSTEM_ENGINE")
public class EngineStartDno {

    private String SERVER_ID;
    private Integer API_PORT_NO;

    /**
     * sever id get
     * @return String server id
     */
    public String getSERVER_ID() {
        return SERVER_ID;
    }

    /**
     * port number get
     * @return Integer port number
     */
    public Integer getAPI_PORT_NO() {
        return API_PORT_NO;
    }


}
