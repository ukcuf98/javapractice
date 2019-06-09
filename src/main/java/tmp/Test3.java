package tmp;

public class Test3 {

    private static final Integer CONSTANT_ONE = 1;

    public void g(Test1 one) {
        System.out.println("g(C1)");
        one.f();
    }
    public void g(Test2 two) {
        System.out.println("g(C2)");
        two.f();
    }

    public void method1() throws NullPointerException{
        //doSomething
        throw new NullPointerException();
    }

    public void method2(){
        try{
            //doSomething
            throw new NullPointerException();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("程序结束");
        }
    }
}
