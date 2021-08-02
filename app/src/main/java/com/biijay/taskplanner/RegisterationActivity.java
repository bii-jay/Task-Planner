package com.biijay.taskplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterationActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btn_reg;
    private TextView login_txt;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        email = findViewById(R.id.email_reg);
        pass = findViewById(R.id.password_reg);

        btn_reg = findViewById(R.id.btn_reg);

        login_txt = findViewById(R.id.login_txt);

        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);


        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(),MainActivity.class));

                Intent intent = new Intent(RegisterationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"register is selected",Toast.LENGTH_LONG).show();

                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Required Field..");
                    return;


                } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    email.setError("Invalid Email");
                    return;
                }



                if (TextUtils.isEmpty(mPass)) {
                    pass.setError("Required Field..");
                    return;

                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                    mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                              // startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                                Intent intent = new Intent(RegisterationActivity.this, HomeActivity.class);
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




    }
}
