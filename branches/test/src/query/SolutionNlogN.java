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
public class SolutionNlogN extends Solution {

    public ArrayList<DATAandSOURCE_pair> all = new ArrayList<DATAandSOURCE_pair>();
    public int[] dataPlace;
    int t, got;

    public SolutionNlogN(int len) {
        dataPlace = new int[len];
        int i;
        for (i = 0; i < len; i++) {
            dataPlace[i] = -1;
        }
        t = 0;
        got = 0;
    }

    public void init() {
        t = got = 0;
        time = fresh = 0;
        int i;
        for (i = 0; i < dataPlace.length; i++) {
            dataPlace[i] = -1;
        }
    }

    public void pushToTime(int nowTime) {
        int i, j;
        time = nowTime;
        while (t < all.size() && all.get(t).accessTime <= nowTime) {
            i = all.get(t).num;
            j = dataPlace[i];
            if (all.get(t).fresh > 0) {
                if (j >= 0) {
                    fresh -= all.get(j).fresh;
                } else {
                    got++;
                }
                fresh += all.get(t).fresh;
                dataPlace[i] = t;
            } else {
                if (j < 0) {
                    dataPlace[i] = t;
                    got++;
                }
            }
            t++;
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
