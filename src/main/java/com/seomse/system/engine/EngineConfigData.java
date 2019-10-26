package com.seomse.system.engine;

import com.seomse.commons.config.ConfigData;

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
public class EngineConfigData extends ConfigData {

    private Properties properties = new Properties();

    private String engineCode;

    /**
     * 생성자
     * @param engineCode 엔진코드
     */
    EngineConfigData(String engineCode){
        this.engineCode = engineCode;
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

    private long updateTime = 0L;


}
