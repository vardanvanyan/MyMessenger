package com.example.mymessenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mymassanger.R;
import com.example.mymessenger.users.UserListFr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    LoginFr loginFr;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        loadText();


    }

    public void loadText() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        String pass = sharedPreferences.getString("pass", "");
        Log.i("==p", "loadText: " + login + pass);
        assert login != null;
        assert pass != null;
        if (!login.isEmpty() && !pass.isEmpty()) {
            singIn(login, pass);
        } else {
            loginFragment();
        }
    }

    public void singIn(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this
                        , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Bundle bundle = new Bundle();
                                    assert user != null;
                                    bundle.putString("KEY", user.getUid());
                                    bundle.putString("login", email);
                                    UserListFr userListFr = new UserListFr();
                                    userListFr.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.LLmain, userListFr).commit();
                                } else {
                                    loginFragment();


                                }
                            }
                        });
    }

    private void loginFragment() {
        fragmentManager = getSupportFragmentManager();
        loginFr = new LoginFr();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.LLmain, loginFr);
        fragmentTransaction.commit();
    }

}




