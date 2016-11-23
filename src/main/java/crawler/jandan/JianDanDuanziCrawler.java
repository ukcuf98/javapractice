package crawler.jandan;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.util.Date;

/**
 * @Description:段子抓取
 * @author: Lucifer
 * @date: 2016/11/22 10:54
 */
public class JianDanDuanziCrawler {
	// 起始页码
	private static final int page_start = 1001;
	private static final int page_end = 1500;
	private static final String type = "duan/";//"ooxx/"或"pic/"
	private static final String subpath = "duan/";//"meizi/"或"wuliao/"
	private static final String pictypename = "段子";//"无聊图/"或"妹子图/"
	public static void main(String[] args) {
		// HttpClient 超时配置
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD)
				.setConnectionRequestTimeout(6000).setConnectTimeout(6000)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig).build();
		System.out.println("5秒后开始抓取煎蛋"+pictypename+"……");
		for (int i = page_start; i <= page_end; i++) {
			// 创建一个GET请求http://jandan.net/duan/page-1657
			Date date = new Date();
			HttpGet httpGet = new HttpGet("http://jandan.net/"+type+"page-" + i);
			httpGet.addHeader(
					"User-Agent",
//					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
			httpGet.addHeader(
					"Cookie",
					"jdna=01b0531fab6a989460dd1b231010b496#"+date.getTime());
			try {
				// 发送请求，并执行
				CloseableHttpResponse response = httpClient.execute(httpGet);
				InputStream in = response.getEntity().getContent();
				String html = Utils.convertStreamToString(in);
				// 网页内容解析
				new Thread(new JianDanHtmlParser(html, i,subpath)).start();
				//
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}