package com.app.mintpartners;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText passwordEt;
    EditText emailEt;
    Button loginBtn;
    Button logoutBtn;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEt = findViewById(R.id.email_edit_text);
        passwordEt = findViewById(R.id.password_edit_text);

        mAuth.signOut();

        loginBtn = findViewById(R.id.log_in_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        logoutBtn = findViewById(R.id.log_out_button);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    public void logIn() {

        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Invalid email address or password", Toast.LENGTH_SHORT).show();
            Log.d("logIn", "invalid password or email");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            companyCheck();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid email address or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void companyCheck() {
        String partnerId = mAuth.getUid();
        DocumentReference documentReference;
        if (partnerId != null) {
            documentReference = db.collection("partners").document(partnerId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String companyId = task.getResult().getString("company");
                        Intent intent = new Intent(MainActivity.this, ManageCompany.class);
                        intent.putExtra("COMPANY_ID_EXTRA", companyId);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Et ole vielä Mint partneri, ota yhteyttä!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}



