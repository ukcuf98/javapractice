package crawler.jandan;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:抓取内容处理
 * @author: Lucifer
 * @date: 2016/11/22 10:54
 */
public class JianDanHtmlParser implements Runnable {
	private String html;
	private int page;
	private String subpath;
	public JianDanHtmlParser(String html,int page,String subpath) {
		this.html = html;
		this.page = page;
		this.subpath = subpath;
	}
	@Override
	public void run() {
		System.out.println("==========第"+page+"页============");
		if(subpath.equals("wuliao/")){
			List<String> list = new ArrayList<String>();
			html = html.substring(html.indexOf("commentlist"));
			String[] images = html.split("li>");
			for (String image : images) {
				String[] ss = image.split("br");
				for (String s : ss) {
					if (s.indexOf("<img src=") > 0) {
						try{
							int i = s.indexOf("<img src=\"") + "<img src=\"".length();
							list.add(s.substring(i, s.indexOf("\"", i + 1)));
						}catch (Exception e) {
							System.out.println(s);
						}

					}
				}
			}
			for(String imageUrl : list){
				if(imageUrl.indexOf("sina")>0){
					new Thread(new JianDanImageCreator(imageUrl,page,subpath)).start();
				}
			}
		}else if(subpath.equals("duan/")){
			System.out.println(html);
		}
	}
}
