import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:日期时间操作/转化/格式化
 * @author: Lucifer
 * @date: 2016/10/25 9:28
 */
public class DateOrTimeOper {

    public static void main(String[] args) {
//        getDayOfWeek();
//        getTimeByCalendar();
        sdfFormate();
    }

    /**
     * 取当前星期几
     */
    public static void getDayOfWeek(){
        //method1
        Date date=new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        System.out.println("method1:"+dateFm.format(date));
        //method2
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        System.out.println("method2:"+weekDays[w]);
    }

    /**
     * Calendar使用
     */
    public static void getTimeByCalendar(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份(月份需要+1)
        int day=cal.get(Calendar.DATE);//获取日
        int amOrPm = cal.get(Calendar.AM_PM);//获取上下午
        System.out.println(amOrPm == Calendar.PM ? "pm":"am");
        int hourOfTheDay = cal.get(Calendar.HOUR_OF_DAY);//获取当前hour(24h制)
        System.out.println(hourOfTheDay);
        int hour=cal.get(Calendar.HOUR);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天(周日为0)
        System.out.println("现在的时间是：公元"+year+"年"+month+"月"+day+"日      "+hour+"时"+minute+"分"+second+"秒       星期"+WeekOfYear);
    }

    /**
     * SimpleDateFormat格式化
     */
    public static void sdfFormate(){
        /**
         *
         G 年代标志符
         y 年
         M 月
         d 日
         h 时 在上午或下午 (1~12)
         H 时 在一天中 (0~23)
         m 分
         s 秒
         S 毫秒
         E 星期
         D 一年中的第几天
         F 一月中第几个星期几
         w 一年中第几个星期
         W 一月中第几个星期
         a 上午 / 下午 标记符
         k 时 在一天中 (1~24)
         K 时 在上午或下午 (0~11)
         z 时区
         */
        SimpleDateFormat sdf = new SimpleDateFormat("G yyyy-MM-dd HH:mm:ss:SSSS \n周几：E \n一年中的第几天：D \n一月中的第几个星期F w W");
        System.out.println(sdf.format(new Date()));
    }


}
