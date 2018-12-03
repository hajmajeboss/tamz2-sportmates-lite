package cz.greapp.sportmateslite;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import cz.greapp.sportmateslite.Data.Adapters.PlayerAdapter;
import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.TableGateways.GameTableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.TableGateway;
import cz.greapp.sportmateslite.Listeners.RecyclerItemClickListener;

public class GameActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView sportText;
    TextView placeText;
    TextView dateTimeText;
    FloatingActionButton joinFab;
    Button showCommentsButton;
    RecyclerView playersListView;
    RecyclerView.Adapter playersListAdapter;
    RecyclerView.LayoutManager playersListLayoutManager;
    Game game;
    User user;
    Context ctx;
    Activity activity;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ctx = this;
        activity = this;
        toolbar = (Toolbar) findViewById(R.id.gameDetailToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Načtení uživatele
        preferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        user = new User(preferences.getString("username", null), preferences.getString("useremail", null));
        user.setId(preferences.getString("userid", null));


        Bundle bundle = getIntent().getExtras();
        game = (Game)bundle.getSerializable("game");

        sportText = (TextView) findViewById(R.id.detailSportText);
        placeText = (TextView) findViewById(R.id.detailPlaceText);
        dateTimeText = (TextView) findViewById(R.id.detailDateTimeText);

        sportText.setText(game.getSport().getName());
        placeText.setText(game.getPlace());
        dateTimeText.setText(game.getDate() + " | od " + game.getTimeFrom() + " | do " + game.getTimeTo());


        showCommentsButton = findViewById(R.id.showCommentsButton);
        showCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(ctx, ConversationActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable("game", game);
                    intent.putExtras(extras);
                    startActivity(intent);
            }
        });

        playersListView = (RecyclerView) findViewById(R.id.playersListView);

        playersListLayoutManager = new LinearLayoutManager(this);
        playersListView.setLayoutManager(playersListLayoutManager);

        playersListAdapter = new PlayerAdapter(game.getPlayers());
        playersListView.setAdapter(playersListAdapter);

        playersListView.addOnItemTouchListener(
                new RecyclerItemClickListener(ctx, playersListView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                     /*   ShareCompat.IntentBuilder.from(activity)
                                .setType("message/rfc822")
                                .addEmailTo(game.getPlayers().get(position).getEmail())
                                .setSubject("Zpráva z aplikace Sportmates")
                                .setText(game.getSport().getName() + " - " + game.getPlace() + " - " + game.getDate() + " " + game.getTimeFrom() + "-" + game.getTimeTo() + "\n\n" + "Text zprávy: ")
                                .setChooserTitle("Vyberte emailového klienta:")
                                .startChooser();*/



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        joinFab = (FloatingActionButton) findViewById(R.id.joinFab);

        if (game.getPlayers().size() == 1 && user.getName() != null && user.getEmail() != null && !game.getPlayers().get(0).getEmail().equals(user.getEmail())) {
            joinFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog dlg = ProgressDialog.show(ctx, "Přidat se", "Může to trvat několik vteřin...");
                    GameTableGateway gw = new GameTableGateway();
                    gw.addSecondPlayer(new OnFirebaseQueryResultListener() {
                        @Override
                        public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
                            if (resultCode == TableGateway.RESULT_OK) {
                                dlg.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                dlg.dismiss();
                                Toast.makeText(ctx, "Chyba při přidání uživatele do hry. Zkontrolujte, že jste připojeni k internetu.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 500, game, user);
                }
            });
        }
        //moje hra
        else if (game.getPlayers().size() == 2 && game.getPlayers().get(1).getEmail().equals(user.getEmail())) {
            joinFab.setImageResource(R.drawable.account_remove_outline);
            joinFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog dlg = ProgressDialog.show(ctx, "Opustit hru", "Může to trvat několik vteřin...");
                    GameTableGateway gw = new GameTableGateway();
                    gw.removeSecondPlayer(new OnFirebaseQueryResultListener() {
                        @Override
                        public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
                            if (resultCode == TableGateway.RESULT_OK) {
                                dlg.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                dlg.dismiss();
                                Toast.makeText(ctx, "Chyba při opouštění hry. Zkontrolujte, že jste připojeni k internetu.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 501, game, user);
                }
            });
        }
        else if (game.getPlayers().get(0).getEmail().equals(user.getEmail())) {
            joinFab.setImageResource(R.drawable.trash_can_outline);
            joinFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog dlg = ProgressDialog.show(ctx, "Smazat hru", "Může to trvat několik vteřin...");
                    GameTableGateway gw = new GameTableGateway();
                    gw.removeGame(new OnFirebaseQueryResultListener() {
                        @Override
                        public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
                            if (resultCode == TableGateway.RESULT_OK) {
                                dlg.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                dlg.dismiss();
                                Toast.makeText(ctx, "Chyba při mazání hry. Zkontrolujte, že jste připojeni k internetu.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 502, game);
                }
            });
        }
        else {
            joinFab.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

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


}
