package com.hp.blsaudit;

import edu.princeton.cs.algs4.StdOut;

public class Calculate {
    public static void main(String[] args) {
        double M = 0.04;
        double E = 9;
        double P = 4.63;
        double H = 20;

        int n = 1;
        int s = 1;
        int z = 50;

//        StdOut.println(((2 * n + 1) * P + n * (s * (2 + z) + 1) * (E + M) - 3 * M) / 1000 + "s");
        StdOut.println(((n + 2) * P + (n * s * z + 1) * E + n * (s * (1 + z) - 1) * M + n * (1 + s * z) * H) / 1000 + "s");

    }
}
