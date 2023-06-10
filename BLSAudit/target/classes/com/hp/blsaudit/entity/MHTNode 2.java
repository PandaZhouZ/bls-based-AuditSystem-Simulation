package com.hp.blsaudit.entity;

import it.unisa.dia.gas.jpbc.Element;

public class MHTNode {
    Element value;
    MHTNode left;
    MHTNode right;

    public MHTNode(Element value) {
        this.value = value;
    }

    public Element getValue() {
        return value;
    }

    public void setValue(Element value) {
        this.value = value;
    }

    public MHTNode getLeft() {
        return left;
    }

    public void setLeft(MHTNode left) {
        this.left = left;
    }

    public MHTNode getRight() {
        return right;
    }

    public void setRight(MHTNode right) {
        this.right = right;
    }
}
