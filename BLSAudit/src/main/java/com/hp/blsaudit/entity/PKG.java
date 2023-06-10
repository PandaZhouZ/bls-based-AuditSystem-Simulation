package com.hp.blsaudit.entity;

import com.hp.blsaudit.configure.Configure;
import com.hp.blsaudit.crypto.Digest;
import com.hp.blsaudit.crypto.PS06Sign;
import com.hp.blsaudit.util.Transform;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PKG {
    private Field Zp;
    private Field G1;
    private Field G2;
    private Field GT;
    private Element g1;
    private Element g2;
    private Pairing pairing;
    private AsymmetricCipherKeyPair keyPair;

    // 生成 TypeA (对称质数阶) 双线性群
    public PKG() {
        iniPair(Configure.NU, Configure.NM);
    }

    public PKG(int rBits, int qBits, int nU, int nM) {
        // 初始化 JPBC TypeA pairing generator
        TypeACurveGenerator pg = new TypeACurveGenerator(rBits, qBits);
        // 生成参数
        PairingParameters typeAParams = pg.generate();
        // 将参数写入文件 typeAparams.properties
        String fileName = Configure.PROPROOT + "typeAparams.properties";
        Out out = new Out(fileName);
        out.println(typeAParams);
        iniPair(nU, nM);
    }

    // 读取文件 typeAparams.properties 初始化双线性群
    public void iniPair(int nU, int nM) {
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        String parametersPath = Configure.PROPROOT + "typeAparams.properties";
        this.pairing = PairingFactory.getPairing(parametersPath);
        // 获取 Zp
        this.Zp = pairing.getZr();
        // 获取 G1
        this.G1 = pairing.getG1();
        // 获取 G2
        this.G2 = pairing.getG2();
        // 获取 GT
        this.GT = pairing.getGT();
        // 获取 g1
        this.g1 = G1.newRandomElement().getImmutable();
        // 获取 g2
        this.g2 = G2.newRandomElement().getImmutable();

        this.keyPair = PS06Sign.setUp(PS06Sign.createParameters(nU, nM));
    }

    public Field getZp() {
        return Zp;
    }

    public Field getG1() {
        return G1;
    }

    public Field getG2() {
        return G2;
    }

    public Field getGT() {
        return GT;
    }

    public Element getg1() {
        return g1;
    }

    public Element getg2() {
        return g2;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public AsymmetricCipherKeyPair getKeyPair() {
        return keyPair;
    }

    // H: {0, 1}* -> G1
    public Element hashToG(String str) {
        byte[] bytes = str.getBytes();
        byte[] bytes512 = Digest.sha512(bytes);
        return G1.newElementFromHash(bytes512, 0, bytes512.length).getImmutable();
    }

    // 随机生成伪随机函数所需的 key: Zp -> {0, 1}*
    public String fKey() {
        Element random = Zp.newRandomElement().getImmutable();
        return Transform.bytesToHex(random.toBytes());
    }

    // 伪随机函数 pseudo-random function f: {0, 1}* -> Zp
    public Element pseudoFunc(String key, int index) {
        try {
            byte[] bytes = (key + index).getBytes();
            return Transform.bytesToZp(this, bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return Zp.newRandomElement().getImmutable();
        }
    }

    // 随机生成伪随机置换函数所需的 key: Zp -> int
    public int piKey() {
        try {
            Element random = Zp.newRandomElement().getImmutable();
            ByteBuffer wrapped = ByteBuffer.wrap(random.toBytes());
            return wrapped.getShort();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 伪随机置换函数 pseudo-random permutation π: int -> List<Integer>
    public List<Integer> pseudoPerm(int key, int n, int s) {
        List<Integer> result = new ArrayList<Integer>(s);
        if (s < n) {
            List<Integer> list = new ArrayList<Integer>(n);
            for (int i = 0; i < n; i++) {
                list.add(i);
            }
            Random rnd = new Random(key);
            Collections.shuffle(list, rnd);
            for (int i = 0; i < s; i++) {
                result.add(list.get(i));
            }
        } else {
            StdOut.println("pseudo random permutation error!");
        }
        return result;
    }
}
