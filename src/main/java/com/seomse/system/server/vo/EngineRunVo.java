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
@Table(name="ENGINE")
public class EngineRunVo {

	@PrimaryKey(seq = 1)
	private String ENGINE_ID;
	
	private Integer MEMORY_MB_MAX_VALUE;
	private Integer MEMORY_MB_MIN_VALUE;
	private String EXE_FILE_PATH;
	private String CONFIG_FILE_PATH;

	public String getENGINE_ID() {
		return ENGINE_ID;
	}

	public void setENGINE_ID(String ENGINE_ID) {
		this.ENGINE_ID = ENGINE_ID;
	}

	public Integer getMEMORY_MB_MAX_VALUE() {
		return MEMORY_MB_MAX_VALUE;
	}

	public void setMEMORY_MB_MAX_VALUE(Integer MEMORY_MB_MAX_VALUE) {
		this.MEMORY_MB_MAX_VALUE = MEMORY_MB_MAX_VALUE;
	}

	public Integer getMEMORY_MB_MIN_VALUE() {
		return MEMORY_MB_MIN_VALUE;
	}

	public void setMEMORY_MB_MIN_VALUE(Integer MEMORY_MB_MIN_VALUE) {
		this.MEMORY_MB_MIN_VALUE = MEMORY_MB_MIN_VALUE;
	}

	public String getEXE_FILE_PATH() {
		return EXE_FILE_PATH;
	}

	public void setEXE_FILE_PATH(String EXE_FILE_PATH) {
		this.EXE_FILE_PATH = EXE_FILE_PATH;
	}

	public String getCONFIG_FILE_PATH() {
		return CONFIG_FILE_PATH;
	}

	public void setCONFIG_FILE_PATH(String CONFIG_FILE_PATH) {
		this.CONFIG_FILE_PATH = CONFIG_FILE_PATH;
	}
}
