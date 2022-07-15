package com.qlive.uikitcore.activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.qlive.core.QLiveLogUtil
import com.qlive.uikitcore.R
import com.qlive.uikitcore.dialog.LoadingDialog


/**
 * activity 基础类  封装toolbar
 *
 */
abstract class BaseFrameActivity : AppCompatActivity(){

    open fun isCustomCreate():Boolean{
        return false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isCustomCreate()){
            return
        }
        val startTime = System.currentTimeMillis()
        setContentView(getLayoutId())
        QLiveLogUtil.d(
            "ActivityonCreate",
            "onCreate cost ${System.currentTimeMillis() - startTime}"
        )
        init()
    }

    abstract fun init()
    abstract fun getLayoutId(): Int
    fun showLoading(toShow: Boolean) {
        if (toShow) {
            LoadingDialog.showLoading(supportFragmentManager)
        } else {
            LoadingDialog.cancelLoadingDialog()
        }
    }
}