package com.niucube.mediaplayer

import android.content.Context
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.FrameLayout
import com.pili.pldroid.player.common.Logger
import com.pili.pldroid.player.common.ViewMeasurer
import com.qncube.lcommon.PreviewMode
import com.qncube.lcommon.QPlayerRenderView
import com.qncube.lcommon.QRenderCallback

open class QPlayerTextureRenderView : FrameLayout, QPlayerRenderView {

    private lateinit var mRenderView: PLTextureRenderView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mRenderView = PLTextureRenderView(context)
        val lp = LayoutParams(-1, -1, 17)
        this.mRenderView.layoutParams = lp
        this.addView(this.mRenderView)
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.requestFocus()
    }

    private var mDisplayAspectRatio = PreviewMode.ASPECT_RATIO_FIT_PARENT
    private var mVideoWidth = 0
    private var mVideoHeight = 0
    private val mSplitMode = 6789
    private val mPreferredSplitWidth = -1
    private val mPreferredSplitHeight = -1
    private val mTransformMatrix = Matrix()
    private var mScaledX = -1.0f
    private var mScaledY = -1.0f
    private var mDisplayOrientation = 0
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mSurface: Surface? = null

    internal fun setVideoSize(width: Int, height: Int) {
        mVideoWidth = width
        mVideoHeight = height
        requestLayout()
    }

    internal fun stopPlayback() {
        releaseSurfaceTexture()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private fun releaseSurfaceTexture() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture!!.release()
            mSurfaceTexture = null
        }
    }

    private var mQRenderCallback: QRenderCallback? = null
    override fun setRenderCallback(rendCallback: QRenderCallback) {
        mQRenderCallback = rendCallback
    }

    override fun setDisplayAspectRatio(previewMode: PreviewMode) {
        mDisplayAspectRatio = previewMode
    }

    override fun getView(): View {
        return this
    }

    fun setMirror(mirror: Boolean) {
        this.scaleX = if (mirror) -1.0f else 1.0f
    }

    fun setDisplayOrientation(degree: Int): Boolean {
        var degreeTemp = degree
        return if (degree != 0 && degree != 90 && degree != 180 && degree != 270) {
            false
        } else {
            degreeTemp %= 360
            if (this.mDisplayOrientation != degreeTemp) {
                this.mDisplayOrientation = degreeTemp
                this.invalidate()
                requestLayout()
            }
            true
        }
    }

    private fun getDisplayAspectRatio(): Int {
        return this.mDisplayAspectRatio.intValue
    }

    override fun onLayout(change: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val height = bottom - top
        when (mDisplayOrientation) {
            0, 180 -> this.mRenderView.layout(0, 0, width, height)
            90, 270 -> this.mRenderView.layout(0, 0, height, width)
        }
    }

    override fun onAttachedToWindow() {
        this.mRenderView.setPivotX(0.0f)
        this.mRenderView.setPivotY(0.0f)
        super.onAttachedToWindow()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var w = 0
        var h = 0
        when (mDisplayOrientation) {
            0, 180 -> {
                measureChild(this.mRenderView, widthSpec, heightSpec)
                w = this.mRenderView.getMeasuredWidth()
                h = this.mRenderView.getMeasuredHeight()
            }
            90, 270 -> {
                measureChild(this.mRenderView, heightSpec, widthSpec)
                w = this.mRenderView.getMeasuredHeight()
                h = this.mRenderView.getMeasuredWidth()
            }
        }
        setMeasuredDimension(w, h)
        when (mDisplayOrientation) {
            0 -> {
                this.mRenderView.setTranslationX(0.0f)
                this.mRenderView.setTranslationY(0.0f)
            }
            90 -> {
                this.mRenderView.setTranslationX(0.0f)
                this.mRenderView.setTranslationY(h.toFloat())
            }
            180 -> {
                this.mRenderView.setTranslationX(w.toFloat())
                this.mRenderView.setTranslationY(h.toFloat())
            }
            270 -> {
                this.mRenderView.setTranslationX(w.toFloat())
                this.mRenderView.setTranslationY(0.0f)
            }
        }
        this.mRenderView.setRotation((-mDisplayOrientation).toFloat())
    }

    private inner class PLTextureRenderView : TextureView {

        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
        ) {
            surfaceTextureListener = mTextureListener
        }

        private val mTextureListener: TextureView.SurfaceTextureListener = object :
            TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                if (mSurfaceTexture != null) {
                    setSurfaceTexture(mSurfaceTexture!!)
                    surfaceTexture.release()
                    Logger.i("PLVideoTextureView", "onSurfaceTextureAvailable: replace surface")
                } else {
                    mSurfaceTexture = surfaceTexture
                    mSurface =
                        Surface(mSurfaceTexture)
                    Logger.i("PLVideoTextureView", "onSurfaceTextureAvailable: new surface")
                }
                if (mQRenderCallback != null) {
                    Logger.i("PLVideoTextureView", "onSurfaceTextureAvailable: onSurfaceCreated")
                    mQRenderCallback!!.onSurfaceCreated(
                        mSurface!!,
                        width,
                        height
                    )
                }
            }

            override fun onSurfaceTextureSizeChanged(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                if (mQRenderCallback != null) {
                    mQRenderCallback!!.onSurfaceChanged(
                        mSurface!!,
                        width,
                        height
                    )
                }
            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                if (mQRenderCallback != null) {
                    mQRenderCallback!!.onSurfaceDestroyed(mSurface!!)
                    Logger.i("PLVideoTextureView", "onSurfaceTextureDestroyed")
                }
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            val dimension = ViewMeasurer.measure(
                getDisplayAspectRatio(),
                widthMeasureSpec,
                heightMeasureSpec,
                mVideoWidth,
                mVideoHeight,
                mSplitMode,
                mPreferredSplitWidth,
                mPreferredSplitHeight
            )
            if (dimension.viewWidth != 0 && dimension.viewHeight != 0 && dimension.videoWidth != 0 && dimension.videoHeight != 0) {
                when (mSplitMode) {
                    4567 -> {
                        mScaledX =
                            dimension.videoWidth.toFloat() / dimension.viewWidth.toFloat() * (dimension.viewHeight.toFloat() / dimension.videoHeight.toFloat())
                        mScaledY = 1.0f
                        val scaledWidth: Int =
                            (dimension.videoWidth.toFloat() * mScaledX).toInt()
                        if (scaledWidth <= dimension.viewWidth) {
                            mScaledX = 1.0f
                            mScaledY =
                                dimension.videoHeight.toFloat() / dimension.viewHeight.toFloat() * (dimension.viewWidth.toFloat() / dimension.videoWidth.toFloat())
                        }
                    }
                    5678 -> {
                        mScaledX = 1.0f
                        mScaledY =
                            dimension.videoHeight.toFloat() / dimension.viewHeight.toFloat() * (dimension.viewWidth.toFloat() / dimension.videoWidth.toFloat())
                        val scaledHeight: Int =
                            (dimension.videoHeight.toFloat() * mScaledY).toInt()
                        if (scaledHeight <= dimension.viewHeight) {
                            mScaledX =
                                dimension.videoWidth.toFloat() / dimension.viewWidth.toFloat() * (dimension.viewHeight.toFloat() / dimension.videoHeight.toFloat())
                            mScaledY = 1.0f
                        }
                    }
                    6789 -> {
                        mScaledX = -1.0f
                        mScaledY = -1.0f
                    }
                }
                if (mScaledX == -1.0f && mScaledY == -1.0f) {
                    mTransformMatrix.setScale(1.0f, 1.0f)
                } else {
                    mTransformMatrix.setScale(
                        mScaledX,
                        mScaledY,
                        dimension.viewWidth.toFloat() / 2.0f,
                        dimension.viewHeight.toFloat() / 2.0f
                    )
                }
                setTransform(mTransformMatrix)
                setMeasuredDimension(dimension.viewWidth, dimension.viewHeight)
            } else {
                setMeasuredDimension(dimension.viewWidth, dimension.viewHeight)
            }
        }
    }
}