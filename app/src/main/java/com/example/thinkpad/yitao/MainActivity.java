package com.example.thinkpad.yitao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thinkpad.yitao.common.ActivityUtils;
import com.example.thinkpad.yitao.me.MeFragment;
import com.example.thinkpad.yitao.monder.CachePreferences;
import com.example.thinkpad.yitao.shop.ShopFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_toolbar)
    Toolbar mMainToolbar;
    @BindView(R.id.viewpage)
    ViewPager mViewpage;
    @BindViews({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    TextView[] textViews;
    @BindView(R.id.linerout)
    LinearLayout mLinerout;
    @BindView(R.id.activity_main)
    RelativeLayout mActivityMain;
    @BindView(R.id.text1)
    TextView mText1;
    private boolean isExit = false;
    private ActivityUtils activityUtils;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        //设置一下actionbar
        setSupportActionBar(mMainToolbar);
        //设置一下ActionBae标题为空，否则默认显示应用名
        getSupportActionBar().setTitle("");
        init();
    }

    private void init() {
        //刚进来默认选择市场
        textViews[0].setSelected(true);
        //TODO 用户是否登录，从而选择不同的适配器
        if (CachePreferences.getUser().getName() == null){
            mViewpage.setAdapter(unLogAdapter);
           mViewpage.setCurrentItem(0);
        }else{
            mViewpage.setAdapter(loginAdapter);
           mViewpage.setCurrentItem(0);
        }

        //viewpager添加滑动事件
        mViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //textView选择处理
                for (TextView textview : textViews
                        ) {
                    textview.setSelected(false);
                }
                //更改title，设置选择效果
                mText1.setText(textViews[position].getText());
                textViews[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private FragmentStatePagerAdapter unLogAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShopFragment();
                case 1:
                    return new UnloginFragment();
                case 2:
                    return new UnloginFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };
    private FragmentStatePagerAdapter loginAdapter = new
            FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position){
                        //市场
                        case 0:
                            return new ShopFragment();
                        //消息
                        case 1:
                            // TODO: 2016/11/23 0023 环信消息fragment
                            return new UnloginFragment();
                        //通讯录
                        case 2:
                            // TODO: 2016/11/23 0023 环信的通讯录fragment
                            return new UnloginFragment();
                        //我的
                        case 3:
                            return new MeFragment();
                    }
                    return null;
                }
                @Override
                public int getCount() {
                    return 4;
                }
            };



    @OnClick({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    public void onClick(View view) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setSelected(false);
            textViews[i].setTag(i);
        }
        //设置选择效果
        view.setSelected(true);
        //参数false代表瞬间切换，而不是平滑过渡
         mViewpage.setCurrentItem((Integer) view.getTag(),false);
        mText1.setText(textViews[(Integer) view.getTag()].getText());

    }
    //点击2次返回，退出程序

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            activityUtils.showToast("再摁一次退出程序");
            //两秒内再次点击返回则退出
            mViewpage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }

            }, 2000);
        } else {
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
