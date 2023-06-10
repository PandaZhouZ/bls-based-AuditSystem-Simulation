package com.hp.blsaudit.util;

import com.hp.blsaudit.crypto.Digest;
import com.hp.blsaudit.entity.A1PKG;
import com.hp.blsaudit.entity.PKG;
import it.unisa.dia.gas.jpbc.Element;

public class Transform {
    // byte 数组转为对应的 16 进制串
    public static String bytesToHex(byte[] bytes) {
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            sb.append(HEX.charAt(b & 0x0f));
        }
        return sb.toString();
    }

    // byte[] -> Zp
    public static Element bytesToZp(PKG pkg, byte[] bytes) {
        byte[] hash = Digest.sha512(bytes);
        return pkg.getZp().newElementFromHash(hash, 0, bytes.length).getImmutable();
    }

    public static Element bytesToZr(A1PKG pkg,byte[] bytes){
        byte[] hash=Digest.sha512(bytes);
        return pkg.getZr().newElementFromHash(hash,0,bytes.length).getImmutable();
    }
}
