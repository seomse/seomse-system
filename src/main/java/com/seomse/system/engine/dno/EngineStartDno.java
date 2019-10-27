package com.seomse.system.engine.dno;

import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : EngineTimeDno.java
 *  설    명 : 엔진 시작시간 끝시간 업데이트용 객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="TB_SYSTEM_ENGINE")
public class EngineStartDno {

    private String SERVER_ID;
    private Integer API_PORT_NB;

    public String getSERVER_ID() {
        return SERVER_ID;
    }

    public Integer getAPI_PORT_NB() {
        return API_PORT_NB;
    }


}
