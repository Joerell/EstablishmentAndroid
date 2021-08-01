package com.example.establishmentandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_User extends AppCompatActivity {
    Button btn_update_info,btn_update_email_pass;
    TextView tv_name,profile_estab_type,profile_estab_contact_no,profile_estab_address,profile_email_address;
    CircleImageView iv_profile_picture;
    ImageView veri_mark;
    FrameLayout profile_email_veification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        btn_update_info = findViewById(R.id.btn_update_information);
        btn_update_email_pass = findViewById(R.id.btn_update_email_password);
        tv_name = findViewById(R.id.tv_name);
        profile_estab_type = findViewById(R.id.profile_estab_type);
        profile_estab_contact_no = findViewById(R.id.profile_estab_contact_no);
        profile_estab_address = findViewById(R.id.profile_estab_address);
        iv_profile_picture = findViewById(R.id.iv_profile_picture);
        profile_email_address = findViewById(R.id.profile_email_address);
        profile_email_veification = findViewById(R.id.profile_email_verification);
        veri_mark = findViewById(R.id.veri_mark);

        FirebaseAuth  fAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = fAuth.getCurrentUser();

        String currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Establishment").child(currentuserId);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       String email = user.getEmail();
        profile_email_address.setText(email);

        if (fUser.isEmailVerified()){
            veri_mark.setImageResource(R.drawable.ic_check);
        }else {
            veri_mark.setImageResource(R.drawable.ic_x_mark);
        }
        //email verification
        profile_email_veification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fUser.sendEmailVerification();
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_User.this);

                builder.setTitle("Email Verification");
                builder.setMessage("Check your email to verify your own email, logging out!.");

                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    Snackbar.make(findViewById(R.id.layout_profile),"Email Verification Has Been Sent To Your Email",Snackbar.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Profile_User.this, Login_Form.class);
                    startActivity(intent);
                    finish();
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //display info
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String Address = (String) snapshot.child("Address").getValue();
                String Name = (String) snapshot.child("Establishment_Name").getValue();
                String Contact_no = (String) snapshot.child("Contact_No").getValue();
                String Business = (String) snapshot.child("Business").getValue();

                tv_name.setText(Name);
                profile_estab_address.setText(Address);
                profile_estab_contact_no.setText(Contact_no);
                profile_estab_type.setText(Business);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        try {
            StorageReference profilepicref = FirebaseStorage.getInstance().getReference("Users/").child(currentuserId).child("Profile/").child("imageprofile");
            File localfile = File.createTempFile("imageprofile", "jpg");
            profilepicref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    iv_profile_picture.setImageBitmap(bitmap);
                }
            });

        } catch (IOException e) {
        }
            btn_update_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Profile_User.this, UpdateProfile.class));

                }
            });

            btn_update_email_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Profile_User.this, UpdateEmailPass.class));
                }
            });
        }
}