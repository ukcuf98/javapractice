package es;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/9/8 14:30
 */
public class ElasticsearchUtil {
    /**
     * 索引集群名称
     */
    private static final String ES_INDEX_NAME = "escluster";
    static Map<String, String> m = new HashMap<String, String>();
    /**
     * 设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，
     */
    static Settings settings = ImmutableSettings.settingsBuilder().put(m)
            .put("cluster.name", ES_INDEX_NAME)
            .put("client.transport.sniff", true).build();

    private static TransportClient client;

    static {
        try {
            Class<?> clazz = Class.forName(TransportClient.class.getName());
            Constructor<?> constructor = clazz
                    .getDeclaredConstructor(Settings.class);
            constructor.setAccessible(true);
            client = (TransportClient) constructor.newInstance(settings);
            //测试10.2.1.195
			client.addTransportAddress(new InetSocketTransportAddress(
					"10.2.1.195", 9300));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 取得实例
    public static synchronized TransportClient getTransportClient() {
        return client;
    }
}
