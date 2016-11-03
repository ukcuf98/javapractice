package executeorder;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/11/3 9:54
 */
public class InitClass extends SuperInitClass {
    static {
        System.out.println("InitClass->静态代码块 1");
    }

    static String s1 = "InitClass->静态变量s1";

    static String s2 = getS2();

    static {
        System.out.println("InitClass->静态代码块 2");
        System.out.println("InitClass->静态代码块 2->访问static String s1=" + s1);
    }


    String s3 = "InitClass->成员变量s3";
    String s4 = getS4();
    static String s5;

    {
        System.out.println("InitClass->构造方法代码块");
        System.out.println("InitClass->构造方法代码块 执行时候成员变量s3是否已经初始化了？" + (s3 != null));
    }

    InitClass() {
        System.out.println("InitClass->构造方法");
        System.out.println("InitClass->构造方法 执行时候成员变量s3是否已经初始化了？" + (s3 != null));
    }


    static String getS2() {
        System.out.println("InitClass->getS2()执行->初始化静态变量s1");
        return "InitClass->初始化静态变量s1";
    }

    static String getS4() {
        System.out.println("InitClass->getS4()执行->初始化成员变量s4");
        return "InitClass->初始化成员变量s4";
    }

    public static void main(String[] args) {
        System.out.println("main.........run");
        System.out.println();
        System.out.println("------------InitClass initClass = new InitClass();----------------执行");
        InitClass initClass = new InitClass();
        System.out.println();
        System.out.println("------------SuperInitClass superInitClass = new InitClass();----------------执行");
        SuperInitClass superInitClass = new InitClass();
    }
}
