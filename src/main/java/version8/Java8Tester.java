package version8;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>
 * <p>Description:version8.lambda</p>
 * <p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>
 * <p>Company: http://www.5i5j.com</p>
 *
 * @author zwq
 * @version 1.0
 * @date 2018/11/15 14:16
 */
public class Java8Tester {
	public static void main(String args[]){
		try {
//			testTimeApi();
			testLambda();
			testStream();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void testLambda(){
		List<String> names1 = new ArrayList<>();
		names1.add("Google ");
		names1.add("Runoob ");
		names1.add("Taobao ");
		names1.add("Baidu ");
		names1.add("Sina ");

		List<String> names2 = new ArrayList<>();
		names2.add("Google ");
		names2.add("Runoob ");
		names2.add("Taobao ");
		names2.add("Baidu ");
		names2.add("Sina ");

		Java8Tester tester = new Java8Tester();
		System.out.println("使用 Java 7 语法: ");

		tester.sortUsingJava7(names1);
		System.out.println(names1);
		System.out.println("使用 Java 8 语法: ");

		tester.sortUsingJava8(names2);
		System.out.println(names2);

		String[] members = {"a","b","c"};
		Arrays.sort(members,(String s1,String s2)->(
			s1.compareTo(s2)
		));
		System.out.println(members);

		/**
		 * Thread
		 */
		new Thread(()->{
			System.out.println("Thread");
		}).start();
		/**
		 * runnable
		 */
		Runnable runnable = ()->{
			System.out.println("runnable");
		};
		runnable.run();
	}

	// 使用 java 7 排序
	private void sortUsingJava7(List<String> names){
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
	}

	// 使用 java 8 排序
	private void sortUsingJava8(List<String> names){
		Collections.sort(names, (s1, s2) -> s1.compareTo(s2));
	}

	private static void testTimeApi() throws Exception{
		/**
		 *	间隔毫秒数
		 */
		Instant start = Instant.now();
		Thread.sleep(500);
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		long millis = duration.toMillis();
		System.out.println("millis = " + millis);

		/**
		 * time API
		 */
		LocalDateTime dateTime = LocalDateTime.now();
		System.out.println("LocalDateTime:"+dateTime);
		System.out.println("DayOfWeek"+dateTime.getDayOfWeek());
		System.out.println("DayOfMonth"+dateTime.getDayOfMonth());
		System.out.println("DayOfYear"+dateTime.getDayOfYear());

		LocalDate localDate = LocalDate.now().plusDays(1);
		System.out.println("LocalDateTime:"+localDate);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		System.out.println(dateTime.format(dateTimeFormatter));
	}

	private static void testStream(){

	}
}
