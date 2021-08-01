package com.example.establishmentandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class UpdateEmailPass extends AppCompatActivity {
    EditText et_update_email,et_old_password,et_new_password;
    Button btn_update_email_pass;
    String email;
    private static final String TAG ="UpdateEmailPass";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email_pass);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_update_email = findViewById(R.id.et_update_email);
        et_old_password = findViewById(R.id.et_old_password);
        et_new_password = findViewById(R.id.et_new_password);
        btn_update_email_pass = findViewById(R.id.btn_update_submit_email_pass);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        et_update_email.setText(email);
        btn_update_email_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_update_email.getText().toString().trim();
                String old_password = et_old_password.getText().toString().trim();
                String new_password = et_new_password.getText().toString().trim();

                if (email.isEmpty()){
                    et_update_email.setError("Email is Required");
                }
                if (old_password.isEmpty()){
                    et_old_password.setError("Update password is required");
                }
                if (new_password.isEmpty()){
                    et_new_password.setError("New password is required");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEmailPass.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to change your email and password?");
                builder.setPositiveButton("Yes",(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "User email address updated.");
                                }
                            }
                        });

                        AuthCredential credential = EmailAuthProvider.getCredential(email,old_password);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    user.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(UpdateEmailPass.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(UpdateEmailPass.this, "Password Successfully Modified", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(UpdateEmailPass.this,Login_Form.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(UpdateEmailPass.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                })).setNegativeButton("No",(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }));
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
}