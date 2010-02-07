
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.ArrayList;

public class User {

    int userID;
    // as long as user pay, if user budget run out, all remaining queries do not success
    int successQuery;
    int failQuery;
    int totalQuery;
    //初始钱数
    static double budget = 1000;
    //已花费
    double cost;
    //QC spec
    int maxQos, maxQod, minQos, minQod, relDeadline;
    double stale;
    private double nslope_qos;
    private double nslope_qod;

    User(int uid, int reld, double f, int qos, int qod, int nqos, int nqod) {
        userID = uid;
        relDeadline = reld;
        stale = f;
        maxQos = qos;
        maxQod = qod;
        minQos = nqos;
        minQod = nqod;
        nslope_qos = maxQos / (double) relDeadline;
        nslope_qod = maxQod / (double) stale;

        successQuery = 0;
        failQuery = 0;
    }

    String getString() {
        return "UserID:" + Integer.toString(userID) + " successQ:" + Integer.toString(successQuery)
                + " totalQuery:" + Integer.toString(totalQuery)
                + " rate:" + Float.toString((float) successQuery / (float) totalQuery)
                + " cost:" + Double.toString(cost) + "\n";

    }
    static String config = Sittings.userConfig;

    public boolean hasMoney() {
        return budget > cost;
    }

    //linear
    public double getQos_linearPositive(int time) {
        if (time > relDeadline) {
            return 0.0;
        } else {
            return nslope_qos * (relDeadline - time);
        }
    }

    public double getQod_linearPositive(double staleness) {
        if (staleness >= stale) {
            return 0.0;
        } else {
            return nslope_qod * (stale - staleness);
        }
    }

    //step function
    public double getQod_stepFunction(double staleness) {
        if (staleness >= stale) {
            return 0.0;
        } else {
            return maxQod;
        }
    }

    public double getQos_stepFunction(int time) {
        if (time > relDeadline) {
            return 0.0;
        } else {
            return maxQos;
        }
    }

    //the money you need to pay
    //linear
    public double pay_linearPositive(int responseTime, float datastale) {

        return getQos_linearPositive(responseTime) + getQod_linearPositive(datastale);
    }
    //step function

    public double pay_stepFunction(int responseTime, float datastale) {

        return getQos_stepFunction(responseTime) + getQod_stepFunction(datastale);
    }

    //the money you pay(linear or step function)
    public double pay(int responseTime, float datastale) {

        System.out.println("------ResponseTime: " + responseTime + "------ pay:" + getQos_linearPositive(responseTime));
        System.out.println("------datastale: " + datastale + "------ pay:" + getQod_linearPositive(datastale));

        double spend = pay_linearPositive(responseTime, datastale);
//    	double spend = pay_stepFunction(responseTime, datastale);

        successQuery++;
        if (!hasMoney()) {
            throw new RuntimeException("can't call pay if run out of money");
        }
        if (spend + cost >= budget) {
            double actualPay = budget - cost;
            cost = budget;
            return actualPay;
        } else {
            cost += spend;
            return spend;
        }
    }
    /*
     * @param: access, userprofile,
     * @return:
     */

    public static ArrayList<User> addUser(final ArrayList<Access> inputAccess, String inputUserProfile, String output) {

        Vector<Integer> usrid = new Vector<Integer>();
        Vector<Integer> usrlist = new Vector<Integer>();
        Random ran = Sittings.randomSeed;

        int usrIndex = -1, usrPos = -1;
        int usrID, queryNum;
        int maxQos, minQos, relDeadline, maxQod, minQod;
        double stale;
        ArrayList<User> users = new ArrayList<User>();

        String line;
        StringTokenizer line_tokenizer;

        //read inputUserProfile and put into an array, get usrNum, build ArrayList of id and list;
        try {
            FileReader fr_access = new FileReader(inputUserProfile);
            BufferedReader br_access = new BufferedReader(fr_access);

            line = br_access.readLine();
            while (line != null) {
                line_tokenizer = new StringTokenizer(line);
                try {
                    //100	1000	5	100	-0.0	5	1	-0.0
                    usrID = Integer.parseInt(line_tokenizer.nextToken());
                    queryNum = Integer.parseInt(line_tokenizer.nextToken());
                    maxQos = Integer.parseInt(line_tokenizer.nextToken());
                    relDeadline = Integer.parseInt(line_tokenizer.nextToken());
                    minQos = (int) (Double.parseDouble(line_tokenizer.nextToken()));
                    maxQod = Integer.parseInt(line_tokenizer.nextToken());
                    stale = Double.parseDouble(line_tokenizer.nextToken());
                    minQod = (int) (Double.parseDouble(line_tokenizer.nextToken()));

                    //keep same index in usrid and prof
                    if (usrid.indexOf(usrID) == -1) {
                        usrid.add(new Integer(usrID));

                        User u = new User(usrID, relDeadline, stale, maxQos, maxQod, minQos, minQod);
                        u.totalQuery = queryNum;
                        users.add(u);
                    }
                    for (int i = 0; i < queryNum; i++) {
                        usrlist.add(new Integer(usrID));
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("FormatException. While reading Line ignored:");
                    System.out.println(line);
                    //System.exit(0);
                } catch (NoSuchElementException exception) {
                    System.out.println("NoSuchElementException. While reading Line ignored:");
                    System.out.println(line);
                    //System.exit(0);
                }
                line = br_access.readLine();
            }
            br_access.close();

            int total = inputAccess.size();
            Access a;
            User u;
            for (int i = 0; i < total; i++) {
                usrIndex = ran.nextInt(total - i);
                usrID = (usrlist.get(usrIndex)).intValue();
                usrPos = usrid.indexOf(usrID); //position in user id list, index of profile

                u = users.get(usrPos);
                a = inputAccess.get(i);
                a.u = u;
                usrlist.remove(usrIndex);
            }

            FileOutputStream out = new FileOutputStream(output);
            ;
            PrintStream out_p = new PrintStream(out);

            // dump output file
            int len;
            StringBuilder b = new StringBuilder(1024);
            for (int i = 0; i < total; i++) {
                a = inputAccess.get(i);
                //output format: queryID|data1,data2,...,datan|arrTime|userID|maxQos|relDeadline|maxQod|stale
                b.append(a.queryID);
                b.append('|');
                len = a.data.length;
                b.append(a.data[0]);
                for (int j = 1; j < len; j++) {
                    b.append(',');
                    b.append(a.data[j]);
                }
                b.append('|');
                b.append(a.timestamp);
                b.append('|');
                b.append(a.u.userID);
                b.append('|');
                b.append(a.u.maxQos);
                b.append('|');
                b.append(a.u.relDeadline);
                b.append('|');
                b.append(a.u.maxQod);
                b.append('|');
                b.append(a.u.stale);
                b.append('\n');

                out_p.print(b.toString());
                b.setLength(0);
            }
            out_p.close();

        } catch (FileNotFoundException exception) {
            System.out.println("The file " + inputUserProfile + " was not found.");
            System.exit(0);
        } catch (IOException exception) {
            System.out.println(exception);
            System.exit(0);
        }

        return users;

    }
//    public static void main(String[] args) throws IOException{
//		//new UserProfileGenerator("userConfig.txt", "userProfile.txt");
//		//AddUser(a, "userProfile.txt", "query.txt");
//		// User
//		new UserProfileGenerator("userConfig.txt", "userProfile.txt");
//
//	}
}

