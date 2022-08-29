package com.example.common;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpClient {
    private static final okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .build();

//    public static String get(String url) {
//        return execute(new Request.Builder().url(url).get().build());
//    }

    public static String postForm(String url, Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
        }
        MultipartBody body = builder.build();
        return execute(new Request.Builder().url(url).post(body).build());
    }

    private static String execute(Request request) {
        String result = null;
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                response.body().close();
            }
        } catch (IOException e) {
            log.error("OkHttpClientHelper execute error:{} url:{} method:{}", e.getMessage(), request.url(), request.method(), e);
        } finally {
            if (null != response && null != response.body()) {
                response.body().close();
            }
        }
        return result;
    }

}
