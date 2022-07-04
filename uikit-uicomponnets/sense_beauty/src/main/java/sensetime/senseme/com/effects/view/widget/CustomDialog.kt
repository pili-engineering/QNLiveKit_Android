package sensetime.senseme.com.effects.view.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import sensetime.senseme.com.effects.utils.LogUtils
import kotlinx.android.synthetic.main.dialog_custom.*
import sensetime.senseme.com.effects.R

class CustomDialog(context: Context) : Dialog(context) {
    companion object {
        const val TAG = "CustomDialog"
        const val DLG_TYPE_1 = 0
        const val DLG_TYPE_2 = 1
    }

    private var mTitle: String = ""
    private var mContent: String = ""
    private var mDlgType = 0

    // 定义标题，内容
    constructor(context: Context, title: String, content: String, type: Int, listener:OnClickListener) : this(context) {
        mTitle = title
        mContent = content
        mDlgType = type
        mListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initView()
    }

    private fun initView() {
        tvTitle.text = mTitle
        tvContent.text = mContent
        tvOk.setOnClickListener {
            dismiss()
            LogUtils.iTag(TAG, "点击了---")
            mListener?.onClickOk()
        }

        when (mDlgType) {
            0 -> {
                tvCancel.visibility = View.GONE
            }
            1 -> {
                tvCancel.visibility = View.VISIBLE
                tvOk.visibility = View.VISIBLE
            }
        }
    }

    private var mListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        mListener = listener
    }

    interface OnClickListener {
        fun onClickOk() {}
        fun onClickCancel() {}
    }
}

