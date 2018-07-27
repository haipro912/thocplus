package com.stringee;

/**
 * Created by luannguyen on 9/27/16.
 */
public class StringeeIceServer {
    public final String uri;
    public final String username;
    public final String password;

    public StringeeIceServer(String uri, String username, String password) {
        this.uri = uri;
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return this.uri + "[" + this.username + ":" + this.password + "]";
    }
}
