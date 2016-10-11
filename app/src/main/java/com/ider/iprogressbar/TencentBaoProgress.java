package com.ider.iprogressbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;


public class TencentBaoProgress extends View {

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mBlockPaint;
    // 进度：0~100之间
    private float progress;

    private int defaultWidth = 700;
    private int defaultHeight = 100;

    // 滑块左边位置。控制滑块移动
    private int flickerLeft;
    private Bitmap flickerBitmap;
    private boolean isStoped;

    public TencentBaoProgress(Context context) {
        super(context);
        initPaint();
        initFlicker();

    }

    public TencentBaoProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initFlicker();
    }

    private void initFlicker() {
        flickerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.flicker);
        flickerLeft = -flickerBitmap.getWidth();
        new Thread(flickerRunnable).start();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorLightBlue));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(40);
        mTextPaint.setStyle(Paint.Style.FILL);

        mBlockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlockPaint.setColor(getResources().getColor(R.color.colorAccent));
        mBlockPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("tag", "onMeasure");
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = defaultWidth;
                break;
            case MeasureSpec.EXACTLY:
                width = measuredWidth;
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.i("tag", "UNSPECIFIED");
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = defaultHeight;
                break;
            case MeasureSpec.EXACTLY:
                height = measuredHeight;
                break;
            case MeasureSpec.UNSPECIFIED:

                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private void drawView(Canvas canvas) {

        drawBord(canvas);

        drawProgress(canvas);

        if (!isStoped) {
            drawMoveBlock(canvas);
        }

        drawProgressText(canvas);

    }

    public void drawBord(Canvas canvas) {
        //边框
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, width, height, mPaint);
    }

    public void drawProgress(Canvas canvas) {
        // 与边框相同颜色的进度条
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mPaint.setStyle(Paint.Style.FILL);
        float stop = width * progress / 100;
        canvas.drawRect(0, 0, stop, height, mPaint);
    }

    private void drawProgressText(Canvas canvas) {
        // 已下载文字
        mTextPaint.setColor(getResources().getColor(R.color.colorLightBlue));
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        String text = getProgressText();
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textBounds);
        int textWidth = textBounds.width();
        int textHeight = textBounds.height();
        float startX = (width - textWidth) / 2;
        float startY = (height - textHeight) / 2 + textHeight;
        canvas.drawText(text, 0, text.length(), startX, startY, mTextPaint);

        float measuredWidth = 0;
        int length = mTextPaint.breakText(text, 0, text.length(), true, 100, new float[]{measuredWidth});
        Log.i("s", length + "/" + measuredWidth);

        float processX = width * progress / 100;
        if (processX > startX) {
            mTextPaint.setColor(Color.WHITE);
            canvas.save(Canvas.CLIP_SAVE_FLAG);
            float right = Math.min(startX + textWidth + 10, processX);
            canvas.clipRect(startX, 0, right, height);
            canvas.drawText(text, 0, text.length(), startX, startY, mTextPaint);
            canvas.restore();
        }



    }

    private void drawMoveBlock(Canvas canvas) {
        float progressWidth = getMeasuredWidth() * progress / 100;
        canvas.save();
        canvas.clipRect(0, 0, progressWidth, getMeasuredHeight());
        int left = flickerLeft;
        int top = 0;
        canvas.drawBitmap(flickerBitmap, left, top, mBlockPaint);
        canvas.restore();
    }

    private String getProgressText() {
        if(progress == 100) {
            return "下载完成";
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String percent = decimalFormat.format(progress) + "%";
        return String.format(getResources().getString(R.string.progress_percent), percent);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        isStoped = progress == 100;
        if(isStoped) {
            mPaint.setColor(getResources().getColor(R.color.colorDownloadOver));
        }
        invalidate();
    }


    Runnable flickerRunnable = new Runnable() {
        @Override
        public void run() {
            while (!isStoped) {

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                float progressWidth = getMeasuredWidth() * progress / 100;
                flickerLeft += 2;
                if (flickerLeft > progressWidth) {
                    flickerLeft = -flickerBitmap.getWidth();
                }
            }
        }
    };

    public void stop() {
        isStoped = true;
    }


}
