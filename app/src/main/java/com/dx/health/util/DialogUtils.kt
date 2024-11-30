package com.dx.health.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import com.dx.health.R
import com.google.android.material.bottomsheet.BottomSheetDialog


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */
object DialogUtils {
    fun showLoadingDialog(context: Context?) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
    }

    fun inputDialog(context: Activity) {
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_sign, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(dialogView) //                .setTitle("Custom Input Dialog")
            .setPositiveButton("确认") { dialog, which ->
                val editText = dialogView.findViewById<EditText>(R.id.edit_phone)
                val inputText = editText.text.toString()
            }
            .setNegativeButton("取消") { dialog, which ->
                dialog.dismiss()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showBottomDialog(activity: Activity) {
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_sign, null)

        // Create the BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(activity)
        bottomSheetDialog.setContentView(dialogView)

        // Set up the input field and button
        val editText = dialogView.findViewById<EditText>(R.id.edit_phone)

        //        Button submitButton = dialogView.findViewById(R.id.edit_commit);

//        submitButton.setOnClickListener(v -> {
//            String inputText = editText.getText().toString();
//            // Handle the input text
//            bottomSheetDialog.dismiss();
//        });
        bottomSheetDialog.setOnDismissListener { dialog: DialogInterface? -> }
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        // Show the dialog
        bottomSheetDialog.show()
    }

    fun showFullScreenPopup(activity: Activity) {
        // Inflate the custom layout/view
        val inflater = activity.layoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_sign, null)

        // Create the PopupWindow
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // Set up the button inside the popup
        val editPhone = popupView.findViewById<EditText>(R.id.sign_edit_phone)
        val editEmail = popupView.findViewById<EditText>(R.id.sign_edit_email)
        val editName = popupView.findViewById<EditText>(R.id.sign_edit_name)
        val editAge = popupView.findViewById<EditText>(R.id.sign_edit_age)
        val editHeight = popupView.findViewById<EditText>(R.id.sign_edit_height)
        val editKg = popupView.findViewById<EditText>(R.id.sign_edit_kg)

        val button = popupView.findViewById<Button>(R.id.sign_edit_commit)
        val close = popupView.findViewById<ImageView>(R.id.sign_close)

        close.setOnClickListener { v: View? -> popupWindow.dismiss() }
        button.setOnClickListener { v: View? ->
            val phone = editPhone.text.toString().trim { it <= ' ' }
            val email = editEmail.text.toString().trim { it <= ' ' }
            val name = editName.text.toString().trim { it <= ' ' }
            val age = editAge.text.toString().trim { it <= ' ' }
            val height2 = editHeight.text.toString().trim { it <= ' ' }
            val kg = editKg.text.toString().trim { it <= ' ' }

            // Handle the input text
            popupWindow.dismiss()
        }


        // Set the background to a semi-transparent color
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Dim the background when the popup is shown
        popupWindow.setOnDismissListener {
            val lp = activity.window.attributes
            lp.alpha = 1.0f
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            activity.window.attributes = lp
        }

        // Show the popup window
        popupWindow.showAtLocation(
            activity.findViewById(android.R.id.content),
            Gravity.CENTER,
            0,
            0
        )

        // Dim the background
        val lp = activity.window.attributes
        lp.alpha = 0.5f
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        activity.window.attributes = lp
    }
}