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
package com.seomse.system.commons;

import com.seomse.commons.data.NullData;
import com.seomse.jdbc.JdbcQuery;

import java.util.List;

/**
 * 공통 코드 관련 유틸성 메소드
 * @author macle
 */
public class CommonCodes {

    /**
     * code array 얻기
     * @param codeCategoryId String code category id
     * @return String []
     */
    public static String [] getCodes(String codeCategoryId){

        if(!JdbcQuery.isRowData("SELECT CODE_CATEGORY_ID FROM T_COMMON_CODE_CATEGORY WHERE CODE_CATEGORY_ID='" + codeCategoryId +"' AND DEL_FG='N'")){
          return NullData.EMPTY_STRING_ARRAY;
        }

        List<String> codeList = JdbcQuery.getStringList("SELECT CODE FROM T_COMMON_CODE WHERE CODE_CATEGORY_ID='" + codeCategoryId +"' AND DEL_FG='N' ORDER BY ORD_SEQ");
        if( codeList.size() == 0){
            return NullData.EMPTY_STRING_ARRAY;
        }
        String [] result = codeList.toArray(new String[0]);
        codeList.clear();
        return result;
    }

}
