package com.jwnba24.database_parse_project.controller;

/**
 * Created by jiwen on 2019/1/2.
 */
public class Result {
    boolean status;
    String data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
