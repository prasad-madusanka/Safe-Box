package com.prasad.safeboxApp;


/**
 * Created by Prasad on 1/8/17.
 */

public class vaultGetterSetter {

    private String uname,pwd,desc;
    private int id;

    protected vaultGetterSetter(int id, String uname, String pwd, String desc) {
        this.id=id;
        this.desc = desc;
        this.pwd = pwd;
        this.uname = uname;
    }


    protected int getId() {
        return id;
    }

    protected String getUname() {
        return uname;
    }

    protected String getPwd() {
        return pwd;
    }

    protected String getDesc() {
        return desc;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setUname(String uname) {
        this.uname = uname;
    }

    protected void setPwd(String pwd) {
        this.pwd = pwd;
    }

    protected void setDesc(String desc) {
        this.desc = desc;
    }

}
