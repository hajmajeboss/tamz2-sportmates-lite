package cz.greapp.sportmateslite;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gamesList;

    public static class GameViewHolder extends RecyclerView.ViewHolder {

        public TextView sportText;
        public TextView placeText;
        public TextView dateTimeText;
        public GameViewHolder(ConstraintLayout v) {
            super(v);
            sportText = (TextView) v.findViewById(R.id.sportText);
            placeText = (TextView) v.findViewById(R.id.placeText);
            dateTimeText = (TextView) v.findViewById(R.id.dateTimeText);
        }
    }

    public GameAdapter(List<Game> games) {
        gamesList = games;
    }

    @Override
    public GameAdapter.GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_item, parent, false);

        GameViewHolder vh = new GameViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        Game game = gamesList.get(position);
        holder.sportText.setText(game.getSport().getName());
        holder.placeText.setText(game.getPlace());
        holder.dateTimeText.setText(game.getDate() + " | od " + game.getTimeFrom() + " | do " + game.getTimeTo());
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

}
