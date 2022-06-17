package com.qlive.uikit.hook

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.QLiveClient
import com.qlive.core.QLiveLogUtil
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QUIKitContext
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.QComponent
import com.qlive.uikitcore.QLiveComponent


class KITLiveInflaterFactory(
    private val appDelegate: AppCompatDelegate,
    private val roomClient: QLiveClient,
    private val kitContext: QLiveUIKitContext
) : LayoutInflater.Factory2, QClientLifeCycleListener {

    companion object {
        val replaceViews = HashMap<String, Class<out QLiveComponent>>()
        val innerComponentClass = HashMap<String, Class<out QLiveComponent>>()
    }
    private val mComponents = HashSet<QLiveComponent>()
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val viewClass = replaceViews[name]?:innerComponentClass[name]
        val view = if (viewClass != null) {
            val constructor =
                viewClass.getConstructor(Context::class.java, AttributeSet::class.java)
            constructor.newInstance(context, attrs) as View
        } else {
            appDelegate.createView(parent, name, context, attrs)
        }
        QLiveLogUtil.d("KITInflaterFactory","onCreateView "+ name)
        if(view is QLiveComponent){
            QLiveLogUtil.d("KITInflaterFactory","onCreateView "+ name+" attachKitContext ")
            (view as QLiveComponent).attachKitContext(kitContext)
            (view as QLiveComponent).attachLiveClient(roomClient)
            mComponents.add(view)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }


    override fun onEntering(liveId: String, user: QLiveUser) {
        mComponents.forEach {
            it.onEntering(liveId, user)
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        mComponents.forEach {
            it.onJoined(roomInfo)
        }
    }

    override fun onLeft() {
        mComponents.forEach {
            it.onLeft()
        }
    }

    override fun onDestroyed() {
        mComponents.forEach {
            it.onDestroyed()
        }
        mComponents.clear()
    }
}

class KITInflaterFactory(
    private val appDelegate: AppCompatDelegate,
    private val QUIKitContext: QUIKitContext
) : LayoutInflater.Factory2 {
    companion object {
        val replaceViews = HashMap<String, Class<out QComponent>>()
        val innerComponentClass = HashMap<String, Class<out QComponent>>()
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val viewClass = replaceViews[name]?:innerComponentClass[name]
        val view = if (viewClass != null) {
            val constructor =
                viewClass.getConstructor(Context::class.java, AttributeSet::class.java)
            constructor.newInstance(context, attrs) as View
        }
        else {
            appDelegate.createView(parent, name, context, attrs)
        }
        QLiveLogUtil.d("KITInflaterFactory","onCreateView "+ name+" "+(view==null))
        if (view is QComponent){
            (view as QComponent).attachKitContext(QUIKitContext)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }
}