package command;

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
        for (int i = 10; i < 100; i++) {
            String address = "10.1.71.";
            address = address + i;
//            System.out.println(address);
            try {
                boolean flag = get(address);
                if (flag) {
                    System.out.println("====" + address);
                }
            } catch (Exception e) {

            }
        }
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
