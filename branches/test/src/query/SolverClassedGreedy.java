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
public class SolverClassedGreedy extends Solver {

    public static Solver getSolver() {
        return new SolverClassedGreedy();
    }

    public double pay(User u, int datalen, Cache c) {

//		double max = 0;
        if (!solution.isValid(u, datalen)) {
            u.failQuery++;
            return 0;
        }
        double max = solution.pay(u, datalen);
        c.updateCache(solution.data, solution.source);
//        System.out.println(" DataPrice~~~~~~~~~~~" + solution.getDataPrice());
        return max;
    }
    public SolutionSimple2 solution;

    public void tryBound(Data[] data, Cache c, double bound) {
        double[] sumTime = new double[c.s.length + 1];
        int i, j;
        int bestStalness, bestJ = 0;
        for (i = 0; i < data.length; i++) {
            bestStalness = -1;
            bestJ = -3;
            for (j = -2; j < data[i].replicas.size(); j++) {
                if (j == -2 && c.inCache(data[i]) && sumTime[0] + Cache.cacheAccessTime <= bound) {
                    if (bestStalness < 0 || bestStalness > data[i].cacheUnappliedUpdate) {
                        bestJ = -2;
                        bestStalness = data[i].cacheUnappliedUpdate;
                    }
                }
                if (j == -1 && data[i].src.getRecordAccessTime() + sumTime[data[i].src.id + 1] <= bound) {
                    if (bestStalness < 0 || bestStalness > 0) {
                        bestJ = -1;
                        bestStalness = data[i].cacheUnappliedUpdate;
                    }
                }
                if (j >= 0 && data[i].replicas.get(j).getRecordAccessTime() + sumTime[data[i].replicas.get(j).id + 1] <= bound) {
                    if (bestStalness < 0 || bestStalness > data[i].unappliedUpdates[j]) {
                        bestJ = j;
                        bestStalness = data[i].unappliedUpdates[j];
                    }
                }
            }
            if (bestStalness < 0) {
                bestJ = -1;
            }
            if (bestStalness >= 0) {
                solution.source[i] = bestJ;
                if (bestJ == -2) {
                    sumTime[0] += Cache.cacheAccessTime;
                }
                if (bestJ == -1) {
                    sumTime[data[i].src.id+1] += data[i].src.getRecordAccessTime();
                }
                if(bestJ>=0){
                    sumTime[data[i].replicas.get(bestJ).id+1]+=data[i].replicas.get(bestJ).getRecordAccessTime();
                }
            }

        }
    }

    public void solve(Data[] data, Cache c) {
        solution = Sittings.solution.getSolution(data.length);
        solution.data = data;
        for (int i = 0; i < data.length; i++) {
            data[i].access();
            if (c.inCache(data[i])) {
                // get from cache
//                solution.source[i] = -2;
            } else {
                // get from src
                Cache.notinCacheCount++;
//                solution.source[i] = -1;
            }
        }
        int bound;
        todo
        solution.getFreshAndTime();
    }
}
