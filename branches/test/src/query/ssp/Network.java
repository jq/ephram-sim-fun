/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query.ssp;

import java.util.*;

/**
 *
 * @author hillboy
 */
public class Network {

    static final int N = 128;
    static final int INF = 1 << 28;
    public ArrayList<Edge> eg;
    public ArrayList<ArrayList<Edge>> net;
    public Edge[] prev = new Edge[N];
    public int v, s, t;
    public int flow, cost;
    public int[] phi = new int[N], dis = new int[N], pre = new int[N];

    void initNet() {
        net = new ArrayList<ArrayList<Edge>>(N);
        for (int i = 0; i < v; i++) {
            net.add(new ArrayList<Edge>());
        }
        for (int i = eg.size() - 1; i >= 0; i--) {
            net.get(eg.get(i).u).add(eg.get(i));
            net.get(eg.get(i).v).add(eg.get(i));
        }
    }

    public void initFlow() {
        flow = cost = 0;
        Arrays.fill(phi, 0);
        initNet();
    }

    public boolean dijkstra() {
        for (int i = 0; i < v; i++) {
            dis[i] = INF;
        }
        dis[s] = 0;
        prev[s] = null;
        pre[s] = -1;
        boolean[] vst = new boolean[N];
        Arrays.fill(vst, false);
        for (int i = 1; i < v; i++) {
            int md = INF, u = 0;
            for (int j = 0; j < v; j++) {
                if (!vst[j] && md > dis[j]) {
                    md = dis[j];
                    u = j;
                }
            }
            if (md == INF) {
                break;
            }
            vst[u] = true;
            for (int j = net.get(u).size() - 1; j >= 0; j--) {
                Edge ce = net.get(u).get(j);
                if (ce.cap(u) > 0) {
                    int p = ce.other(u), cw = ce.ecost(u) - phi[u] + phi[p];
                    if (dis[p] > dis[u] + cw) {
                        dis[p] = dis[u] + cw;
                        prev[p] = ce;
                        pre[p] = u;
                    }
                }
            }
        }
        return (dis[t] != INF);
    }

    public int getCost() {
        return cost;
    }

    public int getFlow() {
        return flow;
    }

    public int mincost(int ss, int tt) {
        s = ss;
        t = tt;
        initFlow();
        while (dijkstra()) {
            int ex = INF;
            for (int c = t; c != s; c = pre[c]) {
                if (ex > prev[c].cap(pre[c])) {
                    ex = prev[c].cap(pre[c]);
                }
            }
            for (int c = t; c != s; c = pre[c]) {
                prev[c].addFlow(pre[c], ex);
            }
            flow += ex;
            cost += ex * (dis[t] - phi[t]);
            for (int i = 0; i < v; i++) {
                phi[i] -= dis[i];
            }
        }
        return cost;
    }
};
