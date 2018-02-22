package com.example.miranda.monitoringpdam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DataHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_history);

        TextView textView1 = (TextView)findViewById(R.id.time);
        TextView textView2 = (TextView)findViewById(R.id.severity);
        TextView textView3 = (TextView)findViewById(R.id.recovery);
        TextView textView4 = (TextView)findViewById(R.id.status);
        TextView textView5 = (TextView)findViewById(R.id.info);
        TextView textView6 = (TextView)findViewById(R.id.host);
        TextView textView7 = (TextView)findViewById(R.id.problem);
        TextView textView8 = (TextView)findViewById(R.id.duration);
        TextView textView9 = (TextView)findViewById(R.id.ack);
        TextView textView10 = (TextView)findViewById(R.id.actions);
        TextView textView11 = (TextView)findViewById(R.id.tags);

        Bundle b =getIntent().getExtras();

        textView1.setText(b.getString("time"));
        textView2.setText(b.getString("severity"));
        textView3.setText(b.getString("recovery_time"));
        textView4.setText(b.getString("status"));
        textView5.setText(b.getString("info"));
        textView6.setText(b.getString("host"));
        textView7.setText(b.getString("problem"));
        textView8.setText(b.getString("duration"));
        textView9.setText(b.getString("ack"));
        textView10.setText(b.getString("actions"));
        textView11.setText(b.getString("tags"));

    }


}
