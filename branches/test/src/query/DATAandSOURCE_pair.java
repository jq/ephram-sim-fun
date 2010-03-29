/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import mainpackage.*;
import java.util.*;

/**
 *
 * @author hillboy
 */
public class DATAandSOURCE_pair {

    public int src, accessTime, num;
    public Data data;
    public int fresh;

    public void init() {
        if (src == -2) {
            accessTime = Cache.cacheAccessTime;

        }
        if (src == -1) {

            accessTime = data.src.getRecordAccessTime();

        }
        if (src >= 0) {

            accessTime = data.replicas.get(src).getRecordAccessTime();

        }
        //update refresh
        fresh = 0;
        if (src == -2 && data.cacheUnappliedUpdate == 0) {
            fresh = 1;
        }
        if (src == -1) {
            fresh = 1;
        }
        if (src >= 0 && data.unappliedUpdates[src] == 0) {
            fresh = 1;
        }
    }
}
