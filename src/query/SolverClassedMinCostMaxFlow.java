/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import mainpackage.*;
import java.util.*;
import query.ssp.*;

/**
 *
 * @author hillboy
 */
public class SolverClassedMinCostMaxFlow extends Solver {

    public static Solver getSolver() {
        return new SolverClassedMinCostMaxFlow();
    }

    public double pay(User u, int datalen, Cache c) {

//		double max = 0;
        int bound;
        int i, bestI = -1;
        double max = 0, now, k, bestK = 0;
        for (k = 1; k <= 1; k += 1) {
            for (i = 0; i < c.s.length; i++) {
                tryBound(solution.data, c, c.s[i].getRecordAccessTime() * k);
                solution.getFreshAndTime();
                now = solution.tryPay(u, datalen);
                if (bestI < 0 || max < now) {
                    max = now;
                    bestI = i;
                    bestK = k;
                }
            }
        }
        tryBound(solution.data, c, c.s[bestI].getRecordAccessTime() * bestK);
        solution.getFreshAndTime();
        if (!solution.isValid(u, datalen)) {
            u.failQuery++;
            return 0;
        }
        max = solution.pay(u, datalen);
        c.updateCache(solution.data, solution.source);
//        System.out.println(" DataPrice~~~~~~~~~~~" + solution.getDataPrice());
        return max;
    }
    public SolutionSimple2 solution;

    public void tryBound(Data[] data, Cache c, double bound) {
        // building a network
        // 0: source
        // 1~data.length : data
        // data.length+1~data.length+number of servers : servers
        // data.length+number of servers+1 : cache
        // data.length+number of servers+2 : terminal
        // number of servers is set to c.s.length. this can be change when it's large while data.length is small
        Network flow = new Network();
        flow.eg = new ArrayList<Edge>();
        int i, j;
        int n = data.length;
        int m = c.s.length;
        for (i = 1; i <= n; i++) {
            flow.eg.add(new Edge(0, i, 1, 0, 0));
        }
        for (i = 0; i < n; i++) {
            flow.eg.add(new Edge(i + 1, data[i].src.id + n + 1, 1, 0, 0));

            if (data[i].cacheUnappliedUpdate == 0) {
                flow.eg.add(new Edge(i + 1, n + m + 1, 1, 0, 0));
            } else {
                flow.eg.add(new Edge(i + 1, n + m + 1, 1, 0, 1));
            }
            for (j = 0; j < data[i].replicas.size(); j++) {
                if (data[i].unappliedUpdates[j] == 0) {
                    flow.eg.add(new Edge(i + 1, n + data[i].replicas.get(j).id + 1, 1, 0, 0));
                } else {
                    flow.eg.add(new Edge(i + 1, n + data[i].replicas.get(j).id + 1, 1, 0, 1));
                }
            }
        }
        for (i = 0; i < m; i++) {
            flow.eg.add(new Edge(n + i + 1, n + m + 2, (int) ((bound) / c.s[i].getRecordAccessTime() + 1e-6), 0, 0));
        }
        flow.eg.add(new Edge(n + m + 1, n + m + 2, (int) ((bound) / c.cacheAccessTime + 1e-6), 0, 0));
        flow.v = n + m + 3;
        flow.initFlow();
        flow.mincost(0, n + m + 2);
        for (i = 1; i <= n; i++) {
            solution.source[i - 1] = -3;
            for (j = flow.net.get(i).size() - 1; j >= 0; j--) {
//                flow.net.get(i).get(j).output();
                if (flow.net.get(i).get(j).u == i && flow.net.get(i).get(j).flow == 1) {
                    int v = flow.net.get(i).get(j).v;
                    if (v == n + m + 1) {
                        solution.source[i - 1] = -2;
                    } else if (v == data[i - 1].src.id + n + 1) {
                        solution.source[i - 1] = -1;
                    } else {
                        for (int r = 0; r < data[i - 1].replicas.size(); r++) {
                            if (v == data[i - 1].replicas.get(r).id + n + 1) {
                                solution.source[i - 1] = r;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            if (solution.source[i - 1] == -3) {
                solution.source[i - 1] = -1;
            }
        }
//        System.out.println(n+" "+flow.getCost()+" "+flow.getFlow());
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

    }
}
