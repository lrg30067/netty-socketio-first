package com.sinovoice.hcicloud.nettysocketiofirst;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapTest {
    public static ConcurrentHashMap<String, Integer> countHashmap = new ConcurrentHashMap(16);

    @Test
    public void hashmapTest() throws Exception {
        System.out.println("111");
    }

    @Test
    public void linkedhashmapTest() throws Exception {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("a", "2");
        linkedHashMap.put("b", "2");
        linkedHashMap.put("c", "2");
        System.out.println(linkedHashMap);
    }

    @Test
    public void intTest() throws Exception {
        int lenCur;
        if (!countHashmap.containsKey("agent")) {
            countHashmap.put("agent", lenCur = 1);
        }else{
            lenCur = countHashmap.get("agent") + 1;
            countHashmap.put("agent", lenCur);
        }
        System.out.println(lenCur & (32 - 1));
        if ((lenCur & (32 - 1)) == 0 || lenCur == 1) {
            System.out.println("执行新线程");
        }
    }





}
