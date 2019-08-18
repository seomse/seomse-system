package com.seomse.system.server.vo;

import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerEngineVo.java
 *  설    명 : 엔진실행용 정보
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017 ~ 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="SERVER_CONFIG")
public class ServerConfigVo {

	@PrimaryKey(seq = 1)
	private String SERVER_ID;

	@PrimaryKey(seq = 2)
	private String CONFIG_KEY;

	private String CONFIG_VALUE;

	public String getSERVER_ID() {
		return SERVER_ID;
	}

	public void setSERVER_ID(String SERVER_ID) {
		this.SERVER_ID = SERVER_ID;
	}

	public String getCONFIG_KEY() {
		return CONFIG_KEY;
	}

	public void setCONFIG_KEY(String CONFIG_KEY) {
		this.CONFIG_KEY = CONFIG_KEY;
	}

	public String getCONFIG_VALUE() {
		return CONFIG_VALUE;
	}

	public void setCONFIG_VALUE(String CONFIG_VALUE) {
		this.CONFIG_VALUE = CONFIG_VALUE;
	}
}
