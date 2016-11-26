package com.example.thinkpad.yitao.me.perinfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.thinkpad.yitao.MainActivity;
import com.example.thinkpad.yitao.R;
import com.example.thinkpad.yitao.common.ActivityUtils;
import com.example.thinkpad.yitao.compant.AvatarLoadOptions;
import com.example.thinkpad.yitao.compant.PicWindow;
import com.example.thinkpad.yitao.compant.ProgressDialogFragment;
import com.example.thinkpad.yitao.monder.CachePreferences;
import com.example.thinkpad.yitao.monder.ItemShow;
import com.example.thinkpad.yitao.monder.User;
import com.example.thinkpad.yitao.network.EasyShopApi;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.thinkpad.yitao.R.id.listView;

public class PersonActivity extends MvpActivity<PersonView, Personprester> implements PersonView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_user_head)
    CircularImageView mIvUserHead;
    @BindView(listView)
    ListView mListView;
    private ActivityUtils activityUtils;
    private ProgressDialogFragment progressDialogFragment;
    private List<ItemShow> list = new ArrayList<>();
    private PersonAdapter adapter;
    private PicWindow picWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PersonAdapter(list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(onItemClickListener);

        //获取用户头像

        updataAvatar(CachePreferences.getUser().getHead_image());
    }

    //方便修改完昵称，回来更改数据
    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        init();
        adapter.notifyDataSetChanged();
    }

    private void init() {
        User user = CachePreferences.getUser();
        list.add(new ItemShow(getResources().getString(R.string.username), user.getName()));
        list.add(new ItemShow(getResources().getString(R.string.nickname), user.getNick_name()));
        list.add(new ItemShow(getResources().getString(R.string.hx_id), user.getHx_Id()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    activityUtils.showToast(getResources().getString(R.string.username_update));
                    break;
                case 1:
                    activityUtils.startActivity(NickNameActivity.class);
                    break;
                case 2:
                    activityUtils.showToast(getResources().getString(R.string.id_update));
                    break;
            }
        }
    };

    @NonNull
    @Override
    public Personprester createPresenter() {
        return new Personprester();
    }

    @Override
    public void showPrb() {
        if (progressDialogFragment == null)
            progressDialogFragment = new ProgressDialogFragment();
        if (progressDialogFragment.isVisible())
            return;
        progressDialogFragment.show(getSupportFragmentManager(), "progress_dialog_fragment");
    }

    @Override
    public void hidePrb() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void updataAvatar(String url) {
        //// TODO: 2016/11/23 0023 设置头像
        ImageLoader.getInstance().displayImage(EasyShopApi.IMAGE_URL+url,mIvUserHead, AvatarLoadOptions.build());
    }

    @OnClick({R.id.iv_user_head, R.id.btn_login_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
                if (picWindow == null){
                    //图片选择弹窗的自定义监听
                    picWindow = new PicWindow(this,listener);
                }
                if (picWindow.isShowing()){
                    picWindow.dismiss();
                    return;

                }
                picWindow.show();
                break;
            case R.id.btn_login_out:
                //清空本地配置
                CachePreferences.clearAllData();
                Intent intent = new Intent(this, MainActivity.class);
                //清除所有旧的activtiy
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
    private PicWindow.Listener listener=new PicWindow.Listener() {
        @Override
        public void toGallery() {
            //从相册中选择
            //清空裁剪的缓存
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent=CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent,CropHelper.REQUEST_CROP);
        }

        @Override
        public void toCamera() {
            //从相机中选择
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent,CropHelper.REQUEST_CAMERA);
        }
    };

    //图片裁剪
    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //通过uri拿到图片文件
            File file = new File(uri.getPath());
            //业务类上传头像
            presenter.updataAvatar(file);
        }

        @Override
        public void onCropCancel() {
        }

        @Override
        public void onCropFailed(String message) {
        }

        @Override
        public CropParams getCropParams() {
            //自定义裁剪大小参数
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 400;
            cropParams.aspectY = 400;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return PersonActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(cropHandler,requestCode, resultCode, data);
    }
}
