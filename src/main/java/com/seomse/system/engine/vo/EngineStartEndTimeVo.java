package com.seomse.system.engine.vo;

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : EngineStartEndTimeVo.java
 *  설    명 : 엔진 시작종료시간 정보객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="ENGINE")
public class EngineStartEndTimeVo {

	@PrimaryKey(seq = 1)
	private String ENGINE_ID;
	
	@DateTime
	private Long START_DATE;
	@DateTime
	private Long END_DATE;

	public String getENGINE_ID() {
		return ENGINE_ID;
	}

	public void setENGINE_ID(String ENGINE_ID) {
		this.ENGINE_ID = ENGINE_ID;
	}

	public Long getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(Long START_DATE) {
		this.START_DATE = START_DATE;
	}

	public Long getEND_DATE() {
		return END_DATE;
	}

	public void setEND_DATE(Long END_DATE) {
		this.END_DATE = END_DATE;
	}
}
