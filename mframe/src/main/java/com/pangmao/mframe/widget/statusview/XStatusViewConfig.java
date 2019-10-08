package com.pangmao.mframe.widget.statusview;

import com.pangmao.mframe.R;

import androidx.annotation.LayoutRes;

public class XStatusViewConfig {

    private int emptyViewResId = R.layout.xloading_empty_view;
    private int errorViewResId = R.layout.xloading_error_view;
    private int loadingViewResId = R.layout.xloading_loading_view;
    private int noNetworkViewResId = R.layout.xloading_no_network_view;

    public int getEmptyViewResId() {
        return emptyViewResId;
    }

    public XStatusViewConfig setEmptyViewResId(@LayoutRes int emptyViewResId) {
        this.emptyViewResId = emptyViewResId;
        return this;
    }

    public int getErrorViewResId() {
        return errorViewResId;
    }

    public XStatusViewConfig setErrorViewResId(@LayoutRes int errorViewResId) {
        this.errorViewResId = errorViewResId;
        return this;
    }

    public int getLoadingViewResId() {
        return loadingViewResId;
    }

    public XStatusViewConfig setLoadingViewResId(@LayoutRes int loadingViewResId) {
        this.loadingViewResId = loadingViewResId;
        return this;
    }

    public int getNoNetworkViewResId() {
        return noNetworkViewResId;
    }

    public XStatusViewConfig setNoNetworkViewResId(@LayoutRes int noNetworkViewResId) {
        this.noNetworkViewResId = noNetworkViewResId;
        return this;
    }
}
