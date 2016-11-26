package com.example.thinkpad.yitao.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thinkpad.yitao.LogActivity;
import com.example.thinkpad.yitao.R;
import com.example.thinkpad.yitao.common.ActivityUtils;
import com.example.thinkpad.yitao.compant.AvatarLoadOptions;
import com.example.thinkpad.yitao.me.perinfo.PersonActivity;
import com.example.thinkpad.yitao.monder.CachePreferences;
import com.example.thinkpad.yitao.network.EasyShopApi;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.thinkpad.yitao.R.id.iv_user_head;
import static com.example.thinkpad.yitao.R.id.tv_login;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {


    @BindView(iv_user_head)
    ImageView mIvUserHead;
    @BindView(tv_login)
    TextView mTvLogin;
    private View view;
    private ActivityUtils activityUtils;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_me, container, false);
            ButterKnife.bind(this, view);
        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CachePreferences.getUser().getName() == null) return;
         if (CachePreferences.getUser().getNick_name() == null) {
            mTvLogin.setText("请输入昵称");
        } else {
            mTvLogin.setText(CachePreferences.getUser().getNick_name());
        }
        ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL + CachePreferences.getUser().
                getHead_image(),mIvUserHead, AvatarLoadOptions.build());
        // TODO: 2016/11/23 0023 更改用户头像，待实现
    }


    @OnClick({iv_user_head, R.id.tv_login, R.id.tv_person_info, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick(View view) {
        if (CachePreferences.getUser().getName() == null) {
            activityUtils.startActivity(LogActivity.class);
            return;
        }

        switch (view.getId()) {
            case iv_user_head:

            case R.id.tv_login:

            case R.id.tv_person_info:
                activityUtils.startActivity(PersonActivity.class);
                break;
            case R.id.tv_person_goods:
                activityUtils.showToast("我的商品 待实现");
                break;
            case R.id.tv_goods_upload:
                activityUtils.showToast("商品上传 待实现");
                break;
        }
    }
}
