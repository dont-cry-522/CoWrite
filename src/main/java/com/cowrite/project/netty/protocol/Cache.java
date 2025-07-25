package com.cowrite.project.netty.protocol;

public class Cache {

    private String docId;

    private String content;

    public Cache(String docId,String content) {
        this.docId=docId;
        this.content = content;
    }
}
