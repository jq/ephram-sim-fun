/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package query.ssp;

/**
 *
 * @author hillboy
 */
public class Edge {

    public int u, v, cuv, cvu, flow, cost;

    public Edge(int cu, int cv, int ccu, int ccv, int cc) {
        u = cu;
        v = cv;
        cuv = ccu;
        cvu = ccv;
        flow = 0;
        cost = cc;
    }

    public int other(int p) {
        return p == u ? v : u;
    }

    public int cap(int p) {
        return p == u ? cuv - flow : cvu + flow;
    }

    public void output(){
        System.out.println("u:"+u+" v:"+v+" flow:"+flow);
    }

    public void addFlow(int p, int f) {
        flow += (p == u ? f : -f);
    }

    public int ecost(int p) {
        if (flow == 0) {
            return cost;
        } else if (flow > 0) {
            return p == u ? cost : -cost;
        } else {
            return p == u ? -cost : cost;
        }
    }
};
