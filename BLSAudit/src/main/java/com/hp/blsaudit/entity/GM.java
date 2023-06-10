package com.hp.blsaudit.entity;

import com.hp.blsaudit.util.Transform;
import it.unisa.dia.gas.jpbc.Element;

public class GM {
    private Element gsk;
    private Element gpk;
    private String id;

    public GM(PKG pkg) {
        this.gsk = pkg.getZp().newRandomElement().getImmutable();
        this.gpk = pkg.getg1().powZn(gsk).getImmutable();
        this.id = Transform.bytesToHex(gpk.toBytes());
    }

    public Element getGpk() {
        return gpk;
    }
}
