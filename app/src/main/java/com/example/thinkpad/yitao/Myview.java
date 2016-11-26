package com.example.thinkpad.yitao;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface Myview extends MvpView{
    void showPrb();

    void hidePrb();

    void showMsg(String msg);
}
