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

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * engine start time, end time
 * @author macle
 */
@Table(name="T_SYSTEM_ENGINE")
public class EngineTimeDno {
    @PrimaryKey(seq = 1)
    private String ENGINE_ID;

    @DateTime
    private Long START_DT;
    @DateTime
    private Long STOP_DT;

    /**
     * engine id get
     * @return String engine id
     */
    public String getENGINE_ID() {
        return ENGINE_ID;
    }

    /**
     * engine id set
     * @param ENGINE_ID String engine id
     */
    public void setENGINE_ID(String ENGINE_ID) {
        this.ENGINE_ID = ENGINE_ID;
    }

    /**
     * start time get
     * (unix time)
     * @return start time (unix time)
     */
    public Long getSTART_DT() {
        return START_DT;
    }

    /**
     * start time set
     * (unix time)
     * @param START_DT start time (unix time)
     */
    public void setSTART_DT(Long START_DT) {
        this.START_DT = START_DT;
    }

    /**
     * stop time
     * (unix time)
     * @return stop time (unix time)
     */
    public Long getSTOP_DT() {
        return STOP_DT;
    }

    /**
     * stop time set
     * (unix time)
     * @param STOP_DT stop time ((unix time)
     */
    public void setSTOP_DT(Long STOP_DT) {
        this.STOP_DT = STOP_DT;
    }
}
