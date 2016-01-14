package com.zto.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

public class RedisHelper implements InitializingBean {
	private static int TIME = 2592000;

	private JedisPool pool;

	private int maxIdle;

	private boolean testOnBorrow;

	private String host;

	private int port;

	private int timeout;

	private String password;

	private int maxTotal;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(maxIdle);
			config.setMaxTotal(maxTotal);
			config.setTestOnBorrow(testOnBorrow);
			pool = new JedisPool(config, host, port, timeout, password);

		}
	}

	public String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return value;
	}

	public String setMap(String key, Map<String, String> hash) {
		String map = null;

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			map = jedis.hmset(key, hash);
			// map key的个数
			System.out.println("map的key的个数" + jedis.hlen("1"));
			// map key
			System.out.println("map的key" + jedis.hkeys("1"));
			// map value
			System.out.println("map的value" + jedis.hvals("1"));
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return map;
	}

	// 删除
	public void del(String key, String begin, String end) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.hdel(key, begin, end);
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}

	public Map<String, String> getMapList(String key, String begin, String end) {
		Map<String, String> map = new HashMap<String, String>();

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String b = jedis.hget(key, "begin");
			String e = jedis.hget(key, "end");
			if (b != null && e != null) {
				map.put("begin", b);
				map.put("end", e);
			}
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return map;
	}

	public long llen(String key) {
		long size = 0L;
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			size = jedis.llen(key).longValue();
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return size;
	}

	public void set(String key, String value) {
		set(key, value, TIME);
	}

	public void set(String key, String value, int s) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			value = jedis.set(key, value);
			jedis.expire(key, s);
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}

	public List<Object> lpushBtach(String key, List<String> data) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Pipeline p = jedis.pipelined();
			for (String s : data) {
				p.rpush(key, s);
			}
			return p.syncAndReturnAll();
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return new ArrayList<Object>();
	}

	public List<Object> sadd(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Pipeline p = jedis.pipelined();
			for (String member : members) {
				p.sadd(key, member);
			}
			return p.syncAndReturnAll();
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return new ArrayList<Object>();
	}

	public void srem(String key, String... members) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Pipeline p = jedis.pipelined();
			for (String member : members) {
				p.srem(key, member);
			}
			p.sync();
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}

	public List<Object> lindex(List<String[]> keys) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Pipeline p = jedis.pipelined();
			for (String[] e : keys) {
				p.lindex(e[0].getBytes(), Integer.parseInt(e[1]));
			}
			return p.syncAndReturnAll();
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return new ArrayList<Object>();
	}

	public List<byte[]> lrange(String key) {
		List<byte[]> values = new ArrayList<byte[]>();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			values = jedis.lrange(key.getBytes(), 0, -1);
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return values;
	}

	public List<String> blpop(String key) {
		List<String> values = new ArrayList<String>();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			values = jedis.blpop(300, new String[] { key });
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return values;
	}

	public Long rpush(String key, String[] value) {
		Jedis jedis = null;
		Long size = null;
		try {
			jedis = pool.getResource();
			size = jedis.rpush(key, value);
		} catch (Exception e) {
			jedis.close();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return size;
	}

	public static int getTIME() {
		return TIME;
	}

	public static void setTIME(int tIME) {
		TIME = tIME;
	}

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

}
