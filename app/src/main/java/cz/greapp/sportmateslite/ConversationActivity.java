package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.common.collect.Table;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cz.greapp.sportmateslite.Data.Adapters.GameAdapter;
import cz.greapp.sportmateslite.Data.Adapters.MessageAdapter;
import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.Message;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.Parsers.MessageSnapshotParser;
import cz.greapp.sportmateslite.Data.QueryResultObserver;
import cz.greapp.sportmateslite.Data.TableGateways.GameTableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.MessageTableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.TableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.UserTableGateway;

public class ConversationActivity extends AppCompatActivity implements OnFirebaseQueryResultListener {

    Toolbar toolbar;

    RecyclerView messagesListView;
    RecyclerView.Adapter messagesListAdapter;
    RecyclerView.LayoutManager messagesListLayoutManager;
    List<Message> messages;
    Context ctx;

    FloatingActionButton sendFab;

    SwipeRefreshLayout swipeRefreshLayout;

    OnFirebaseQueryResultListener listener;


    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    EditText messageText;

    User user;
    Game game;

    Thread updateThread;

    public static final int REQUEST_MESSAGES_BY_GAME = 999;
    public static final int REQUEST_SEND_MESSAGE = 997;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        ctx = this;
        listener = this;

        toolbar = findViewById(R.id.convoToolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        user = new User(preferences.getString("username", ""), preferences.getString("useremail", ""));
        user.setId(preferences.getString("userid", null));

        messagesListView = (RecyclerView) findViewById(R.id.convoListView);

        messagesListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        messagesListView.setLayoutManager(messagesListLayoutManager);

        messages = new ArrayList<Message>();
        messagesListAdapter = new MessageAdapter(messages, user);
        messagesListView.setAdapter(messagesListAdapter);

        messageText = findViewById(R.id.messageField);


        game = (Game)getIntent().getExtras().getSerializable("game");


        sendFab = findViewById(R.id.sendMessageFab);
        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageText.getText().toString().equals("")) {
                    Toast.makeText(ctx, "Nelze odeslat prázdnou zprávu.", Toast.LENGTH_SHORT).show();
                    return;
                }
                final MessageTableGateway gw = new MessageTableGateway();
                swipeRefreshLayout.setRefreshing(true);
                gw.putMessage(listener, REQUEST_SEND_MESSAGE, game, new Message(messageText.getText().toString(), user, ""));
            }
        });

        swipeRefreshLayout = findViewById(R.id.convoSwipeRefresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MessageTableGateway gw = new MessageTableGateway();
                gw.getGameMesasges(listener, REQUEST_MESSAGES_BY_GAME, game);
            }
        });


        MessageTableGateway gw = new MessageTableGateway();
        gw.getGameMesasges(this, REQUEST_MESSAGES_BY_GAME, game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                MessageTableGateway gw = new MessageTableGateway();
                                gw.getGameMesasges(listener, REQUEST_MESSAGES_BY_GAME, game);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        updateThread.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        // Interrupts updating conversation
        updateThread.interrupt();
        // Removes listener - you won't receive updates from different conversation
        QueryResultObserver.getInstance().removeListener(this);
        finish();
        super.onBackPressed();
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
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
        if (requestCode == REQUEST_MESSAGES_BY_GAME ) {
            if (resultCode == TableGateway.RESULT_OK) {
                MessageSnapshotParser parser = new MessageSnapshotParser();
                List<Message> messagesLst = parser.parseQuerySnapshot(result);

                Collections.sort(messagesLst);
                messagesListAdapter = new MessageAdapter(messagesLst, user);
                messagesListView.setAdapter(messagesListAdapter);
              //  ((MessageAdapter)messagesListAdapter).updateDataSet(messagesLst);
                swipeRefreshLayout.setRefreshing(false);
            }
        }

        if (requestCode == REQUEST_SEND_MESSAGE) {
            if (resultCode == TableGateway.RESULT_OK) {
                Toast.makeText(ctx, "Zpráva byla úspěšně odeslána", Toast.LENGTH_SHORT).show();
                messageText.setText("");
                MessageTableGateway gw = new MessageTableGateway();
                gw.getGameMesasges(this, REQUEST_MESSAGES_BY_GAME, game);
            }
            else {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ctx, "Odeslání se nezdařilo. Zkontrolujte prosím připojení k internetu.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

