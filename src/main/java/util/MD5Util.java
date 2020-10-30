package util;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MD5Util {

    private static final String SIGN_PARAM_SEPARATOR = "&";

    /**
     * 组织签名数据
     * @param map
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String getSignMsg(Map map){
        Map<String, String> treeMap = new TreeMap<String, String>(map);
        StringBuffer sb = new StringBuffer();
        String key = "";
        for(Iterator<String> it = treeMap.keySet().iterator(); it.hasNext(); ){
            key = it.next();
            String value = treeMap.get(key);
            if ("".equals(value) || null == value) {
                continue;
            }
            sb.append(key).append("=").append(treeMap.get(key)).append(SIGN_PARAM_SEPARATOR);
        }
        if(sb.indexOf(SIGN_PARAM_SEPARATOR) > -1)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String byteArrayToHexString(byte[] bytes) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }

    public static String MD5Encode(String msg) {
        String resultString = null;
        try {
            resultString = msg;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] strArr = md.digest(resultString.getBytes());
            System.out.println(new String(strArr));
            resultString = byteArrayToHexString(strArr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return resultString.toUpperCase();
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("contractId","500000013");
        map.put("payer","1");
        map.put("sign","FD63478E7A8FDE8D57EB042F409FA445");
        String signMsg = getSignMsg(map);
        System.out.println(signMsg);
        System.out.println(MD5Encode(signMsg));
    }
}
