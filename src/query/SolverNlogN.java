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
public class SolverNlogN extends SolverSimple2 {

    public static Solver getSolver() {
        return new SolverNlogN();
    }
    public SolutionNlogN solution;

    public double pay(User u, int datalen, Cache c) {
        int[] allServerAccessTimes = new int[1 + c.s.length];
        allServerAccessTimes[0] = Cache.cacheAccessTime;
        for (int i = 1; i < 1 + c.s.length; i++) {
            allServerAccessTimes[i] = c.s[i - 1].getRecordAccessTime();
        }
        Arrays.sort(allServerAccessTimes);
        double max = 0, tempmax;
        int maxnum = -1, i;
        for (i = 0; i < allServerAccessTimes.length; i++) {
            solution.pushToTime(allServerAccessTimes[i]);
            if (solution.got < solution.dataPlace.length) {
                continue;
            }
            if (!solution.isValid2(u, datalen)) {
                continue;
            }
            tempmax = solution.tryPay(u, datalen);
            if (maxnum < 0 || tempmax > max) {
                max = tempmax;
                maxnum = i;
            }
        }
        if (maxnum < 0) {
            u.failQuery++;
            return 0;
        }
        solution.init();
        int[] source=new int[solution.dataPlace.length];
        Data[] data=new Data[solution.dataPlace.length];
        solution.pushToTime(allServerAccessTimes[maxnum]);
        for ( i = 0; i < solution.dataPlace.length; i++) {
            data[i]=solution.all.get(solution.dataPlace[i]).data;
            source[i]=solution.all.get(solution.dataPlace[i]).src;
            if(source[i]!=-2){
                Cache.notinCacheCount++;
            }
        }
        System.out.println(solution.tryPay(u, datalen)-max);
        solution.pay(u, datalen);
        c.updateCache(data, source);
        return max;
    }

    public void solve(Data[] data, Cache c) {
        solution = new SolutionNlogN(data.length);
        int i, j;
        for (i = 0; i < data.length; i++) {
            for (j = -2; j < 4; j++) {
                DATAandSOURCE_pair temp = new DATAandSOURCE_pair();
                temp.data = data[i];
                temp.num = i;
                temp.src = j;
                temp.init();
                solution.all.add(temp);
            }
        }
        Collections.sort(solution.all, new SortDataSourceByAccessTime());
    }
}
