package com.seomse.system.engine;

import com.seomse.commons.annotation.Priority;
import com.seomse.sync.SyncService;

/**
 * <pre>
 *  파 일 명 : EngineSyncServiceInitializer.java
 *  설    명 : 동기화 서비스 Initializer
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜섬세한사람들. All right reserved.
 */
@Priority(seq = 0) //동기화 서비스보다 먼저실행되어야하면 seq 를 -로 해서 입력
public class EngineSyncServiceInitializer implements EngineInitializer{
    @Override
    public void init() {
        SyncService syncService = new SyncService();
        syncService.setDelayedStart(true);
        Thread thread = new Thread(syncService);
        thread.start();
    }
}
