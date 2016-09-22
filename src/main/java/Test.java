import entity.TestVo;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/8/30 17:04
 */
public class Test {

    public static void main(String[] args){
        //不用new关键词创建一个类
        //方法1:Using newInstance() Method
        try {
            Class testVoClass = Class.forName("entity.TestVo");
            TestVo testVo = (TestVo)testVoClass.newInstance();
            System.out.println(testVo.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //方法2:clone
//        TestVo test1 = new TestVo();
//        TestVo test2 = test1.clone();
        //

    }
}
