package com.hp.blsaudit.util;

//Request
public class Req {
    private String type;
    private long timestamp;
    private String from;
    private String to;
    private String fileName;
    private byte[] signature;

    public Req(String fileName) {
        this.fileName = fileName;
    }

    public Req(String type, long timestamp, String fileName) {
        this.type = type;
        this.timestamp = timestamp;
        this.fileName = fileName;
    }

    public Req(String type, long timestamp, String from, String to, String fileName, byte[] signature) {
        this.type = type;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.fileName = fileName;
        this.signature = signature;
    }

    public String getFileName() {
        return fileName;
    }
}
