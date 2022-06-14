package com.qncube.playerclient.player

import android.content.Context
import android.media.AudioManager
import android.view.Surface
import com.pili.pldroid.player.*
import com.qncube.playerclient.QPlayerEventListener

internal class PLEngine(val context: Context) {

    private var mPlayerEventListener: QPlayerEventListener? = null
    private var isLossPause = false

    val mIMediaPlayer: PLMediaPlayer by lazy {
        val m = PLMediaPlayer(context,
            AVOptions().apply {
                setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
                setInteger(AVOptions.KEY_FAST_OPEN, 1);
                setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);
                setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
            }
        )
        m.isLooping = false
        m.setOnPreparedListener(mOnPreparedListener)
        m.setOnErrorListener(mOnErrorListener)
        m.setOnInfoListener(mOnInfoListener)
        m.setOnVideoSizeChangedListener(mPLOnVideoSizeChangedListener)
        m
    }
    private var audioFocusChangeListener: AudioManager.OnAudioFocusChangeListener =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT//Pause playback
                -> {
                    if (isPlaying()) {
                        mIMediaPlayer.pause()
                        isLossPause = true
                    }
                }
                AudioManager.AUDIOFOCUS_GAIN//Resume playback
                -> {
                    if (isLossPause) {
                        mIMediaPlayer.start()
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

    private val mAudioManager: AudioManager by lazy {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager
    }

    private fun reqestFouces() {
        isLossPause = false
        mAudioManager.requestAudioFocus(
            audioFocusChangeListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
    }

    private fun releaseFouces() {
        mAudioManager.abandonAudioFocus(audioFocusChangeListener)
    }

    fun releasePlayer() {
        mIMediaPlayer.release()
        if (mRenderView is QTextureRenderView) {
            (mRenderView as QTextureRenderView).stopPlayback()
        }
    }

    fun setUpUrl(uir: String, headers: Map<String, String>?) {
        mIMediaPlayer.stop()
        mIMediaPlayer.setDataSource(uir, headers)
    }

    fun start() {
        reqestFouces()
        try {
            mSurface?.let {
                mIMediaPlayer.setSurface(it)
            }
            mIMediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        releaseFouces()
        mIMediaPlayer.stop()
    }

    fun setPlayerEventListener(listener: QPlayerEventListener) {
        this.mPlayerEventListener = listener
    }

    private var mSurface: Surface? = null
    private var mRenderView: QPlayerRenderView? = null
    private var mQRenderCallback: QRenderCallback = object : QRenderCallback {
        override fun onSurfaceCreated(var1: Surface, var2: Int, var3: Int) {
            mSurface = var1
            mIMediaPlayer.setSurface(mSurface)
        }

        override fun onSurfaceChanged(var1: Surface, var2: Int, var3: Int) {
            mSurface = var1
            mIMediaPlayer.setSurface(mSurface)
        }

        override fun onSurfaceDestroyed(var1: Surface) {
            mSurface = null
        }
    }

    fun setPlayerRenderView(renderView: QPlayerRenderView) {
        this.mRenderView = renderView
        renderView.setRenderCallback(mQRenderCallback)
    }

    private fun isPlaying(): Boolean {
        return mIMediaPlayer.isPlaying
    }

    private val mOnPreparedListener = PLOnPreparedListener { mp ->
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
            if (mRenderView is QTextureRenderView) {
                (mRenderView as QTextureRenderView).setVideoSize(p0, p1)
            }
            mPlayerEventListener?.onVideoSizeChanged(p0, p1)
        }
}