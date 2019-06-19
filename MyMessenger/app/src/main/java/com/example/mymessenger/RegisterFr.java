package com.example.mymessenger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymassanger.R;
import com.example.mymessenger.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RegisterFr extends Fragment {

    EditText name, surName, mail,  password, repassword;
    Button btn_registracion;
    private FirebaseAuth mAuth;

    public RegisterFr() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        findId(view);
        mAuth = FirebaseAuth.getInstance();

        btn_registracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                boolean namebool = true;
                boolean surNamebool = true;
                boolean mailbool = true;
                boolean passbool = true;
                boolean repasswordbool = true;
                if (name.getText().toString().length() < 3) {
                    name.setError(getString(R.string.Invalide_name));
                    namebool = false;
                }
                if (surName.getText().toString().length() < 5) {
                    surName.setError(getString(R.string.invalide_surname));
                    surNamebool = false;
                }
                if (mail.getText().toString().length() < 5 || !mail.getText().toString().substring(2).contains("@")) {
                    mail.setError(getString(R.string.Invalide_mail));
                    mailbool = false;
                }

                if (!passwordUppercase(password.getText().toString()) || !passwordToLowercase(password.getText().toString()) ||
                        !paswordNumbers(password.getText().toString())) {
                    password.setError(getString(R.string.invalide_pass));
                    passbool = false;
                }
                if (!repassword.getText().toString().equals(password.getText().toString())) {
                    repassword.setError(getString(R.string.invalide_repass));
                    repasswordbool = false;
                }
                if ((namebool && surNamebool && mailbool &&  passbool && repasswordbool)) {


                    registracion(mail.getText().toString(), password.getText().toString());

                }
            }
        });

        return view;
    }


    private void findId(View view) {
        name = view.findViewById(R.id.name);
        surName = view.findViewById(R.id.surname);
        mail = view.findViewById(R.id.mail);

        password = view.findViewById(R.id.password);
        repassword = view.findViewById(R.id.repassword);
        btn_registracion = view.findViewById(R.id.btn_reg_fr_registr);
    }


    public boolean passwordUppercase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (password.contains(password.substring(i, i + 1).toUpperCase())) {
                return !upperCaseLowerCase(password, i);
            }
        }
        return false;

    }

    public boolean passwordToLowercase(String password) {
        for (int i = 0; i < (password.length()); i++) {
            if (password.contains(password.substring(i, i + 1).toLowerCase())) {
                if (upperCaseLowerCase(password, i))
                    return true;
            }
        }
        return false;
    }


    public boolean paswordNumbers(String passwrord) {
        for (int i = 0; i < 10; i++) {
            if (passwrord.contains(String.valueOf(i))) {
                return true;
            }

        }
        return false;
    }

    private boolean upperCaseLowerCase(String password, int i) {
        for (int j = 0; j < 10; j++) {
            if (password.substring(i, i + 1).contains(String.valueOf(j))) {
                return true;
            }
        }
        return false;
    }

    public void registracion(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            assert user != null;
                            User user1 = new User(name.getText().toString(), surName.getText().toString(), user.getUid());
                            myRef.child(user.getUid()).setValue(user1);
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            LoginFr loginFr = new LoginFr();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.LLmain, loginFr);
                            fragmentTransaction.commit();


                        } else {

                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

}