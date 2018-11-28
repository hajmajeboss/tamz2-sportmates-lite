package cz.greapp.sportmateslite.Data.Parsers;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.Sport;
import cz.greapp.sportmateslite.Data.Models.User;

public class GameSnapshotParser {

    public List<Game> parseQuerySnapshot(QuerySnapshot snapshot) {
        List<Game> games = new ArrayList<>();

        for (DocumentSnapshot snap : snapshot.getDocuments()) {
            Game game = parseDocumentSnapshot(snap);
            games.add(game);
        }
        return games;
    }

    public Game parseDocumentSnapshot (DocumentSnapshot snapshot) {
        Game game = new Game();
        game.setDate(snapshot.getString("date"));
        game.setPlace(snapshot.getString("place"));
        game.setTimeFrom(snapshot.getString("time_from"));
        game.setTimeTo(snapshot.getString("time_to"));
        List<User> players = new ArrayList<>();
        User play1 = new User(snapshot.getString("player1_name"), snapshot.getString("player1_email"));
        players.add(play1);

        if (!(((Object)snapshot.getString("player2_email")) == null)) {
            User play2 = new User(snapshot.getString("player2_name"), snapshot.getString("player2_email"));
            players.add(play2);
        }
        game.setPlayers(players);
        SportIdParser sportParser = new SportIdParser();
        game.setSport(sportParser.getSportFromKey(snapshot.getString("sport")));
        game.setPlace(snapshot.getString("location"));
        return game;
    }
}
