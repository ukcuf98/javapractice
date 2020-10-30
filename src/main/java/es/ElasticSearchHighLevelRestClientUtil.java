package es;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;

public class ElasticSearchHighLevelRestClientUtil {

    public static RestHighLevelClient getClient(){
        RestClientBuilder restClientBuilder = RestClient.builder(
            new HttpHost("10.2.2.101", 9200, "http")
        );
        Header[] defaultHeaders = new Header[]{new BasicHeader("Authorization", "Basic ZWxhc3RpYzoxcTJ3M2U0cg==")};
        restClientBuilder.setDefaultHeaders(defaultHeaders);
        RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);
       return client;
    }

    public static boolean createIndex(String indexName,Integer number_of_shards, Integer number_of_replicas){
        try{
            RestHighLevelClient client = ElasticSearchHighLevelRestClientUtil.getClient();
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(Settings.builder()
                    .put("index.number_of_shards", number_of_shards)
                    .put("index.number_of_replicas", number_of_replicas)
            );
            //同步执行
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(createIndexResponse.isAcknowledged());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        ElasticSearchHighLevelRestClientUtil.createIndex("tmp_zwq",3,2);
    }
}
