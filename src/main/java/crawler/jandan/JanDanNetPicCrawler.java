package crawler.jandan;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
/**
 * @Description:图片抓取
 * @author: Lucifer
 * @date: 2016/11/22 10:54
 */
public class JanDanNetPicCrawler {
	// 起始页码
	private static final int page = 7910;
	private static final int page_start = 7909;
	private static final String pictype = "pic/";//"ooxx/"或"pic/"
	private static final String subpath = "wuliao/";//"meizi/"或"wuliao/"
	private static final String pictypename = "无聊图";//"无聊图/"或"妹子图/"
	public static void main(String[] args) {
		// HttpClient 超时配置
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD)
				.setConnectionRequestTimeout(6000).setConnectTimeout(6000)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig).build();
		System.out.println("5秒后开始抓取煎蛋"+pictypename+"……");
		for (int i = page; i > page_start; i--) {
			// 创建一个GET请求http://jandan.net/ooxx/page-1633#comments
			//http://jandan.net/pic/page-7909#comments
			HttpGet httpGet = new HttpGet("http://jandan.net/"+pictype+"page-" + i);
			httpGet.addHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
			httpGet.addHeader(
					"Cookie",
					"_gat=1; nsfw-click-load=off; gif-click-load=on; _ga=GA1.2.1861846600.1423061484");
			try {
				// 不敢爬太快
				Thread.sleep(5000);
				// 发送请求，并执行
				CloseableHttpResponse response = httpClient.execute(httpGet);
				InputStream in = response.getEntity().getContent();
				String html = Utils.convertStreamToString(in);
				// 网页内容解析
				new Thread(new JianDanHtmlParser(html, i,subpath)).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}