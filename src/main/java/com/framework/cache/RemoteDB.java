package com.framework.cache;

/**
 * 远程DB接口——将redis作为数据库
 * Created by guoyongzheng on 15/3/17.
 */
public interface RemoteDB {
    /**
     * 在缓存数据库中设置key的值为value
     *
     * @param key
     * @param value
     * @return
     */
    String set(final String key, final String value);


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
     * @param keys
     * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
     *         0 if none of the specified key existed
     */
    Long del(final String... keys);

    Long del(final String key);
}
