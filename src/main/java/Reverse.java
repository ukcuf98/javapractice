import java.util.*;

/**
 * @Description:反转操作
 * @author: Lucifer
 * @date: 2016/10/10 11:27
 */
public class Reverse {

    public static void main(String[] args) {
//        reverseArrayList(new String[]{"a","b","c"});
//        reverseLinkedList(new String[]{"a","b","c"});
        reverseArray(new String[]{"a","b","c"});
    }

    /**
     * 反转ArrayList
     * @param arr
     */
    public static void reverseArrayList(String[] arr) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        Collections.reverse(list);
        System.out.println(list);
    }

    /**
     * 反转LinkedList
     * @param args
     */
    public static void reverseLinkedList(String[] args) {
        LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < args.length; i++) {
            list.add(args[i]);
        }
        Collections.reverse(list);
        System.out.println(list);
    }

    /**
     * 反转数组
     * @param args
     */
    public static void reverseArray(String[] args) {
        String tmp = null;
        for (int i = 0; i < args.length/2; i++) {
            tmp = args[i];
            args[i] = args[args.length-1-i];
            args[args.length-1-i] = tmp;
        }
        System.out.println(Arrays.toString(args));
    }
}
