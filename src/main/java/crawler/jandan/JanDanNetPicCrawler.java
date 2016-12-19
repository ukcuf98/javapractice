package crawler.jandan;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.util.Date;

/**
 * @Description:图片抓取
 * @author: Lucifer
 * @date: 2016/11/22 10:54
 */
public class JanDanNetPicCrawler {
	private static final int opertype = 1;//1:无聊图2、妹子图
	// 起始页码
	private static int page_start = 1;
	private static int page_end = 1;
	private static String pictype = "";//"ooxx/"或"pic/"url使用
	private static String subpath = "";//"meizi/"或"wuliao/"
	private static String pictypename = "";//"无聊图/"或"妹子图/"

	public static void initConfig(){
		if(opertype == 1){
			page_start = 1001;
			page_end = 1500;
			pictype = "pic/";//"ooxx/"或"pic/"url使用
			subpath = "wuliao/";//"meizi/"或"wuliao/"
			pictypename = "无聊图";//"无聊图/"或"妹子图/"
		}else if(opertype == 2){
			page_start = 1001;
			page_end = 1500;
			pictype = "ooxx/";//"ooxx/"或"pic/"url使用
			subpath = "meizi/";//"meizi/"或"wuliao/"
			pictypename = "妹子图";//"无聊图/"或"妹子图/"
		}
	}

	public static void main(String[] args) {
		initConfig();
		if(StringUtils.isBlank(pictype)){
			return;
		}
		// HttpClient 超时配置
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD)
				.setConnectionRequestTimeout(6000).setConnectTimeout(6000)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig).build();
		System.out.println("5秒后开始抓取煎蛋"+pictypename+"……");
		for (int i = page_start; i <= page_end; i++) {
			// 创建一个GET请求http://jandan.net/pic/page-2368
			Date date = new Date();
			HttpGet httpGet = new HttpGet("http://jandan.net/"+pictype+"page-" + i);
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
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}