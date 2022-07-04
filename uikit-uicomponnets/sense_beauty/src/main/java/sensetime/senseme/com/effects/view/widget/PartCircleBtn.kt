package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import sensetime.senseme.com.effects.utils.LogUtils
import com.google.android.material.button.MaterialButton
import sensetime.senseme.com.effects.R

class PartCircleBtn : LinearLayout {

    companion object {
        private const val TAG = "PartCircleBtn"
    }

    var mRegionId: Int = 0

    constructor(context: Context, regionId: Int) : super(context) {
        LogUtils.iTag(TAG, "null() called with: context = $context, regionId = $regionId")
        this.mRegionId = regionId
        init(context)
        mbtn_location.text = "$mRegionId"
    }

    private fun init(context: Context) {
        initView(context)
    }

    private lateinit var mbtn_location: MaterialButton

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_try_on_part, this, true)
        mbtn_location = view.findViewById(R.id.mbtn_location)

    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        when (selected) {
            true -> {
                mbtn_location.setStrokeColorResource(R.color.color_f383fd)
            }
            false -> {
                mbtn_location.setStrokeColorResource(R.color.color_4d818181)
            }
        }
    }
}