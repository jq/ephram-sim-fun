package cache;

import mainpackage.*;

/**
 *
 * @author hillboy
 */
public class Cache_FIFO extends Cache {

    /**
     * Problem1: cache management
     *
     * Algorithm1: FIFO
     */
    // a new data add to cache
    public boolean addToCache(Data data) {
        if(inCache(data))return true;
        if (cacheItems.size() == cachesize) {
            cacheItems.removeFirst();
        }
        cacheItems.addLast(data);
        return true;
    }
}
