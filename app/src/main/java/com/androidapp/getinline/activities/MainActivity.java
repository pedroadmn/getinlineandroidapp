package com.androidapp.getinline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.getinline.R;
import com.androidapp.getinline.Session;
import com.androidapp.getinline.entities.User;
import com.androidapp.getinline.fragments.UpdateFragment;
import com.androidapp.getinline.fragments.UpdateLoginFragment;
import com.androidapp.getinline.fragments.UpdatePasswordFragment;
import com.androidapp.getinline.fragments.ViewPagerTabFragment;
import com.androidapp.getinline.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * Text View user email
     */
    TextView tv_email;

    /**
     * Text view user name
     */
    TextView tv_name;

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private FirebaseAuth mAuth;

    /**
     * Listener called when there is a change in the authentication state.
     */
    private FirebaseAuth.AuthStateListener authStateListener;

    /**
     * A standard toolbar for use within application content.
     */
    private Toolbar toolbar;

    /**
     * Top-level container for window content that allows for interactive "drawer" views to be pulled out from one or both vertical edges of the window.
     */
    private DrawerLayout mDrawerLayout;

    /**
     * Activity title attribute
     */
    private String mActivityTitle;

    /**
     * Map to retrieve user info from Shared Preference
     */
    private HashMap<String, String> userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("OncreateMain", "Entrou");

        Session session = new Session(this);
        userSession = session.getUserDetails();

        init();

        mActivityTitle = getTitle().toString();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        final DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseDatabase.child(userSession.get(Session.KEY_USER_ID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.isSocialNetworkLogged(getBaseContext())){
                    tv_name.setText(userSession.get(Session.KEY_USER_NAME));
                    tv_email.setText(userSession.get(Session.KEY_USER_EMAIL));
                } else {
                    tv_name.setText(user.getName());
                    tv_email.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_body, ViewPagerTabFragment.newInstance(), "rageComicList")
                    .commit();
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_body, new ViewPagerTabFragment());
//        fragmentTransaction.commit();
    }

    /**
     * Method to initiate the navigation drawer. Setup drawer layout and the navigation view list and menu item.
     */
    public void initNavigationDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navList);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.favorites_establishments:
                        Intent i = new Intent(getBaseContext(), FavoritesEstablishments.class);
                        startActivity(i);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setup_guide:
                        Intent tutorialIntent = new Intent(getBaseContext(), MaterialTutorialActivity.class);
                        tutorialIntent.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, Util.getTutorialItems(getBaseContext()));
                        startActivity(tutorialIntent);
                        break;
                    case R.id.legal:
                        Toast.makeText(getApplicationContext(), "Legal", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                }
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);

        ImageView iv_photo = (ImageView) header.findViewById(R.id.profile_photo);
        tv_email = (TextView) header.findViewById(R.id.profile_email);
        tv_name = (TextView) header.findViewById(R.id.profile_name);

        tv_email.setText(userSession.get(Session.KEY_USER_EMAIL));
        tv_name.setText(userSession.get(Session.KEY_USER_NAME));

        Glide.with(getApplicationContext()).load(userSession.get(Session.KEY_USER_URL_PHOTO))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_photo);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                getSupportActionBar().setTitle(getResources().getString(R.string.profile));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OncreateResume", "Entrou");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Method to initiate the main screen layout. The action bar, view pager and tab layout
     */
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initNavigationDrawer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        User user = new User();
        if (user.isSocialNetworkLogged(this)) {
            getMenuInflater().inflate(R.menu.menu_social_network_logged, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.action_update) {
            fragment = new UpdateFragment();
        } else if (id == R.id.action_update_login) {
            fragment = new UpdateLoginFragment();
        } else if (id == R.id.action_link_accounts) {
            startActivity(new Intent(this, LinkAccountsActivity.class));
        } else if (id == R.id.action_update_password) {
            fragment = new UpdatePasswordFragment();
        } else if (id == R.id.action_remove_user) {
            startActivity(new Intent(this, RemoveUserActivity.class));
        }

        if (fragment != null) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
//            getSupportActionBar().setTitle(title);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("OnStartMain", "Entrou");
    }

}
