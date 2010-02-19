package mainpackage;

import java.util.Random;

/**
 * the entrance of the program
 * @author hillboy
 */
public class Main {

    public static void main(String[] args) throws Exception {
        double a;
        for (a = 0.1; a < 1; a += 0.1) {
//            System.out.println("alpha=\t" + a);
            Statistics.QoDrejection = Statistics.QoSrejection = 0;
            Sittings.randomSeed = new Random();
            Cache c = new cache.Cache_FIFO();
//            Data.alpha = 0.9;
            TestRunner.test(c);
        }
    }
}
