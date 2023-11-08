package com.example.assettelematics.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.telematics.asset.trucktask.activity.MainActivity


fun Activity.callMainPage() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
}

fun Activity.shortToast(msg:String) {
    Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
}

fun Activity.openKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}