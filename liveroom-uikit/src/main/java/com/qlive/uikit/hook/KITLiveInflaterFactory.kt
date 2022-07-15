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
import com.qlive.uikit.component.*
import com.qlive.uikitcore.*
import com.qlive.uikitdanmaku.DanmakuTrackManagerView
import com.qlive.uikitdanmaku.SendDanmakuView
import com.qlive.uikitlinkmic.MicLinkersView
import com.qlive.uikitlinkmic.StartLinkView
import com.qlive.uikitpk.PKAnchorPreview
import com.qlive.uikitpk.PKCoverView
import com.qlive.uikitpublicchat.PublicChatView
import com.qlive.uikitpublicchat.RoomNoticeView
import com.qlive.uikitshopping.ExplainingQItemCardView
import com.qlive.uikitshopping.GoShoppingImgView
import com.qlive.uikituser.*

/**
 * 直播间内 UI装载器
 */
class KITLiveInflaterFactory(
    private val appDelegate: AppCompatDelegate,
    private val roomClient: QLiveClient,
    private val kitContext: QLiveUIKitContext
) : LayoutInflater.Factory2, QClientLifeCycleListener {

    private val mComponents = HashSet<QLiveComponent>()

    //减少反射次数
    private fun checkCreateView(
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return when (name) {
            QKitImageView::class.java.canonicalName -> QKitImageView(context, attrs)
            PKAnchorPreview::class.java.canonicalName -> PKAnchorPreview(context, attrs)
            MicLinkersView::class.java.canonicalName -> MicLinkersView(context, attrs)
            LivePreView::class.java.canonicalName -> LivePreView(context, attrs)
            QBackRoomNavigationImg::class.java.canonicalName -> QBackRoomNavigationImg(
                context,
                attrs
            )
            ShowBeautyPreview::class.java.canonicalName -> ShowBeautyPreview(context, attrs)
            SwitchCameraView::class.java.canonicalName -> SwitchCameraView(context, attrs)
            RoomCoverViewPager::class.java.canonicalName -> RoomCoverViewPager(context, attrs)
            RoomHostView::class.java.canonicalName -> RoomHostView(context, attrs)
            OnlineUserView::class.java.canonicalName -> OnlineUserView(context, attrs)
            RoomMemberCountView::class.java.canonicalName -> RoomMemberCountView(context, attrs)
            RoomIdView::class.java.canonicalName -> RoomIdView(context, attrs)
            RoomTimerView::class.java.canonicalName -> RoomTimerView(context, attrs)
            SendDanmakuView::class.java.canonicalName -> SendDanmakuView(context, attrs)
            GoShoppingImgView::class.java.canonicalName -> GoShoppingImgView(context, attrs)
            ShowBeautyView::class.java.canonicalName -> ShowBeautyView(context, attrs)
            ShowStickerBeautyView::class.java.canonicalName -> ShowStickerBeautyView(context, attrs)
            ExplainingQItemCardView::class.java.canonicalName -> ExplainingQItemCardView(
                context,
                attrs
            )
            RoomNoticeView::class.java.canonicalName -> RoomNoticeView(context, attrs)
            PublicChatView::class.java.canonicalName -> PublicChatView(context, attrs)
            PKCoverView::class.java.canonicalName -> PKCoverView(context, attrs)
            DanmakuTrackManagerView::class.java.canonicalName -> DanmakuTrackManagerView(
                context,
                attrs
            )
            CloseRoomView::class.java.canonicalName -> CloseRoomView(context, attrs)
            StartLinkView::class.java.canonicalName -> StartLinkView(context, attrs)
            else -> null
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        var view = checkCreateView(name, context, attrs)
        if (view == null) {
            var viewClass: Class<*>? = null
            try {
                viewClass = Class.forName(name)
            } catch (e: Exception) {
            }
            view =
                if (viewClass != null && QLiveComponent::class.java.isAssignableFrom(viewClass)) {
                    val constructor =
                        viewClass.getConstructor(Context::class.java, AttributeSet::class.java)
                    constructor.newInstance(context, attrs) as View
                } else {
                    appDelegate.createView(parent, name, context, attrs)
                }
        }

        if (view is QLiveComponent) {
            //   QLiveLogUtil.d("KITInflaterFactory", "onCreateView " + name + " attachKitContext ")
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

/**
 * 直播间外UI组件装载器
 */
class KITInflaterFactory(
    private val appDelegate: AppCompatDelegate,
    private val QUIKitContext: QUIKitContext
) : LayoutInflater.Factory2 {

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        var viewClass: Class<*>? = null
        try {
            viewClass = Class.forName(name)
        } catch (e: Exception) {
        }
        val view = if (viewClass != null && QComponent::class.java.isAssignableFrom(viewClass)) {
            val constructor =
                viewClass.getConstructor(Context::class.java, AttributeSet::class.java)
            constructor.newInstance(context, attrs) as View
        } else {
            appDelegate.createView(parent, name, context, attrs)
        }
        if (view is QComponent) {
            (view as QComponent).attachKitContext(QUIKitContext)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }
}