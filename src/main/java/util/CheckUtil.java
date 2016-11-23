package util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:校验工具类
 * @author: Lucifer
 * @date: 2016/11/23 14:10
 */
public class CheckUtil {
    /**
     * 字符串转为Integer
     * @param str
     * @return
     */
    public static Integer parseInteger(String str){
        if(StringUtils.isBlank(str)){
            return  null;
        }
        try {
            Integer result = Integer.parseInt(str);
            return result;
        }catch (Exception e){
            return null;
        }
    }
}
