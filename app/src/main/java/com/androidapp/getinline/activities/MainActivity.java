package com.androidapp.getinline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.androidapp.getinline.adapters.ViewPagerAdapter;
import com.androidapp.getinline.entities.User;
import com.androidapp.getinline.fragments.EstablishmentsFragment;
import com.androidapp.getinline.fragments.LineFragment;
import com.androidapp.getinline.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class MainActivity extends AppCompatActivity {

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
     * A User
     */
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getIntent().getParcelableExtra(Util.KEY_USER);
        Log.d("KEYUSER", user.getEmail());

        mActivityTitle = getTitle().toString();

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d("TOKEEN", token);

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
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setup_guide:
                        Intent tutorialIntent = new Intent(getBaseContext(), MaterialTutorialActivity.class);
                        tutorialIntent.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, Util.getTutorialItems(getBaseContext()));
                        startActivity(tutorialIntent);
                        Toast.makeText(getApplicationContext(), "Setup Guide", Toast.LENGTH_SHORT).show();
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
        TextView tv_email = (TextView) header.findViewById(R.id.profile_email);
        TextView tv_name = (TextView) header.findViewById(R.id.profile_name);

        String profileName = getIntent().getStringExtra("profileName");
        String profileEmail = getIntent().getStringExtra("profileEmail");
        String profilePhoto = getIntent().getStringExtra("profilePhoto");

        tv_email.setText(profileEmail);
        tv_name.setText(profileName);

        Glide.with(getApplicationContext()).load(profilePhoto)
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
        init();
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

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Method to build the view pager with the correspondent fragments
     *
     * @param viewPager View Pager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EstablishmentsFragment(), getResources().getString(R.string.establishments));
        adapter.addFragment(new LineFragment(), getResources().getString(R.string.inline));
        viewPager.setAdapter(adapter);
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

        if (id == R.id.action_update) {
            startActivity(new Intent(this, UpdateActivity.class));
        } else if (id == R.id.action_update_login) {
            startActivity(new Intent(this, UpdateLoginActivity.class));
        } else if (id == R.id.action_link_accounts) {
            startActivity(new Intent(this, LinkAccountsActivity.class));
        } else if (id == R.id.action_update_password) {
            startActivity(new Intent(this, UpdatePasswordActivity.class));
        } else if (id == R.id.action_remove_user) {
            startActivity(new Intent(this, RemoveUserActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
