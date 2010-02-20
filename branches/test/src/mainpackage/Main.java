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


        Cache c = new cache.Cache_FIFO();
//            Data.alpha = 0.9;
        TestRunner.test(c);

    }
}
