import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:Json使用
 * @author: Lucifer
 * @date: 2016/8/30 17:31
 */
public class JsonItem {

    public static void main(String[] args){
        System.out.println("1234567".substring(2,4)+"1234567".indexOf("2"));
        try {
            byte buffer[] = new byte[4096];
            String s = new String(buffer, 0, 10, "GBK");
            System.out.println(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",1);
        map.put("name","name");

        List<Map<String,Object>> pics = new ArrayList<Map<String, Object>>();
        Map<String,Object> picMap1 = new HashMap<String, Object>();
        picMap1.put("type",1);
        picMap1.put("picname","图片1");
        Map<String,Object> picMap2 = new HashMap<String, Object>();
        picMap2.put("type",2);
        picMap2.put("picname","图片2");
        pics.add(picMap1);
        pics.add(picMap2);
        map.put("photos",pics);

        String toJsonStr = JSONObject.toJSONString(map);
        System.out.println(toJsonStr);

        JSONObject object = JSONObject.parseObject(toJsonStr);

        JSONArray photos = object.getJSONArray("photos");
        String photosStr =photos.toJSONString();
        System.out.println(photosStr);



    }
}
