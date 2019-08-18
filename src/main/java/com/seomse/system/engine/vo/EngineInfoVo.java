package com.seomse.system.engine.vo;

import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : EngineInfoVo.java
 *  설    명 : 엔진 정보 객체
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
public class EngineInfoVo {

	@PrimaryKey(seq = 1)
	private String ENGINE_ID;

	private String SERVER_ID;
	private Integer API_PORT;

	public String getENGINE_ID() {
		return ENGINE_ID;
	}

	public void setENGINE_ID(String ENGINE_ID) {
		this.ENGINE_ID = ENGINE_ID;
	}

	public String getSERVER_ID() {
		return SERVER_ID;
	}

	public void setSERVER_ID(String SERVER_ID) {
		this.SERVER_ID = SERVER_ID;
	}

	public Integer getAPI_PORT() {
		return API_PORT;
	}

	public void setAPI_PORT(Integer API_PORT) {
		this.API_PORT = API_PORT;
	}
}
