package com.seomse.system.server.vo;

import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerConnectVo.java
 *  설    명 : 서버 접속 정보 객체
 *
 *  작 성 자 : macle
 *  작 성 일 : 2018.03
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜섬세한사람들. All right reserved.
 */
@Table(name="SERVER")
public class ServerConnectVo {
	
	private String HOST_ADDRESS;
	private Integer API_PORT;
	private String CLIENT_CONNECT_TYPE = "I";
	private String OUT_HOST_ADDRESS;
	private Integer OUT_API_PORT;

	public String getHOST_ADDRESS() {
		return HOST_ADDRESS;
	}

	public void setHOST_ADDRESS(String HOST_ADDRESS) {
		this.HOST_ADDRESS = HOST_ADDRESS;
	}

	public Integer getAPI_PORT() {
		return API_PORT;
	}

	public void setAPI_PORT(Integer API_PORT) {
		this.API_PORT = API_PORT;
	}

	public String getCLIENT_CONNECT_TYPE() {
		return CLIENT_CONNECT_TYPE;
	}

	public void setCLIENT_CONNECT_TYPE(String CLIENT_CONNECT_TYPE) {
		this.CLIENT_CONNECT_TYPE = CLIENT_CONNECT_TYPE;
	}

	public String getOUT_HOST_ADDRESS() {
		return OUT_HOST_ADDRESS;
	}

	public void setOUT_HOST_ADDRESS(String OUT_HOST_ADDRESS) {
		this.OUT_HOST_ADDRESS = OUT_HOST_ADDRESS;
	}

	public Integer getOUT_API_PORT() {
		return OUT_API_PORT;
	}

	public void setOUT_API_PORT(Integer OUT_API_PORT) {
		this.OUT_API_PORT = OUT_API_PORT;
	}
}
