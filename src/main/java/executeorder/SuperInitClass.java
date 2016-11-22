package executeorder;

/**
 * @Description:代码执行顺序父类
 * @author: Lucifer
 * @date: 2016/11/3 9:53
 */
public class SuperInitClass {
    static {
        System.out.println("SuperInitClass->静态代码块 1");
    }

    static String s1 = "SuperInitClass->静态变量s1";

    static String s2 = getS2();

    static {
        System.out.println("SuperInitClass->静态代码块 2");
        System.out.println("SuperInitClass->静态代码块 2->访问static String s1=" + s1);
    }

    String s3 = "SuperInitClass->成员变量s3";
    String s4 = getS4();

    People people = new People();

    {
        System.out.println("SuperInitClass->构造方法代码块");
        System.out.println("SuperInitClass->构造方法代码块 执行时候成员变量s3是否已经初始化了？" + (s3 != null));
    }

    SuperInitClass() {
        System.out.println("SuperInitClass->构造方法");
        System.out.println("SuperInitClass->构造方法 执行时候成员变量s3是否已经初始化了？" + (s3 != null));
    }

    static String getS2() {
        System.out.println("SuperInitClass->getS2()执行->初始化静态变量s1");
        return "SuperInitClass->初始化静态变量s1";
    }

    static String getS4() {
        System.out.println("SuperInitClass->getS4()执行->初始化成员变量s4");
        return "SuperInitClass->初始化成员变量s4";
    }
}
