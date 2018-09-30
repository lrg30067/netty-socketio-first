package com.sinovoice.hcicloud.nettysocketiofirst.arithmetic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 咖啡拿铁 文稿
 *
 * 2018年9月6日 18:22:01
 * mhn
 * LinkedHashMap中维护了一个entry(用来放key和value的对象)链表。在每一次get或者put的时候都会把插入的新entry，
 * 或查询到的老entry放在我们链表末尾。 可以注意到我们在构造方法中，设置的大小特意设置到max*1.4，
 * 在下面的removeEldestEntry方法中只需要size>max就淘汰，这样我们这个map永远也走不到扩容的逻辑了
 */
public class LRUMap extends LinkedHashMap {
    private int max = 0;
    private Object lock;

    public LRUMap(int max, Object lock) {

        // 取消扩容
        // size() > threshold = max * 1.4 * 0.75 = max * 1.05
        // size() > max 就开始执行 lru 算法
        super((int) ( max * 1.4f), 0.75f, true);
        this.max = max;
        this.lock = lock;
    }

    /**
     * 重写LinkedHashMap的removeEldestEntry方法即可
     * 在Put的时候判断，如果为true，就会删除最老的
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > max;
    }

    public Object getValue(Object key) {
        synchronized (lock) {
            return get(key);
        }
    }

    public void putValue(Object key, Object value) {
        synchronized (lock) {
            put(key, value);
        }
    }

    public boolean removeValue(Object key) {
        synchronized (lock) {
            return  remove(key) != null;
        }
    }

    public boolean removeAll() {
        clear();
        return true;
    }

    public static void main(String[] args) {
        Object lock = new Object();
        LRUMap lru = new LRUMap(6, lock);

        String s = "abcdefghijkl";
        for (int i = 0; i < s.length(); i++) {
            lru.put(s.charAt(i), i);
        }
        System.out.println("LRU中key为h的Entry的值为： " + lru.get('h'));
        System.out.println("LRU的大小 ：" + lru.size());
        System.out.println("LRU ：" + lru);
    }


}
