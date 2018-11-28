package cz.greapp.sportmateslite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.Sport;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.TableGateways.GameTableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.TableGateway;

public class NewGameActivity extends AppCompatActivity implements OnFirebaseQueryResultListener {

    Toolbar toolbar;
    Spinner sportSpinner;

    TextInputEditText newGamePlaceField;
    TextInputEditText newGameDateField;
    TextInputEditText newGameTimeFromField;
    TextInputEditText newGameTimeToField;
    FloatingActionButton newGameFab;

    ProgressDialog progressDialog;
    OnFirebaseQueryResultListener listener;
    Context ctx;

    User user;

    public static final int REQUEST_NEW_GAME = 422;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        listener = this;
        ctx = this;

        toolbar = (Toolbar) findViewById(R.id.newGameToolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
        else {
            user = new User("gg", "wp");
            Toast.makeText(ctx, "Fuck it all", Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sportSpinner = (Spinner) findViewById(R.id.newSportSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unique_sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(adapter);

        newGamePlaceField = findViewById(R.id.newGamePlace);
        newGameDateField = findViewById(R.id.newGameDate);
        newGameTimeFromField = findViewById(R.id.newGameTimeFrom);
        newGameTimeToField = findViewById(R.id.newGameTimeTo);
        newGameFab = findViewById(R.id.newGameFab);

        newGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTableGateway gw = new GameTableGateway();
                Game game = new Game();
                game.setPlace(newGamePlaceField.getText().toString());
                game.setDate(newGameDateField.getText().toString());
                game.setTimeFrom(newGameTimeFromField.getText().toString());
                game.setTimeTo(newGameTimeToField.getText().toString());
                List<User> players = new ArrayList<>();
                players.add(new User(user.getName(), user.getEmail()));
                game.setPlayers(players);
                game.setSport(new Sport(sportSpinner.getSelectedItem().toString()));
                gw.putGame(listener, REQUEST_NEW_GAME, game);
                progressDialog = ProgressDialog.show(ctx, "Přidat hru", "Může to trvat několik vteřin...");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
        if (requestCode == REQUEST_NEW_GAME) {
            if (resultCode == TableGateway.RESULT_OK) {
                progressDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(this, "Přidání hry selhalo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
