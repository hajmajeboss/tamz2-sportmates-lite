package cz.greapp.sportmateslite.Data.TableGateways;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.Parsers.SportIdParser;
import cz.greapp.sportmateslite.Data.QueryResultObserver;

public class GameTableGateway extends TableGateway {

    public GameTableGateway() {
        super();
    }

    public void getGames(final OnFirebaseQueryResultListener listener, final int requestCode) {
        if (listener != null) {
            QueryResultObserver.getInstance().attachListener(listener);
        }
        db.collection("games").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, requestCode, task.getResult());
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, requestCode, task.getResult());
                }
            }
        });
    }

    public void getUserGames(final OnFirebaseQueryResultListener listener, final int requestCode, User user) {
        if (listener != null) {
            QueryResultObserver.getInstance().attachListener(listener);
        }
        db.collection("games").whereArrayContains("player_emails", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, requestCode, task.getResult());
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, requestCode, task.getResult());
                }
            }
        });
    }

    public void putGame(final OnFirebaseQueryResultListener listener, final int requestCode, Game g) {
        QueryResultObserver.getInstance().attachListener(listener);
        Map<String, Object> game = new HashMap<>();
        game.put("date", g.getDate());
        game.put("time_from", g.getTimeFrom());
        game.put("time_to", g.getTimeTo());
        game.put("location", g.getPlace());
        SportIdParser sportIdParser = new SportIdParser();
        game.put("sport", sportIdParser.getKeyFromSport(g.getSport()));
        game.put("player1_name", g.getPlayers().get(0).getName());
        game.put("player1_email", g.getPlayers().get(0).getEmail());
        List<String> emails = new ArrayList<>();
        emails.add(g.getPlayers().get(0).getEmail());
        game.put("player_emails", emails);

        db.collection("games").add(game).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, requestCode, null);
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, requestCode, null);
                }
            }
        });
    }


}
