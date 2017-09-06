package command;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;


/**
 * @Description:
 * @author: Lucifer
 * @date: 2017/6/1 20:16
 */
public class CommandTest {
    public static void main(String[] args) {
        String str = "\\U591a\\U4e00\\U5206\\U771f\\U8bda\\Uff0c\\U5c11\\U4e00\\U70b9\\U5957\\U8def\\Ud83d\\Ude14";
//        String test = StringEscapeUtils.unescapeJava(str);
        try {
            String test = new String(str.getBytes(),"UTF-8");
            System.out.println(test.trim());
        }catch (Exception e){

        }


//        String test = decode(str);

//        for (int i = 1; i < 100; i++) {
//            String address = "10.1.71.";
//            address = address + i;
////            System.out.println(address);
//            try {
//                boolean flag = get(address);
//                if (flag) {
//                    System.out.println("====" + address);
//                }
//            } catch (Exception e) {
//
//            }
//        }
    }

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

    public static boolean get(String address) throws IOException {
        Process process = Runtime.getRuntime().exec("ping " + address);
        String returnMsg = "";

//        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
//        String inline;
//        while ((inline = br.readLine()) != null) {
//            String string = new String(inline.getBytes());
//            returnMsg += string;
//        }
        InputStreamReader r = new InputStreamReader(process.getInputStream(), "gbk");
        LineNumberReader returnData = new LineNumberReader(r);

        String line = "";
        while ((line = returnData.readLine()) != null) {
//            System.out.println(line);
            returnMsg += line;
        }
        if (returnMsg.indexOf("TTL") >= 0) {
//            System.out.println("与 " + address + " 连接畅通.");
            return true;
        } else {
//            System.out.println("与 " + address + " 连接不畅通.");
            return false;
        }
    }
}
