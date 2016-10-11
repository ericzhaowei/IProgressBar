package com.ider.iprogressbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;


public class TencentActivity extends Activity {
    TencentBaoProgress progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tencent_progress);
        progress = (TencentBaoProgress) findViewById(R.id.progress);


        start();


    }


    public void start() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 100);
        animator.setDuration(6000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress.setProgress((float)valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    public float getDensity() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    @Override
    protected void onDestroy() {
        progress.stop();
        super.onDestroy();
    }
}
