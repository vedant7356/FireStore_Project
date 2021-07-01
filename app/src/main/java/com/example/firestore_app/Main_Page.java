package com.example.firestore_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Main_Page extends AppCompatActivity {
    Button lg_out,add_resume,upload_resume,emp_contact,emp_details,up_eve_add;
    Uri filepath;
    StorageReference storageReference;
    String userid;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);
        fAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        firestore=FirebaseFirestore.getInstance();
        lg_out=(Button)findViewById(R.id.Log_Out);
       add_resume=(Button)findViewById(R.id.add_res);
       upload_resume=(Button)findViewById(R.id.add_res2);
        emp_details=(Button)findViewById(R.id.up_eve_details);
        emp_contact=(Button)findViewById(R.id.emp_contact_data);
        up_eve_add=(Button)findViewById(R.id.up_event);
        upload_resume.setEnabled(false);
        up_eve_add.setEnabled(false);

        emp_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Main_Page.this, Up_eve.class));
            }
        });

        up_eve_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Main_Page.this, Up_eve.class));
            }
        });

        emp_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main_Page.this, List_Of_Employeer.class));
            }
        });

        lg_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        
        add_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_pdf();
            }
        });

        upload_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_process();
            }
        });
    }

    private void start_process() {

        final StorageReference reference=storageReference.child("pdf_files/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(fAuth.getUid());
                        Map<String,Object> user= new HashMap<>();
                        user.put("resume",uri.toString());
                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Main_Page.this, "Resume Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Main_Page.this, "Resume Updatation Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }

    private void upload_pdf() {
        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            filepath = data.getData();
            Toast.makeText(this, "PDF Updated", Toast.LENGTH_SHORT).show();
            upload_resume.setEnabled(true);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            upload_resume.setEnabled(false);
        }

    }
}