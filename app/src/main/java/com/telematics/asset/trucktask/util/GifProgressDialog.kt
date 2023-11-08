package com.androidtutorial.BaseActivity

import android.app.Activity
import android.app.Dialog
import android.widget.LinearLayout
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ImageView
import com.telematics.asset.trucktask.R


class GifProgressDialog(var activity: Activity) {
    var dialog: Dialog? = null
    fun createDialog() {
        /*dialog = Dialog(activity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //...set cancelable false so that it's never get hidden
        dialog!!.setCancelable(false)
        //...that's the layout i told you will inflate later
        dialog!!.setContentView(R.layout.progress_dialog)
        val linearLayout = dialog!!.findViewById<LinearLayout>(R.id.loaderLayout)
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.style.CustomProgressDialogTheme));
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(activity.getColor(R.color.clear_colors)))
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //...initialize the imageView form infalted layout
        val gifImageView = dialog!!.findViewById<ImageView>(R.id.custom_loading_imageView)
        linearLayout.setBackgroundColor(0xffffff)
        Glide.with(activity).asGif().load(R.raw.s).into(gifImageView)*/
    }

    fun show() {
        if (dialog != null && !dialog!!.isShowing) {
            dialog!!.show()
        }
    }

    //..also create a method which will hide the dialog when some work is done
    fun hide() {
        if (dialog != null) dialog!!.dismiss()
    }

    fun check(): Boolean {
        return dialog!!.isShowing
    }

    //..we need the context else we can not create the dialog so get context in constructor
    init {
        createDialog()
    }
}