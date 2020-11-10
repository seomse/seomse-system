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
package com.seomse.system.commons;

import com.seomse.jdbc.annotation.*;

/**
 * 공통 코드 클레스
 * @author macle
 */
@Table(name="T_COMMON_CONFIG", orderBy = "UPT_DT ASC")
public class CommonConfig {
    @PrimaryKey
    @Column(name = "CONFIG_KEY")
    String key;
    @Column(name = "CONFIG_VALUE")
    String value;
    @FlagBoolean
    @Column(name = "DEL_FG")
    boolean isDelete;
    @DateTime
    @Column(name = "UPT_DT")
    long updateTime;
}
