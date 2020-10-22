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

import com.seomse.jdbc.annotation.Table;

/**
 * 엔진 시작용
 * @author macle
 */
@Table(name="T_SYSTEM_ENGINE")
public class EngineRunDno {
    private Integer API_PORT_NO;
    private Integer MIN_MEMORY_MB;
    private Integer MAX_MEMORY_MB;
    private String EXE_FILE_PATH;

    /**
     * @return Integer api port number
     */
    public Integer getAPI_PORT_NO() {
        return API_PORT_NO;
    }

    /**
     * @return Integer min memory mega byte
     */
    public Integer getMIN_MEMORY_MB() {
        return MIN_MEMORY_MB;
    }

    /**
     * @return Integer max memory mega byte
     */
    public Integer getMAX_MEMORY_MB() {
        return MAX_MEMORY_MB;
    }

    /**
     * @return String execute file path
     */
    public String getEXE_FILE_PATH() {
        return EXE_FILE_PATH;
    }
}
