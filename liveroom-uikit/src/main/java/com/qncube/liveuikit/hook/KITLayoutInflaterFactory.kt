package com.qncube.liveuikit.hook

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.qncube.liveroomcore.QClientLifeCycleListener
import com.qncube.liveroomcore.QNLiveClient
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QComponent
import com.qncube.uikitcore.QLiveComponent


class KITLayoutInflaterFactory(
    private val appDelegate: AppCompatDelegate,
    private val roomClient: QNLiveClient,
    private val kitContext: KitContext
) : LayoutInflater.Factory2, QClientLifeCycleListener {

    companion object {
        val replaceViews = HashMap<String, Class<out QComponent>>()
    }

    val mComponents = HashSet<QLiveComponent>()
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View {
        val viewClass = replaceViews[name]
        val view = if (viewClass != null) {
            val constructor =
                viewClass.getConstructor(Context::class.java, AttributeSet::class.java)
            constructor.newInstance(constructor, attrs) as View
        } else {
            appDelegate.createView(parent, name, context, attrs)
        }
        if (view is QComponent) {
            view.attachKitContext(kitContext)
            kitContext.lifecycleOwner.lifecycle.addObserver(view)
        }
        if (view is QLiveComponent) {
            view.attachLiveClient(roomClient)
            mComponents.add(view)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View {
        return onCreateView(null, name, context, attrs)
    }


    override fun onEntering(liveId: String, user: QNLiveUser) {
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
    }
}