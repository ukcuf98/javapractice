package crawler.jandan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.xsoup.Xsoup;
import util.CheckUtil;
import util.SpringUtil;

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
			Document document = Jsoup.parse(html);
			List<String> commentlist = Xsoup.compile("//ol[@class='commentlist']/li/html()").evaluate(document).list();
			System.out.println(commentlist.size());
			JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtil
					.getBean("jdbcTemplate");
			for (String li:commentlist){
				Document li_doc = Jsoup.parse(li);
				String refid = Xsoup.compile("//div/div/div[@class='text']/span[@class='righttext']/a/text()").evaluate(li_doc).toString();
				String content = Xsoup.compile("//div/div/div[@class='text']/p/html()").evaluate(li_doc).toString();
				String upvote = Xsoup.compile("//div/div/div[@class='text']/div[@class='vote']/span[@id='cos_support-"+refid+"']/text()").evaluate(li_doc).toString();
				String downvote = Xsoup.compile("//div/div/div[@class='text']/div[@class='vote']/span[@id='cos_unsupport-"+refid+"']/text()").evaluate(li_doc).toString();
//				System.out.println("id="+refid+";content:"+content+";upvote:"+upvote+";downvote:"+downvote);

				String sql_query = "select count(id) from duanzi where refid=?";
				int count = jdbcTemplate.queryForObject(sql_query,new Object[]{refid},Integer.class);
				if(count==0){
					dealDuanZi(jdbcTemplate,refid,content,upvote,downvote);
				}
			}
		}
		System.out.println("=======第"+page+"页end========");
	}

	/**
	 * 保存已抓取的段子内容
	 * @param jdbcTemplate
	 * @param refidStr
	 * @param contentStr
	 * @param upvoteStr
     * @param downvoteStr
     */
	public void dealDuanZi(JdbcTemplate jdbcTemplate,String refidStr,String contentStr,String upvoteStr,String downvoteStr){
		try {
			Integer refid = CheckUtil.parseInteger(refidStr);
			Integer upvote = CheckUtil.parseInteger(upvoteStr);
			Integer downvote = CheckUtil.parseInteger(downvoteStr);
			String sql_insert = "insert into duanzi (refid,title,content,upvote,downvote) values(?,?,?,?,?)";
			jdbcTemplate.update(sql_insert,new Object[]{refid,refidStr,contentStr,upvote,downvote});
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("page:"+page+";refid:"+refidStr);
		}
	}
}
