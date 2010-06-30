package query;

import mainpackage.*;

import java.util.*;

public class SolutionSimple2 extends Solution {

    public Data[] data;
    public int[] source;

    public static SolutionSimple2 getSolution(int len) {
        return new SolutionSimple2(len);
    }

    public SolutionSimple2(int len) {
        source = new int[len];
    }

    public void getFreshAndTime() {
        time = 0;
        fresh = 0;
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

    public boolean isValid2(User u, float datalen) {
        float stale = (datalen - fresh) / datalen;
        boolean cool = true;
        if (u.getQod_linearPositive(stale) == 0) {
            //Statistics.QoDrejection++;
            cool = false;
        }
        if (u.getQos_linearPositive(time) == 0) {
            //Statistics.QoSrejection++;
            cool = false;
        }
        return cool;
    }
}
