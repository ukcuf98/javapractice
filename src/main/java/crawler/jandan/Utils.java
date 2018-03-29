package crawler.jandan;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Description:工具
 * @author: Lucifer
 * @date: 2016/11/22 10:54
 */
public class Utils {
	private static String[] number2Chinese_s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
	private static String[] number2Chinese_s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

	public static void writeToFile(InputStream in, String path) {
		File file = new File(path);
		System.out.println("是否为文件：" + file.isFile());
		try {
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("================文件写入失败==================");
		}
	}

	/*
	 * 
	 */
	public static String convertStreamToString(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
//				sb.append(line + "/n");
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	/**
	 * 数字转汉字
	 * @param number
	 * @param suffix
	 * @return
	 */
	public static String number2Chinese(String number, String suffix){
		if(StringUtils.isBlank(number)){
			return "";
		}
//		if(number.equals("99")&& !suffix.equals("年")){
//			return "多"+suffix;
//		}
		StringBuilder result = new StringBuilder();
		int n = number.length();
		for (int i = 0; i < n; i++) {
			int num = number.charAt(i) - '0';
			if (i != n - 1 && num != 0) {
				if(num<0){
					System.out.println("number:"+number+"suffix:"+suffix);
				}
				result.append(number2Chinese_s1[num]);
				result.append(number2Chinese_s2[n - 2 - i]);
			} else {
				result.append(number2Chinese_s1[num]);
			}
//			if(result.toString().equals("一十")){//习惯特殊处理
//				result.append(number2Chinese_s2[0]);
//			}
		}
		if(StringUtils.isNotBlank(result)){
			result.append(suffix);
		}
		return result.toString();
	}
}
