package es;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基础的几类常用api
 */
public class ElasticsearchUtil {
    public static RestHighLevelClient client;

    /**
     * 建立链接
     */
    public static void createConnection() {
        try {
//            String host = "127.0.0.1";
            String host = "10.2.2.101";
            Integer port = 9200;
            //加密
            String user = "elastic";
            String password = "1q2w3e4r";
            String base64 = new String(Base64.encodeBase64((user+":"+password).getBytes()));
//            String base64 = "ZWxhc3RpYzoxcTJ3M2U0cg==";
            HttpHost[] httpHosts = new HttpHost[1];
            httpHosts[0] = new HttpHost(host, port);
            RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
            //安全认证信息
            BasicHeader basicHeader0 = new BasicHeader("Authorization", "Basic "+base64);
            Header[] defaultHeaders = new Header[]{basicHeader0};
            restClientBuilder.setDefaultHeaders(defaultHeaders);
            client = new RestHighLevelClient(restClientBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证是否有客户端链接
     */
    public static void validateClient() {
        if (client == null) {
            createConnection();
        }
    }

    /**
     * 验证是否有客户端链接
     */
    public static RestHighLevelClient getClient() {
        if (client == null) {
            createConnection();
        }
        return client;
    }

    /**
     * 创建索引
     * @param indexName index
     * @return 是否成功
     */
    public static boolean createIndex(String indexName) {
        validateClient();
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try{
                client.close();
                System.out.println(client);
            }catch (Exception e){

            }
        }
        return true;
    }

    /**
     * 验证索引是否存在。
     *
     * @param indexName index
     * @return 是否成功
     */
    public static boolean indexExist(String indexName) {
        boolean flag;
        validateClient();
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest();
            getIndexRequest.indices(indexName);
            flag = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }
        return flag;
    }

    /**
     * 删除index
     *
     * @param indexName index
     * @return 是否成功
     */
    public static boolean deleteIndex(String indexName) {
        boolean flag;
        validateClient();
        if (!indexExist(indexName)) {
            return true;
        }
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request, RequestOptions.DEFAULT);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }
        return flag;
    }

    /**
     *
     * @param index
     * @param query
     * @return
     */
    public static boolean deleteByQuery(String index, QueryBuilder query){
        validateClient();
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(index);
            request.setQuery(query);
            BulkByScrollResponse response = client.deleteByQuery(request,RequestOptions.DEFAULT);
//            System.out.println(response.getStatus());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            try{
                client.close();
                System.out.println(client);
            }catch (Exception e){

            }
        }
    }

    /**
     * 删除指定索引下的所有数据
     * @param index
     * @return
     */
    public static boolean deleteByIndex(String index){
        MatchAllQueryBuilder allQueryBuilder = QueryBuilders.matchAllQuery();
        return deleteByQuery(index,allQueryBuilder);
    }

    /**
     * 单字段条件删除
     * 实现a=1
     * @param index
     * @param column
     * @param value
     * @return
     */
    public static boolean deleteBySingleColumn(String index,String column,String value){
        QueryBuilder query = QueryBuilders.matchQuery(column, value);
        return deleteByQuery(index,query);
    }

    /**
     * 根据单字段多个值条件进行删除
     * 实现a in (1,2)
     * @param index
     * @param column
     * @param value
     * @return
     */
    public static boolean deleteBySingleColumnConditon(String index, String column, List<String> value){
        QueryBuilder query = QueryBuilders.termsQuery(column, value);
        return deleteByQuery(index,query);
    }

    /**
     * 根据多字段删除
     *  实现a in (1,2) and b in (3,4)
     * @param index
     * @param param key：List
     * @return
     */
    public static boolean deleteByMultipleColumnConditon(String index, Map<String,List> param){
        BoolQueryBuilder querySql = QueryBuilders.boolQuery();
        Set<Map.Entry<String, List>> entries = param.entrySet();
        for (Map.Entry<String, List> entry : entries) {
            querySql.must(QueryBuilders.termsQuery(entry.getKey(), entry.getValue()));
        }
        return deleteByQuery(index,querySql);
    }

    /**
     * 多条件删除
     * a=1 and b=2 and c=3
     * @param index
     * @param prama
     * @return
     */
    public static boolean deleteByMultiStringColumnConditon(String index,Map<String,Object> prama){
        BoolQueryBuilder querySql = QueryBuilders.boolQuery();
        Set<Map.Entry<String, Object>> entries = prama.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            querySql.must(QueryBuilders.termsQuery(entry.getKey(), String.valueOf(entry.getValue())));
        }
        return deleteByQuery(index,querySql);
    }

    /**
     * 根据id删除
     * @param index
     * @param ids 多个逗号分隔
     * @throws Exception
     */
    public static void deleteDataByids(String index, String ids){
        validateClient();
        try{
            String[] idArr = ids.split(",");
            for (int i = 0; i < idArr.length; i++) {
                String tmpId = idArr[i];
                if(StringUtils.isNotBlank(tmpId)){
                    DeleteRequest request = new DeleteRequest(index, tmpId);
                    client.delete(request,RequestOptions.DEFAULT);
                }
            }
        }catch (Exception e){

        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }
    }

    /**
     * 根据id删除
     * @param index
     * @param ids
     * @throws Exception
     */
    public static boolean deleteDataByids(String index, List<String> ids){
        if(null == ids || ids.size() == 0){
            return true;
        }
        try{
            for (int i = 0; i < ids.size(); i++) {
                String tmpId = ids.get(i);
                if(StringUtils.isNotBlank(tmpId)){
                    DeleteRequest request = new DeleteRequest(index, tmpId);
                    client.delete(request,RequestOptions.DEFAULT);
                }
            }
        }catch (Exception e){
            return false;
        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }
        return true;
    }

    /**
     * 批量同步
     * @param index
     * @param data
     * @throws Exception
     */
    public static boolean bulkSyncMapToEs(String index, List<Map<String, Object>> data){
        validateClient();
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (Map<String, Object> map : data) {
                IndexRequest request = new IndexRequest(index);
                request.source(map);
                bulkRequest.add(request);
            }
            BulkResponse responses =client.bulk(bulkRequest, RequestOptions.DEFAULT);
            boolean flag = responses.hasFailures();
            if(flag){
                String failureMsg = responses.buildFailureMessage();
                System.out.println(failureMsg);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }
    }

    /**
     * 批量同步
     * @param index
     * @param data 数据json集合
     * @throws Exception
     */
    public static boolean bulkSyncJsonToEs(String index, List<String> data){
        validateClient();
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (String json : data) {
                IndexRequest request = new IndexRequest(index);
                request.source(json,XContentType.JSON);
                bulkRequest.add(request);
            }
            BulkResponse responses =client.bulk(bulkRequest, RequestOptions.DEFAULT);
            boolean flag = responses.hasFailures();
            if(flag){
                String failureMsg = responses.buildFailureMessage();
                System.out.println(failureMsg);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }
    }
    /**
     * 批量同步
     * @param index
     * @param data XContent数据集合
     * @throws Exception
     */
    public static boolean bulkSyncXContentToEs(String index, List<XContentBuilder> data){
        validateClient();
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (XContentBuilder json : data) {
                IndexRequest request = new IndexRequest(index);
                request.source(json);
                bulkRequest.add(request);
            }
            BulkResponse responses =client.bulk(bulkRequest, RequestOptions.DEFAULT);
            boolean flag = responses.hasFailures();
            if(flag){
                String failureMsg = responses.buildFailureMessage();
                System.out.println(failureMsg);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }
    }

    /**
     * 查索引总数
     * @param index
     * @return
     */
    public static Long queryTotalByIndex(String index){
        validateClient();
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.trackTotalHits(true);
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);
            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            Long total = response.getHits().getTotalHits().value;
            System.out.println("total:"+total);
            return total;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try{
                client.close();
            }catch (Exception e){

            }
        }

    }

    public static void main(String[] args) {
        //添加数据
        try{
//            //xcontent方式
//            List<XContentBuilder> jsonList = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
////                String json;
//                XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
//                builder.field("name","name"+i);
//                builder.field("memo","memo"+i);
//                Double[] location = {123.123,34.34};
//                builder.field("location",location);
////                json = builder.endObject();
//                builder.endObject();
//                jsonList.add(builder);
//            }
//            ElasticsearchUtil.bulkSyncXContentToEs("tmp_apitest",jsonList);
            //json方式
//            List<String> jsonList = new ArrayList<>();
//            String json;
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("name","jsonname"+0);
//            jsonObject.put("memo","jsonmemo"+0);
//            Double[] location = {123.123,34.34};
//            jsonObject.put("location",location);
//            json = jsonObject.toJSONString();
//            jsonList.add(json);
//            ElasticsearchUtil.bulkSyncJsonToEs("tmp_apitest",jsonList);
//            ElasticsearchUtil.deleteByIndex("tmp_apitest");
//            //xcontent方式
//            XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
//                    .startObject();
//            contentBuilder.field("name","test1");
//            contentBuilder.field("memo","memo1");
//            contentBuilder.endObject();
//            List<XContentBuilder> builderList = new ArrayList<>();
//            builderList.add(contentBuilder);
//            ElasticsearchUtil.bulkSyncXContentToEs("tmp_apitest",builderList);
//            XContentBuilder contentBuilder1 = XContentFactory.jsonBuilder()
//                    .startObject();
//            contentBuilder1.field("name","test2");
//            contentBuilder1.field("memo","memo2");
//            contentBuilder1.endObject();
//            List<XContentBuilder> builderList1 = new ArrayList<>();
//            builderList1.add(contentBuilder1);
//            ElasticsearchUtil.bulkSyncXContentToEs("tmp_apitest",builderList1);

            //坐标格式错误的情况,会有报错信息
//            List<String> jsonList = new ArrayList<>();
//            String json;
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("name","jsonname_"+0);
//            jsonObject.put("memo","jsonmemo_"+0);
//            Double[] location = {123.123,123.123};
//            jsonObject.put("location",location);
//            json = jsonObject.toJSONString();
//            jsonList.add(json);
//            ElasticsearchUtil.bulkSyncJsonToEs("tmp_apitest",jsonList);
            //删除数据
//            ElasticsearchUtil.deleteDataByids("tmp_apitest","bonfh3UBD2oShwWtWxH4");
            //
//            ElasticsearchUtil.createIndex("tmp_apitest");
//            ElasticsearchUtil.deleteIndex("tmp_zwq");
            ElasticsearchUtil.queryTotalByIndex("housemaketmv1");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
