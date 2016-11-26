package com.example.thinkpad.yitao.monder;

/**
 * Created by cjw on 2016/11/18.
 */

public class Result {
    private String createdAt;
    private String objectId;
    private String sessionToken;

    //alt+inster。。。。。构造一些方法

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        return "Result{" +
                "createdAt='" + createdAt + '\'' +
                ", objectId='" + objectId + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }
}
