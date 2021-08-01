package com.example.establishmentandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Login_Form extends AppCompatActivity {
    EditText regName,regEmail,regMobile, regPassword,loginEmail,loginPassword;
    Button BtnRegister;
    ImageView btnProceed;
    ImageView  btnSignup;
    FirebaseAuth fAuth;
    TextView tv_forgot_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        loginEmail=(EditText)findViewById(R.id.LoginEmail);
        loginPassword=(EditText)findViewById(R.id.LoginPassword);
        regName = (EditText) findViewById(R.id.reg_name);
        regEmail = (EditText) findViewById(R.id.reg_email);
        regMobile = (EditText) findViewById(R.id.reg_mobile);
        regPassword = (EditText) findViewById(R.id.reg_password);
        BtnRegister = (Button) findViewById(R.id.btnRegister);
        btnProceed = (ImageView) findViewById(R.id.btnProceed);
        btnSignup = (ImageView) findViewById(R.id.btnSignup);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        fAuth = FirebaseAuth.getInstance();


        btnProceed.setOnClickListener(view -> {
            loginUser();
        });

        btnSignup.setOnClickListener(view -> {
            startActivity(new Intent(Login_Form.this, Signup.class));
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Password Reset");
                passwordResetDialog.setMessage("Enter your email to recieve a reset password link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login_Form.this,"Reset link sent to your email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(Login_Form.this,"Email is not registered0"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwordResetDialog.create().show();
            }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void loginUser() {


                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    loginEmail.setError("Email is Required.");
                    loginEmail.requestFocus();
                }

                if (TextUtils.isEmpty(email)){
                    loginPassword.setError("Password is Required.");
                    loginPassword.requestFocus();
                }

                if (password.length() < 6 ){
                    loginPassword.setError("Password is must greater than 6 Characters.");
                    return;
                }else{

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login_Form.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else {
                            Toast.makeText(Login_Form.this,"Log in Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }


    }