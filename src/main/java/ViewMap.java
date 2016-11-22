import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description:查看map
 * @author: Lucifer
 * @date: 2016/11/3 10:55
 */
public class ViewMap {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("1","a");
        map.put("2","b");
        map.put("3","c");
        //method1
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        //method2
        for (String key:map.keySet()){
            System.out.println(key+":"+map.get(key));
        }
        //method3
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
