package es;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            HttpHost[] httpHosts = new HttpHost[1];
            httpHosts[0] = new HttpHost(host, port);
            RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
            //安全认证信息
//            BasicHeader basicHeader = new BasicHeader("Authorization", "Basic ZWxhc3RpYzoxcTJ3M2U0cg==");
//            Header[] defaultHeaders = new Header[]{basicHeader};
//            System.out.println(basicHeader.toString());

//            BasicHeader basicHeader0 = new BasicHeader("auth_user", "elastic");
//            BasicHeader basicHeader1 = new BasicHeader("auth_password", "1q2w3e4r");
//            Header[] defaultHeaders = new Header[]{basicHeader0,basicHeader1};

            BasicHeader basicHeader0 = new BasicHeader("Authorization", "Basic ZWxhc3RpYzoxcTJ3M2U0cg==");
            BasicHeader basicHeader1 = new BasicHeader("Content-Type", "application/json");

            BasicHeader basicHeader3 = new BasicHeader("auth_user", "elastic");
            BasicHeader basicHeader4 = new BasicHeader("auth_password", "1q2w3e4r");
            Header[] defaultHeaders = new Header[]{basicHeader0,basicHeader1,basicHeader3,basicHeader4};
            restClientBuilder.setDefaultHeaders(defaultHeaders);
            client = new RestHighLevelClient(RestClient.builder(httpHosts));
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
            System.out.println(response.getStatus());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
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
    public static void deleteDataByids(String index, String ids) throws Exception{
        validateClient();
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            String tmpId = idArr[i];
            if(StringUtils.isNotBlank(tmpId)){
                DeleteRequest request = new DeleteRequest(index, tmpId);
                client.delete(request,RequestOptions.DEFAULT);
            }
        }
    }

    /**
     * 根据id删除
     * @param index
     * @param ids
     * @throws Exception
     */
    public static boolean deleteDataByids(String index, List<String> ids) throws Exception{
        if(null == ids || ids.size() == 0){
            return true;
        }
        for (int i = 0; i < ids.size(); i++) {
            String tmpId = ids.get(i);
            if(StringUtils.isNotBlank(tmpId)){
                DeleteRequest request = new DeleteRequest(index, tmpId);
                client.delete(request,RequestOptions.DEFAULT);
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
    public static void bulkSyncMapToEs(String index, List<Map<String, Object>> data){
        validateClient();
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (Map<String, Object> map : data) {
                IndexRequest request = new IndexRequest(index);
                request.source(map);
                bulkRequest.add(request);
            }
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 批量同步
//     * @param index
//     * @param data 数据json集合
//     * @throws Exception
//     */
//    public static void bulkSyncJsonToEs(String index, List<String> data){
//        validateClient();
//        try {
//            BulkRequest bulkRequest = new BulkRequest();
//            for (String json : data) {
//                IndexRequest request = new IndexRequest(index);
//                request.source(json,XContentType.JSON);
//                bulkRequest.add(request);
//            }
//            client.bulk(bulkRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * 批量同步
     * @param index
     * @param data 数据json集合
     * @throws Exception
     */
    public static void bulkSyncJsonToEs(String index, List<XContentBuilder> data){
        validateClient();
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (XContentBuilder json : data) {
                IndexRequest request = new IndexRequest(index);
                request.source(json);
                bulkRequest.add(request);
            }
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 单字段查询
//     * @param index
//     * @param type
//     * @param primaryColumn
//     * @param primaryColumn_value
//     * @return
//     */
//    public static List<String> queryData(String index, String type, String primaryColumn, String primaryColumn_value){
//
//        return null;
//    }

    public static void main(String[] args) {
        //添加数据
        try{
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
//            ElasticsearchUtil.bulkSyncJsonToEs("tmp_apitest",jsonList);
            ElasticsearchUtil.deleteByIndex("tmp_apitest");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
