package com.androidtutorial.BaseActivity
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.view.Gravity
import android.content.Intent

abstract class BaseActivity : AppCompatActivity() {
    private var toast: Toast? = null
    open var progressDialog: GifProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        progressDialog = GifProgressDialog(this)
    }

    public override fun onDestroy() {
        super.onDestroy()
        //Log.e(TAG,"MainActivity onDestroy called");
    }

    public override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun showToast(message: String?) {
        if (toast == null) {
            toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            with(toast) { this?.setGravity(Gravity.CENTER, 0, 0) }
        }
        if (!toast!!.view!!.isShown) {
            toast!!.setText(message)
            toast!!.show()
        }
    }

    fun launchIntent(cls: Class<out AppCompatActivity?>?, finish: Boolean) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        if (finish) finish()
    }

    fun launchIntent(cls: Class<out AppCompatActivity?>?, bundle: Bundle?, finish: Boolean) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtras(bundle!!)
        startActivity(intent)
        if (finish) finish()
    }

    open fun showProgressDialog() {
        if (progressDialog != null) progressDialog!!.show()
    }

    open fun dismissProgressDialog() {
        if (progressDialog != null) progressDialog!!.hide()
    }

    companion object {
        //public static AppCompatActivity activity;
        val TAG = BaseActivity::class.java.simpleName
    }
}