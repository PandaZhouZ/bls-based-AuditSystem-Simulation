package com.hp.blsaudit.util;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;
import java.util.Map;

public class Chal extends Req {
    private Map<Integer, Element> chal;
    private Element gamma;
    private List<Long> lohs;

    public Chal(String fileName, Map<Integer, Element> chal, Element gamma) {
        super(fileName);
        this.chal = chal;
        this.gamma = gamma;
    }

    public Chal(String type, long timestamp, String fileName, Map<Integer, Element> chal, Element gamma) {
        super(type, timestamp, fileName);
        this.chal = chal;
        this.gamma = gamma;
    }

    public Chal(String type, long timestamp, String from, String to, String fileName, byte[] signature, Map<Integer, Element> chal, Element gamma, List<Long> lohs) {
        super(type, timestamp, from, to, fileName, signature);
        this.chal = chal;
        this.gamma = gamma;
        this.lohs = lohs;
    }

    public Map<Integer, Element> getChal() {
        return chal;
    }

    public Element getGamma() {
        return gamma;
    }
}
