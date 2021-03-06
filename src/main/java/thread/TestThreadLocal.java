package thread;

/**
 * @Description:
 * 	参考https://www.zhihu.com/question/23089780/answer/62097840
 * @author: Lucifer
 * @date: 2018/3/29 16:53
 */
public class TestThreadLocal {
	private static final ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};

	private static final  ThreadLocal<String> str = new ThreadLocal<String>(){
		@Override
		protected String initialValue() {
			return super.initialValue();
		}
	};

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			new Thread(new MyThread(i)).start();
		}
	}

	static class MyThread implements Runnable {
		private int index;

		public MyThread(int index) {
			this.index = index;
		}

		public void run() {
			System.out.println("线程" + index + "的初始value:" + value.get());
			for (int i = 0; i < 10; i++) {
				value.set(value.get() + i);
			}
			System.out.println("线程" + index + "的累加value:" + value.get());
		}
	}
}
