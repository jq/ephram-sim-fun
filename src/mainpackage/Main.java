package mainpackage;

import java.util.Random;

/**
 * the entrance of the program
 * @author hillboy
 */
public class Main {

    public static void main(String[] args) throws Exception {
        double a;

//            System.out.println("alpha=\t" + a);
//        for (Crawler.beta = 0.1; Crawler.beta <= 1; Crawler.beta += 0.1) {
        for(int i=0;i<1;i++){
            
            Cache c = new cache.Cache_FIFO();
//            Data.alpha = 0.9;

            TestRunner.init();
            TestRunner.test(c);
        }

    }
}
