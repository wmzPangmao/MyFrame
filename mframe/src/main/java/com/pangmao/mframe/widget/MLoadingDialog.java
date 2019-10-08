package com.pangmao.mframe.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pangmao.mframe.R;
import com.pangmao.mframe.utils.MEmptyUtils;
import com.pangmao.mframe.utils.XOutdatedUtils;

import androidx.annotation.ColorInt;

/**
 * 加载等待提示框
 */
public class MLoadingDialog extends Dialog {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static MLoadingDialog dialog;
    private Context context;
    private TextView loadingMessage;
    private ProgressBar progressBar;
    private LinearLayout loadingView;
    private MColorDrawable drawable;

    public MLoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
        drawable = new MColorDrawable();
        setContentView(R.layout.xloading_dialog);
        loadingMessage = (TextView) findViewById(R.id.xframe_loading_message);
        progressBar = (ProgressBar) findViewById(R.id.xframe_loading_progressbar);
        loadingView = (LinearLayout) findViewById(R.id.xframe_loading_view);
        loadingMessage.setPadding(15, 0, 0, 0);
        drawable.setColor(Color.WHITE);
        XOutdatedUtils.setBackground(loadingView, drawable);
    }

    public static MLoadingDialog with(Context context) {
        if (dialog == null) {
            dialog = new MLoadingDialog(context);
        }
        return dialog;
    }

    public MLoadingDialog setOrientation(int orientation) {
        loadingView.setOrientation(orientation);
        if (orientation == HORIZONTAL) {
            loadingMessage.setPadding(15, 0, 0, 0);
        } else {
            loadingMessage.setPadding(0, 15, 0, 0);
        }
        return dialog;
    }

    public MLoadingDialog setBackgroundColor(@ColorInt int color) {
        drawable.setColor(color);
        XOutdatedUtils.setBackground(loadingView, drawable);
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog != null) {
            dialog = null;
        }
    }

    public MLoadingDialog setCanceled(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        setCancelable(cancel);
        return dialog;
    }

    public MLoadingDialog setMessage(String message) {
        if (!MEmptyUtils.isSpace(message)) {
            loadingMessage.setText(message);
        }
        return this;
    }

    public MLoadingDialog setMessageColor(@ColorInt int color) {
        loadingMessage.setTextColor(color);
        return this;
    }
}
