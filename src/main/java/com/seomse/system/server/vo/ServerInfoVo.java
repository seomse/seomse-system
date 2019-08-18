package com.seomse.system.server.vo;

import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;

/**
 * <pre>
 *  파 일 명 : ServerInfoVo.java
 *  설    명 : 서버 정보 객체
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
public class ServerInfoVo {

	@PrimaryKey(seq = 1)
	private String SERVER_ID;

	private String HOST_ADDRESS;
	private Integer API_PORT;
	private String OS_TYPE;

	public String getSERVER_ID() {
		return SERVER_ID;
	}

	public void setSERVER_ID(String SERVER_ID) {
		this.SERVER_ID = SERVER_ID;
	}

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

	public String getOS_TYPE() {
		return OS_TYPE;
	}

	public void setOS_TYPE(String OS_TYPE) {
		this.OS_TYPE = OS_TYPE;
	}
}
