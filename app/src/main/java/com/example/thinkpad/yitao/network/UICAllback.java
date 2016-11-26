package com.example.thinkpad.yitao.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by cjw on 2016/11/18.
 */

public abstract  class UICAllback implements Callback {
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public void onFailure(final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailureUI(call,e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        //判断成功的情况
        if (!response.isSuccessful()){
            throw new IOException("error code:" + response.code());
        }
        final String boby=response.body().string();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponseUI(call,boby);
            }
        });

    }
    public abstract void onFailureUI(Call call, IOException e);

    public abstract void onResponseUI(Call call, String body);

}
