package cache;

import mainpackage.*;
import java.util.*;

/**
 *
 * @author hillboy
 */
public class Cache_Q extends Cache {

    public ArrayList<Data> allItems = new ArrayList<Data>();

    public boolean addToCache(Data data) {

        if (data.src.getRecordAccessTime() < THRESHOLD_ACCESS_TIME) {
            System.out.println("data source access time is so short that we neednt cache it!!!!!!!!!");
            return false;
        }
        if (inCache(data)) {
            int i = allItems.indexOf(data);
            if (i > 0) {
                allItems.set(i, allItems.get(0));
                allItems.set(0, data);
            }
            return true;
        }
        
        int i;
        if (allItems.indexOf(data) < 0) {
            allItems.add(0, data);
            i=0;
        } else {
            i = allItems.indexOf(data);
            if (i > 0) {
                allItems.set(i, allItems.get(i - 1));
                allItems.set(i - 1, data);
            }
        }
        if(i>=cachesize)return false;
        if (cacheItems.size() == cachesize) {
            cacheItems.removeLast();
        }
        cacheItems.addFirst(data);
        return true;
    }
}
