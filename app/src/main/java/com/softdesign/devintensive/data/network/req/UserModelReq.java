package com.softdesign.devintensive.data.network.req;

/**
 * Created by shmakova on 09.07.16.
 */
public class UserModelReq {

    private String email;
    private String password;

    public UserModelReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
