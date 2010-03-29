package query;

import mainpackage.*;
import java.util.*;


public class SolverSimple2 extends Solver {

    public static Solver getSolver() {
        return new SolverSimple2();
    }

    public double pay(User u, int datalen, Cache c) {

//		double max = 0;
        //    if (!solution.isValid(u, datalen)) {
        //        u.failQuery++;
        //        return 0;
        //    }
        //    double max = solution.pay(u, datalen);
        //    c.updateCache(solution.data, solution.source);
//        System.out.println(" DataPrice~~~~~~~~~~~" + solution.getDataPrice());
        //    return max;
        double max = 0;
        int maxNum = 0;
        boolean ifmaxInitial = false;
        for (int i = 0; i < 1 + c.s.length; i++) {
            if (solutions.get(i).isValid2(u, datalen)) {
                if (!ifmaxInitial) {
                    ifmaxInitial = true;
                    maxNum = i;
                    max = solutions.get(i).tryPay(u, datalen);
                } else if (solutions.get(i).tryPay(u, datalen) > max) {
                    maxNum = i;
                    max = solutions.get(i).tryPay(u, datalen);
                }
            }
        }
        if (!ifmaxInitial) {
            for (int i = 0; i < solutions.get(0).data.length; i++) {
                if (solutions.get(0).source[i] != -2) {
                    Cache.notinCacheCount++;
                }
            }
            solutions.get(0).isValid(u, datalen);
            u.failQuery++;
            return 0;
        }
        for (int i = 0; i < solutions.get(maxNum).data.length; i++) {
            if (solutions.get(maxNum).source[i] != -2) {
                Cache.notinCacheCount++;
            }
        }
        max = solutions.get(maxNum).pay(u, datalen);
        c.updateCache(solutions.get(maxNum).data, solutions.get(maxNum).source);
        return max;
    }
    public ArrayList<SolutionSimple2> solutions;

    public void solve(Data[] data, Cache c) {
        solutions = new ArrayList<SolutionSimple2>(1 + c.s.length);
        int[] allServerAccessTimes = new int[1 + c.s.length];
        allServerAccessTimes[0] = Cache.cacheAccessTime;
        for (int i = 1; i < 1 + c.s.length; i++) {
            allServerAccessTimes[i] = c.s[i - 1].getRecordAccessTime();
        }
        for (int j = 0; j < 1 + c.s.length; j++) {
            SolutionSimple2 solution = new SolutionSimple2(data.length);
            solution.data = data;
            for (int i = 0; i < data.length; i++) {
                data[i].access();
                if (c.inCache(data[i])) {
                    // get from cache
                    solution.source[i] = -2;
                } else {
                    // get from src
                    //Cache.notinCacheCount++;
                    //solution.source[i] = -1;
                    //get from src or replicas
                    if (data[i].src.getRecordAccessTime() <= allServerAccessTimes[j]) {
                        solution.source[i] = -1;
                    } else {
                        int getFromReplicasNum = 0;
                        for (int k = 1; k < 4; k++) {
                            if (data[i].replicas.get(k).getRecordAccessTime() <= allServerAccessTimes[j] && data[i].unappliedUpdates[k] < data[i].unappliedUpdates[getFromReplicasNum]) {
                                getFromReplicasNum = k;
                            }
                        }
                        if (getFromReplicasNum == 0 && data[i].unappliedUpdates[getFromReplicasNum] > allServerAccessTimes[j]) {
                            solution.source[i] = -1;
                        } else {
                            solution.source[i] = getFromReplicasNum;
                        }
                    }
                }
            }
            solution.getFreshAndTime();
            solutions.add(solution);
        }
    }
}
