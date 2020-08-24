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

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * server
 * start time, end time
 * @author macle
 */
@Table(name="T_SYSTEM_SERVER")
public class ServerTimeDno {

    @PrimaryKey(seq = 1)
    private String SERVER_ID;
    @DateTime
    private Long START_DT;
    @DateTime
    private Long END_DT;

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
     * @return Long start time
     */
    public Long getSTART_DT() {
        return START_DT;
    }

    /**
     *
     * @param START_DT Long start time
     */
    public void setSTART_DT(Long START_DT) {
        this.START_DT = START_DT;
    }

    /**
     *
     * @return Long end time
     */
    public Long getEND_DT() {
        return END_DT;
    }

    /**
     *
     * @param END_DT Long end time
     */
    public void setEND_DT(Long END_DT) {
        this.END_DT = END_DT;
    }
}
