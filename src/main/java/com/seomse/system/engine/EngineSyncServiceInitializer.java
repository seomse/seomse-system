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
package com.seomse.system.engine;

import com.seomse.commons.annotation.Priority;
import com.seomse.commons.config.Config;
import com.seomse.sync.SyncService;

/**
 * engine memory sync service
 * in memory update
 *
 * @author macle
 */
@Priority(seq = 0) //동기화 서비스보다 먼저실행되어야하면 seq 를 -로 해서 입력
public class EngineSyncServiceInitializer implements EngineInitializer{
    @Override
    public void init() {
        SyncService syncService = new SyncService();
        //지연된 시작
        syncService.setDelayStartTime(Config.getLong("sync.service.sleep.time", 3600000L));
        syncService.start();
    }
}
