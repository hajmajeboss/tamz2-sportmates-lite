package cz.greapp.sportmateslite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Table;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.TableGateways.GameTableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.TableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.UserTableGateway;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnProfileFragmentInteractionListener, MessagesFragment.OnFragmentInteractionListener, MyGamesFragment.OnFragmentInteractionListener, FindGameFragment.OnFragmentInteractionListener, OnFirebaseQueryResultListener {

    private User user;
    FloatingActionButton mainFab;
    Context ctx;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEdit;

    ProgressDialog progressDialog;

    Toolbar toolbar;

    public static final int NEW_GAME_REQUEST = 0;
    public static final int GAME_ADD_PLAYER = 500;
    public static final int GAME_REMOVE_PLAYER = 501;
    public static final int GAME_REMOVE_GAME = 502;
    public static final int REQUEST_USER_BY_EMAIL = 100;

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
                case R.id.navigation_profile:
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    ProfileFragment profileFragment = new ProfileFragment();
                    fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                    fragmentTransaction.commit();

                    mainFab.setVisibility(View.VISIBLE);
                    mainFab.setImageResource(R.drawable.account_settings);
                    mainFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ctx, ProfileSettingsActivity.class);
                            Bundle extras = new Bundle();
                            extras.putSerializable("user", user);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });
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

        //Zjistí a nastaví jméno uživatele
        UserTableGateway gw = new UserTableGateway();
        gw.getUserByEmail(this, user.getEmail(), REQUEST_USER_BY_EMAIL);

        progressDialog = ProgressDialog.show(ctx, "Načítám", "Může to trvat několik vteřin...");


        mainFab = (FloatingActionButton) findViewById(R.id.mainFab);
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, NewGameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
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

        /*
        if (id == R.id.main_navigation_settings) {
            Intent intent = new Intent(ctx, SettingsActivity.class);
            startActivity(intent);
            return true;
        }*/

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

    @Override
    public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
        if (requestCode == REQUEST_USER_BY_EMAIL) {
            if (resultCode == TableGateway.RESULT_OK) {
                List<DocumentSnapshot> refList = result.getDocuments();
                if (refList != null && refList.size() > 0 ) {
                    DocumentSnapshot ref = refList.get(0);
                    user = new User(ref.getString("name"), ref.getString("email"));
                    user.setId(ref.getId());

                    prefEdit = preferences.edit();
                    prefEdit.putString("username", user.getName());
                    prefEdit.putString("useremail", user.getEmail());
                    prefEdit.putString("userid", user.getId());
                    prefEdit.commit();

                    FirebaseStorage storageRef = FirebaseStorage.getInstance();

                    StorageReference gsReference = storageRef.getReferenceFromUrl("gs://sportmateslite.appspot.com/" + ref.getId() +  ".jpg");

                    try {
                        final File localFile = File.createTempFile("images", "jpg");
                        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                user.setProfileImage(localFile);
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                    catch (IOException e) {

                    }

                }

            }
            else {
                Toast.makeText(ctx, "Chyba", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        if (requestCode == GAME_ADD_PLAYER || requestCode == GAME_REMOVE_PLAYER || requestCode == GAME_REMOVE_GAME) {
            GameTableGateway gw = new GameTableGateway();
            gw.getGames(null, FindGameFragment.REQUEST_GAMES);
            gw.getUserGames(null, MyGamesFragment.REQUEST_USER_GAMES, user);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_GAME_REQUEST) {
            if (resultCode == RESULT_OK) {
                GameTableGateway gw = new GameTableGateway();
                gw.getGames(null, FindGameFragment.REQUEST_GAMES);
                gw.getUserGames(null, MyGamesFragment.REQUEST_USER_GAMES, getUser());
            }
        }

    }
}
