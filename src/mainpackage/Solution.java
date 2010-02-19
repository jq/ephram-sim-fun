package mainpackage;

import java.util.LinkedList;

public class Solution {

    public int fresh;
    public int time;
    public double dataPrice;
    public LinkedList<Data> freshData;
    public LinkedList<Data> staleData;

    public Solution() {
        fresh = 0;
        time = 0;
        dataPrice = 0;
    }

    Solution(int f, int t) {
        fresh = f;
        time = t;
        freshData = new LinkedList<Data>();
        staleData = new LinkedList<Data>();
        dataPrice = 0.0;
    }

    public Solution(int f, int t, Data d, boolean isStale, double dPrice) {
        fresh = f;
        time = t;
        freshData = new LinkedList<Data>();
        staleData = new LinkedList<Data>();
        if (isStale) {
            staleData.addFirst(d);
        } else {
            freshData.addFirst(d);
        }
        dataPrice = dPrice;
    }

    void AddFresh() {
        fresh++;
    }

    int getFresh() {
        return fresh;
    }

    void setTime(int t) {
        if (t > time) {
            time = t;
        }
    }

    int getTime() {
        return time;
    }

    public double getDataPrice() {
        return dataPrice;
    }

    void setDataPrice(double price) {
        dataPrice = price;
    }

    void addSolution(Solution s) {
        //fresh++;
        fresh += s.getFresh();

        setTime(s.time);
        freshData.addAll(s.freshData);
        staleData.addAll(s.staleData);
        this.setDataPrice(dataPrice + s.getDataPrice());
    }

    public double tryPay(User u, float datalen) {
        float stale = (datalen - fresh) / datalen;
        return u.pay_linearPositive(time, stale);

    }

    public double pay(User u, float datalen) {
        float stale = (datalen - fresh) / datalen;
        return u.pay(time, stale);

    }

    public boolean isValid(User u, float datalen) {
        float stale = (datalen - fresh) / datalen;
        boolean cool = true;
        if (u.getQod_linearPositive(stale) == 0) {
            Statistics.QoDrejection++;
            cool = false;
        }
        if (u.getQos_linearPositive(time) == 0) {
            Statistics.QoSrejection++;
            cool = false;
        }
        return cool;
    }

//	void apply(Cache c) {
//		for (int i = 0; i< freshData.size(); ++i) {
//			//maybe in stale
//			Data data = freshData.get(i);
//			if(c.inCacheStale(data))
//				c.stale.remove(data);
//			
//			c.addToCache(data, false);
//		}
//		for (int i = 0; i< staleData.size(); ++i) {
//			Data data = staleData.get(i);
//			if (c.inCacheStale(data)) {
//				c.adjustCache(data, true);
//			}else if(c.inCacheFresh(data)){
//				
//			}else {
//			    c.addToCache(data, true);
//			}
//		}
//	}
    public void apply(Cache c) {
//		for (int i = 0; i< freshData.size(); ++i)
//		{
//			Data data = freshData.get(i);
//			if(c.inCacheFresh(data)||c.inCacheStale(data))
//				break;
////                            continue;
////			c.addToCache(data, false);
//			c.addToCache(data);
//		}
        for (int i = 0; i < freshData.size(); ++i) {
            c.addToCache(freshData.get(i));
        }
        for (int i = 0; i < staleData.size(); ++i) {
            c.addToCache(staleData.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    Solution( int f, int t, LinkedList<Data> f_, LinkedList<Data> s_) {
        fresh = f;
        time = t;
        freshData = (LinkedList<Data>) f_.clone();
        staleData = (LinkedList<Data>) s_.clone();
    }

    public Solution clone() {
        return new Solution(fresh, time, freshData, staleData);
    }
}
