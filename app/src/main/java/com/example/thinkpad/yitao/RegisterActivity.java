package com.example.thinkpad.yitao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.thinkpad.yitao.common.ActivityUtils;
import com.example.thinkpad.yitao.common.RegexUtils;
import com.example.thinkpad.yitao.compant.AlertDialogFragment;
import com.example.thinkpad.yitao.compant.ProgressDialogFragment;
import com.example.thinkpad.yitao.regiter.RegisterPresenter;
import com.example.thinkpad.yitao.regiter.RegisterView;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.thinkpad.yitao.R.id.et_pwd;

public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_pwdAgain)
    EditText mEtPwdAgain;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    private String username;
    private String password;
    private String pwd_again;
    ActivityUtils activityUtils;
    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        init();
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    private void init() {
        mEtUsername.addTextChangedListener(textWatcher);
        mEtPwd.addTextChangedListener(textWatcher);
        mEtPwdAgain.addTextChangedListener(textWatcher);
        setSupportActionBar(mToolbar);
        //给左上角加一个返回图标,需要重写菜单点击事件，否则点击无效
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //给左上角加一个返回图标,需要重写菜单点击事件，否则点击无效
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            username = mEtUsername.getText().toString();
            password = mEtPwd.getText().toString();
            pwd_again = mEtPwdAgain.getText().toString();
            boolean canLogin = !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(pwd_again));
            mBtnRegister.setEnabled(canLogin);
        }
    };

    @OnClick(R.id.btn_register)
    public void onClick() {
        if (RegexUtils.verifyUsername(username) != RegexUtils.VERIFY_SUCCESS) {
            String msg = getString(R.string.username_rules);
            showUserPasswordError(msg);
            return;
        } else if (RegexUtils.verifyPassword(password) != RegexUtils.VERIFY_SUCCESS) {
            String msg = getString(R.string.password_rules);
            showUserPasswordError(msg);
            return;
        } else if (!TextUtils.equals(password, pwd_again)) {
            String msg = getString(R.string.pwd_again);
            showUserPasswordError(msg);
            return;
        }
        presenter.register(username,password);

    }


    @Override
    public void showPrb() {
        //关闭软键盘
        activityUtils.hideSoftKeyboard();
        if (dialogFragment == null) {
            dialogFragment = new ProgressDialogFragment();

        } else if (dialogFragment.isVisible()) {
            return;
        }
        dialogFragment.show(getSupportFragmentManager(), "progress_dialog_fragment");

    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void registerFailed() {
        mEtUsername.setText("");
    }

    @Override
    public void registerSuccess() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void showUserPasswordError(String msg) {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(msg);
        fragment.show(getSupportFragmentManager(), getString(R.string.username_pwd_rule));
    }
}
