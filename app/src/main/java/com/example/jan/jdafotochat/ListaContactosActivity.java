package com.example.jan.jdafotochat;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ListaContactosActivity extends AppCompatActivity {
    ArrayList<String> listEmails;
    RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        Bundle b = getIntent().getExtras();
        String uriStr = b.getString("URI");
        Uri uri = Uri.parse(uriStr);
        Log.i("URIparse", uri.toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        listEmails = new ArrayList<>();
        final AdapterDatos adapter = new AdapterDatos(listEmails, uri);
        recycler.setAdapter(adapter);

        myRef.child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listEmails.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.i("lol", snapshot.getValue().toString());
                            User user = snapshot.getValue(User.class);
                            listEmails.add(user.email);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
