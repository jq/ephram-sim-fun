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
public class SolutionSimple extends Solution {

    public Data[] data;
    public int[] source;

    public SolutionSimple(int len) {
        source = new int[len];
    }

    public void getFreshAndTime() {

        for (int i = 0; i < data.length; i++) {
            //update time

            if (source[i] == -2) {
                if (Cache.cacheAccessTime > time) {
                    time = Cache.cacheAccessTime;
                }
            }
            if (source[i] == -1) {
                if (data[i].src.getRecordAccessTime() > time) {
                    time = data[i].src.getRecordAccessTime();
                }
            }
            if (source[i] >= 0) {
                if (data[i].replicas.get(source[i]).getRecordAccessTime() > time) {
                    time = data[i].replicas.get(source[i]).getRecordAccessTime();
                }
            }
            //update refresh
            if (source[i] == -2 && data[i].cacheUnappliedUpdate == 0) {
                fresh++;
            }
            if (source[i] == -1) {
                fresh++;
            }
            if (source[i] >= 0 && data[i].unappliedUpdates[source[i]] == 0) {
                fresh++;
            }
        }

    }
}
