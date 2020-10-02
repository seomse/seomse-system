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

import com.seomse.jdbc.Database;
import com.seomse.jdbc.JdbcQuery;
import com.seomse.jdbc.objects.JdbcObjects;

/**
 * 공통설정 database 연동성 유틸
 * @author macle
 */
public class CommonConfigs {

    /**
     * 설정값 얻기
     * @param key String 설정 키
     * @return String 설정 값
     */
    public static String getConfig(String key){
        return JdbcQuery.getResultOne("SELECT CONFIG_VALUE FROM T_COMMON_CONFIG WHERE CONFIG_KEY='" + key + "'");
    }


    /**
     * 업데이트
     * @param key String 업데이트 키
     * @param value String 업데이트 값
     */
    public static void update(String key, String value){
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.key = key;
        commonConfig.value = value;
        commonConfig.isDelete = false;
        commonConfig.updateTime = Database.getDateTime();
        JdbcObjects.insertOrUpdate(commonConfig, true);
    }

}
