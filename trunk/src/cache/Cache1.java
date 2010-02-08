package cache;

import mainpackage.*;

/**
 *
 * @author hillboy
 */
public class Cache1 extends Cache{

     public void addToCache(Data data) {
         if(inCacheFresh(data)||inCacheStale(data))return;
        //no need to cache
        if (data.src.getRecordAccessTime() < THRESHOLD_ACCESS_TIME) {
            System.out.println("data source access time is so short that we neednt cache it!!!!!!!!!");
            return;
        }
        //remove the data with minimum M
        if (cacheItems.size() == cachesize) {
            Data minData = findMinData();
            if (data.computeM() < minData.computeM()) {
                return;
            }
            cacheItems.remove(minData);
        }
        cacheItems.addLast(data);
    }
}
