package com.example.thinkpad.yitao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.thinkpad.yitao.common.ActivityUtils;
import com.example.thinkpad.yitao.compant.ProgressDialogFragment;
import com.example.thinkpad.yitao.login.LoginPresenter;
import com.example.thinkpad.yitao.login.LoginView;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.thinkpad.yitao.R.id.btn_login;
import static com.example.thinkpad.yitao.R.id.et_pwd;

public class LogActivity extends MvpActivity<LoginView,LoginPresenter> implements LoginView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(et_pwd)
    EditText mEtPwd;
    @BindView(btn_login)
    Button mBtnLogin;
    private ActivityUtils activityUtils;
    private ProgressDialogFragment dialogFragment;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        init();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    private void init() {
        mEtUsername.addTextChangedListener(textWatcher);
        mEtPwd.addTextChangedListener(textWatcher);
        if (dialogFragment == null) {
            dialogFragment = new ProgressDialogFragment();
        }
        setSupportActionBar(mToolbar);
        //给左上角加一个返回图标,需要重写菜单点击事件，否则点击无效
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //给左上角加一个返回图标,需要重写菜单点击事件，否则点击无效

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        //这里的s表示改变之前的内容，通常start和count组合，可以在s中读取本次改变字段中被改变的内容。
        //而after表示改变后新的内容的数量。
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //这里的s表示改变之后的内容，通常start和count组合，可以在s中读取本次改变字段中新的内容。
        //而before表示被改变的内容的数量。
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //表示最终内容
        @Override
        public void afterTextChanged(Editable s) {
            username =mEtUsername.getText().toString();
            password = mEtPwd.getText().toString();
            //判断用户名和密码是否为空
            boolean canLogin = !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
            mBtnLogin.setEnabled(canLogin);
        }
    };

    @OnClick({btn_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case btn_login:
             presenter.login(username,password);
                break;
            case R.id.tv_register:
                activityUtils.startActivity(RegisterActivity.class);
                break;
        }
    }

    @Override
    public void showPrb() {
        activityUtils.hideSoftKeyboard();
        if (dialogFragment == null) dialogFragment = new ProgressDialogFragment();
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(),"progress_dialog_fragment");
    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void loginFailed() {
        mEtUsername.setText("");
    }

    @Override
    public void loginSuccess() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }
}
