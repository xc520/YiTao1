package com.example.thinkpad.yitao.shop;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thinkpad.yitao.R;
import com.example.thinkpad.yitao.common.ActivityUtils;
import com.example.thinkpad.yitao.monder.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.example.thinkpad.yitao.R.id.refreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends MvpFragment<ShopView, Shoppresentr> implements ShopView {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(refreshLayout)
    PtrClassicFrameLayout mRefreshLayout;
    @BindView(R.id.tv_load_error)
    TextView mTvLoadError;
    private ActivityUtils activityUtils;
    private ShopAdapter mAdapter;
    //获取商品时，商品类型，获取全部商品时为空
    private String pageType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        mAdapter = new ShopAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intview();
    }

    //初始化视图
    private void intview() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //给adapter提那家商品点击监听事件
        mAdapter.setListener(new ShopAdapter.onItemClickedListener() {
            @Override
            public void onPhotoClicked(GoodsInfo goodsInfo) {
                // TODO: 2016/11/25 0025 跳转到详情页
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //初始化refreshlayout
        //使用本对象作为key，用来记录上一次刷新时间，如果两次下拉间隔太近，不会触发刷新方法
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        //设置刷新时显示的背景色
        mRefreshLayout.setBackgroundResource(R.color.recycler_bg);
        //关闭header所耗时长
        mRefreshLayout.setDurationToCloseHeader(1500);
        //加载，刷新回调
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //调用业务类的方法
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //调用业务类的方法
                presenter.refreshData(pageType);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter.getItemCount() == 0) {
            mRefreshLayout.autoRefresh();
        }

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void showRefreshError(String msg) {
        mTvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshEnd() {
        activityUtils.showToast(getResources().getString(R.string.refresh_more_end));
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        //停止刷新
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void showLoadMoreLoading() {
        mTvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        mRefreshLayout.refreshComplete();  //停止刷新
        if (mAdapter.getItemCount() > 0) {
            activityUtils.showToast(msg);
        }
        mTvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(getResources().getString(R.string.refresh_more_end));
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        mAdapter.addData(data);
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        mAdapter.clear();
        if (data != null) {
            mAdapter.addData(data);
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public Shoppresentr createPresenter() {
        return new Shoppresentr();
    }

    @OnClick(R.id.tv_load_error)
    public void onClick() {
        //自动刷新一下
        mRefreshLayout.autoRefresh();
    }
}
