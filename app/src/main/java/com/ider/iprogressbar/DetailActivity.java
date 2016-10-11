package com.ider.iprogressbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.parent);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        View view;
        switch (position) {
            case 0:
                view = new Win10Progressbar(this);
                break;
            default:
                view = new Win10Progressbar(this);
                break;
        }
        parent.addView(view);



    }
}
