package com.hp.blsaudit.util;

import it.unisa.dia.gas.jpbc.Element;

public class Item {
    private long version;
    private long timestamp;
    private long last_op_height;
    private String last_op_type;
    private int last_op_state;
    private Element tag;

    public Item() {
        this.version = 1L;
        this.timestamp = System.currentTimeMillis();
        this.last_op_height = -1L;
        this.last_op_type = "insert";
        this.last_op_state = -1;
        this.tag = null;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getLoh() {
        return last_op_height;
    }

    public void setLoh(long last_op_height) {
        this.last_op_height = last_op_height;
    }

    public String getLot() {
        return last_op_type;
    }

    public void setLot(String last_op_type) {
        this.last_op_type = last_op_type;
    }

    public int getLos() {
        return last_op_state;
    }

    public void setLos(int last_op_state) {
        this.last_op_state = last_op_state;
    }

    public Element getTag() {
        return tag;
    }

    public void setTag(Element tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "v=" + version + ", ts=" + timestamp + ", loh=" + last_op_height + ", lot=" + last_op_type + ", los=" + last_op_state;
    }
}
