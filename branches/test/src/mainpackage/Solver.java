package mainpackage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class Solver {

    public LinkedList<Solution> list = new LinkedList<Solution>();

    public void insertCacheFresh() {
        if (list.size() == 0) {
            list.addFirst(new Solution(1, Cache.cacheAccessTime));
        } else {
            ListIterator<Solution> itr = list.listIterator();
            while (itr.hasNext()) {
                Solution s1 = itr.next();
                s1.AddFresh();

            }
        }
    }

    public void insert(Solution s) {
        if (list.size() == 0) {
            list.addFirst(s);
        } else {
            insert(list, s);
        }
    }

    //把Solution s合并到链表l中每个solution中
    public static void insert(LinkedList<Solution> l, Solution s) {
        ListIterator<Solution> itr = l.listIterator();
        while (itr.hasNext()) {
            Solution s1 = itr.next();
            s1.addSolution(s);

        }

    }

    @SuppressWarnings("unchecked")
    public static LinkedList<Solution> deepCopy(final LinkedList<Solution> s) {
        LinkedList<Solution> n = (LinkedList<Solution>) s.clone();
        ListIterator<Solution> itr = n.listIterator();
        while (itr.hasNext()) {
            Solution s1 = itr.next();
            itr.set(s1.clone());

        }
        return n;
    }

    public void insert(ArrayList<Solution> s) {
        if (list.size() == 0) {
            list.addAll(s);

        } else {
            ArrayList<LinkedList<Solution>> ls = new ArrayList<LinkedList<Solution>>(s.size());
            ls.add(list);
            for (int i = 1; i < ls.size(); ++i) {
                ls.add(deepCopy(list));
            }

            for (int i = 0; i < ls.size(); ++i) {
                insert(ls.get(i), s.get(i));
            }

            for (int i = 1; i < ls.size(); ++i) {
                list.addAll(ls.get(i));
            }

        }
    }

    public double pay(User u, int datalen, Cache c) {

//		double max = 0;
        double max = -10;
        Solution s = null;
        ListIterator<Solution> itr = list.listIterator();
        System.out.println(list.size());
        while (itr.hasNext()) {
            Solution s1 = itr.next();
            if (!s1.isValid(u, datalen)) {
                continue;
//			 double value = s1.tryPay(u, datalen);
            }
            double value = s1.tryPay(u, datalen) - s1.getDataPrice();
            if (value >= max) {
                max = value;
                s = s1;
            }
        }
        // apply this solution's change
        if (s != null) {
            s.pay(u, datalen);
            s.apply(c);
            System.out.println(" DataPrice~~~~~~~~~~~" + s.getDataPrice());
        } else {
            u.failQuery++;
            return 0;
        //throw new RuntimeException();
        }
        return max;
    }

    public void solve(Data[] data, Cache c) {
        Solver s=this;
        for (int i = 0; i < data.length; i++) {
            //data access num++
            data[i].access();

            Data d = data[i];
            int dTime = 0;
            if (c.inCacheFresh(d)) {
                // no need to add to solution, since it is just one choice
                s.insertCacheFresh();
                //c.adjustCache(d, true);
//                System.out.println("inCacheFresh------------" + i + "---" + d);
                Cache.inCacheFreshCount++;

            } else if (c.inCacheStale(d)) {
                // just try src of data and cache
//        	    ArrayList<Solution> ss = new ArrayList<Solution>(2);
//        	    Solution staleCache = new Solution(0, Cache.cacheAccessTime, d, true);
//        	    Solution freshServer = new Solution(0, d.src.accessTime, d, false);
//        	    ss.add(staleCache);
//        	    ss.add(freshServer);
//        	    s.insert(ss);
                ArrayList<Solution> ss = d.getSolutions();
                Solution staleCache = new Solution(0, Cache.cacheAccessTime, d, true, 0);
                ss.add(staleCache);
                s.insert(ss);

//                System.out.println("inCacheStale------------" + i + "---" + d);
                Cache.inCacheStaleCount++;
            } else {

                // get it from servers
                ArrayList<Solution> ss = d.getSolutions();
                s.insert(ss);
//                System.out.println("notinCache------------" + i + "---" + d);
                Cache.notinCacheCount++;
            }
        }

    }
    public static Solver getSolver(){
        return new Solver();
    }
}
