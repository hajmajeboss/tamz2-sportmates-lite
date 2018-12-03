package cz.greapp.sportmateslite.Data.Adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.R;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<User> playersList;

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {

        public TextView playerNameText;
        public TextView playerEmailText;
        public PlayerViewHolder(ConstraintLayout v) {
            super(v);
            playerNameText = (TextView) v.findViewById(R.id.senderName);
            playerEmailText = (TextView) v.findViewById(R.id.messageText);
        }
    }

    public PlayerAdapter(List<User> players) {
        playersList = players;
    }

    @Override
    public PlayerAdapter.PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_list_item, parent, false);

        PlayerViewHolder vh = new PlayerViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        User player = playersList.get(position);
        holder.playerNameText.setText(player.getName());
        holder.playerEmailText.setText(player.getEmail());
    }

    @Override
    public int getItemCount() {
        return playersList.size();
    }

}
