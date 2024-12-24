package com.dxm.aimodel.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dxm.aimodel.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * Author: Meng
 * Date: 2024/10/25
 * Modify: 2024/10/25
 * Desc:
 */
public class DialogUtils {

    public static void showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public static void inputDialog(Activity context) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sign, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
//                .setTitle("Custom Input Dialog")
                .setPositiveButton("确认", (dialog, which) -> {
                    EditText editText = dialogView.findViewById(R.id.edit_phone);
                    String inputText = editText.getText().toString();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showBottomDialog(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sign, null);

        // Create the BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(dialogView);

        // Set up the input field and button
        EditText editText = dialogView.findViewById(R.id.edit_phone);
//        Button submitButton = dialogView.findViewById(R.id.edit_commit);

//        submitButton.setOnClickListener(v -> {
//            String inputText = editText.getText().toString();
//            // Handle the input text
//            bottomSheetDialog.dismiss();
//        });

        bottomSheetDialog.setOnDismissListener(dialog -> {
            // Handle the dialog dismiss event
        });
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        // Show the dialog
        bottomSheetDialog.show();
    }
}
