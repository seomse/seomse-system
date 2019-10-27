package com.seomse.system.engine;

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
 * <pre>
 *  파 일 명 : EngineConfigData.java
 *  설    명 : 엔진 설정 데이타
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Priority(seq = 0) //동기화 우선순위용
public class EngineConfigData extends ConfigData implements Synchronizer {


    private static final Logger logger = LoggerFactory.getLogger(EngineConfigData.class);

    private Properties properties = new Properties();

    private String engineId;

    /**
     * 생성자
     * @param engineId engine id
     */
    EngineConfigData(String engineId){
        this.engineId = engineId;
    }

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
        return 0;
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
        List<EngineConfig> engineConfigList;
        if(updateTime == 0L){
            engineConfigList = JdbcObjects.getObjList(EngineConfig.class);
        }else{
            engineConfigList = JdbcObjects.getObjList(EngineConfig.class, "ENGINE_ID='" +engineId +"' UPT_LAST_DT > ?" , PrepareStatements.newTimeMap(updateTime));
        }

        if(engineConfigList.size() == 0){
            return;
        }

        logger.debug("engine config update size: " + engineConfigList.size());

        ConfigInfo[] infos = new ConfigInfo[engineConfigList.size()];

        //순서정보가 명확해야할때는 fori 구문 사용
        for (int i = 0; i < infos.length ; i++) {
            EngineConfig engineConfig = engineConfigList.get(i);
            ConfigInfo configInfo = new ConfigInfo(engineConfig.key, engineConfig.value);
            if(engineConfig.isDelete){
                configInfo.setDelete();
            }
            infos[i] = configInfo;
        }
        setConfig(infos);

        updateTime = engineConfigList.get(engineConfigList.size()-1).updateTime;


    }
}
