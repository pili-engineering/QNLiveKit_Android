package sensetime.senseme.com.effects.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.SenseMeApplication

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/25/21 1:34 PM
 */
object GlideUtils {

    fun load1(url: String?, iv: ImageView) {
        Glide.with(ContextHolder.getContext())
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .placeholder(R.drawable.shape_default_placeholder)
            .error(R.drawable.none)
            .into(iv)
    }

    fun load2(url: String?, iv: ImageView) {
        Glide.with(ContextHolder.getContext())
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(iv)
    }

    fun compressBitmap(path: String): Bitmap {
        val picWidth = Glide.with(ContextHolder.getContext()).asBitmap().load(path)
            .submit().get().width
        return if (picWidth > 2500) {
            Glide.with(ContextHolder.getContext()).asBitmap().load(path)
                .submit(2000, 2000)
                .get()
        } else {
            Glide.with(ContextHolder.getContext()).asBitmap().load(path)
                .submit()
                .get()
        }
    }

    fun compressBitmap3(path: String): Bitmap? {
        val resultBitmap:Bitmap? = try {
            val picWidth = Glide.with(ContextHolder.getContext()).asBitmap().load(path)
                .submit().get().width
            return if (picWidth > 1080) {
                Glide.with(ContextHolder.getContext()).asBitmap().load(path)
                    .submit(1080, 2000)
                    .get()
            } else {
                Glide.with(ContextHolder.getContext()).asBitmap().load(path)
                    .submit()
                    .get()
            }

        } catch (e:Exception) {
            null
        }
        return resultBitmap
    }

    fun compressBitmap1(path: String): Bitmap {
        return Glide.with(ContextHolder.getContext()).asBitmap().load(path)
            .submit()
            .get()
    }

    fun load(context: Context, url: String?, iv: ImageView) {
//        url?.let {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .error(R.drawable.shape_default_avatar)
            //.centerCrop()
            .into(iv)
//        }
    }

    fun load2(context: Context, url: String?, iv: ImageView) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
            .error(R.drawable.none)
            .placeholder(R.drawable.shape_default_placeholder)
            .into(iv)
    }

    fun loadCircle(imgUrl: String?, imageView: ImageView) {
        Glide.with(SenseMeApplication.getContext())
            .load(imgUrl)
            .error(R.drawable.shape_default_avatar)
            .placeholder(R.drawable.shape_default_avatar)
            .fallback(R.drawable.shape_default_avatar)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }
}
