import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:CollectionUtils类方法使用
 * @author: Lucifer
 * @date: 2016/10/21 11:16
 */
public class CollectionUtilTest {

    public static void main(String[] args) {

        List<Integer> a = new ArrayList<Integer>();
        List<Integer> b = null;
        List<Integer> c = new ArrayList<Integer>();
        c.add(5);
        c.add(6);

        //判断集合是否为空
        System.out.println(CollectionUtils.isEmpty(a));
        System.out.println(CollectionUtils.isEmpty(b));
        System.out.println(CollectionUtils.isEmpty(c));

        //判断集合是否不为空
        System.out.println(CollectionUtils.isNotEmpty(a));
        System.out.println(CollectionUtils.isNotEmpty(b));
        System.out.println(CollectionUtils.isNotEmpty(c));

        //两个集合间的操作
        List<Integer> e = new ArrayList<Integer>();
        e.add(2);
        e.add(1);
        List<Integer> f = new ArrayList<Integer>();
        f.add(1);
        f.add(2);
        List<Integer> g = new ArrayList<Integer>();
        g.add(12);
        //比较两集合值
        System.out.println(CollectionUtils.isEqualCollection(e,f));
        System.out.println(CollectionUtils.isEqualCollection(f,g));

        List<Integer> h = new ArrayList<Integer>();
        h.add(1);
        h.add(2);
        h.add(3);;
        List<Integer> i = new ArrayList<Integer>();
        i.add(3);
        i.add(3);
        i.add(4);
        i.add(5);
        //并集
        System.out.println(CollectionUtils.union(i,h));
        //交集
        System.out.println(CollectionUtils.intersection(i,h));
        //交集的补集
        System.out.println(CollectionUtils.disjunction(i,h));
        //e与h的差
        System.out.println(CollectionUtils.subtract(h,i));
        System.out.println(CollectionUtils.subtract(i,h));
    }
}
