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
package com.seomse.system.server;

/**
 * ServerInitializer
 * 서버가 시작 할때 시작 해야 하는 내용 이면
 * 이 클래스 implements
 *
 * Priority annotation 을 활용 하면 우선순위를 설정할 수 있음
 *
 * @author macle
 */
public interface ServerInitializer {
	
	/**
	 * init
	 */
	void init();

}
