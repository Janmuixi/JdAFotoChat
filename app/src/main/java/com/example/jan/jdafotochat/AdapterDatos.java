package com.example.jan.jdafotochat;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {

    ArrayList<String> listDatos;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    HashMap<String, String> users = new HashMap<>();
    Uri uri;

    public AdapterDatos(ArrayList<String> listDatos, Uri uri) {
        this.listDatos = listDatos;
        this.uri = uri;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDatos viewHolderDatos, int i) {
        viewHolderDatos.asignarDatos(listDatos.get(i));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView dato;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            dato = itemView.findViewById(R.id.idDato);

            dato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllUsers();
                    String email = dato.getText().toString();
                    HashMap<String, String> usersMap = getAllUsers();
                    String sendToKey = usersMap.get(email);
                    Log.i("emailesbueno", "-" + sendToKey);
                    if (sendToKey != null) {
                        sendImage(sendToKey, uri);
                    }

                }
            });
        }


        public void asignarDatos(String s) {
            dato.setText(s);
        }
    }

    private void sendImage(String sendTo, Uri uri) {
        Log.i("uridesdesendimage", uri.toString());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference users = db.getReference().child("users");
        DatabaseReference user = users.child(sendTo);
        DatabaseReference imagesFolder = user.child("imgFolder");
        UUID uuid = UUID.randomUUID();
        DatabaseReference temporalFolder = imagesFolder.child(uuid.toString());
        DatabaseReference temporalImg = temporalFolder.child("imgURL");
        temporalImg.setValue(uri.toString());
    }

    private HashMap<String, String> getAllUsers() {

        myRef.child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            users.put(user.getEmail(), snapshot.getKey());
                            Log.i("INSIDEMAP", user.getEmail() + snapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
        return users;
    }
}
