package com.example.mymessenger.message;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.example.mymassanger.R;
import com.example.mymessenger.users.UserListFr;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Calendar;


public class MessengerActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btn_send;
    EditText ed_text;
    ArrayList<MessengerItem> list;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    MessRecAdapter adapter;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        btn_send = findViewById(R.id.bnt_send);
        ed_text = findViewById(R.id.mass_ed);
        btn_send.setOnClickListener(this);
        key = getIntent().getStringExtra("key");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("message").child(UserListFr.KEY+key);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.res_view_mass1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessRecAdapter(list);
        recyclerView.setAdapter(adapter);
        updateList();
    }



    @Override
    public void onClick(View v) {
        if (!ed_text.getText().toString().isEmpty()) {
            DatabaseReference myRef = database.getReference("message");
            MessengerItem item = new MessengerItem(ed_text.getText().toString(), UserListFr.name);
            myRef.child(key+ UserListFr.KEY).child(String.valueOf(System.currentTimeMillis())).setValue(item);
            myRef.child(UserListFr.KEY+key).child(String.valueOf(Calendar.getInstance().getTime())).setValue(item);


            DatabaseReference myRef1 = database.getReference("notification");
            myRef1.child(key).setValue("send");
            myRef1.child(key+1).setValue(UserListFr.name);
            myRef1.child(key+2).setValue(ed_text.getText().toString());
            ed_text.setText("");

        } else {
            ed_text.setError("mutqagreq sms");
        }


    }

    public void updateList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        list.add(dataSnapshot.getValue(MessengerItem.class));
                         adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        MessengerItem user1 = dataSnapshot.getValue(MessengerItem.class);
                        int index = getItem(user1);
                        list.set(index, user1);
                        adapter.notifyItemChanged(index);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        MessengerItem user1 = dataSnapshot.getValue(MessengerItem.class);
                        int index = getItem(user1);
                        list.remove(index);
                        adapter.notifyItemRemoved(index);

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).start();

    }

    private int getItem(MessengerItem user) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMessage().equals(user.getMessage())) {
                index = i;
                break;
            }
        }
        return index;
    }

 }
