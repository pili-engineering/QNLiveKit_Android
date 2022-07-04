package sensetime.senseme.com.effects.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import sensetime.senseme.com.effects.R;

public class StretchTextView extends AppCompatTextView {
    private CharSequence textContent;
    private float mSpacingMult;
    private final float mSpacingAdd;
    private Layout.Alignment alignment;
    private int widthL;
    private int heightL;
    private float cacheTargetSize;
    private float newSize = 0;

    public StretchTextView(Context context) {
        this(context, null);
    }

    public StretchTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StretchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StretchTextView);
        //StaticLayout 的LayoutAlign
        int layoutAlign = a.getInteger(R.styleable.StretchTextView_layoutalign, 0);
        mSpacingMult = a.getInteger(R.styleable.StretchTextView_mSpacingMult, 1);
        mSpacingAdd = a.getInteger(R.styleable.StretchTextView_mSpacingAdd, 0);
        switch (layoutAlign) {
            case 0:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case 1:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            case 2:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            default:
                break;
        }
        a.recycle();
        textContent = getText();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        textContent = getText();
        widthL = (w - oldw) - getCompoundPaddingLeft() - getCompoundPaddingRight();
        heightL = (h - oldh) - getCompoundPaddingBottom() - getCompoundPaddingTop();
        setNewText(widthL, heightL);
    }

    public void changeTextSize(float newTextSize) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
    }

    public float setStretchTextView(String text, int width, int height, TextPaint paint, Layout.Alignment alignment, Canvas canvas) {
        cacheTargetSize = paint.getTextSize();
        int cacheHeight = getHeightByWidth(text, alignment, paint, width, cacheTargetSize).getHeight();
        //默认textSize的最小值
        float minSize = 1.0f;
        while (cacheHeight > height && cacheTargetSize > minSize) {
            cacheTargetSize = Math.max(cacheTargetSize - 1, minSize);
            cacheHeight = getHeightByWidth(text, alignment, paint, width, cacheTargetSize).getHeight();
        }
        if (canvas != null) {
            getHeightByWidth(text, alignment, paint, width, cacheTargetSize).draw(canvas);
        }
        return cacheTargetSize;
    }

    private void setNewText(int widthL, int heightL) {
        this.widthL = widthL;
        this.heightL = heightL;
        if (widthL >= 0 && heightL >= 0 && textContent != null && textContent.length() > 0) {
            TextPaint tPaint = new TextPaint(getPaint());
            newSize = setNewSize(textContent.toString().trim(), widthL, heightL, tPaint, alignment);
        }
        changeTextSize(newSize);
    }

    private float setNewSize(String text, int width, int height, TextPaint paint, Layout.Alignment alignment) {
        cacheTargetSize = setStretchTextView(text, width, height, paint, alignment, null);
        return cacheTargetSize;
    }

    private StaticLayout getHeightByWidth(CharSequence source, Layout.Alignment align, TextPaint paint, int width, float textSize) {
        paint.setTextSize(textSize);
        mSpacingMult = mSpacingMult < 1.0 ? 1.0f : mSpacingMult;
        return new StaticLayout(source, paint, width, align, mSpacingMult, mSpacingAdd, true);
    }
}
