package cz.greapp.sportmateslite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    SharedPreferences preferences;

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


        preferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        user = new User(preferences.getString("username", null), preferences.getString("useremail", null));
        user.setId(preferences.getString("userid", null));

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

                String dateRegex = "^[0-3]?[0-9].[0-3]?[0-9].(?:[0-9]{2})?[0-9]{2}$";
                String dateRegex1 = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
                String timeRegex = "^[0-2][0-9]:[0-5][0-9]$";

                Pattern datePattern = Pattern.compile(dateRegex);
                Pattern datePattern1 = Pattern.compile(dateRegex1);
                Pattern timePattern = Pattern.compile(timeRegex);

                Matcher dateMatcher = datePattern.matcher(newGameDateField.getText().toString());
                Matcher dateMatcher1 = datePattern1.matcher(newGameDateField.getText().toString());
                Matcher timeFromMatcher = timePattern.matcher(newGameTimeFromField.getText().toString());
                Matcher timeToMatcher = timePattern.matcher(newGameTimeToField.getText().toString());

                if (newGamePlaceField.getText().toString().equals("")) {
                    Toast.makeText(ctx, "Vložte prosím místo konání.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(dateMatcher.matches() || dateMatcher1.matches())) {
                    Toast.makeText(ctx, "Vložte prosím validní datum.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!timeFromMatcher.matches() || !timeToMatcher.matches()) {
                    Toast.makeText(ctx, "Vložte prosím validní čas.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.parseInt(newGameTimeFromField.getText().toString().split(":")[0]) > 23
                        || Integer.parseInt(newGameTimeToField.getText().toString().split(":")[0]) > 23 )  {
                    Toast.makeText(ctx, "Vložte prosím validní čas.", Toast.LENGTH_SHORT).show();
                    return;
                }



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
