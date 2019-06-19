package com.example.mymessenger.users;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymassanger.R;
import com.example.mymessenger.LoginFr;
import com.example.mymessenger.message.MyService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserListFr extends Fragment {
    public static  String KEY;
    public static  String name;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    List<User> list;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference referans;
    RecViewUserAdapter adapter;



    public UserListFr() {
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        name=bundle.getString("login");
        KEY=bundle.getString("KEY");
        database = FirebaseDatabase.getInstance();
        referans = database.getReference("users");
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rec_view_user);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecViewUserAdapter((ArrayList<User>) list);

        recyclerView.setAdapter(adapter);
        updateLiset();
        setHasOptionsMenu(true);

        Intent intent = new Intent(getContext(), MyService.class);

        Objects.requireNonNull(getContext()).startService(intent);

        return view;
    }
    public void updateLiset() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                referans.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        list.add(dataSnapshot.getValue(User.class));
                        for (int i = 0; i <list.size() ; i++) {
                            if (list.get(i).getKey().equals(UserListFr.KEY)){
                                list.remove(i);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        User user1 = dataSnapshot.getValue(User.class);
                        int index = getitem(user1);
                        list.set(index, user1);
                        adapter.notifyItemChanged(index);

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        User user1 = dataSnapshot.getValue(User.class);
                        int index = getitem(user1);
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

    private int getitem(User user) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals(user.getKey())) {
                index = i;
                break;

            }
        }
        return index;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exit,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("login");
        editor.remove("pass");
        editor.apply();
        fragmentManager =getFragmentManager();
        LoginFr loginFr = new LoginFr();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.LLmain, loginFr);
        fragmentTransaction.commit();
        return super.onOptionsItemSelected(item);
    }

}
