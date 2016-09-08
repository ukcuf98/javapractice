import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description:查找字符串中重复字符
 * @author: Lucifer
 * @date: 2016/9/8 14:02
 */
public class DuplicateCharactersInString {
    public static void main(String[]args){
        duplicateCharactersInString("what are you doing");
    }

    /**
     *  查找字符串中重复
     * @param str
     */
    public static void duplicateCharactersInString(String str){
        char[]chars = str.toCharArray();
        Map<Character,Integer> map = new HashMap<Character, Integer>();
        for (char i : chars){
            Integer count = map.get(i);
            count = count==null?0:count;
            map.put(i,count+1);
        }

        Set<Character> characterSet = map.keySet();
        for (char key : characterSet){
            if(map.get(key)>1){
                System.out.println("char:("+key+")");
            }
        }
    }
}
