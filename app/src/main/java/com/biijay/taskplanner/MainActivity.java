package com.biijay.taskplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private TextView signup;

    private EditText loginEmail;
    private EditText loginPass;
    private Button btn_login;

    //Firebase

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.signup_txt);

        loginEmail = findViewById(R.id.email_login);
        loginPass = findViewById(R.id.password_login);

        btn_login = findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null) {
            //startActivity(new Intent(getApplicationContext(),HomeActivity.class));

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        mDialog = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mEmail = loginEmail.getText().toString().trim();
                String mPass = loginPass.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)) {

                    loginEmail.setError("Required Field..");
                    return;

                }

                if(TextUtils.isEmpty(mPass)) {

                    loginPass.setError("Required Field..");
                    return;

                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                           // startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            mDialog.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();

                        }
                    }
                });

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),RegisterationActivity.class));

                Intent intent = new Intent(MainActivity.this, RegisterationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


    }


}
