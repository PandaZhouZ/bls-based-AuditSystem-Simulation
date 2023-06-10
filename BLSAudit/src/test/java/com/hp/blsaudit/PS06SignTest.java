package com.hp.blsaudit;

import com.hp.blsaudit.configure.Configure;
import com.hp.blsaudit.crypto.PS06Sign;
import edu.princeton.cs.algs4.StdOut;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

public class PS06SignTest {
    public static void main(String[] args) {

        // setup() -> (public key, master secret key)
        AsymmetricCipherKeyPair keyPair = PS06Sign.setUp(PS06Sign.createParameters(Configure.NU, Configure.NM));

        // extract() -> secret key for identity "01001101"
        String identity = "01001101";
        CipherParameters secretKey = PS06Sign.extract(keyPair, identity);

        // Sign
        long start = System.currentTimeMillis();
        String message = "Hello World!!!";
        byte[] signature = PS06Sign.sign(message, secretKey);

        // verify
        StdOut.println(PS06Sign.verify(keyPair.getPublic(), message, identity, signature));
        long end = System.currentTimeMillis();
        StdOut.println(end - start);
        StdOut.println(PS06Sign.verify(keyPair.getPublic(), message, "01001100", signature));
        StdOut.println(PS06Sign.verify(keyPair.getPublic(), "Hello World!!", identity, signature));
    }
}
