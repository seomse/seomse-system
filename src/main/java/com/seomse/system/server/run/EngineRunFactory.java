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

import com.seomse.system.server.Server.OsType;
import com.seomse.system.server.exception.NotSupportOsTypeException;

/**
 * EngineRun 생성
 * @author macle
 */
public class EngineRunFactory {
	
	/**
	 * EngineRun 생성
	 * @param osType OsType
	 * @return EngineRun
	 */
	public static EngineRun newEngineRun(OsType osType){
		switch(osType){
		case UNIX:
			return new EngineRunUnix();
		case WINDOWS:
			return new EngineRunWindows();
		default:
			throw new NotSupportOsTypeException(osType.toString());
		}
	}

}
