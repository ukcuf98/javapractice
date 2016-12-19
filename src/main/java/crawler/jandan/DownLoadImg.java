package crawler.jandan;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.jdbc.core.JdbcTemplate;
import util.SpringUtil;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/11/26 21:41
 */
public class DownLoadImg {

    private static final int page_start = 1210;
    private static final int page_end = 1500;
    private static final String subpath = "meizi/";
    private static final String basePath = "E:/jiandan/";
    private static CloseableHttpClient httpclient;
    private static final int TIMEOUT_SECONDS = 5;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    private static final int POOL_SIZE = 120;
    private static Set<String> yuMingFilterSet = new HashSet<String>();

    public static void main(String[] args) {
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_SECONDS * 1000)
                .setConnectTimeout(TIMEOUT_SECONDS * 1000).build();

        initSet();
        JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtil
                .getBean("jdbcTemplate");
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File subdir = new File(basePath+subpath);
        if(!subdir.exists()){
            subdir.mkdirs();
        }
        String tableName = "";
        if(subpath.equals("meizi/")){
            tableName = "meizipic";
        }else if(subpath.equals("wuliao/")){
            tableName = "wuliaopic";
        }
        String sql = "select * from "+tableName+" where page>=? and page<=? order by page,refid";
        List<Map<String,Object>> dataList = jdbcTemplate.queryForList(sql,new Object[]{page_start,page_end});
        if(null != dataList && dataList.size()>0){
            System.out.println(dataList.size());
        }
        String imgUrls = "";
        Integer refid = null;
        int page = 0;
        boolean filterFlag = false;
        int count =0;
        for(Map<String,Object> tmpMap : dataList){
            httpclient = HttpClients.custom().setUserAgent(USER_AGENT).setMaxConnTotal(POOL_SIZE)
                    .setMaxConnPerRoute(POOL_SIZE).setDefaultRequestConfig(defaultRequestConfig).build();
            imgUrls = (String) tmpMap.get("imgurls");
            refid = (Integer) tmpMap.get("refid");
            if(StringUtils.isBlank(imgUrls)){
                continue;
            }
            page = Integer.parseInt(tmpMap.get("page").toString());
            String[] imgUrlArr = imgUrls.split(";");
            for (String tmpUrl:imgUrlArr){
                count++;
                System.out.println("第"+count+"张图片;page:"+page+";refid:"+refid+";url:"+tmpUrl);
                if(!tmpUrl.startsWith("http") && !tmpUrl.startsWith("https")){
                    continue;
                }
                filterFlag = checkIsFiletered(tmpUrl);
                if(filterFlag){
                    continue;
                }
                String imageName = tmpUrl.substring(tmpUrl.lastIndexOf("/")+1);
                int sonPath = getCurrentPageRange(page);
                File sondir = new File(basePath+subpath+sonPath+"/");
                if(!sondir.exists()){
                    sondir.mkdirs();
                }
                if(imageName.indexOf(".")<0){
                    imageName = imageName+".jpg";
                }
                tmpUrl = tmpUrl.replaceAll("/bmiddle","/large");
                tmpUrl = tmpUrl.replaceAll("/thumb180","/large");
                tmpUrl = tmpUrl.replaceAll("/mw600","/large");
                try {
                    HttpGet httpget = new HttpGet(tmpUrl);
//                    httpget.setHeader("Referer", "http://www.google.com");

                    CloseableHttpResponse response = httpclient.execute(httpget);

                    try {
                        HttpEntity entity = response.getEntity();

                        if (response.getStatusLine().getStatusCode() >= 400) {
                            throw new IOException("Got bad response, error code = " + response.getStatusLine().getStatusCode()
                                    + " imageUrl: " + tmpUrl);
                        }
                        if (entity != null) {
                            InputStream input = entity.getContent();
                            OutputStream output = new FileOutputStream(new File(basePath+subpath+sonPath+"/"+page+"--"+imageName));
                            IOUtils.copy(input, output);
                            output.close();
                            input.close();
                        }
                    } finally {
                        response.close();

                    }
                }catch (Exception e){
                    System.out.println("下载失败url:"+tmpUrl);
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化图片地址过滤域名Set集合
     */
    private static void initSet(){
        yuMingFilterSet.add("min.us");
        yuMingFilterSet.add("huanqiu.com");
        yuMingFilterSet.add("http://photocdn.sohu.com");
        yuMingFilterSet.add("http://bbs.replays.net");
        yuMingFilterSet.add("http://7.imgbed.com");
        yuMingFilterSet.add("static.flickr.com");
        yuMingFilterSet.add("http://news.top10recent.com");
        yuMingFilterSet.add("http://p.qihoo.com");
        yuMingFilterSet.add("http://i577.photobucket.com");
        yuMingFilterSet.add("http://www.installb.com");
        yuMingFilterSet.add("http://decapitateanimals.taokitamoto.dk");
        yuMingFilterSet.add("http://img12.nnm.ru");
        yuMingFilterSet.add("http://afuckaday.tumblr.com");
        yuMingFilterSet.add("media.tumblr.com");
        yuMingFilterSet.add("http://www.soubaike.net");
        yuMingFilterSet.add("http://www.97jr.com");
        yuMingFilterSet.add("http://www.qiushibaike.com");
        yuMingFilterSet.add("http://www.asiacool.com");
        yuMingFilterSet.add("http://image1.daqi.com");
        yuMingFilterSet.add("http://www.gregfoto.com");
        yuMingFilterSet.add("http://www.fanci.cn");
        yuMingFilterSet.add("http://image.fotomen.cn");
        yuMingFilterSet.add("http://img.huabao.me");
        yuMingFilterSet.add("http://www2.ff369.com");
        yuMingFilterSet.add("http://www.48club.com");
        yuMingFilterSet.add("http://www.123kmm.com");
        yuMingFilterSet.add("deviantart.net");
        yuMingFilterSet.add("http://www.beautifullife.info");
        yuMingFilterSet.add("http://show.tom.com");
        yuMingFilterSet.add("http://img.ffffound.com");
        yuMingFilterSet.add("http://2photo.ru");
        yuMingFilterSet.add("http://images.cjb.net");
        yuMingFilterSet.add("storage.live.com");
        yuMingFilterSet.add("http://i298.photobucket.com");
    }

    /**
     * 校验是否
     * @return
     */
    private static boolean checkIsFiletered(String imgurl){
        for(String tmp : yuMingFilterSet){
            if(imgurl.indexOf(tmp)>=0){
                return true;
            }
        }
        return  false;
    }
    /**
     * 获取当前页所属路径
     * @param page
     * @return
     */
    private static int getCurrentPageRange(int page){
        int result = page/50;
        return (result+1)*50;
    }
}
