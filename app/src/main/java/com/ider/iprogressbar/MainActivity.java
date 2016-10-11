package com.ider.iprogressbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ider.iprogressbar.R.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        listview = (ListView) findViewById(id.listview);
        String[] titles = getResources().getStringArray(array.titles);

        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                } else if(position == 1) {
                    Intent intent = new Intent(MainActivity.this, TencentActivity.class);
                    startActivity(intent);
                }

            }
        });
    }



}
