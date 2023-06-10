package com.hp.blsaudit.util;

import com.hp.blsaudit.entity.DO;
import com.hp.blsaudit.entity.GM;
import com.hp.blsaudit.entity.PKG;
import it.unisa.dia.gas.jpbc.Element;
import java.util.List;
import java.util.Map;

public class Audit {
    public static boolean proofAudit(PKG pkg, GM gm, DO dataOwner, Chal chal, Proof proof) {
        Element dbi = pkg.getG1().newOneElement();
        List<Item> items = dataOwner.getEact().getMetadata().get(chal.getFileName());
        for (Map.Entry<Integer, Element> entry : chal.getChal().entrySet()) {
            int index = entry.getKey();
            Element random = entry.getValue();

            Item tmp = items.get(index);
            dbi = dbi.mul(pkg.hashToG("" + tmp.getVersion() + tmp.getTimestamp()).powZn(random)).getImmutable();
        }
        dbi = pkg.getPairing().pairing(dbi, gm.getGpk()).getImmutable();
        Element left = pkg.getPairing().pairing(proof.getTagProof(), chal.getGamma()).getImmutable();
        Element right = dbi.mul(pkg.getPairing().pairing(pkg.getg2().powZn(proof.getDataProof()), gm.getGpk())).getImmutable();
        return left.isEqual(right);
    }

//    public static boolean batchAudit(PKG pkg, GM gm, List<DO> dataOwners, List<Chal> chals, List<Proof> proofs) {
//
//    }
}
