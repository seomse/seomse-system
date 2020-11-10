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

import com.seomse.commons.annotation.Priority;
import com.seomse.commons.config.ConfigData;
import com.seomse.commons.config.ConfigInfo;
import com.seomse.jdbc.PrepareStatements;
import com.seomse.jdbc.objects.JdbcObjects;
import com.seomse.sync.Synchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * 공통 설정 database 와 memory 데이터 싱크용
 * @author macle
 */
@Priority(seq = 10) //동기화 우선순위용
public class CommonConfigData extends ConfigData implements Synchronizer {
    private static final Logger logger = LoggerFactory.getLogger(CommonConfigData.class);

    private final Properties properties = new Properties();

    @Override
    public String getConfig(String key) {
        return properties.getProperty(key);
    }

    @Override
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    protected void put(String key, String value) {
        properties.put(key, value);
    }

    @Override
    protected String remove(String key) {
        Object obj = properties.remove(key);

        if(obj == null){
            return null;
        }
        if(obj.getClass() == String.class){
            return (String) obj;
        }

        return obj.toString();
    }

    private long updateTime = 0L;


    @Override
    public void sync() {
        List<CommonConfig> commonConfigList;
        if(updateTime == 0L){
            commonConfigList = JdbcObjects.getObjList(CommonConfig.class);
        }else{
            commonConfigList = JdbcObjects.getObjList(CommonConfig.class, "UPT_DT > ?" , PrepareStatements.newTimeMap(updateTime));
        }
        if(commonConfigList.size() == 0){
            logger.debug("common config update size: 0") ;
            return;
        }

        logger.debug("common config update size: " + commonConfigList.size());

        ConfigInfo[] infos = new ConfigInfo[commonConfigList.size()];

        //순서정보가 명확해야할때는 fori 구문 사용
        for (int i = 0; i < infos.length ; i++) {
            CommonConfig commonConfig = commonConfigList.get(i);
            ConfigInfo configInfo = new ConfigInfo(commonConfig.key, commonConfig.value);
            if(commonConfig.isDelete){
                configInfo.setDelete();
            }
            infos[i] = configInfo;
        }
        setConfig(infos);
        updateTime = commonConfigList.get(commonConfigList.size()-1).updateTime;
    }
}
