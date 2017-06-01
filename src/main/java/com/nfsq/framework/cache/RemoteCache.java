package com.nfsq.framework.cache;

/**
 * 远程缓存接口——相对于本地cache
 * Created by guoyongzheng on 15/3/12.
 */
public interface RemoteCache {

    /**
     * 在缓存中设置key的值为value，并在seconds秒后过期
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    String setex(final String key, final int seconds, final String value);


    /**
     * Get the value of the specified key. If the key does not exist null is returned. If the value
     * stored at key is not a string an error is returned because GET can only handle string values.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Bulk reply
     */
    String get(final String key);

    /**
     * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
     * returned. Note that even keys set with an empty string as value will return "1". Time
     * complexity: O(1)
     *
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    Boolean exists(final String key);

    /**
     * Remove the specified keys. If a given key does not exist no operation is performed for this
     * key. The command returns the number of keys removed. Time complexity: O(1)
     * @param key
     * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
     *         0 if none of the specified key existed
     */
    Long del(final String key);
}
