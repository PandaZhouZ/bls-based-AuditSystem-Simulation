package com.hp.blsaudit.entity;

import com.hp.blsaudit.util.*;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CSP {
    private Element csk;
    private Element cpk;
    private String id;

    public CSP(PKG pkg) {
        this.csk = pkg.getZp().newRandomElement().getImmutable();
        this.cpk = pkg.getg1().powZn(csk).getImmutable();
        this.id = Transform.bytesToHex(cpk.toBytes());
    }

    public Proof newhpProofGen(PKG pkg,DO dataOwner,List<Chal> chals){
        Element tp = pkg.getG1().newOneElement();
        Element dp = pkg.getZp().newZeroElement();
        Chal chal;

        for (int i=0;i<chals.size();++i){
            chal=chals.get(i);
            List<Item> items = dataOwner.getEact().getMetadata().get(chal.getFileName());

            for (Map.Entry<Integer, Element> entry : chal.getChal().entrySet()) {
                int index = entry.getKey();
                Element random = entry.getValue();
                Element tag = items.get(index).getTag();
                tp = tp.mul(tag.powZn(random)).getImmutable();
                dp = dp.add(Transform.bytesToZp(pkg, FileOp.toByteArray(chal.getFileName(), dataOwner.getId(),index)).mul(random)).getImmutable();
            }
        }
        return new Proof(tp,dp);
    }

    public Proof proofGen(PKG pkg, DO dataOwner, Chal chal) {
        List<Item> items = dataOwner.getEact().getMetadata().get(chal.getFileName());
        Element tp = pkg.getG1().newOneElement();
        Element dp = pkg.getZp().newZeroElement();

        for (Map.Entry<Integer, Element> entry : chal.getChal().entrySet()) {
            int index = entry.getKey();
            Element random = entry.getValue();
            Element tag = items.get(index).getTag();
            tp = tp.mul(tag.powZn(random)).getImmutable();
            dp = dp.add(Transform.bytesToZp(pkg, FileOp.toByteArray(chal.getFileName(), dataOwner.getId(),index)).mul(random)).getImmutable();
        }
        return new Proof(tp, dp);
    }



    public Element paperProofGen(PKG pkg,A1PKG a1PKG,DO dataOwner,Chal chal){
        List<Item> items = dataOwner.getEact().getMetadata().get(chal.getFileName());
        Element dp = a1PKG.getZr().newZeroElement().getImmutable();

        for (Map.Entry<Integer, Element> entry : chal.getChal().entrySet()) {
            int index = entry.getKey();
            Element random = entry.getValue();
            dp = dp.add(Transform.bytesToZp(pkg, FileOp.toByteArray(chal.getFileName(), dataOwner.getId(),index)).mul(random)).getImmutable();
        }
        return dp;
    }

    public DredasProof dredasProofGen(PKG pkg,DO dataOwner,List<Chal> chals){
        DredasProof proof = new DredasProof();
        Element lk = pkg.getZp().newRandomElement().getImmutable();
        proof.setRandk(dataOwner.getUsk().powZn(lk).getImmutable());
        List<Element> tp=proof.getTagProof();
        List<Element> hp=proof.getHashProof();
        List<Element> dp=proof.getDataProof();
        Element lkh = lk.mul(Transform.bytesToZp(pkg,proof.getRandk().toBytes())).getImmutable();
        Element tempTp;
        Element tempHp;
        Element tempDp;
        Chal chal;
        for (int i=0;i<chals.size();++i){
            chal=chals.get(i);
            List<Item> items = dataOwner.getEact().getMetadata().get(chal.getFileName());
            tempDp=lkh;
            tempHp=pkg.getG1().newOneElement().getImmutable();
            tempTp=pkg.getG1().newOneElement().getImmutable();
            for (Map.Entry<Integer,Element> entry:chal.getChal().entrySet()){
                int index = entry.getKey();
                Element random=entry.getValue();
                tempDp=tempDp.add(Transform.bytesToZp(pkg, FileOp.toByteArray(chal.getFileName(), dataOwner.getId(),index)).mul(random)).getImmutable();
                tempTp=tempTp.mul(items.get(index).getTag().powZn(random)).getImmutable();
                tempHp=tempHp.mul(pkg.hashToG(FileOp.toByteArray(chal.getFileName(), dataOwner.getId(),index).toString()).powZn(random)).getImmutable();
            }
            tp.add(tempTp);
            hp.add(tempHp);
            dp.add(tempDp);
        }
        return proof;
    }
}
