package com.hp.blsaudit;

import com.hp.blsaudit.entity.PKG;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;

public class Test2 {
    public static void main(String[] args) {
        PKG pkg = new PKG();
        long start, end;
        long avg = 0L;
        Element a, b;
        for (int i = 1; i <= 1000; i++) {
            start = System.currentTimeMillis();
            pkg.hashToG(ConfigureTest.V_J + ConfigureTest.TS_J);
            end = System.currentTimeMillis();
//            a = pkg.getG1().newRandomElement();
//            b = pkg.getG1().newRandomElement();
//            start = System.currentTimeMillis();
//            pkg.getPairing().pairing(a, b);
//            end = System.currentTimeMillis();
            avg += (end - start);
        }
        StdOut.println("Hash一次所需时间：" + (avg / 1000.0) + "ms");
//        StdOut.println("P一次所需时间：" + (avg / 1000.0) + "ms");
    }
}
