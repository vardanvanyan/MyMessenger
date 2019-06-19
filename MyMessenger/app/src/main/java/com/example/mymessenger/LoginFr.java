package com.example.mymessenger;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymassanger.R;
import com.example.mymessenger.users.UserListFr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginFr extends Fragment implements View.OnClickListener {
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    RegisterFr registerFr;
    UserListFr userListFr;
    Button btn_reg;
    Button btn_login;
    EditText login;
    EditText parol;
    CheckBox save;
    SharedPreferences sharedPreferences;

    public LoginFr() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        btn_reg = view.findViewById(R.id.btn_pass);
        btn_login = view.findViewById(R.id.btn_login);
        login = view.findViewById(R.id.et_login);
        parol = view.findViewById(R.id.et_pass);
        save = view.findViewById(R.id.chek_b);
        mAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seveText();
                }
            }
        });


        return view;
    }

    public void singIn(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity())
                        , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Bundle bundle = new Bundle();
                                    assert user != null;
                                    bundle.putString("KEY", user.getUid());
                                    bundle.putString("login", login.getText().toString());
                                    userListFr = new UserListFr();
                                    userListFr.setArguments(bundle);
                                    fragmentManager = getFragmentManager();
                                    assert fragmentManager != null;
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.LLmain, userListFr);
                                    fragmentTransaction.commit();

                                } else {
                                    Toast.makeText(getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    login.setError("invalid");
                                    parol.setError("invalid");

                                }
                            }
                        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                if (login.getText().toString().isEmpty() || parol.getText().toString().isEmpty()) {
                    login.setError("invalid");
                    parol.setError("invalid");
                } else {
                    singIn(login.getText().toString(), parol.getText().toString());

                }
                break;
            }
            case R.id.btn_pass: {
                fragmentManager = getFragmentManager();
                registerFr = new RegisterFr();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.LLmain, registerFr);
                fragmentTransaction.commit();
                break;
            }
        }
    }

    public void seveText() {
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login", login.getText().toString());
        editor.putString("pass", parol.getText().toString());
        editor.apply();
    }
}
