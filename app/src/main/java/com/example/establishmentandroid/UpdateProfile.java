package com.example.establishmentandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner update_business_type;
    String update_establishment_type;
    EditText Update_Name,Update_Address,Update_Contact_no;
    TextView tv_update_profile_picture;
    CircleImageView iv_update_profile_picture;
    Button btn_update_submit_info;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        update_business_type = findViewById(R.id.spn_update_business_type);
        Update_Name = findViewById(R.id.et_update_name);
        Update_Address = findViewById(R.id.et_update_address);
        Update_Contact_no = findViewById(R.id.et_update_contactnumber);
        iv_update_profile_picture = findViewById(R.id.iv_update_profile_picture);
        tv_update_profile_picture = findViewById(R.id.tv_update_upload_profile_pic);
        btn_update_submit_info = findViewById(R.id.btn_update_submit_info);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.business_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_business_type.setAdapter(adapter);
        update_business_type.setOnItemSelectedListener(this);

        String currentuserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Establishment").child(currentuserId);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String Address = (String) snapshot.child("Address").getValue();
                String Name = (String) snapshot.child("Establishment_Name").getValue();
                String Contact_no = (String) snapshot.child("Contact_No").getValue();
                String Business = (String) snapshot.child("Business").getValue();

                Update_Address.setText(Address);
                Update_Name.setText(Name);
                Update_Contact_no.setText(Contact_no);
                if (Business.equals("Service Business")){
                update_business_type.setSelection(0);
                }else if (Business.equals("Merchandising Business")){
                    update_business_type.setSelection(1);
                }else if (Business.equals("Manufacturing Business")){
                    update_business_type.setSelection(2);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Update Profile
            btn_update_submit_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String estab_name = Update_Name.getText().toString().trim();
                    String estab_address = Update_Address.getText().toString().trim();
                    String estab_contact_no = Update_Contact_no.getText().toString().trim();


                    HashMap hashMap =new HashMap();
                    hashMap.put("Establishment_Name",estab_name);
                    hashMap.put("Address",estab_address);
                    hashMap.put("Contact_No",estab_contact_no);
                    hashMap.put("Business",update_establishment_type);
                    reff.updateChildren(hashMap);
                    startActivity(new Intent(UpdateProfile.this, Profile_User.class));
                }
            });


        // Upload ProfilePic
        tv_update_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }
    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
        iv_update_profile_picture.setImageURI(imageUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode ==RESULT_OK && data != null && data.getData()!=null){
            imageUri = data.getData();
            uploadPicture();

        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(UpdateProfile.this);
        pd.setTitle("Uploading Image...");
        pd.show();
        final String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference riversRef = storageReference.child("Users").child(Uid).child("Profile").child("imageprofile");

        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(UpdateProfile.this, "Profile Picture Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpdateProfile.this, "Failed To Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                pd.setMessage("Uploading...");
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        update_establishment_type = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}