package com.androidapp.getinline.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.androidapp.getinline.R;


/**
 * Created by pedroadmn-PC on 3/14/2017.
 */

public class EstablishmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment);

        TextView name = (TextView) findViewById(R.id.tv_est_name);
        //TextView id = (TextView) findViewById(R.id.tv_est_id);
        TextView email = (TextView) findViewById(R.id.tv_est_email);
        TextView size = (TextView) findViewById(R.id.tv_est_size);
        TextView time = (TextView) findViewById(R.id.tv_est_time);

        String sName = getIntent().getStringExtra("establishmentName");
        String sId = getIntent().getStringExtra("establishmentId");
        String sEmail = getIntent().getStringExtra("establishmentEmail");
        String sSize = getIntent().getStringExtra("establishmentSize");
        String sTime = getIntent().getStringExtra("establishmentTime");

        name.setText(sName);
        //id.setText(sId);
        email.setText(sEmail);
        size.setText(sSize);
        time.setText(sTime);
    }
}
