package com.seomse.system.server.dno;

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerTimeDno.java
 *  설    명 : 서버 시작시간 끝시간 업데이트용 객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="T_SYSTEM_SERVER")
public class ServerTimeDno {

    @PrimaryKey(seq = 1)
    private String SERVER_ID;
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
