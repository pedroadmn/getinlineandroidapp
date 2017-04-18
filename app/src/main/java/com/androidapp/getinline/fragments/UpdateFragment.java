package com.androidapp.getinline.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UpdateFragment extends Fragment implements ValueEventListener, DatabaseReference.CompletionListener {

    /**
     * A standard toolbar for use within application content.
     */
    private Toolbar toolbar;

    /**
     * A User object
     */
    private User user;

    /**
     * The user name variable
     */
    private AutoCompleteTextView name;

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private FirebaseAuth mAuth;

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.activity_update, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update profile");

        name = (AutoCompleteTextView) rootView.findViewById(R.id.name);
        user = new User();
        user.setId(mAuth.getInstance().getCurrentUser().getUid());
        user.contextDataDB(getActivity().getApplicationContext());

        Button button = (Button) rootView.findViewById(R.id.bt_update_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setId(mAuth.getInstance().getCurrentUser().getUid());
                user.setName(name.getText().toString());
                user.updateDB();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError != null) {
            FirebaseCrash.report(databaseError.toException());
            Toast.makeText(getActivity(), getResources().getString(R.string.failed) + databaseError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.successfully_updated), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("ONDATACHANGE", "ENTROU");
        User u = dataSnapshot.getValue(User.class);
        name.setText(u.getName());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("onCancelled", "ENTROU");
        FirebaseCrash.report(databaseError.toException());
    }

}
