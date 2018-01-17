package cache;

import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis缓存单例类
 */
public class RedisCache extends RedisBaseCache {

	private final static RedisCache instance = new RedisCache();

	private RedisCache() {}

	public static RedisCache getInstance() {
		return instance;
	}

	/**
	 * 设置String型值
	 * @param key
	 * @param value
	 * @return String
	 */
	public String putString(String key, String value) {
		String result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.set(key, value);
		} catch (Exception e) {
			return null;
		}
		return result;
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
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.setex(key, seconds, value);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 获取String型值
	 * @param key
	 * @return String
	 */
	public String getString(String key) {
		String result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.get(key);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 设置Map型值
	 * @param key
	 * @param map
	 * @return String
	 */
	public String putMap(String key, Map<String, String> map) {
		String result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.hmset(key, map);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 设置Map型值
	 * @param key
	 * @param map
	 * @param seconds 过期时间(秒)
	 * @return String
	 */
	public String putMap(String key, Map<String, String> map, int seconds) {
		String result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.hmset(key, map);
			client.expire(key, seconds);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 获取Map型值
	 * @param key
	 * @return Map<String, String>
	 */
	public Map<String, String> getMap(String key) {
		Map<String, String> result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.hgetAll(key);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 在List头部增加元素
	 * @param key
	 * @param string
	 * @return Long
	 */
	public Long putListOnLeft(String key, String string) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.lpush(key, string);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 在List头部增加元素
	 * @param key
	 * @param string
	 * @param seconds 过期时间(秒)
	 * @return Long
	 */
	public Long putListOnLeft(String key, String string, int seconds) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.lpush(key, string);
			client.expire(key, seconds);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 在List尾部增加元素
	 * @param key
	 * @param string
	 * @return Long
	 */
	public Long putListOnRight(String key, String string) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.rpush(key, string);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 在List尾部增加元素
	 * @param key
	 * @param string
	 * @param seconds 过期时间(秒)
	 * @return Long
	 */
	public Long putListOnRight(String key, String string, int seconds) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.rpush(key, string);
			client.expire(key, seconds);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 获取List型值
	 * @param key
	 * @param start
	 * @param end
	 * @return List<String>
	 */
	public List<String> getList(String key, long start, long end) {
		List<String> result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.lrange(key, start, end);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 设置Set型值
	 * @param key
	 * @param string
	 * @return Long
	 */
	public Long putSet(String key, String string) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.sadd(key, string);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 设置Set型值
	 * @param key
	 * @param string
	 * @param seconds 过期时间(秒)
	 * @return Long
	 */
	public Long putSet(String key, String string, int seconds) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.sadd(key, string);
			client.expire(key, seconds);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 获取Set型值
	 * @param key
	 * @return Set<String>
	 */
	public Set<String> getSet(String key) {
		Set<String> result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.smembers(key);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

//	/**
//	 * 设置Byte型值
//	 * @param key
//	 * @param value
//	 * @return String
//	 */
//	public String putByte(byte[] key, byte[] value) {
//		String result = null;
//		JedisCluster client = getJedis();
//		if (client == null) {
//			return result;
//		}
//		try {
//			result = client.set(key, value);
//		} catch (Exception e) {
//			return null;
//		}
//		return result;
//	}
//
//	/**
//	 * 设置Byte型值
//	 * @param key
//	 * @param value
//	 * @param value 过期时间(秒)
//	 * @return String
//	 */
//	public String putByte(byte[] key, byte[] value, int seconds) {
//		String result = null;
//		JedisCluster client = getJedis();
//		if (client == null) {
//			return result;
//		}
//		try {
//			result = client.set(key, value);
//			client.expire(key, seconds);
//		} catch (Exception e) {
//			return null;
//		}
//		return result;
//	}
//
//	/**
//	 * 获取Byte型值
//	 * @param key
//	 * @return byte[]
//	 */
//	public byte[] getByte(byte[] key) {
//		byte[] result = null;
//		JedisCluster client = getJedis();
//		if (client == null) {
//			return result;
//		}
//		try {
//			result = client.get(key);
//		} catch (Exception e) {
//			return null;
//		}
//		return result;
//	}

	/**
	 * 删除key的值
	 * @param key
	 * @return Long
	 */
	public Long del(String key) {
		Long result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.del(key);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

//	/**
//	 * 删除字节key的值
//	 * @param key
//	 * @return Long
//	 */
//	public Long del(byte[] key) {
//		Long result = null;
//		JedisCluster client = getJedis();
//		if (client == null) {
//			return result;
//		}
//		try {
//			result = client.del(key);
//		} catch (Exception e) {
//			return null;
//		}
//		return result;
//	}

	/**
	 * 清空所有
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public String flushAll() {
		String result = null;
		JedisCluster client = getJedis();
		if (client == null) {
			return result;
		}
		try {
			result = client.flushAll();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	public Boolean exists(String key) {
		return super.exists(key);
	}

	public Long expire(String key, int seconds) {
		return super.expire(key, seconds);
	}

	public Long expireAt(String key, long unixTime) {
		return super.expireAt(key, unixTime);
	}

	public Long ttl(String key) {
		return super.ttl(key);
	}

	public static void main(String[]args){
		RedisCache redisCache = RedisCache.getInstance();
		redisCache.putString("a","a1");
		String result = redisCache.getString("a");
		System.out.println(result);
//		redisCache.del("a");
//		result = redisCache.getString("a");
//		System.out.println(result);

//		for (int i=0;i<10000;i++){
//			String setResult = redisCache.putString("test_"+i,(i+1)+"",300);
//			if(!setResult.equals("OK")){
//				System.out.println("setResult:"+i+"==="+setResult);
//			}
//			redisCache.del("test_"+i);
//		}
	}
}
