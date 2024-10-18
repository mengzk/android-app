package com.dxm.aimodel.base;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.dxm.aimodel.R;

/**
 * Author: Meng
 * Date: 2024/10/17
 * Modify: 2024/10/17
 * Desc:
 */
public class Loading {
    private static Dialog dialog;
    private static int count = 0;

    public static void show(Context context, String msg) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        count = 0;
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null, false);
        if (msg == null) {
            msg = "加载中...";
        }
        ((TextView)view.findViewById(R.id.tv_loading_text)).setText(msg);
        view.setOnClickListener(v -> {
            if(count > 5) {
                dialog.dismiss();
            }
            count++;
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
