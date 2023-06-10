package com.hp.blsaudit.util;

import com.hp.blsaudit.entity.PKG;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;

public class DredasProof {
    private Element Randk;
    private List<Element> TagProof;
    private List<Element> HashProof;
    private List<Element> DataProof;

    public DredasProof() {
        this.TagProof=new ArrayList<>();
        this.HashProof=new ArrayList<>();
        this.DataProof=new ArrayList<>();
    }

    public Element getRandk() {
        return Randk;
    }

    public void setRandk(Element randk) {
        Randk = randk;
    }

    public List<Element> getTagProof() {
        return TagProof;
    }

    public void setTagProof(List<Element> tagProof) {
        TagProof = tagProof;
    }

    public List<Element> getHashProof() {
        return HashProof;
    }

    public void setHashProof(List<Element> hashProof) {
        HashProof = hashProof;
    }

    public List<Element> getDataProof() {
        return DataProof;
    }

    public void setDataProof(List<Element> dataProof) {
        DataProof = dataProof;
    }
}
