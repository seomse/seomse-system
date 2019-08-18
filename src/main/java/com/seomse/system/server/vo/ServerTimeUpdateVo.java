package com.seomse.system.server.vo;

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerTimeUpdateVo.java
 *  설    명 : 시작 종료 시간 업데이트용 Vo
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="SERVER")
public class ServerTimeUpdateVo {

	@PrimaryKey(seq = 1)
	private String SERVER_ID;
	
	@DateTime
	private Long START_DATE;

	@DateTime
	private Long END_DATE;

	public String getSERVER_ID() {
		return SERVER_ID;
	}

	public void setSERVER_ID(String SERVER_ID) {
		this.SERVER_ID = SERVER_ID;
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
