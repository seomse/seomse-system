package com.seomse.system.server.dno;

import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerDno.java
 *  설    명 : Server 데이터베이스 네이밍 객체
 *            초기 정보를 가져오는 객체이므로 워닝표시하지 않음
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
@Table(name="T_SYSTEM_SERVER")
public class ServerDno {

    @PrimaryKey(seq = 1)
    private String SERVER_ID;
    private String SERVER_NM;
    private String HOST_ADDR;
    private Integer API_PORT_NB;
    private String OS_TP = "UNIX" ;

    public String getSERVER_ID() {
        return SERVER_ID;
    }

    public void setSERVER_ID(String SERVER_ID) {
        this.SERVER_ID = SERVER_ID;
    }

    public String getHOST_ADDR() {
        return HOST_ADDR;
    }

    public Integer getAPI_PORT_NB() {
        return API_PORT_NB;
    }

    public String getOS_TP() {
        return OS_TP;
    }

    public String getSERVER_NM() {
        return SERVER_NM;
    }

}
