package com.seomse.system.commons;

import com.seomse.commons.utils.string.StringArray;
import com.seomse.jdbc.JdbcQuery;

import java.util.List;

/**
 * <pre>
 *  파 일 명 : CommonCodes.java
 *  설    명 : 공통코드관련 유틸성 클래스
 *  작 성 자 : macle
 *  작 성 일 : 2019.10.27
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 ㈜섬세한사람들. All right reserved.
 */
public class CommonCodes {

    public static String [] getCodes(String codeCategoryId){

        if(!JdbcQuery.isRowData("SELECT CODE_CATEGORY_ID FROM T_COMMON_CODE_CATEGORY WHERE CODE_CATEGORY_ID='" + codeCategoryId +"' AND DEL_FG='N'")){
          return StringArray.EMPTY_STRING_ARRAY;
        }

        List<String> codeList = JdbcQuery.getStringList("SELECT CODE FROM T_COMMON_CODE WHERE CODE_CATEGORY_ID='" + codeCategoryId +"' AND DEL_FG='N' ORDER BY ORD_SEQ");
        if( codeList.size() == 0){
            return StringArray.EMPTY_STRING_ARRAY;
        }
        String [] result = codeList.toArray(new String[0]);
        codeList.clear();
        return result;
    }

}
