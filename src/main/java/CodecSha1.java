import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/9/28 16:42
 */
public class CodecSha1 {

    public static void main(String[] args){
        List<String> list = new ArrayList<String>();
        list.add("abc");
        list.add("15236");
        list.add("lucifer");
        Collections.sort(list);
        String result = DigestUtils.shaHex(list.get(0)+list.get(1)+list.get(2));
        System.out.println(result);
    }
}
