import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @Description:数组和List之间的转化
 * @author: Lucifer
 * @date: 2016/10/10 15:00
 */
public class ParseOfArrayAndList {
    public static void main(String[] args) {

    }

    /**
     *使用Arrays.asList
     */
    public static  void arrayToList1(){
        String[] array = new String[] {"1", "2", "3", "4", "5", "6"};
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(array));
        System.out.println(list);
    }

    /**
     * 使用Collections.addAll
     * 或者list.addAll
     */
    public static  void arrayToList2(){
        String[] array = new String[] {"1", "2", "3", "4", "5", "6"};
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, array);
        System.out.println(list);
        //或者
        list.addAll(Arrays.asList(array));
    }

    /**
     * java8方式
     * （后续补充）
     */
    public static void arrayToList3(){

    }


    /**
     * listToArray
     */
    public static void listToArray1(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        String[] array = new String[list.size()];
        list.toArray(array);
        for (String string : array){
            System.out.println(string);
        }
    }

}
