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
    public SolutionSimple2 simpleSolution;

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
            simpleSolution.getFreshAndTime();
            if (simpleSolution.isValid(u, datalen)) {
                System.out.print("oops");
            }
            return 0;
        }
        solution.init();
        solution.pushToTime(allServerAccessTimes[maxnum]);
        for (i = 0; i < solution.dataPlace.length; i++) {
            simpleSolution.source[i] = solution.all.get(solution.dataPlace[i]).src;
//            System.out.print(simpleSolution.source[i] + ",");
            if (simpleSolution.source[i] != -2) {
                Cache.notinCacheCount++;
            }
        }
//        System.out.println(max);
//        i=0;
//        System.out.println((10/i));
//        solution.pay(u, datalen);
        simpleSolution.getFreshAndTime();
        c.updateCache(simpleSolution.data, simpleSolution.source);
        return simpleSolution.pay(u, datalen);
    }

    public void solve(Data[] data, Cache c) {
        solution = new SolutionNlogN(data.length);
        simpleSolution = new SolutionSimple2(data.length);
        simpleSolution.data = data;
        int i, j;
        for (i = 0; i < data.length; i++) {
            data[i].access();
            simpleSolution.source[i] = -1;
            for (j = -2; j < 4; j++) {
                if (j == -2 && !c.inCache(data[i])) {
                    continue;
                }
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
