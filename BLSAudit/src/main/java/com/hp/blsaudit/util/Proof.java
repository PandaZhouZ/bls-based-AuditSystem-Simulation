package com.hp.blsaudit.util;

import it.unisa.dia.gas.jpbc.Element;

public class Proof {
    private Element tp;
    private Element dp;

    public Proof(Element tp, Element dp) {
        this.tp = tp;
        this.dp = dp;
    }

    public Element getTagProof() {
        return tp;
    }

    public Element getDataProof() {
        return dp;
    }
}
