package com.example.common;
public class RestResp<T> {

    public final int status;
    public final String message;
    public T data;

    public RestResp(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private RestResp(int status, String message) {
        this(status, message, null);
    }

    public static <T> RestResp success(T data) {
        return new RestResp(1, "success", data);
    }

    public static <T> RestResp success() {
        return new RestResp<T>(1, "success");
    }

    public static <T> RestResp fail(String message, T data) {
        return new RestResp(-1, message, data);
    }

    public static <T> RestResp fail(String message) {
        return new RestResp<T>(-1, message);
    }

    public static <T> RestResp fail() {
        return new RestResp<T>(-1, "fail");
    }

}