package cz.greapp.sportmateslite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView sportText;
    TextView placeText;
    TextView dateTimeText;
    RecyclerView playersListView;
    RecyclerView.Adapter playersListAdapter;
    RecyclerView.LayoutManager playersListLayoutManager;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        toolbar = (Toolbar) findViewById(R.id.gameDetailToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        game = (Game)bundle.getSerializable("game");

        sportText = (TextView) findViewById(R.id.detailSportText);
        placeText = (TextView) findViewById(R.id.detailPlaceText);
        dateTimeText = (TextView) findViewById(R.id.detailDateTimeText);

        sportText.setText(game.getSport().getName());
        placeText.setText(game.getPlace());
        dateTimeText.setText(game.getDate() + " | od " + game.getTimeFrom() + " | do " + game.getTimeTo());

        game.addPlayer(new Player("David Vybíral", "davca@seznam.cz"));
        game.addPlayer(new Player("Filip Vaníček", "filda@seznam.cz"));
        playersListView = (RecyclerView) findViewById(R.id.playersListView);

        playersListLayoutManager = new LinearLayoutManager(this);
        playersListView.setLayoutManager(playersListLayoutManager);

        playersListAdapter = new PlayerAdapter(game.getPlayers());
        playersListView.setAdapter(playersListAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
