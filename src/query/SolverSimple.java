package query;

import mainpackage.*;
import java.util.*;

/**
 *
 * @author hillboy
 */
public class SolverSimple extends Solver {

    public static Solver getSolver() {
        return new SolverSimple();
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

    public void solve(Data[] data, Cache c) {
        solution = Sittings.solution.getSolution(data.length);
        solution.data = data;
        for (int i = 0; i < data.length; i++) {
            data[i].access();
            if (c.inCache(data[i])) {
                // get from cache
                solution.source[i] = -2;
            } else {
                // get from src
                Cache.notinCacheCount++;
                solution.source[i] = -1;
            }
        }
        solution.getFreshAndTime();
    }
}
