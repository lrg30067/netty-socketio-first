package com.sinovoice.hcicloud.nettysocketiofirst.common.atomic;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存 cticode-->sessionid 对应表
 */
public class ConcurrentUtil {
    private static ConcurrentHashMap<String, Object> cacheTable = new ConcurrentHashMap<>();

    public static Object put(String key, Object value) {
        return cacheTable.put(key, value);
    }

    public static Object get(String key) {
        return cacheTable.get(key);
    }

    public static Object remove(String key) {
        return cacheTable.remove(key);
    }

    public static int size() {
        return cacheTable.size();
    }
}
