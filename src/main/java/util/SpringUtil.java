package util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/11/22 10:54
 */
public class SpringUtil {

    private static ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");

    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }

    public static void main(String[] args) {
        JdbcTemplate JdbcTemplate = (JdbcTemplate) SpringUtil
                .getBean("jdbcTemplate");
        System.out.println(JdbcTemplate.queryForList("select * from sys_menu1"));
    }
}
