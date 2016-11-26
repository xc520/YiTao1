package com.example.thinkpad.yitao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements Animation.AnimationListener {

    @BindView(R.id.imageView)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.wave_scale);
        animation.setAnimationListener(this);
        mImageView.startAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade,R.anim.hold);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
