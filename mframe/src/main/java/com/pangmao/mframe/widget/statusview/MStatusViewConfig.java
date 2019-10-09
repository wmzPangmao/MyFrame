package com.pangmao.mframe.widget.statusview;

import com.pangmao.mframe.R;

import androidx.annotation.LayoutRes;

public class MStatusViewConfig {

    private int emptyViewResId = R.layout.xloading_empty_view;
    private int errorViewResId = R.layout.xloading_error2_view;
    private int loadingViewResId = R.layout.xloading_loading_view;
    private int noNetworkViewResId = R.layout.xloading_no_network_view;
    private int successViewResId = R.layout.xloading_success_view;

    public int getEmptyViewResId() {
        return emptyViewResId;
    }

    public MStatusViewConfig setEmptyViewResId(@LayoutRes int emptyViewResId) {
        this.emptyViewResId = emptyViewResId;
        return this;
    }

    public int getErrorViewResId() {
        return errorViewResId;
    }

    public MStatusViewConfig setErrorViewResId(@LayoutRes int errorViewResId) {
        this.errorViewResId = errorViewResId;
        return this;
    }

    public int getLoadingViewResId() {
        return loadingViewResId;
    }

    public MStatusViewConfig setLoadingViewResId(@LayoutRes int loadingViewResId) {
        this.loadingViewResId = loadingViewResId;
        return this;
    }

    public int getNoNetworkViewResId() {
        return noNetworkViewResId;
    }

    public MStatusViewConfig setNoNetworkViewResId(@LayoutRes int noNetworkViewResId) {
        this.noNetworkViewResId = noNetworkViewResId;
        return this;
    }

    public int getSuccessViewResId() {
        return successViewResId;
    }

    public void setSuccessViewResId(int successViewResId) {
        this.successViewResId = successViewResId;
    }
}
