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
package com.seomse.system.server.exception;

/**
 * 지원 하지 않는 os 유형에 대한 예외 처리
 * @author macle
 */
public class NotSupportOsTypeException extends RuntimeException{

	private static final long serialVersionUID = -7937577737311154941L;

	/**
	 * 생성자
	 * @param oyType String
	 */
	public NotSupportOsTypeException(String oyType){
		super("not support os type: " + oyType);
	}

}
