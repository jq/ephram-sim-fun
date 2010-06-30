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
public class SolutionClassed extends SolutionSimple2 {

    public static SolutionSimple2 getSolution(int len) {
        return new SolutionClassed(len);
    }

    public SolutionClassed(int len) {
        super(len);
    }

    public void getFreshAndTime() {
        time = 0;
        fresh = 0;
        int nowTime = 0;
        int[] eachTime = new int[data.length];
        Server[] eachSource = new Server[data.length];
        for (int i = 0; i < data.length; i++) {
            //update time

            if (source[i] == -2) {
                eachSource[i] = null;
                nowTime = Cache.cacheAccessTime;

            }
            if (source[i] == -1) {
                nowTime = data[i].src.getRecordAccessTime();
                eachSource[i] = data[i].src;
            }
            if (source[i] >= 0) {
                eachSource[i] = data[i].replicas.get(source[i]);
                nowTime = data[i].replicas.get(source[i]).getRecordAccessTime();

            }
            eachTime[i] = nowTime;
            for (int j = i - 1; j >= 0; j--) {
                if (eachSource[j] == eachSource[i]) {
                    eachTime[i] += eachTime[j];
                    break;
                }
            }
            if (eachTime[i] > time) {
                time = eachTime[i];
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
