/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seomse.system.server.run;

/**
 * <pre>
 *  파 일 명 : EngineRun.java
 *  설    명 : 엔진 실행
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.25
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
public interface EngineRun {

	String CONFIG_PATH_KEY = "config.xml.path";
	String LOG_CONFIG_PATH_KEY = "logback.xml.path";


	/**
	 * 엔진 시작
	 * @param engineId 엔진 ID
	 * @return message
	 */
	String start(String engineId);
	
}