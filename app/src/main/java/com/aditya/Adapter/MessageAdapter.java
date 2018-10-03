package com.aditya.Adapter;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.groupchatting.Layout2;
import com.aditya.groupchatting.R;
import com.firebase.client.utilities.Utilities;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<BeanClass> mMessageList;
    Activity activity;

    public MessageAdapter(Activity activity, ArrayList<BeanClass> beanClasses) {

        mMessageList = beanClasses;
        this.activity =activity;
        Log.e("MessageAdapter", ""+mMessageList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;


        if (i == 1) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_sent, viewGroup, false);

            return new SentMessageHolder(view);
        } else if (i == 2) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate( R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageHolder(view);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar calobj = Calendar.getInstance();


        if (mMessageList!=null &&mMessageList.get( i ).getType().equalsIgnoreCase( "1" ))
        {
            ((SentMessageHolder) viewHolder).nameText.setText( "You");
            ((SentMessageHolder) viewHolder).messageText.setText( mMessageList.get( i).getMessage() );
         ((SentMessageHolder) viewHolder).timeText.setText( mMessageList.get(i).getTime());
        }
        else if (mMessageList!=null &&mMessageList.get( i ).getType().equalsIgnoreCase( "2" )){
            ((ReceivedMessageHolder) viewHolder).messageText.setText( mMessageList.get( i ).getMessage() );
            ((ReceivedMessageHolder) viewHolder).nameText.setText( mMessageList.get( i ).getName() );
            ((ReceivedMessageHolder) viewHolder).timeText.setText(mMessageList.get( i ).getTime());
        }

    }

    @Override
    public int getItemViewType(int position) {
        // UserMessage message = (UserMessage) mMessageList.get( position );
        String name  = mMessageList.get( position ).getName();
        if  (mMessageList!=null &&mMessageList.get( position ).getType().equalsIgnoreCase( "1" )) {
            // If the current user is the sender of the message
            return 1;
        } else {
            // If some other user sent the message
            return 2;
        }
    }


    @Override
    public int getItemCount() {

        return mMessageList.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText,nameText;

        SentMessageHolder(View itemView) {
            super(itemView);
            nameText =(TextView)itemView.findViewById( R.id.text_message_name );
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        }


    }



}


