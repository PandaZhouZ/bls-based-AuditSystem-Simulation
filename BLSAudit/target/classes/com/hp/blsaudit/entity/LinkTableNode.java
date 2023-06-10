package com.hp.blsaudit.entity;

import it.unisa.dia.gas.jpbc.Element;

public class LinkTableNode {
    LinkTableNode pre;
    LinkTableNode post;
    Element value;

    public LinkTableNode() {
    }

    public LinkTableNode(Element value) {
        this.value = value;
    }

    public LinkTableNode getPre() {
        return pre;
    }

    public void setPre(LinkTableNode pre) {
        this.pre = pre;
    }

    public LinkTableNode getPost() {
        return post;
    }

    public void setPost(LinkTableNode post) {
        this.post = post;
    }

    public Element getValue() {
        return value;
    }

    public void setValue(Element value) {
        this.value = value;
    }
}
