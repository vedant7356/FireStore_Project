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

public class Up_eve extends AppCompatActivity {
    ArrayList<evemodel> arr;
    RecyclerView recview;
    mynewadapter adapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_eve);


        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        recview=(RecyclerView)findViewById(R.id.recy);
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        arr= new ArrayList<evemodel>();

        adapter= new mynewadapter(Up_eve.this,arr);


        recview.setAdapter(adapter);

        EventChangeListener();

        Toast.makeText(this, "Fetching Upcoming Events", Toast.LENGTH_SHORT).show();
    }

    private void EventChangeListener() {

        db.collection("Upcomming Events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error!=null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Toast.makeText(Up_eve.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){

                            if (dc.getType()==DocumentChange.Type.ADDED){

                                arr.add(dc.getDocument().toObject(evemodel.class));
                            }

                            adapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                });
    }
}