package com.ider.iprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * 1.一个匀速圆周运动的点：通过PathMeasure.getSegment来截取圆周上的一个点
 * 2.多个匀速圆周运动的点：每隔0.05f就重新画一个点
 * 3.多个圆周运动的点，速度由快到慢：start = length*value为直线方程，改为start = -length*value*value+2*length*value抛物线形式
 * 4.四个点最终重叠成一个点：y = x-n*space为平行4条平行直线，y = x - n*space*(1-x)最终汇聚在(1,1)点
 * 5.动画重新开始时手动画点防止闪一下
 */

public class Win10Progressbar extends View {

    private Paint mPaint;
    private Path mPath;
    private float mValue;
    private PathMeasure mPathMeasure;

    public Win10Progressbar(Context context) {
        super(context);
        initPaint();
        initPath();
        start();
    }

    public Win10Progressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
        start();
    }


    // 初始化画笔
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initPath() {
        mPath = new Path();
        mPath.addCircle(0, 0, 100, Path.Direction.CW);
        mPathMeasure = new PathMeasure(mPath, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        drawView(canvas);

    }

    public void drawView(Canvas canvas) {
        float length = mPathMeasure.getLength();
        Path dst = new Path();
        float start;
        float startValue;
        float space = 0.08f;
        int num = (int) (mValue / space);
        switch (num) {
            default:
            case 4:
                startValue = mValue - 4 * space * (1 - mValue);
                start = -length * startValue * startValue + 2 * length * startValue;
                mPathMeasure.getSegment(start, start+1, dst, true);
            case 3:
                startValue = mValue - 3 * space * (1 - mValue);
                start = -length * startValue * startValue + 2 * length * startValue;
                mPathMeasure.getSegment(start, start+1, dst, true);
            case 2:
                startValue = mValue - 2 * space * (1 - mValue);
                start = -length * startValue * startValue + 2 * length * startValue;
                mPathMeasure.getSegment(start, start+1, dst, true);
            case 1:
                startValue = mValue - 1 * space * (1 - mValue);
                start = -length * startValue * startValue + 2 * length * startValue;
                mPathMeasure.getSegment(start, start+1, dst, true);
            case 0:
                startValue = mValue;
                start = -length * startValue * startValue + 2 * length * startValue;
                mPathMeasure.getSegment(start, start+1, dst, true);
        }
        if(mValue > 0.95) {
            canvas.drawPoint(100, 0, mPaint);
        }

        canvas.drawPath(dst, mPaint);
    }


    public void start() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        animator.setDuration(2500);
        animator.start();
    }
}
