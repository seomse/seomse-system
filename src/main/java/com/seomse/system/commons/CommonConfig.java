package com.seomse.system.commons;

import com.seomse.jdbc.annotation.*;

/**
 * <pre>
 *  파 일 명 : CommonConfig.java
 *  설    명 : 공통 설정
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="T_COMMON_CONFIG", orderBy = "UPT_LAST_DT ASC")
public class CommonConfig {
    @PrimaryKey
    @Column(name = "CONFIG_KEY")
    String key;
    @Column(name = "CONFIG_VALUE")
    String value;
    @FlagBoolean
    @Column(name = "DEL_FG")
    boolean isDelete;
    @DateTime
    @Column(name = "UPT_LAST_DT")
    long updateTime;
}
