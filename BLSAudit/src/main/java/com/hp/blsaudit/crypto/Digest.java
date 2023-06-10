package com.hp.blsaudit.crypto;

import org.bouncycastle.crypto.digests.SHA512Digest;

public class Digest {
    // SHA512 摘要算法
    public static byte[] sha512(byte[] bytes) {
        SHA512Digest dgst = new SHA512Digest();
        dgst.update(bytes, 0, bytes.length);
        byte[] hash512 = new byte[dgst.getDigestSize()];
        dgst.doFinal(hash512, 0);
        return hash512;
    }
}
