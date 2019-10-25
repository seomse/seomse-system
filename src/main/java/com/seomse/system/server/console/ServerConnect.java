package com.seomse.system.server.console;

import com.seomse.jdbc.annotation.Column;
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
@Table(name="TB_SYSTEM_SERVER")
public class ServerConnect {
    @Column(name = "HOST_ADDR")
    String hostAddress;
    @Column(name = "API_PORT_NB")
    int port;
}
