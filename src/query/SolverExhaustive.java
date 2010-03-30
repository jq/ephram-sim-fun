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
public class SolverExhaustive extends Solver {

    public static Solver getSolver() {
        return new SolverExhaustive();
    }
    public SolutionSimple2 simpleSolution;

    public double pay(User u, int datalen, Cache c) {
        int[] bestSrc = simpleSolution.source.clone();
        int[] bfsSequence = {-1, -2, 0, 1, 2, 3};
        int[] bfsPoint = new int[datalen];
        int t = simpleSolution.source.length;
        double max = -1, nowmax;
        while (t >= 0) {
            if (t == datalen) {
                simpleSolution.getFreshAndTime();
                if (simpleSolution.isValid2(u, datalen)) {
                    nowmax = simpleSolution.tryPay(u, datalen);
                    if (nowmax > max) {
                        max = nowmax;
                        bestSrc = simpleSolution.source.clone();
                    }
                }
                t--;
            } else {
                bfsPoint[t]++;
                if (bfsPoint[t] < bfsSequence.length && bfsSequence[bfsPoint[t]] == -2 && (!c.inCache(simpleSolution.data[t]))) {
                    bfsPoint[t]++;
                }
                if (bfsPoint[t] == bfsSequence.length) {
                    bfsPoint[t] = -1;
                    t--;
                } else {
                    simpleSolution.source[t] = bfsSequence[bfsPoint[t]];
                    t++;
                }
            }
        }
        simpleSolution.source = bestSrc;
        if (max < 0) {
            u.failQuery++;
            simpleSolution.getFreshAndTime();
            if (simpleSolution.isValid(u, datalen)) {
                System.out.print("oops");
            }
            return 0;
        }
        for (int i = 0; i < datalen; i++) {
//            System.out.print(simpleSolution.source[i] + ",");
            if (simpleSolution.source[i] != -2) {
                Cache.notinCacheCount++;
            }
        }
        System.out.println(max+" "+(Statistics.SolverCostTime+System.currentTimeMillis()));
        simpleSolution.getFreshAndTime();
        c.updateCache(simpleSolution.data, simpleSolution.source);
        return simpleSolution.pay(u, datalen);
    }

    public void solve(Data[] data, Cache c) {
        simpleSolution = new SolutionSimple2(data.length);
        simpleSolution.data = data;
        int i, j;
        for (i = 0; i < data.length; i++) {
            data[i].access();
            simpleSolution.source[i] = -1;
        }
    }
}
