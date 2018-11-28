package cz.greapp.sportmateslite;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cz.greapp.sportmateslite.Data.Adapters.PlayerAdapter;
import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.Player;
import cz.greapp.sportmateslite.Listeners.RecyclerItemClickListener;

public class GameActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView sportText;
    TextView placeText;
    TextView dateTimeText;
    FloatingActionButton joinFab;
    RecyclerView playersListView;
    RecyclerView.Adapter playersListAdapter;
    RecyclerView.LayoutManager playersListLayoutManager;
    Game game;
    Context ctx;
    Activity activity;

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

        playersListView.addOnItemTouchListener(
                new RecyclerItemClickListener(ctx, playersListView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        ShareCompat.IntentBuilder.from(activity)
                                .setType("message/rfc822")
                                .addEmailTo(game.getPlayers().get(position).getEmail())
                                .setSubject("Zpráva z aplikace Sportmates")
                                .setText(game.getSport().getName() + " - " + game.getPlace() + " - " + game.getDate() + " " + game.getTimeFrom() + "-" + game.getTimeTo() + "\n\n" + "Text zprávy: ")
                                .setChooserTitle("Vyberte emailového klienta:")
                                .startChooser();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        joinFab = (FloatingActionButton) findViewById(R.id.joinFab);
        joinFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
