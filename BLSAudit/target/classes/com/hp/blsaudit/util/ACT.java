package com.hp.blsaudit.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//
public class ACT {
    private Map<String, LinkedList<Item>> metadata;

    public ACT() {
        this.metadata = new HashMap<>();
    }

    public Map<String, LinkedList<Item>> getMetadata() {
        return metadata;
    }

    public void addList(String fileName, int blkNum) {
        LinkedList<Item> items = new LinkedList<>();
        for (long i = 0; i < blkNum; i++) {
            items.add(new Item());
        }
        metadata.put(fileName, items);
    }
}
