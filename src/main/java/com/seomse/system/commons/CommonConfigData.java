package com.seomse.system.commons;

import com.seomse.commons.config.ConfigData;

/**
 * <pre>
 *  파 일 명 : CommonConfigData.java
 *  설    명 : 공통설정 데이타
 *             아무런 설정을 하지 않을경우
 *             기본우선순위 10
 *             엔진설정 우선순위 0
 *             기본 파일 우선순위 1000
 *             시스템 프로퍼티 우선순위 Integer.Max
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public class CommonConfigData extends ConfigData {
    @Override
    public String getConfig(String s) {
        return null;
    }

    @Override
    public boolean containsKey(String s) {
        return false;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    protected void put(String s, String s1) {

    }
}
