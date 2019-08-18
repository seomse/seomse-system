package com.seomse.system.engine;

import com.seomse.commons.annotation.Priority;
import com.seomse.commons.meta.MetaDataSync;

/**
 * <pre>
 *  파 일 명 : EngineMetaSynchronizer.java
 *  설    명 : 엔진 메타데이터 동기화 장치
 *            가장먼저 실행
 *
 *
 *  작 성 자 : macle
 *  작 성 일 : 2019.05
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights  2019 by ㈜섬세한사람들. All right reserved.
 */
@Priority(seq = 0)
public class EngineMetaSynchronizer implements MetaDataSync {


    @Override
    public void init() {
        sync();
    }

    @Override
    public void update() {
        sync();
    }


    private void sync(){
        Engine engine = Engine.getInstance();
        if(engine != null){
            engine.memoryUpdate();
        }
    }

}
