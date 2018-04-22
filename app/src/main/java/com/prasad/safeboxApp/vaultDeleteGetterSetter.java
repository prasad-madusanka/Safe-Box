package com.prasad.safeboxApp;

/**
 * Created by Prasad on 3/20/17.
 */

public class vaultDeleteGetterSetter {

    protected String uname,description;
    protected int id;

    protected vaultDeleteGetterSetter(int id,String uname,String description) {
        this.description = description;
        this.uname=uname;
        this.id=id;
    }

    protected String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected String getUname() {
        return uname;
    }

    protected void setUname(String uname) {
        this.uname = uname;
    }
}
