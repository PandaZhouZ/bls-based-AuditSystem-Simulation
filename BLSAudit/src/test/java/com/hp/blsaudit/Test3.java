package com.hp.blsaudit;

import java.util.ArrayList;
import java.util.List;

public class Test3 {
    public static void main(String[] args) {
        long[] list = new long[]{
                39723, 39725, 39723, 39721, 39721, 39692, 39699, 39696, 39690, 39708
        };
        long res = 0;
        for (int i = 0; i < list.length; i++) {
            res += list[i];
        }
        System.out.println(res / (list.length * 10000.0));
//        long a = 0;
//        long start = System.currentTimeMillis();
//        for (long i = 1; i <= 1200000000L; i++) {
//            a += i;
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
    }
}
