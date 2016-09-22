package entity;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/9/8 14:30
 */
public class TestVo{

    private String name;
    private String gender;

    public String getName() {
        return name==null?"null123":name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
