package com.androidapp.getinline.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.getinline.R;
import com.androidapp.getinline.Session;
import com.androidapp.getinline.entities.Establishment;
import com.androidapp.getinline.entities.User;
import com.androidapp.getinline.interfaces.EstablishmentsAPI;
import com.androidapp.getinline.util.Util;
import com.squareup.okhttp.ResponseBody;

import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class EstablishmentActivity extends AppCompatActivity {

    /**
     * An Establishment object
     */
    private Establishment establishment;

    /**
     * Map to retrieve user info from Shared Preference
     */
    private HashMap<String, String> userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment);

        Session session = new Session(this);
        userSession = session.getUserDetails();

        Button goToLine = (Button) findViewById(R.id.bt_go_to_line);

        establishment = getIntent().getParcelableExtra(Util.KEY_ESTABLISHMENT);

        createEstablishmentScreen();

        goToLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Util.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EstablishmentsAPI api = retrofit.create(EstablishmentsAPI.class);

                Call<ResponseBody> call = api.postClientToQueue(establishment.getName(), userSession.get(Session.KEY_USER_NAME), userSession.get(Session.KEY_USER_ID),
                        userSession.get(Session.KEY_USER_TOKEN_FCM), establishment.get_id(), userSession.get(Session.KEY_USER_EMAIL));

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        Toast.makeText(getBaseContext(), "You are in " + establishment.getName() + " line", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("ONRESPONSEFAILURE", "ONRESPONSEPOST");
                    }
                });
            }
        });
    }

    /**
     * Method that initialize and set the visual resource of Establiment Screen
     */
    private void createEstablishmentScreen() {
        TextView name = (TextView) findViewById(R.id.tv_est_name);
        TextView email = (TextView) findViewById(R.id.tv_est_email);
        TextView time = (TextView) findViewById(R.id.tv_est_time);
        TextView size = (TextView) findViewById(R.id.tv_est_size);

        name.setText(establishment.getName());
        email.setText(establishment.getEmail());
        size.setText(establishment.getSize());
        time.setText(establishment.getAttendingTime());
    }
}
