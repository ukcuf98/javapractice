package cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * Redis缓存基类
 */
public abstract class RedisBaseCache {

	private static final Logger logger = LoggerFactory.getLogger(RedisBaseCache.class);
	private static JedisCluster jedisCluster = null;

	static {
		initClusterConfig();
	}

	private static void initClusterConfig(){
		try {
			Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();

			/*sit*/
//			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6390));
//			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6391));
//			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6392));
//			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6393));
//			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6394));
//			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6395));

			/*sut*/
			jedisClusterNodes.add(new HostAndPort("10.2.1.199", 6379));
			jedisClusterNodes.add(new HostAndPort("10.2.1.199", 6380));
			jedisClusterNodes.add(new HostAndPort("10.2.1.200", 6381));
			jedisClusterNodes.add(new HostAndPort("10.2.1.200", 6382));
			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6383));
			jedisClusterNodes.add(new HostAndPort("10.2.1.201", 6384));

			/*pro*/
//			jedisClusterNodes.add(new HostAndPort("10.9.2.175", 6379));
//			jedisClusterNodes.add(new HostAndPort("10.9.2.175", 6380));
//			jedisClusterNodes.add(new HostAndPort("10.9.2.176", 6381));
//			jedisClusterNodes.add(new HostAndPort("10.9.2.176", 6382));
//			jedisClusterNodes.add(new HostAndPort("10.9.2.177", 6383));
//			jedisClusterNodes.add(new HostAndPort("10.9.2.177", 6384));
			jedisCluster = new JedisCluster(jedisClusterNodes);
		} catch (Exception e) {
			logger.error("获取Redis集群实例错误:" + e.getMessage(), e);
		}
	}

	protected synchronized static JedisCluster getJedis(){
		if (jedisCluster  == null) {
			initClusterConfig();
		}
		return jedisCluster;
	}

	/**
	 * 设置带有过期时间的String型值，单位:秒
	 * @param key
	 * @param seconds
	 * @param value
	 * @return String
	 */
	public String putString(String key, String value, int seconds) {
		String result = null;
		try {
			result = getJedis().setex(key, seconds, value);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 判断一个key是否存在
	 * @param key
	 * @return Boolean
	 */
	protected Boolean exists(String key) {
		Boolean result = false;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.exists(key);
		} catch (Exception e) {
			return false;
		}
		return result;
	}

	/**
	 * 在某个时间点失效(以秒为单位)
	 * @param key
	 * @param seconds
	 * @return Long
	 */
	protected Long expire(String key, int seconds) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.expire(key, seconds);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 在某个Unix格式时间点失效
	 * @param key
	 * @param unixTime
	 * @return Long
	 */
	protected Long expireAt(String key, long unixTime) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.expireAt(key, unixTime);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 返回某个key剩余生存时间
	 * @param key
	 * @return Long
	 */
	protected Long ttl(String key) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.ttl(key);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
}
