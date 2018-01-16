package es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2017/9/18 20:04
 */
public class ElasticsearchApi {

    /** ESclient */
    private static TransportClient client = ElasticsearchUtil.getTransportClient();

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ElasticsearchApi.class);

    /**
     *
     * @Title: deleteByType
     * @Description: 删除某一类型所有数据
     * @date: 2017年5月26日 下午4:18:21
     * @param index
     * @param type
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean deleteByType(String index,String type){
        //删除es中的视图
        MatchAllQueryBuilder allQueryBuilder = QueryBuilders.matchAllQuery();
        client.prepareDeleteByQuery(index).setQuery(allQueryBuilder).setTypes(type).execute().actionGet();
        return true;
    }

    /**
     *
     * @Title: queryData
     * @Description: 单字段查询
     * @date: 2017年5月10日 上午10:43:31
     * @param index
     * @param type
     * @param primaryColumn
     * @param primaryColumn_value
     * @return
     */
    public static List<String> queryData(String index, String type, String primaryColumn, String primaryColumn_value){
        try {
            BoolQueryBuilder querySql = QueryBuilders.boolQuery();
            querySql.must(QueryBuilders.termQuery(primaryColumn, primaryColumn_value));
            SearchRequestBuilder searchRequest = client.prepareSearch(index)
                    .setTypes(type).setQuery(querySql).setFrom(0).setSize(10000);
            SearchResponse actionGet = searchRequest.execute().actionGet();
            SearchHits hits = actionGet.getHits();
            String json = "";
            List<String> dataList = new ArrayList<String>();
            for (SearchHit hit : hits) {
                json = hit.getSourceAsString();
                dataList.add(json);
            }
            return dataList;
        } catch (Exception e) {
            return null;
        }

    }
    /**
     *
     * @Title: addData
     * @Description: 添加数据
     * @date: 2017年5月9日 下午3:05:52
     * @param index
     * @param type
     * @param dataJson
     * @return
     */
    public static boolean addData(String index,String type,String dataJson) throws Exception{
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        IndexRequest request = client.prepareIndex(index, type)
                .setSource(dataJson).request();
        bulkRequest.add(request);
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            logger.info(bulkResponse.buildFailureMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @Title: deleteBySingleColumnConditon
     * @Description: 删除 列=value
     * @date: 2017年5月9日 下午3:01:53
     * @param index
     * @param type
     * @param column
     * @param value
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean deleteBySingleColumnConditon(String index,String type,String column,String value) throws Exception{
        QueryBuilder query = QueryBuilders.matchQuery(column, value);
        client.prepareDeleteByQuery(index).setTypes(type).setQuery(query).execute().actionGet();
        return true;
    }

    /**
     * <pre>deleteBySingleColumnConditon(根据id=id1,id2删除)
     * 创建时间：2017年7月20日 上午10:51:57
     * 修改时间：2017年7月20日 上午10:51:57
     * 修改备注：
     * @param index
     * @param type
     * @param column
     * @param value 多个条件的值集合
     * @return</pre>
     */
    @SuppressWarnings("deprecation")
    public static boolean deleteBySingleColumnConditon(String index,String type,String column,List<String> value){
        try {
            QueryBuilder query = QueryBuilders.termsQuery(column, value);
            client.prepareDeleteByQuery(index).setTypes(type).setQuery(query).execute().actionGet();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @Title: deleteBySingleColumnConditon
     * @Description: 根据多个主键删除
     * @date: 2017/9/11 18:15
     * @param index
     * @param type
     */
    public static boolean deleteByMultipleColumnConditon(String index,String type,Map<String,List> prama){
        try {
            BoolQueryBuilder querySql = QueryBuilders.boolQuery();
            Set<Map.Entry<String, List>> entries = prama.entrySet();
            for (Map.Entry<String, List> entry : entries) {
                querySql.must(QueryBuilders.termsQuery(entry.getKey(), entry.getValue()));
            }
            client.prepareDeleteByQuery(index).setTypes(type).setQuery(querySql).execute().actionGet();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @Title: queryAllDataByIndexAndType
     * @Description: 查询所有数据
     * @date: 2017年5月18日 下午2:31:00
     * @param index
     * @param type
     * @return
     */
    public static List<String> queryAllDataByIndexAndType(String index,String type){
        try {
            BoolQueryBuilder querySql = QueryBuilders.boolQuery();
            SearchRequestBuilder searchRequest = client.prepareSearch(index)
                    .setTypes(type).setQuery(querySql).setFrom(0).setSize(100000);
            SearchResponse actionGet = searchRequest.execute().actionGet();
            SearchHits hits = actionGet.getHits();
            String json = "";
            List<String> dataList = new ArrayList<String>();
            for (SearchHit hit : hits) {
                json = hit.getSourceAsString();
                dataList.add(json);
            }
            return dataList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @Title: deleteData
     * @Description: 根据id删除数据
     * @date: 2017年6月22日 下午4:44:49
     * @param index
     * @param type
     * @param ids
     */
    @SuppressWarnings("unused")
    public static void deleteDataByids(String index,String type, String ids) throws Exception{
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            DeleteResponse response = client.prepareDelete(index, type, idArr[i])
                    .execute()
                    .actionGet();
        }
    }
//
//    /**
//     * <pre>bulkSyncToEs(批量同步es)
//     * 创建时间：2017年7月20日 上午10:46:52
//     * 修改时间：2017年7月20日 上午10:46:52
//     * 修改备注：
//     * @param list(包含三个参数，index+type:当前数据对应的es的index+type，json为组装好的json格式数据)</pre>
//     */
//    public static boolean bulkSyncToEs(List<Map<String,Object>> list) throws Exception{
//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        Map<String,Object> bean = null;
//        if(null==list || list.size()==0){
//            return true;
//        }
//        for (int i = 0; i < list.size(); i++) {
//            bean = list.get(i);
//            String beanJson = Util.null2String(bean.get("json"));
//            String index = Util.null2String(bean.get("index"));
//            String type = Util.null2String(bean.get("type"));
//            IndexRequest request = client.prepareIndex(index, type)
//                    .setSource(beanJson).request();
//            bulkRequest.add(request);
//        }
//        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
//        if (bulkResponse.hasFailures()) {
//            logger.info(bulkResponse.buildFailureMessage());
//            return false;
//        }
//        return true;
//    }

    /**
     * <pre>bulkSyncToEs(批量同步es)
     * 创建时间：2017年7月20日 上午10:56:05
     * 修改时间：2017年7月20日 上午10:56:05
     * 修改备注：
     * @param list 多条json格式数据集合
     * @param index
     * @param type</pre>
     */
    public static boolean bulkSyncToEs(List<String> list, String index, String type) throws  Exception{
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        String beanJson = null;
        if(null==list || list.size()==0){
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            beanJson = list.get(i);
            IndexRequest request = client.prepareIndex(index, type)
                    .setSource(beanJson).request();
            bulkRequest.add(request);
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            logger.info(bulkResponse.buildFailureMessage());
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("id",1);
        map.put("name","测试伟1");
        map.put("nickname","测试1");
        map.put("type",2);
        Map map1 = new HashMap();
        map1.put("id",1);
        map1.put("name","测试伟2");
        map1.put("nickname","测试2");
        map1.put("type",2);
        Map map2 = new HashMap();
        map2.put("id",1);
        map2.put("name","测试伟3");
        map2.put("nickname","测试3");
        map2.put("type",1);

        List list = new ArrayList();
        list.add(JSONObject.toJSONString(map));
        list.add(JSONObject.toJSONString(map1));
        list.add(JSONObject.toJSONString(map2));
        try {
            bulkSyncToEs(list,"testapi","testapi");
        }catch (Exception e){

        }

    }

}
