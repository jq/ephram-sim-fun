package mainpackage;


import java.util.*;
import java.io.*;

/**
 * to start up experiments
 * @author hillboy
 */
public class TestRunner {

    public static void test(Cache c) throws Exception {
        String asFile = Sittings.asFile;
        String uFile = Sittings.userProfile;
        String oFile = Sittings.output;
        // Server
        String sconfig = Sittings.serverConfig;
        ArrayList<Server> s = Server.getServerFromConfig(sconfig);

        // Access
        // Data can't be cloned, since data's location changed during running
        Data[] d = Data.getDatas((Server[]) s.toArray(new Server[s.size()]));

        // update
        ArrayList<Event> e = new ArrayList<Event>(40000);
        Update.getUpdate(d, e);
        // avoid the .svn folder
        // read access from disk
        ArrayList<Access> a = new ArrayList<Access>(40000);
        Access.getAccess(d, a, asFile);

        //data crawler
        ArrayList<Crawler> crawlerList = new ArrayList<Crawler>(40000);
        Crawler.getCrawler(crawlerList);

        //Server accessTime change
        ArrayList<ServerChange> ServerChangeList = new ArrayList<ServerChange>(40000);
        ServerChange.getServerChange(ServerChangeList);
        //auto update server accesstime
        ArrayList<UpdateServerLatency> UpdateServerLatencyList = new ArrayList<UpdateServerLatency>(40000);
        UpdateServerLatency.getUpdateServerLatency(UpdateServerLatencyList);


        e.addAll(a);
        e.addAll(crawlerList);
        e.addAll(ServerChangeList);
        e.addAll(UpdateServerLatencyList);

        Collections.sort(e);
        ArrayList<User> u = User.addUser(a, uFile, Sittings.queryText);

        //User[] u = User.getUsers();
        Writer output = new BufferedWriter(new FileWriter(new File(oFile)));

        c.init(Sittings.cacheSize, e, d, (Server[]) s.toArray(new Server[s.size()]), output, u);
        c.run();
//        System.out.println("finish!");
//        System.out.println("allAccess:\t"+Access.totalAccessNum);
////        System.out.println("inCacheFresh:\t" + Cache.inCacheFreshCount + " " + Cache.inCacheFreshCount / (Cache.inCacheFreshCount + Cache.inCacheStaleCount + Cache.notinCacheCount + 0.0));
////        System.out.println("inCacheStale:\t" + Cache.inCacheStaleCount + " " + Cache.inCacheStaleCount / (Cache.inCacheFreshCount + Cache.inCacheStaleCount + Cache.notinCacheCount + 0.0));
        System.out.println("notInCache:\t" + Cache.notinCacheCount);
//
////        System.out.println("Total Crawl Time: " + Crawler.totalCrawlTime);
//
        int totalQuery = 0;
        int successQuery = 0;
        for (int i = 0; i < u.size(); i++) {
            totalQuery += u.get(i).totalQuery;
            successQuery += u.get(i).successQuery;
        }
//        System.out.println("totalQuery:\t" + totalQuery);
        System.out.println("successQuery:\t" + successQuery);
//        System.out.println("SuccessRate:\t" + successQuery / (double) totalQuery);
        System.out.println("QoS rejection:\t" + Statistics.QoSrejection);
        System.out.println("QoD rejection:\t" + Statistics.QoDrejection);
        System.out.println("profit:\t" + Double.toString(c.profit));
    }
    public static void testCache(Cache c) throws Exception {
        String asFile = Sittings.asFile;
        String uFile = Sittings.userProfile;
        String oFile = Sittings.output;
        // Server
        String sconfig = Sittings.serverConfig;
        ArrayList<Server> s = Server.getServerFromConfig(sconfig);

        // Access
        // Data can't be cloned, since data's location changed during running
        Data[] d = Data.getDatas((Server[]) s.toArray(new Server[s.size()]));

        // update
        ArrayList<Event> e = new ArrayList<Event>(40000);
        Update.getUpdate(d, e);
        // avoid the .svn folder
        // read access from disk
        ArrayList<Access> a = new ArrayList<Access>(40000);
        Access.getAccess(d, a, asFile);

        //data crawler
        ArrayList<Crawler> crawlerList = new ArrayList<Crawler>(40000);
        Crawler.getCrawler(crawlerList);

        //Server accessTime change
        ArrayList<ServerChange> ServerChangeList = new ArrayList<ServerChange>(40000);
        ServerChange.getServerChange(ServerChangeList);
        //auto update server accesstime
        ArrayList<UpdateServerLatency> UpdateServerLatencyList = new ArrayList<UpdateServerLatency>(40000);
        UpdateServerLatency.getUpdateServerLatency(UpdateServerLatencyList);


        e.addAll(a);
        e.addAll(crawlerList);
        e.addAll(ServerChangeList);
        e.addAll(UpdateServerLatencyList);

        Collections.sort(e);
        ArrayList<User> u = User.addUser(a, uFile, Sittings.queryText);

        //User[] u = User.getUsers();
        Writer output = new BufferedWriter(new FileWriter(new File(oFile)));

        c.init(Sittings.cacheSize, e, d, (Server[]) s.toArray(new Server[s.size()]), output, u);
        c.run();
        System.out.println("finish!");
        System.out.println("inCacheFresh :_" + Cache.inCacheFreshCount + " " + Cache.inCacheFreshCount / (Cache.inCacheFreshCount + Cache.inCacheStaleCount + Cache.notinCacheCount + 0.0));
        System.out.println("inCacheStale :_" + Cache.inCacheStaleCount + " " + Cache.inCacheStaleCount / (Cache.inCacheFreshCount + Cache.inCacheStaleCount + Cache.notinCacheCount + 0.0));
        System.out.println("notInCache :_" + Cache.notinCacheCount + " " + Cache.notinCacheCount / (Cache.inCacheFreshCount + Cache.inCacheStaleCount + Cache.notinCacheCount + 0.0));

        System.out.println("Total Crawl Time: " + Crawler.totalCrawlTime);

        int totalQuery = 0;
        int successQuery = 0;
        for (int i = 0; i < u.size(); i++) {
            totalQuery += u.get(i).totalQuery;
            successQuery += u.get(i).successQuery;
        }
        System.out.println("totalQuery:   " + totalQuery);
        System.out.println("successQuery:   " + successQuery);
        System.out.println("SuccessRate:   " + successQuery / (double) totalQuery);
    }
}
