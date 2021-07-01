package com.example.firestore_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registeration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
Button reg;
ImageButton imageButton;
EditText et1,et2,et3,et4,et5;
Spinner s1;
StorageReference storageReference;
FirebaseAuth fAuth;
String[] courses = { "Student", "Company"};
String email,password,details,inst,spin_val,userid;
String disp_name;
String img_url;
String resume = "";
private static final int PICK_IMAGE_REQUEST = 1;
private Uri mImageUri;
FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        storageReference= FirebaseStorage.getInstance().getReference();
        reg=(Button)findViewById(R.id.reg_bt);
        imageButton=(ImageButton)findViewById(R.id.imageButton);
        et1=(EditText)findViewById(R.id.em_reg);
        et2=(EditText)findViewById(R.id.pass_reg);
        et3=(EditText)findViewById(R.id.det_reg);
        et4=(EditText)findViewById(R.id.inst_reg);
        et5=(EditText)findViewById(R.id.disp_Name);
        s1=(Spinner)findViewById(R.id.spin_reg);
        s1.setOnItemSelectedListener(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        List<String> user_type = new ArrayList<String>();
        user_type.add("Student");
        user_type.add("Company");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user_type);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        s1.setAdapter(dataAdapter);

        fAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et1.getText().toString().trim();
                password = et2.getText().toString().trim();
                details=et3.getText().toString().trim();
                inst=et4.getText().toString().trim();
                disp_name=et5.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    et1.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    et2.setError("Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(details)){
                    et3.setError("Details is Required.");
                    return;
                }

                if(TextUtils.isEmpty(inst)){
                    et4.setError("Institute is Required.");
                    return;
                }

                if(TextUtils.isEmpty(disp_name)){
                    et5.setError("Institute is Required.");
                    return;
                }

                if(password.length() < 6){
                    et2.setError("Password Must be >= 6 Characters");
                    return;
                }


                if(mImageUri==null){
                    Toast.makeText(Registeration.this, "PLS UPLOAD AN IMAGE", Toast.LENGTH_SHORT).show();
                }
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            userid=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=firestore.collection("users").document(userid);
                            Map<String,Object> user= new HashMap<>();
                            user.put("details",details);
                            user.put("displayName",disp_name);
                            user.put("email",email);
                            user.put("imageUrl",img_url);
                            user.put("instituteName",inst);
                            user.put("resume",resume);
                            user.put("uid",userid);
                            user.put("userType",spin_val);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Registeration.this, "Succesful", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registeration.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            startstorageprocessimage();
                               startActivity(new Intent(getApplicationContext(),Main_Page.class));
                        }
                            else{
                              //  Toast.makeText(Registeration.this, "Error ", Toast.LENGTH_SHORT).show();
                            }


                    }
                });

                // startActivity(new Intent(MainActivity.this,User_Main_Activity.class));
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void startstorageprocessimage() {

        if(mImageUri==null){
            Toast.makeText(Registeration.this, "PLS UPLOAD AN IMAGE", Toast.LENGTH_SHORT).show();
        }

        final StorageReference reference=storageReference.child("image_files/"+System.currentTimeMillis()+".jpeg");
        reference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(fAuth.getUid());
                        Map<String,Object> user= new HashMap<>();
                        user.put("imageUrl",uri.toString());
                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Registeration.this, "Photo Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Registeration.this, "Photo Updatation Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(mImageUri).into(imageButton);
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spin_val = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + spin_val, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}