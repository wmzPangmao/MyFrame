package com.pangmao.mframe.widget.loadingview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pangmao.mframe.R;
import com.pangmao.mframe.utils.MStringUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;

/**
 * 简单实用的页面状态统一管理 ，加载中、无网络、无数据、出错等状态的随意切换
 */
public class MLoadingView extends FrameLayout {

    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mSuccessViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private int mContentViewResId;

    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;
    private String showMsg;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, View> mResId = new HashMap<>(10);
    private boolean isChange = false;

    public static MLoadingViewConfig config=new MLoadingViewConfig();

    public static MLoadingViewConfig init() {
        return config;
    }

    public static MLoadingView wrap(Activity activity) {
        return wrap(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
    }

    public static MLoadingView wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }

    public static MLoadingView wrap(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);

        MLoadingView MLoadingView = new MLoadingView(view.getContext());
        parent.addView(MLoadingView, index, lp);
        MLoadingView.addView(view);
        MLoadingView.setContentView(view);
        return MLoadingView;
    }

    public MLoadingView(Context context) {
        this(context, null);
    }

    public MLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XLoadingView, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.XLoadingView_emptyView, config.getEmptyViewResId());
        mErrorViewResId = a.getResourceId(R.styleable.XLoadingView_errorView, config.getErrorViewResId());
        mLoadingViewResId = a.getResourceId(R.styleable.XLoadingView_loadingView, config.getLoadingViewResId());
        mNoNetworkViewResId = a.getResourceId(R.styleable.XLoadingView_noNetworkView, config.getNoNetworkViewResId());
        mSuccessViewResId = a.getResourceId(R.styleable.XLoadingView_successView, config.getSuccessViewResId());
        a.recycle();
    }

    private void setContentView(View view) {
        mContentViewResId = view.getId();
        mResId.put(mContentViewResId, view);
    }

    public final void showEmpty() {
        show(mEmptyViewResId);
    }

    public final void showError() {
        show(mErrorViewResId);
    }

    public final void showSuccess() {show(mSuccessViewResId);}

    public final void showLoading() {
        show(mLoadingViewResId);
    }

    public final void showNoNetwork() {
        show(mNoNetworkViewResId);
    }

    public final void showContent() {
        show(mContentViewResId);
    }

    private void show(int resId) {
        for (View view : mResId.values()) {
            view.setVisibility(GONE);
        }
        layout(resId).setVisibility(VISIBLE);
    }

    private View layout(int resId) {
        if (mResId.containsKey(resId) && !isChange) {
            return mResId.get(resId);
        }
        View view = mInflater.inflate(resId, this, false);
        view.setVisibility(GONE);
        addView(view);
        mResId.put(resId, view);
        isChange = false;
//        if (resId == mErrorViewResId||resId == mNoNetworkViewResId) {
            View v = view.findViewById(R.id.xloading_retry);
            if (mOnRetryClickListener != null) {
                if (v != null) {
                    v.setOnClickListener(mOnRetryClickListener);
                } else {
                    view.setOnClickListener(mOnRetryClickListener);
                }
            }
            View tvMsg = view.findViewById(R.id.tv_xloading_msg);
            if(MStringUtils.isLeagel(showMsg)) {
                if(tvMsg != null) {
                    ((TextView)tvMsg).setText(showMsg);
                }
            }
//        }
        return view;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
        showLoading();
    }
    /**
     * 设置重试点击事件
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
        isChange = true;
    }
    /**
     * 设置显示错误信息
     * @param showMsg
     */
    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
        isChange = true;
    }
}
