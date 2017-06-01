package com.framework.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: JedisClusterUtil
 * @Description: 操作redis工具类
 * @author eriz^zk
 * @date 2016-12-13 13:32:30
 */
public class JedisClusterUtil {
    //logger : (输出日志)
	public final static Logger logger = LoggerFactory.getLogger(JedisClusterUtil.class);
    //获取连接
    private  static JedisCluster  jedisCluster = JedisClusterFactory.getInstance().getJedisCluster();

    private static String keyPrefix ="";

    /**
     * 设置一个key的过期时间（单位：秒）
     * @param key key值
     * @param seconds 多少秒后过期
     * @return 1：设置了过期时间  0：没有设置过期时间/不能设置过期时间
     */
    public static long expire(String key, int seconds) {
        if (key == null || key.equals("")) {
            return 0;
        }
        try {
            return jedisCluster.expire(keyPrefix + key, seconds);
        } catch (Exception ex) {
            logger.error("EXPIRE error[key=" + key + " seconds=" + seconds
                    + "]" + ex.getMessage(), ex);
        }
        return 0;
    }

    /**
     * 设置一个key在某个时间点过期
     * @param key key值
     * @param unixTimestamp unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
     * @return 1：设置了过期时间  0：没有设置过期时间/不能设置过期时间
     */
    public static long expireAt(String key, int unixTimestamp) {
        if (key == null || key.equals("")) {
            return 0;
        }
        try {
            return jedisCluster.expireAt(keyPrefix + key, unixTimestamp);
        } catch (Exception ex) {
            logger.error(
                "EXPIRE error[key=" + keyPrefix + key + " unixTimestamp="
                        + unixTimestamp + "]" + ex.getMessage(), ex);
        }
        return 0;
    }

    /**
     * 截断一个List
     * @param key 列表key
     * @param start 开始位置 从0开始
     * @param end 结束位置
     * @return 状态码
     */
    public static String trimList(String key, long start, long end) {
        if (key == null || key.equals("")) {
            return "-";
        }
        try {
            return jedisCluster.ltrim(keyPrefix + key, start, end);
        } catch (Exception ex) {
            logger.error("LTRIM 出错[key=" + keyPrefix + key + " start=" + start
                    + " end=" + end + "]" + ex.getMessage(), ex);
        }
        return "-";
    }

    /**
     * 检查Set长度
     * @param key
     * @return
     */
    public static long countSet(String key) {
        if (key == null) {
            return 0;
        }
        try {
            return jedisCluster.scard(keyPrefix + key);
        } catch (Exception ex) {
            logger.error("countSet error.", ex);
        }
        return 0;
    }

    /**
     * 添加到Set中（同时设置过期时间）
     * @param key key值
     * @param seconds 过期时间 单位s
     * @param value
     * @return
     */
    public static boolean addSet(String key, int seconds, String... value) {
        boolean result = addSet(key, value);
        if (result) {
            long i = expire(key, seconds);
            return i == 1;
        }
        return false;
    }

    /**
     * 添加到Set中
     * @param key
     * @param value
     * @return
     */
    public static boolean addSet(String key, String... value) {
        if (key == null || value == null) {
            return false;
        }
        try {
            jedisCluster.sadd(keyPrefix + key, value);
            return true;
        } catch (Exception ex) {
            logger.error("setList error.", ex);
        }
        return false;
    }

    /**
     * @param key
     * @param value
     * @return 判断值是否包含在set中
     */
    public static boolean containsInSet(String key, String value) {
        if (key == null || value == null) {
            return false;
        }

        try {

            return jedisCluster.sismember(keyPrefix + key, value);
        } catch (Exception ex) {
            logger.error("setList error.", ex);

        }
        return false;
    }

    /**
     * 获取Set
     * @param key
     * @return
     */
    public static Set<String> getSet(String key) {

        try {

            return jedisCluster.smembers(keyPrefix + key);
        } catch (Exception ex) {
            logger.error("getList error.", ex);

        }
        return null;
    }
    
    /**
     * 获取Set
     * @param key
     * @return
     */
    public static String getSetFirst(String key) {
    	Set<String> set = jedisCluster.smembers(keyPrefix + key);
    	Iterator<String> iter = set.iterator();
    	while(iter.hasNext()){
    		return iter.next();
    	}
    	
        return null;
    }

    /**
     * 从set中删除value
     * @param key
     * @return
     */
    public static boolean removeSetValue(String key, String... value) {

        try {

            jedisCluster.srem(keyPrefix + key, value);
            return true;
        } catch (Exception ex) {
            logger.error("getList error.", ex);

        }
        return false;
    }

    /**
     * 从list中删除value 默认count 1
     * @param key
     * @param values 值list
     * @return
     */
    public  static int removeListValue(String key, List<String> values) {
        return removeListValue(key, 1, values);
    }

    /**
     * 从list中删除value
     * @param key
     * @param count 
     * @param values 值list
     * @return
     */
    public static int removeListValue(String key, long count, List<String> values) {
        int result = 0;
        if (values != null && values.size() > 0) {
            for (String value : values) {
                if (removeListValue(key, count, value)) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     *  从list中删除value
     * @param key
     * @param count 要删除个数
     * @param value
     * @return
     */
    public static boolean removeListValue(String key, long count, String value) {

        try {

            jedisCluster.lrem(keyPrefix + key, count, value);
            return true;
        } catch (Exception ex) {
            logger.error("getList error.", ex);

        }
        return false;
    }

    /**
     * 截取List
     * @param key 
     * @param start 起始位置
     * @param end 结束位置
     * @return
     */
    public static List<String> rangeList(String key, long start, long end) {
        if (key == null || key.equals("")) {
            return null;
        }
        try {
            return jedisCluster.lrange(keyPrefix + key, start, end);
        } catch (Exception ex) {
            logger.error("rangeList 出错[key=" + keyPrefix + key + " start="
                    + start + " end=" + end + "]" + ex.getMessage(), ex);

        }
        return null;
    }

    /**
     * 检查List长度
     * @param key
     * @return
     */
    public static long countList(String key) {
        if (key == null) {
            return 0;
        }
        try {
            return jedisCluster.llen(keyPrefix + key);
        } catch (Exception ex) {
            logger.error("countList error.", ex);

        }
        return 0;
    }

    /**
     * 添加到List中（同时设置过期时间）
     * @param key key值
     * @param seconds 过期时间 单位s
     * @param value 
     * @return 
     */
    public static boolean addList(String key, int seconds, String... value) {
        boolean result = addList(key, value);
        if (result) {
            long i = expire(keyPrefix + key, seconds);
            return i == 1;
        }
        return false;
    }

    /**
     * 添加到List
     * @param key
     * @param value
     * @return
     */
    public static boolean addList(String key, String... value) {
        if (key == null || value == null) {
            return false;
        }

        try {

            jedisCluster.lpush(keyPrefix + key, value);
            return true;
        } catch (Exception ex) {
            logger.error("setList error.", ex);

        }
        return false;
    }

    /**
     * 添加到List(只新增)
     * @param key
     * @param value
     * @return
     */
    public static boolean addList(String key, List<String> list) {
        if (key == null || list == null || list.size() == 0) {
            return false;
        }
        for (String value : list) {
            addList(key, value);
        }
        return true;
    }

    /**
     * 获取List
     * @param key
     * @return
     */
    public static List<String> getList(String key) {

        try {

            return jedisCluster.lrange(keyPrefix + key, 0, -1);
        } catch (Exception ex) {
            logger.error("getList error.", ex);

        }
        return null;
    }

    /**
     * 设置HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @param value  Json String or String value
     * @return
     */
    public static boolean setHSet(String domain, String key, String value) {
        if (value == null)
            return false;

        try {

            jedisCluster.hset(domain, keyPrefix + key, value);
            return true;
        } catch (Exception ex) {
            logger.error("setHSet error.", ex);

        }
        return false;
    }

    /**
     * 获得HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @return Json String or String value
     */
    public static String getHSet(String domain, String key) {

        try {

            return jedisCluster.hget(domain, keyPrefix + key);
        } catch (Exception ex) {
            logger.error("getHSet error.", ex);

        }
        return null;
    }

    /**
     * 删除HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @return 删除的记录数
     */
    public static long delHSet(String domain, String key) {

        long count = 0;
        try {

            count = jedisCluster.hdel(domain, keyPrefix + key);
        } catch (Exception ex) {
            logger.error("delHSet error.", ex);

        }
        return count;
    }

    /**
     * 删除HashSet对象
     *
     * @param domain 域名
     * @param key    键值
     * @return 删除的记录数
     */
    public static long delHSet(String domain, String... key) {

        long count = 0;
        try {

            count = jedisCluster.hdel(domain, keyPrefix + key);
        } catch (Exception ex) {
            logger.error("delHSet error.", ex);

        }
        return count;
    }

    /**
     * 判断key是否存在
     *
     * @param domain 域名
     * @param key    键值
     * @return
     */
    public static boolean existsHSet(String domain, String key) {

        boolean isExist = false;
        try {

            isExist = jedisCluster.hexists(domain, keyPrefix + key);
        } catch (Exception ex) {
            logger.error("existsHSet error.", ex);

        }
        return isExist;
    }

    /**
     * 返回 domain 指定的哈希集中所有字段的value值
     *
     * @param domain
     * @return
     */

    public static List<String> hvals(String domain) {

        List<String> retList = null;
        try {

            retList = jedisCluster.hvals(domain);
        } catch (Exception ex) {
            logger.error("hvals error.", ex);

        }
        return retList;
    }

    /**
     * 返回 domain 指定的哈希集中所有字段的key值
     *
     * @param domain
     * @return
     */

    public static Set<String> hkeys(String domain) {

        Set<String> retList = null;
        try {

            retList = jedisCluster.hkeys(domain);
        } catch (Exception ex) {
            logger.error("hkeys error.", ex);

        }
        return retList;
    }

    /**
     * 返回 domain 指定的哈希key值总数
     *
     * @param domain
     * @return
     */
    public static long lenHset(String domain) {

        long retList = 0;
        try {

            retList = jedisCluster.hlen(domain);
        } catch (Exception ex) {
            logger.error("hkeys error.", ex);

        }
        return retList;
    }

    /**
     * 设置排序集合
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    public static boolean setSortedSet(String key, long score, String value) {

        try {

            jedisCluster.zadd(keyPrefix + key, score, value);
            return true;
        } catch (Exception ex) {
            logger.error("setSortedSet error.", ex);

        }
        return false;
    }

    /**
     * 获得排序集合
     *
     * @param key
     * @param startScore
     * @param endScore
     * @param orderByDesc
     * @return
     */
    public static Set<String> getSoredSet(String key, long startScore, long endScore,
            boolean orderByDesc) {

        try {

            if (orderByDesc) {
                return jedisCluster.zrevrangeByScore(keyPrefix + key, endScore,
                    startScore);
            } else {
                return jedisCluster.zrangeByScore(keyPrefix + key, startScore,
                    endScore);
            }
        } catch (Exception ex) {
            logger.error("getSoredSet error.", ex);

        }
        return null;
    }

    /**
     * 计算排序长度
     *
     * @param key
     * @param startScore
     * @param endScore
     * @return
     */
    public static long countSoredSet(String key, long startScore, long endScore) {

        try {

            Long count = jedisCluster.zcount(keyPrefix + key, startScore,
                endScore);
            return count == null ? 0L : count;
        } catch (Exception ex) {
            logger.error("countSoredSet error.", ex);

        }
        return 0L;
    }

    /**
     * 删除排序集合
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean delSortedSet(String key, String value) {

        try {

            long count = jedisCluster.zrem(keyPrefix + key, value);
            return count > 0;
        } catch (Exception ex) {
            logger.error("delSortedSet error.", ex);

        }
        return false;
    }

    /**
     * 获得排序集合
     *
     * @param key
     * @param startRange
     * @param endRange
     * @param orderByDesc
     * @return
     */
    public static Set<String> getSoredSetByRange(String key, int startRange,
            int endRange, boolean orderByDesc) {

        try {

            if (orderByDesc) {
                return jedisCluster.zrevrange(keyPrefix + key, startRange,
                    endRange);
            } else {
                return jedisCluster.zrange(keyPrefix + key, startRange,
                    endRange);
            }
        } catch (Exception ex) {
            logger.error("getSoredSetByRange error.", ex);

        }
        return null;
    }

    /**
     * 获得排序打分
     *
     * @param key
     * @return
     */
    public static Double getScore(String key, String member) {

        try {

            return jedisCluster.zscore(keyPrefix + key, member);
        } catch (Exception ex) {
            logger.error("getSoredSet error.", ex);

        }
        return null;
    }

    /**
     * 设置缓存值
     * @param key 主键
     * @param value  值
     * @param second 过期时间(秒)
     * @return 
     * @create: 2015年8月21日 上午8:59:12 MYF
     * @history:
     */
    public static boolean set(String key, String value, int second) {

        try {
            jedisCluster.setex(keyPrefix + key, second, value);
            return true;
        } catch (Exception ex) {
            logger.error("set error.", ex);

        }
        return false;
    }

    public static boolean set(String key, String value) {

        try {
            jedisCluster.set(keyPrefix + key, value);
            return true;
        } catch (Exception ex) {
            logger.error("set error.", ex);

        }
        return false;
    }

    public static String get(String key, String defaultValue) {

        try {
            return jedisCluster.get(keyPrefix + key) == null ? defaultValue : jedisCluster.get(keyPrefix + key);
        } catch (Exception ex) {
            logger.error("get error.", ex);

        }
        return defaultValue;
    }

    public static boolean del(String key) {

        try {
            jedisCluster.del(keyPrefix + key);
            return true;
        } catch (Exception ex) {
            logger.error("del error.", ex);

        }
        return false;
    }

    public static long incr(String key) {
        try {
            return jedisCluster.incr(keyPrefix + key);
        } catch (Exception ex) {
            logger.error("incr error.", ex);

        }
        return 0;
    }

    public static long decr(String key) {

        try {
            return jedisCluster.decr(keyPrefix + key);
        } catch (Exception ex) {
            logger.error("incr error.", ex);

        }
        return 0;
    }

}
