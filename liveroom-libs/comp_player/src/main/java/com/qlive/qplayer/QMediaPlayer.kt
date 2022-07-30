package com.qlive.qplayer

import android.content.Context
import android.media.AudioManager
import android.view.Surface
import com.pili.pldroid.player.*
import com.qlive.avparam.QIPlayer
import com.qlive.avparam.QPlayerEventListener
import com.qlive.avparam.QPlayerRenderView
import com.qlive.avparam.QRenderCallback
import com.qlive.liblog.QLiveLogUtil

class QMediaPlayer(val context: Context) : QIPlayer {

    private var mPlayerEventListener: QPlayerEventListener? = null
    private var isLossPause = false
    private var currentUrl = ""
    private var isRelease = false

    private var mIMediaPlayer: PLMediaPlayer? = null

    private fun resetPlayer() {
        mIMediaPlayer = PLMediaPlayer(context,
            AVOptions().apply {
                setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
                setInteger(AVOptions.KEY_FAST_OPEN, 1);
                setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);
                setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
                setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);
                if (!QLiveLogUtil.isLogAble) {
                    setInteger(AVOptions.KEY_LOG_LEVEL, 5)
                } else {
                    setInteger(AVOptions.KEY_LOG_LEVEL, -1)
                }
            }
        )
        mIMediaPlayer?.isLooping = false
        mIMediaPlayer?.setOnPreparedListener(mOnPreparedListener)
        mIMediaPlayer?.setOnErrorListener(mOnErrorListener)
        mIMediaPlayer?.setOnInfoListener(mOnInfoListener)
        mIMediaPlayer?.setOnVideoSizeChangedListener(mPLOnVideoSizeChangedListener)
    }

    private var audioFocusChangeListener: AudioManager.OnAudioFocusChangeListener =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT//Pause playback
                -> {
                    if (isPlaying()) {
                        pause()
                        isLossPause = true
                    }
                }
                AudioManager.AUDIOFOCUS_GAIN//Resume playback
                -> {
                    if (isLossPause) {
                        resume()
                    }
                    isLossPause = false
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK//
                -> {
                }
                AudioManager.AUDIOFOCUS_LOSS//Stop playback
                -> {
                }
            }
        }

    init {
        resetPlayer()
    }

    override fun release() {
        isRelease = true
        currentUrl = ""
        mPlayerEventListener = null
        mIMediaPlayer?.release()
        if (mRenderView is QPlayerTextureRenderView) {
            (mRenderView as QPlayerTextureRenderView).stopPlayback()
        }
        mSurface = null
    }

    //切换rtc模式为了下麦快速恢复保持链接
    override fun onLinkStatusChange(isLink: Boolean) {
        if (isLink) {
            mIMediaPlayer?.stop()
            mIMediaPlayer?.release()
        } else {
            resetPlayer()
            mIMediaPlayer?.setDataSource(currentUrl)
            start()
        }
    }

    override fun setUp(uir: String, headers: Map<String, String>?) {
        currentUrl = uir
        mIMediaPlayer?.stop()
        mIMediaPlayer?.setDataSource(uir, headers)
    }

    override fun start() {
        try {
            mSurface?.let {
                mIMediaPlayer?.setSurface(it)
            }
            mIMediaPlayer?.prepareAsync()
            mIMediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 暂停
     */
    override fun pause() {
        if (currentUrl == "" || isRelease) {
            return
        }
        mIMediaPlayer?.pause()
    }

    override fun stop() {
        mIMediaPlayer?.stop()
    }

    /**
     * 恢复
     */
    override fun resume() {
        if (currentUrl == "" || isRelease) {
            return
        }
        mIMediaPlayer?.start()
    }

    fun setEventListener(listener: QPlayerEventListener) {
        this.mPlayerEventListener = listener
    }

    private var mSurface: Surface? = null
    private var mRenderView: QPlayerRenderView? = null
    private var mQRenderCallback: QRenderCallback = object : QRenderCallback {
        override fun onSurfaceCreated(var1: Surface, var2: Int, var3: Int) {
            mSurface = var1
            mIMediaPlayer?.setSurface(mSurface)
        }

        override fun onSurfaceChanged(var1: Surface, var2: Int, var3: Int) {
            mSurface = var1
            mIMediaPlayer?.setSurface(mSurface)
        }

        override fun onSurfaceDestroyed(var1: Surface) {
            mSurface = null
        }
    }

    fun setPlayerRenderView(renderView: QPlayerRenderView) {
        this.mRenderView = renderView
        renderView.setRenderCallback(mQRenderCallback)
        mSurface = renderView.getSurface()
    }

    private fun isPlaying(): Boolean {
        return mIMediaPlayer?.isPlaying ?: false
    }
    private val mOnPreparedListener = PLOnPreparedListener { mp ->
        // mIMediaPlayer?.start()
        mPlayerEventListener?.onPrepared(mp)
    }
    private val mOnErrorListener = PLOnErrorListener { p0, p1 ->
        mPlayerEventListener?.onError(p0) ?: false
    }
    private val mOnInfoListener = PLOnInfoListener { what, extra, _ ->
        mPlayerEventListener?.onInfo(what, extra)
    }
    private val mPLOnVideoSizeChangedListener =
        PLOnVideoSizeChangedListener { p0, p1 ->
            if (mRenderView is QPlayerTextureRenderView) {
                (mRenderView as QPlayerTextureRenderView).setVideoSize(p0, p1)
            }
            if (mRenderView is QSurfaceRenderView) {
                (mRenderView as QSurfaceRenderView).setVideoSize(p0, p1)
            }
            mPlayerEventListener?.onVideoSizeChanged(p0, p1)
        }
}