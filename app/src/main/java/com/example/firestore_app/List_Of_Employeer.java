package com.example.firestore_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class List_Of_Employeer extends AppCompatActivity {

    ArrayList<emplist> arrayList;
    RecyclerView recview;
    myadapter adapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__of__employeer);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        recview=(RecyclerView)findViewById(R.id.recyclerView);
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));



        db=FirebaseFirestore.getInstance();
        arrayList= new ArrayList<emplist>();

        adapter= new myadapter(List_Of_Employeer.this,arrayList);

        recview.setAdapter(adapter);
        
        EventChangeListener();

        Toast.makeText(this, "Fetching ONLY COMPANY DATA", Toast.LENGTH_SHORT).show();


       // processsearch();

    }

    private void processsearch() {

        ArrayList<emplist>list=new ArrayList<>();
        for (emplist ob : arrayList){

            if (ob.getUserType().equals("Company")){
                list.add(ob);
            }

        }
        myadapter myadapter=new myadapter(this,list);
        recview.setAdapter(myadapter);
    }

    private void EventChangeListener() {

        db.collection("users").whereEqualTo("userType","Company")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error!=null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Toast.makeText(List_Of_Employeer.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){

                            if (dc.getType()==DocumentChange.Type.ADDED){

                                arrayList.add(dc.getDocument().toObject(emplist.class));
                            }

                            adapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                });
    }
}