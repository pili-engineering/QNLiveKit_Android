package sensetime.senseme.com.effects.view;

import static sensetime.senseme.com.effects.MSG.MSG_HIDE_VERTICALSEEKBAR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;


@SuppressLint("AppCompatCustomView")
public class VerticalSeekBar extends SeekBar {
    private Handler mHandler;
    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(),getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-90);
        canvas.translate(-getHeight(),0);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(MSG_HIDE_VERTICALSEEKBAR);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int i=0;
                //获取滑动的距离
                i=getMax() - (int) (getMax() * event.getY() / getHeight());
                //设置进度
                setProgress(i);
                //每次拖动SeekBar都会调用
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                if(event.getAction() ==  MotionEvent.ACTION_UP){
                    mHandler.sendEmptyMessageDelayed(MSG_HIDE_VERTICALSEEKBAR, 2000);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }


}
