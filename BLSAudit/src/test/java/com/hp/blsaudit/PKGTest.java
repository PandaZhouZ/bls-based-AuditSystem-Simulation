package com.hp.blsaudit;

import com.hp.blsaudit.configure.Configure;
import com.hp.blsaudit.entity.PKG;
import edu.princeton.cs.algs4.StdOut;

import java.io.FileNotFoundException;

public class PKGTest {
    public static void main(String[] args) throws Exception {
        PKG pkg = new PKG(Configure.RBITS, Configure.QBITS, Configure.NU, Configure.NM);
        StdOut.println(pkg.getZp().newOneElement());
    }
}
