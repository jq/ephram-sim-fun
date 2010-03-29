/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.io.*;
import java.util.*;
import mainpackage.*;

/**
 *
 * @author hillboy
 */
public class SortDataSourceByAccessTime implements Comparator<DATAandSOURCE_pair> {

    public int compare(DATAandSOURCE_pair o1, DATAandSOURCE_pair o2) {
        return o1.accessTime - o2.accessTime;
    }
}
