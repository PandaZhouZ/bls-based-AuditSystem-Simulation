package com.hp.blsaudit.entity;

import com.hp.blsaudit.configure.Configure;
import com.hp.blsaudit.util.Transform;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class A1PKG {
    private Field Zr;
    private Field G1;
    private Element g;
    private Pairing pairing;
    private AsymmetricCipherKeyPair keyPair;

    public A1PKG() {
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        String parametersPath = Configure.PROPROOT + "a1.properties";
        this.pairing = PairingFactory.getPairing(parametersPath);
        this.Zr=pairing.getZr();
        this.G1=pairing.getG1();
        this.g=G1.newRandomElement().getImmutable();

    }

    public Field getZr() {
        return Zr;
    }

    public Field getG1() {
        return G1;
    }

    public Element getg() {
        return g;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public String fKey() {
        Element random = Zr.newRandomElement().getImmutable();
        return Transform.bytesToHex(random.toBytes());
    }

    public Element pseudoFunc(String key, int index) {
        try {
            byte[] bytes = (key + index).getBytes();
            return Transform.bytesToZr(this, bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return Zr.newRandomElement().getImmutable();
        }
    }

    public int piKey(){
        try {
            Element random=Zr.newRandomElement().getImmutable();
            ByteBuffer wrapped = ByteBuffer.wrap(random.toBytes());
            return wrapped.getShort();
        }catch (Exception e){
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
