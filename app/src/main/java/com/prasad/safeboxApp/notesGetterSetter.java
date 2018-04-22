package com.prasad.safeboxApp;

/**
 * Created by Prasad on 3/10/17.
 */

public class notesGetterSetter {

    protected String header,body;
    protected int id;

    protected notesGetterSetter(int id,String header, String body) {
        this.header = header;
        this.body = body;
        this.id = id;
    }

    protected String getHeader() {
        return header;
    }

    protected String getBody() {
        return body;
    }

    protected int getId() {
        return id;
    }

    protected void setHeader(String header) {
        this.header = header;
    }

    protected void setBody(String body) {
        this.body = body;
    }

    protected void setId(int id) {
        this.id = id;
    }
}
