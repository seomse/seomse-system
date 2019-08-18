package com.seomse.system.engine.vo;

import com.seomse.jdbc.annotation.DateTime;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : EngineConfigVo.java
 *  설    명 : 엔진설정 정보객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2017.10
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2017, 2019 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="ENGINE_CONFIG")
public class EngineConfigVo {

	@PrimaryKey(seq = 1)
	private String ENGINE_ID;
	@PrimaryKey(seq = 2)
	private String CONFIG_KEY;

	private String CONFIG_VALUE;
	
	private String IS_DELETED = "N";

	@DateTime
	private Long LAST_UPDATE_TIME;

	public String getENGINE_ID() {
		return ENGINE_ID;
	}

	public void setENGINE_ID(String ENGINE_ID) {
		this.ENGINE_ID = ENGINE_ID;
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

	public String getIS_DELETED() {
		return IS_DELETED;
	}

	public void setIS_DELETED(String IS_DELETED) {
		this.IS_DELETED = IS_DELETED;
	}

	public Long getLAST_UPDATE_TIME() {
		return LAST_UPDATE_TIME;
	}

	public void setLAST_UPDATE_TIME(Long LAST_UPDATE_TIME) {
		this.LAST_UPDATE_TIME = LAST_UPDATE_TIME;
	}
}
