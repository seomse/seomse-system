package com.seomse.system.commons;

import com.seomse.jdbc.JdbcQuery;

/**
 * <pre>
 *  파 일 명 : CommonConfigs.java
 *  설    명 : 데이터 베이스를 통하여 정보를 가져올때 사용
 *  작 성 자 : macle
 *  작 성 일 : 2019.11.11
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class CommonConfigs {

    /**
     * 설정값 얻기
     * @param key 설정 키
     * @return 설정 값
     */
    public static String getConfig(String key){
        return JdbcQuery.getResultOne("SELECT CONFIG_VALUE TB_COMMON_CONFIG WHERE CONFIG_KEY='" + key + "'");
    }

}
