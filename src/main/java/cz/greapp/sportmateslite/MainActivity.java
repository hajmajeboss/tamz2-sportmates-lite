package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import cz.greapp.sportmateslite.Data.Models.User;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnProfileFragmentInteractionListener, MessagesFragment.OnFragmentInteractionListener, MyGamesFragment.OnFragmentInteractionListener, FindGameFragment.OnFragmentInteractionListener {

    private User user;
    FloatingActionButton mainFab;
    Context ctx;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEdit;

    Toolbar toolbar;

    public static final int NEW_GAME_REQUEST = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_findGame:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FindGameFragment findGameFragment = new FindGameFragment();
                    fragmentTransaction.replace(R.id.fragment_container,findGameFragment);
                    fragmentTransaction.commit();

                    mainFab.setVisibility(View.VISIBLE);
                    mainFab.setImageResource(R.drawable.plus);
                    mainFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ctx, NewGameActivity.class);
                            startActivityForResult(intent, MainActivity.NEW_GAME_REQUEST);
                        }
                    });
                    return true;
                case R.id.navigation_myGames:
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    MyGamesFragment myGamesFragment = new MyGamesFragment();
                    fragmentTransaction.replace(R.id.fragment_container, myGamesFragment);
                    fragmentTransaction.commit();

                    mainFab.setVisibility(View.VISIBLE);
                    mainFab.setImageResource(R.drawable.plus);
                    mainFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ctx, NewGameActivity.class);
                            startActivityForResult(intent, MainActivity.NEW_GAME_REQUEST);
                        }
                    });
                    return true;
                /*case R.id.navigation_messages:
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    MessagesFragment messagesFragment = new MessagesFragment();
                    fragmentTransaction.replace(R.id.fragment_container, messagesFragment);
                    fragmentTransaction.commit();

                    mainFab.setVisibility(View.VISIBLE);
                    mainFab.setImageResource(R.drawable.message_plus);
                    mainFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    return true;*/
                case R.id.navigation_profile:
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    ProfileFragment profileFragment = new ProfileFragment();
                    fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                    fragmentTransaction.commit();

                    mainFab.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        ctx = this;
        preferences = ctx.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Bundle extras = getIntent().getExtras();
        user = (User) extras.getSerializable("user");

        mainFab = (FloatingActionButton) findViewById(R.id.mainFab);
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, NewGameActivity.class);
                startActivityForResult(intent, MainActivity.NEW_GAME_REQUEST);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FindGameFragment findGameFragment = new FindGameFragment();
        fragmentTransaction.replace(R.id.fragment_container,findGameFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_navigation_settings) {
            Intent intent = new Intent(ctx, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.main_navigation_about) {
            Intent intent = new Intent(ctx, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.main_navigation_logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent = new Intent(ctx, LoginActivity.class);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public User getUser() {
        return user;
    }
}
