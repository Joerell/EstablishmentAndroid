package com.example.establishmentandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ImageView btnLogout;
    ImageView btnScan;
    FirebaseAuth mAuth;
    CircleImageView Selectprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Selectprofile = findViewById(R.id.Selectprofile);
        btnScan = findViewById(R.id.btnScan);
        btnLogout = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();

        String currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            StorageReference profilepicref = FirebaseStorage.getInstance().getReference("Users/").child(currentuserId).child("Profile/").child("imageprofile");
            File localfile = File.createTempFile("imageprofile","jpg");
            profilepicref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    Selectprofile.setImageBitmap(bitmap);
                }
            });

        }catch (IOException e) {
        }

        Selectprofile.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this,Profile_User.class));
            finish();
        });


        btnLogout.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this,Login_Form.class));
            finish();
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadMainActivity = new Intent(MainActivity.this,Qr_Scanner.class);
                startActivity(intentLoadMainActivity);
                finish();
            }

            public void logout(View view){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login_Form.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainActivity.this,Login_Form.class));
        }
    }
}