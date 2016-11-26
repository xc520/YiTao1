package com.example.thinkpad.yitao.regiter;

import com.example.thinkpad.yitao.monder.CachePreferences;
import com.example.thinkpad.yitao.monder.User;
import com.example.thinkpad.yitao.monder.UserResult;
import com.example.thinkpad.yitao.network.EasyShopClient;
import com.example.thinkpad.yitao.network.UICAllback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by cjw on 2016/11/23.
 */

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {
    private Call call;

    //视图销毁，取消网络请求

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call!=null){
            call.cancel();
        }
    }
    public void register(String username, String password){
        //显示加载动画
        getView().showPrb();
        call= EasyShopClient.getInstance().register(username,password);
        call.enqueue(new UICAllback() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                //隐藏动画
                getView().hidePrb();
//                显示异常信息
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                //拿到返回的结果
                UserResult userResult = new Gson().fromJson(body,UserResult.class);
                //根据结果码处理不同情况
                if (userResult.getCode()==1){
                    getView().showMsg("注册成功");
                    //拿到用户的实体类
                    User user=userResult.getData();
                    //将用户信息保存到本地配置里
                    CachePreferences.setUser(user);
                    //调用注册成功的方法
                    getView().registerSuccess();
                }else if (userResult.getCode()==2){
                    getView().hidePrb();
                  getView().showMsg(userResult.getMessage());
                    //调用注册失败的方法
                    getView().registerFailed();
                }
                else {
                    getView().showMsg("未知错误");
                }
            }
        });
    }
}
