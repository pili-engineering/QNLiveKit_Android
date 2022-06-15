package com.qncube.liveuikit.hook

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.qncube.linveroominner.QClientLifeCycleListener
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import com.qncube.uikitcore.QUIKitContext
import com.qncube.uikitcore.QLiveKitUIContext
import com.qncube.uikitcore.QComponent
import com.qncube.uikitcore.QLiveComponent


class KITLiveInflaterFactory(
    private val appDelegate: AppCompatDelegate,
    private val roomClient: QLiveClient,
    private val kitContext: QLiveKitUIContext
) : LayoutInflater.Factory2, QClientLifeCycleListener {

    companion object {
        val replaceViews = HashMap<String, Class<out QLiveComponent>>()
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
        (view as QLiveComponent).attachKitContext(kitContext)
        (view as QLiveComponent).attachLiveClient(roomClient)
        mComponents.add(view)
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View {
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
    }
}

class KITInflaterFactory(
    private val appDelegate: AppCompatDelegate,
    private val QUIKitContext: QUIKitContext
) : LayoutInflater.Factory2 {
    companion object {
        val replaceViews = HashMap<String, Class<out QComponent>>()
    }

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
        (view as QComponent).attachKitContext(QUIKitContext)
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }
}