package com.hp.blsaudit;

import edu.princeton.cs.algs4.StdOut;

public class Test4 {
    public static void main(String[] args) {
        long a = 0;
        long start = System.currentTimeMillis();
        for (long i = 1; i <= 1250000000L; i++) {
            a += 1;
        }
        long end = System.currentTimeMillis();
        StdOut.println((end - start) / 1000.0);
    }
}
