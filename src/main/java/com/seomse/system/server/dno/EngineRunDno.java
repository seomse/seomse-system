package com.seomse.system.server.dno;

import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerConnect.java
 *  설    명 : 서버 연결정보
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="T_SYSTEM_ENGINE")
public class EngineRunDno {
    private Integer API_PORT_NB;
    private Integer MIN_MEMORY_MB;
    private Integer MAX_MEMORY_MB;
    private String EXE_FILE_PATH;

    public Integer getAPI_PORT_NB() {
        return API_PORT_NB;
    }

    public Integer getMIN_MEMORY_MB() {
        return MIN_MEMORY_MB;
    }

    public Integer getMAX_MEMORY_MB() {
        return MAX_MEMORY_MB;
    }


    public String getEXE_FILE_PATH() {
        return EXE_FILE_PATH;
    }
}
