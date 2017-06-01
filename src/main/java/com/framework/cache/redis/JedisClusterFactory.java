package com.framework.cache.redis;

import com.framework.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: JedisClusterFactory
 * @Description: redis集群客户端连接工厂类
 * @author eriz^zk
 * @date 2016-12-13 13:32:30
 */
public class JedisClusterFactory {

	private static Logger log = LoggerFactory.getLogger(JedisClusterFactory.class);
    //redis连接
	private JedisCluster jedisCluster = null;
    //集群配置信息从nfsq-framework.properties文件中获取redisClusterHosts
	private String clusters = Config.properties.getProperty("redisClusterHosts", "");
    //最大钝化数
	private int maxIdle = 5;
	//最大连接数
	private int maxTotal = Integer.valueOf(Config.properties.getProperty("maxTotal", "50"));
	//最小钝化数
	private int minIdle = 5;
	//最大等待时间
	private int maxWaitMillis = Integer.valueOf(Config.properties.getProperty("maxWaitMillis", "10000"));
	private boolean isBorrow = true;

	//静态工厂方法
	public static JedisClusterFactory getInstance() {
		return single;
	}
	private static final JedisClusterFactory single = new JedisClusterFactory();

	private  JedisClusterFactory() {

		if("".equals(clusters)||"${redisClusterHosts}".equals(clusters)){
			log.warn("jedis cluster ip config is empty , please config nfsq-framework.properties[redisClusterHosts]");
		}else {
			try {
				Set<HostAndPort> jedisClusterNodes = new HashSet<>();
				String host = "";
				int port = 6379;
				String[] ipArr = clusters.split(",");
				for (String ipPort : ipArr) {
					String[] hostPort = ipPort.split(":");
					host = hostPort[0];
					if(hostPort.length==2) port = Integer.parseInt(hostPort[1]);
					jedisClusterNodes.add(new HostAndPort(host, port));
				}
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxIdle(this.maxIdle);
				config.setMaxTotal(this.maxTotal);
				config.setMinIdle(this.minIdle);
				config.setMaxWaitMillis(this.maxWaitMillis);
				config.setTestOnBorrow(this.isBorrow);
				config.setTestOnReturn(true);
				jedisCluster = new JedisCluster(jedisClusterNodes, config);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
	}


	public void destroy() {
		if (jedisCluster != null) {

			 jedisCluster.close();
		}
	}

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}


	/**
	 * @param clusters
	 */
	public void setClusters(String clusters) {
		this.clusters = clusters;
	}

	/**
	 * @param maxIdle
	 */
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * @param maxTotal
	 */
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	/**
	 * @param minIdle
	 */
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	/**
	 * @param maxWaitMillis
	 */
	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	/**
	 * @param isBorrow
	 */
	public void setBorrow(boolean isBorrow) {
		this.isBorrow = isBorrow;
	}

}
