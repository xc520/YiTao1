package com.example.thinkpad.yitao;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thinkpad.yitao.monder.CachePreferences;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnloginFragment extends Fragment {


    public UnloginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // 判断用户是否
        if (CachePreferences.getUser().getName() != null){
            return inflater.inflate(R.layout.fragment_login, container, false);
        }
        return inflater.inflate(R.layout.fragment_unlogin, container, false);
    }

}
