package cz.greapp.sportmateslite.Data.Adapters;

import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cz.greapp.sportmateslite.Data.Models.Message;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messagesList;
    private User user;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView senderNameText;
        public TextView dateSentText;
        public TextView messageText;

        public TextView mySenderNameText;
        public TextView myDateSentText;
        public TextView myMessageText;

        public ConstraintLayout left;
        public ConstraintLayout right;

        public MessageViewHolder(ConstraintLayout v) {
            super(v);
            senderNameText = v.findViewById(R.id.senderName);
            dateSentText = v.findViewById(R.id.dateSent);
            messageText = v.findViewById(R.id.messageText);

            mySenderNameText = v.findViewById(R.id.mySenderName);
            myDateSentText = v.findViewById(R.id.myDateSent);
            myMessageText = v.findViewById(R.id.myMessageText);

            left = v.findViewById(R.id.messageLayout);
            right = v.findViewById(R.id.myMessageLayout);
        }
    }

    public MessageAdapter(List<Message> messages, User user) {
        messagesList = messages;
        this.user = user;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);

        MessageViewHolder vh = new MessageViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messagesList.get(position);
       /* holder.sportText.setText(message.getSport().getName());
        holder.placeText.setText(message.getPlace());
        holder.dateTimeText.setText(message.getDate() + " | od " + message.getTimeFrom() + " | do " + message.getTimeTo());*/
       if (message.getSender() != null) {
           holder.senderNameText.setText(message.getSender().getName());
       }
       else {
           holder.senderNameText.setText(message.getSenderName());
           holder.mySenderNameText.setText(message.getSenderName());
           if (holder.senderNameText.getText().equals(user.getName())) {
               holder.left.setVisibility(View.GONE);
           }
           else {
                holder.right.setVisibility(View.GONE);
           }
       }
       holder.dateSentText.setText(message.getDateTime());
       holder.myDateSentText.setText(message.getDateTime());
       holder.messageText.setText(message.getText());
       holder.myMessageText.setText(message.getText());

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

}

