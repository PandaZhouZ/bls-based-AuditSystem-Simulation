package com.hp.blsaudit.entity;

import com.hp.blsaudit.util.*;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DO {
    private Element sk;
    private Element pk;
    private CipherParameters ssk;
    private CipherParameters spk;
    private String id;
    private Element inv;
    private Element gamma;
    private Element u;
    private Element usk;

    private ACT eact, dact;

    public DO(String id) {
        this.id = id;
        this.eact = new ACT();
        this.dact = new ACT();
    }

    public DO(PKG pkg) {
        this.sk = pkg.getZp().newRandomElement().getImmutable();
        this.pk = pkg.getg2().powZn(sk).getImmutable();
        this.u = pkg.getG1().newRandomElement().getImmutable();
        this.id = Transform.bytesToHex(pk.toBytes());
        this.usk = u.powZn(sk);
        this.eact = new ACT();
        this.dact = new ACT();
    }

    public DO(PKG pkg, GM gm) {
        this.sk = pkg.getZp().newRandomElement().getImmutable();
        this.pk = gm.getGpk().powZn(sk).getImmutable();
        this.ssk = pkg.getKeyPair().getPrivate();
        this.spk = pkg.getKeyPair().getPublic();
        this.inv = sk.invert().getImmutable();
        this.gamma = gm.getGpk().powZn(inv).getImmutable();
        this.id = Transform.bytesToHex(pk.toBytes());
        this.eact = new ACT();
        this.dact = new ACT();
    }

    public Element getU() {
        return u;
    }

    public Element getUsk() {
        return usk;
    }

    public String getId() {
        return id;
    }

    public Element getPK() {
        return pk;
    }

    public Element getSK() {
        return sk;
    }

    public Element getGamma() {
        return gamma;
    }

    public ACT getEact() {
        return eact;
    }

    public void setEact(ACT eact) {
        this.eact = eact;
    }

    public void testMod(PKG pkg) {
        StdOut.println(sk.mul(inv).toBigInteger().mod(pkg.getZp().getOrder()));
    }

    public int getShards(PKG pkg, String fileName, int shardSize) {
        try {
            int blkNum = FileOp.splitFile(fileName, this.id,shardSize);
            eact.addList(fileName, blkNum);
            long start = System.currentTimeMillis();
            long end;
            for (int i = 0; i < blkNum; i++) {
                byte[] blk = FileOp.toByteArray(fileName,this.id, i);
                Item item = eact.getMetadata().get(fileName).get(i);
                item.setTag(tagGen(pkg, blk, item));
                end = System.currentTimeMillis();
                if ((i + 1) % 50 == 0 && i < 500) {
                    StdOut.println("生成" + (i + 1) + "块的签名总共花费的时间：" + (end - start) + "ms");
                }
            }
            StdOut.println("文件" + fileName + "信息写入EACT！");
            return blkNum;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public int getNewhpShards(PKG pkg, String fileName, int shardSize) {
        try {
            int blkNum = FileOp.splitFile(fileName, this.id,shardSize);
            eact.addList(fileName, blkNum);
            long start = System.currentTimeMillis();
            long end;
            for (int i = 0; i < blkNum; i++) {
                byte[] blk = FileOp.toByteArray(fileName,this.id, i);
                Item item = eact.getMetadata().get(fileName).get(i);
                item.setTag(newhpTagGen(pkg, blk, item));
                end = System.currentTimeMillis();
//                if ((i + 1) % 50 == 0 && i < 500) {
//                    StdOut.println("生成" + (i + 1) + "块的签名总共花费的时间：" + (end - start) + "ms");
//                }
            }
//            StdOut.println("文件" + fileName + "信息写入EACT！");
            return blkNum;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getFabricShards(PKG pkg, String fileName, int shardSize) {
        try {
            int blkNum = FileOp.splitFile(fileName, this.id,shardSize);
            eact.addList(fileName, blkNum);
            long start = System.currentTimeMillis();
            long end;
            for (int i = 0; i < blkNum; i++) {
                byte[] blk = FileOp.toByteArray(fileName,this.id, i);
                Item item = eact.getMetadata().get(fileName).get(i);
                item.setTag(fabricTagGen(pkg, blk));
                end = System.currentTimeMillis();
//                if ((i + 1) % 50 == 0 && i < 500) {
//                    StdOut.println("生成" + (i + 1) + "块的签名总共花费的时间：" + (end - start) + "ms");
//                }
            }
//            StdOut.println("文件" + fileName + "信息写入EACT！");
            return blkNum;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getDredasShards(PKG pkg, String fileName, int shardSize) {
        try {
            int blkNum = FileOp.splitFile(fileName, this.id,shardSize);
            eact.addList(fileName, blkNum);
            long start = System.currentTimeMillis();
            long end;
            for (int i = 0; i < blkNum; i++) {
                byte[] blk = FileOp.toByteArray(fileName,this.id, i);
                Item item = eact.getMetadata().get(fileName).get(i);
                item.setTag(dredasTagGen(pkg,blk,i));
                end = System.currentTimeMillis();
//                if ((i + 1) % 50 == 0 && i < 500) {
//                    StdOut.println("生成" + (i + 1) + "块的签名总共花费的时间：" + (end - start) + "ms");
//                }
            }
//            StdOut.println("文件" + fileName + "信息写入EACT！");
            return blkNum;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getPaperShards(PKG pkg,A1PKG a1PKG,String fileName,int shardSize){
        try {
            long start =System.currentTimeMillis();
            int blkNum=FileOp.blindSplitFile(fileName,this.id,shardSize,pkg);
//            System.out.println("盲化并分块"+blkNum+"块共耗时："+((System.currentTimeMillis()-start))+"ms");
            eact.addList(fileName, blkNum);
            start = System.currentTimeMillis();
            long end;
            for (int i = 0; i < blkNum; i++) {
                byte[] blk = FileOp.toByteArray(fileName,this.id, i);
                Item item = eact.getMetadata().get(fileName).get(i);
                item.setTag(paperTagGen(a1PKG,pkg,blk));
                end = System.currentTimeMillis();
//                if ((i + 1) % 50 == 0 && i < 500) {
//                    StdOut.println("生成" + (i + 1) + "块的签名总共花费的时间：" + (end - start) + "ms");
//                }
            }
//            StdOut.println("文件" + fileName + "信息写入EACT！");
            return blkNum;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Element fabricTagGen(PKG pkg,byte[] blk){
        Element h = pkg.hashToG(blk.toString()).getImmutable();
        Element upart = u.powZn(Transform.bytesToZp(pkg,blk).getImmutable()).getImmutable();
        h=h.mul(upart).getImmutable();
        return h.powZn(sk).getImmutable();
    }

    public Element dredasTagGen(PKG pkg,byte[] blk,int blockIndex){
        Element h = pkg.hashToG(blk.toString()).getImmutable();
        Element upart = u.powZn(Transform.bytesToZp(pkg,blk).getImmutable().add(pkg.getZp().newElement(blockIndex)).getImmutable()).getImmutable();
        h=h.mul(upart).getImmutable();
        return h.powZn(sk).getImmutable();
    }

    public Element paperTagGen(A1PKG a1PKG,PKG pkg,byte[] blk){
        return a1PKG.getg().powZn(Transform.bytesToZp(pkg,blk)).getImmutable();
    }

    public Element newhpTagGen(PKG pkg,byte[] blk,Item item){
        Element h = Transform.bytesToZp(pkg,("" + item.getVersion() + item.getTimestamp()).getBytes()).getImmutable();
        h=h.add(Transform.bytesToZp(pkg, blk).getImmutable()).getImmutable();
        Element g2Pow = pkg.getg2().powZn(h).getImmutable();
        return g2Pow.powZn(sk).getImmutable();
    }

    public Element tagGen(PKG pkg, byte[] blk, Item item) {
        Element h = pkg.hashToG("" + item.getVersion() + item.getTimestamp()).getImmutable();
        Element g2Pow = pkg.getg2().powZn(Transform.bytesToZp(pkg, blk)).getImmutable();
        return h.mul(g2Pow).powZn(sk).getImmutable();
    }


    public Chal chalGen(PKG pkg, String fileName, int z) {
        List<Integer> chalList = pkg.pseudoPerm(pkg.piKey(), eact.getMetadata().get(fileName).size(), z);
        Map<Integer, Element> chal = new HashMap<>();
        String f_key = pkg.fKey();
        for (int i = 0; i < chalList.size(); i++) {
            Element random_i = pkg.pseudoFunc(f_key, chalList.get(i));
            chal.put(chalList.get(i), random_i);
        }
//        StdOut.println("生成了" + z + "个挑战");
        return new Chal(fileName, chal, gamma);
    }

//    public Chal paerChalGen(A1PKG a1PKG,String fileName,int z){
//        List<Integer> chalList = a1PKG.pseudoPerm(a1PKG.piKey(),eact.getMetadata().get(fileName).size(),z);
//        Map<Integer, Element> chal = new HashMap<>();
//        String f_key = a1PKG.fKey();
//        for (int i = 0; i < chalList.size(); i++) {
//            Element random_i = a1PKG.pseudoFunc(f_key, chalList.get(i));
//            chal.put(chalList.get(i), random_i);
//        }
//        StdOut.println("生成了" + z + "个挑战");
//        return new Chal(fileName, chal, gamma);
//    }
}
