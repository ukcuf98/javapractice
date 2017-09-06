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

    /**
     * 转义
     * @param unicodeStr
     * @return
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                        .charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(
                                unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    public static void main(String[] args) {
        String str = "\\U591a\\U4e00\\U5206\\U771f\\U8bda\\Uff0c\\U5c11\\U4e00\\U70b9\\U5957\\U8def\\Ud83d\\Ude14";
        System.out.println(decode(str));
    }
}
