package com.ider.iprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ider-eric on 2016/9/21.
 * Test line for origin/master patch
 * A new line for branch eric
 */

public class IProgressbar extends View {

    private static String TAG = "IProgressbar";



    Paint mPaint;
    Path circle;
    PathMeasure pathMeasure;

    ValueAnimator loadAnimator;
    ValueAnimator.AnimatorUpdateListener updateListener;
    float animatorValue;

    int radius = 50;
    int duration = 3000;
    int strokeWidth = 5;


    public IProgressbar(Context context) {
        super(context);
        initElement();
    }

    public IProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initElement();
    }


    private void initElement() {
        initPaint();
        initPath();
        initListener();
        initAnimation();
        start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBaseline(canvas);

        drawViews(canvas);
    }

    private void drawViews(Canvas canvas) {
        Path dst = new Path();
        int num = (int)(animatorValue / 0.05f);
        float x, start, length;
        switch (num) {
            default:
            case 4:
                x = (animatorValue - 0.20f) / (1 - 0.20f);
                length = pathMeasure.getLength();
                start = -length * x * x + 2 * length * x;
                pathMeasure.getSegment(start, start+1, dst, true);
            case 3:
                x = (animatorValue - 0.15f) / (1 - 0.15f);
                length = pathMeasure.getLength();
                start = -length * x * x + 2 * length * x;
                pathMeasure.getSegment(start, start+1, dst, true);
            case 2:
                x = (animatorValue - 0.10f) / (1 - 0.10f);
                length = pathMeasure.getLength();
                start = -length * x * x + 2 * length * x;
                pathMeasure.getSegment(start, start+1, dst, true);
            case 1:
                x = (animatorValue - 0.05f) / (1 - 0.05f);
                length = pathMeasure.getLength();
                start = -length * x * x + 2 * length * x;
                pathMeasure.getSegment(start, start+1, dst, true);
            case 0:
                x = animatorValue - 0;
                length = pathMeasure.getLength();
                start = -length * x * x + 2 * length * x;
                pathMeasure.getSegment(start, start+1, dst, true);
                break;
        }
        if(animatorValue >= 0.95f) {
            canvas.drawPoint(0, -50, mPaint);
        }

        canvas.drawPath(dst, mPaint);
    }

    private void drawBaseline(Canvas canvas) {
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        Paint lp = new Paint();
        lp.setAlpha(5);
        lp.setColor(Color.BLACK);
        lp.setStrokeWidth(1);
    }


    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
    }

    private void initPath() {
        circle = new Path();
        pathMeasure = new PathMeasure();
        RectF cirF = new RectF(-radius, -radius, radius, radius);
        circle.arcTo(cirF, -90, 359.9f);


    }

    private void initAnimation() {
        loadAnimator = ValueAnimator.ofFloat(0, 1);

        loadAnimator.setDuration(duration);
        loadAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadAnimator.setRepeatMode(ValueAnimator.RESTART);

        loadAnimator.addUpdateListener(updateListener);


    }

    private void initListener() {
        updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        };

    }

    private void start() {
        pathMeasure.setPath(circle, false);
        loadAnimator.start();
    }

}
