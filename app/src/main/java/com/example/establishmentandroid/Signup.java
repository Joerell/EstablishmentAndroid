package com.example.establishmentandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText regName, regEmail, regMobile, regPassword,address;
    Button BtnRegister;
    ImageView btnProceed;
    FirebaseAuth fAuth;
    ImageView btnBack;
    TextView mbtnAlready;
    DatabaseReference reference;
    Spinner business_type;
    String establishment_type;
    DatabaseReference rootDatabaseref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        regName = findViewById(R.id.reg_name);
        regEmail =  findViewById(R.id.reg_email);
        regMobile = findViewById(R.id.reg_mobile);
        regPassword = findViewById(R.id.reg_password);
        BtnRegister = findViewById(R.id.btnRegister);
        btnProceed = findViewById(R.id.btnProceed);
        btnBack = findViewById(R.id.btnBack);
        mbtnAlready = findViewById(R.id.btnAlready);
        address = findViewById(R.id.address);
        business_type = findViewById(R.id.spn_business_type);

        rootDatabaseref = FirebaseDatabase.getInstance().getReference().child("Establishment");

        fAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Monitoring");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.business_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        business_type.setAdapter(adapter);
        business_type.setOnItemSelectedListener(this);

        BtnRegister.setOnClickListener(view -> {
            createUser();
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login_Form.class));
            }
        });
        mbtnAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login_Form.class));
            }
        });

    }


    public void createUser() {
        String email = regEmail.getText().toString().trim();
        String password = regPassword.getText().toString().trim();
        String estab_name = regName.getText().toString().trim();
        String estab_mobile = regMobile.getText().toString().trim();
        String estab_address = address.getText().toString().trim();

        String user_role = "2";

        if (TextUtils.isEmpty(estab_name)) {
            regName.setError("Name is Required.");
            regName.requestFocus();
        }
        if (TextUtils.isEmpty(email)) {
            regEmail.setError("Email is Required.");
            regEmail.requestFocus();
        }
        if (TextUtils.isEmpty(estab_mobile)) {
            regMobile.setError("Contact Nuumber is Required.");
            regMobile.requestFocus();
        }
        if (TextUtils.isEmpty(estab_address)) {
            address.setError("Address is Required.");
            address.requestFocus();
        }
        if (TextUtils.isEmpty(email)) {
            regPassword.setError("Password is Required.");
            regPassword.requestFocus();
        }

        if (password.length() < 6) {
            regPassword.setError("Password is must greater than 6 Characters.");
            return;
        } else {

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        HashMap hashMap = new HashMap();
                        hashMap.put("Address",estab_address);
                        hashMap.put("Business",establishment_type);
                        hashMap.put("Contact_No",estab_mobile);
                        hashMap.put("Establishment_Name",estab_name);
                        hashMap.put("User_Role",user_role);
                        String currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        rootDatabaseref.child(currentuserId).updateChildren(hashMap);
                        Toast.makeText(Signup.this, "User Created", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login_Form.class));
                        finish();
                    } else {
                        Toast.makeText(Signup.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        establishment_type = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

