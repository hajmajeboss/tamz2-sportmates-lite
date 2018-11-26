package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ProfileFragment.OnProfileFragmentInteractionListener, MessagesFragment.OnFragmentInteractionListener, MyGamesFragment.OnFragmentInteractionListener, FindGameFragment.OnFragmentInteractionListener {

    private User user;
    FloatingActionButton mainFab;
    Context ctx;

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ctx = this;

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

    public User getUser() {
        return user;
    }
}
