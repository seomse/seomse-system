package com.seomse.system.server.dno;

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerDno.java
 *  설    명 : Server 데이터베이스 네이밍 객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.24
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="TB_SYSTEM_SERVER")
public class ServerDno {

    @PrimaryKey(seq = 1)
    private String SERVER_ID;
    private String SERVER_NM;
    private String HOST_ADDR;
    private Integer API_PORT_NB;
    private String OS_TP = "UNIX"  ;
    @DateTime
    private Long START_DT;
    @DateTime
    private Long END_DT;


    public String getSERVER_ID() {
        return SERVER_ID;
    }

    public void setSERVER_ID(String SERVER_ID) {
        this.SERVER_ID = SERVER_ID;
    }

    public String getSERVER_NM() {
        return SERVER_NM;
    }

    public void setSERVER_NM(String SERVER_NM) {
        this.SERVER_NM = SERVER_NM;
    }

    public String getHOST_ADDR() {
        return HOST_ADDR;
    }

    public void setHOST_ADDR(String HOST_ADDR) {
        this.HOST_ADDR = HOST_ADDR;
    }

    public Integer getAPI_PORT_NB() {
        return API_PORT_NB;
    }

    public void setAPI_PORT_NB(Integer API_PORT_NB) {
        this.API_PORT_NB = API_PORT_NB;
    }

    public String getOS_TP() {
        return OS_TP;
    }

    public void setOS_TP(String OS_TP) {
        this.OS_TP = OS_TP;
    }

    public Long getSTART_DT() {
        return START_DT;
    }

    public void setSTART_DT(Long START_DT) {
        this.START_DT = START_DT;
    }

    public Long getEND_DT() {
        return END_DT;
    }

    public void setEND_DT(Long END_DT) {
        this.END_DT = END_DT;
    }
}
