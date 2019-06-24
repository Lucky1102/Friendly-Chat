package com.rup.registeractivity;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;




public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference ;
    Context context;


    public MessageAdapter(List<Messages> mMessagesList) {
        this.mMessagesList = mMessagesList;
    }



    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout_of_users,parent,false);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        return new MessageViewHolder(view);
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView smessageText;
        public TextView rmessageText;        ;
        public TextView displayName;
        public TextView displayTime;

        public ImageView messageImage;


        public MessageViewHolder(View itemView) {
            super(itemView);

            smessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            rmessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);


        }


    }


    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {


        String messageSenderID = mAuth.getCurrentUser().getUid();
        Messages messages = mMessagesList.get(position);
        String from_user_id = messages.getFrom();
        String message_type = messages.getType();



        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(from_user_id);


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               if(dataSnapshot.exists())
               {
                   String image =dataSnapshot.child("thumb_image").getValue().toString();

               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       if(message_type.equals("text"))
       {


           if (from_user_id.equals(messageSenderID))
           {
               holder.smessageText.setVisibility(View.VISIBLE);

               holder.rmessageText.setVisibility(View.GONE);

              holder.smessageText.setBackgroundResource(R.drawable.sender_messagetext_background);
              holder.smessageText.setTextColor(Color.WHITE);
              holder.smessageText.setGravity(Gravity.LEFT);
              holder.smessageText.setText(messages.getMessage());
           }

           else
           {
               holder.smessageText.setVisibility(View.GONE);
               holder.rmessageText.setVisibility(View.VISIBLE);


               holder.rmessageText.setBackgroundResource(R.drawable.receiver_message_text_background);
               holder.rmessageText.setTextColor(Color.WHITE);
               holder.rmessageText.setGravity(Gravity.LEFT);
               holder.rmessageText.setText(messages.getMessage());
           }
       }



    }


    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }
}
