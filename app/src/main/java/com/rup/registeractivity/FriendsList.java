package com.rup.registeractivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;


public class FriendsList extends AppCompatActivity {


    private DatabaseReference FriendsRef,PostRef;
    private RecyclerView postList;
    public AlertDialog pd;
    private String online_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        setTitle("Users List");

        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Users");

        postList=(RecyclerView)findViewById(R.id.friendRecycleList);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        postList.setLayoutManager(linearLayoutManager);
        //Showing  Spots Dialog
        pd= new SpotsDialog.Builder()
                .setContext(FriendsList.this).setTheme(R.style.CustomPD).setCancelable(false).build();

        pd.show();

        DisplayPosts();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                //Sign out
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(FriendsList.this, MainActivity.class));
                Toast.makeText(this, "Log out successfully!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void DisplayPosts()
    {
        FirebaseRecyclerAdapter<Friends,FriendsViewHolder>firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                (
                        Friends.class,
                        R.layout.recycle_list_single_user,
                        FriendsViewHolder.class,
                        FriendsRef

                )
        {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends model, int position)
            {
                final String usersIDs = getRef(position).getKey();

                FriendsRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        final String userName= dataSnapshot.child("name").getValue().toString();

                        viewHolder.setName(userName);


                        final String finalUserName = userName;
                       //Dismiss the Dialog
                        pd.dismiss();
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent= new Intent(FriendsList.this,ChatActivity.class);
                                intent.putExtra("user_id",usersIDs);
                                intent.putExtra("user_name", finalUserName);

                                startActivity(intent);
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);


    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setName(String name)
        {
            TextView titlename=(TextView)mView.findViewById(R.id.textViewSingleListName);
            titlename.setText(name);
        }

    }
}
