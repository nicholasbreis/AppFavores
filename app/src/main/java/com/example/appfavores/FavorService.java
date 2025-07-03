package com.example.appfavores;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FavorService {
    private FirebaseFirestore getDb() {
        return FirebaseFirestore.getInstance();
    }

    public void salvarFavor(Favor favor) {
        getDb().collection("favores")
                .add(favor)
                .addOnSuccessListener(documentReference ->
                        Log.d("DB", "Favor salvo com ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.w("DB", "Erro ao salvar favor", e));
    }

    public void listarFavores(OnCompleteListener<QuerySnapshot> listener) {
        getDb().collection("favores").get().addOnCompleteListener(listener);
    }
}

