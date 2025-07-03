package com.example.appfavores;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CadastroDeFavor extends AppCompatActivity {
    private TextInputEditText editTextTitulo;
    private TextInputEditText editTextDescricao;
    private TextInputEditText editTextRecompensa;
    private TextInputEditText editTextDate;
    private TextInputEditText editTextHora;
    private TextInputLayout inputLayoutDate;
    private TextInputLayout inputLayoutHora;
    private TextInputEditText editTextEndereco;
    private TextInputLayout inputLayoutEndereco;
    private Button btnPostarFavor;
    private Button btnMenuPrincipal;
    private Calendar calendar = Calendar.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_cadastro_de_favor);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicialização de componentes
        inputLayoutDate = findViewById(R.id.inputLayoutDate);
        inputLayoutHora = findViewById(R.id.inputLayoutHora);
        editTextDate = findViewById(R.id.editTextDate);
        editTextHora = findViewById(R.id.editTextHora);
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextRecompensa = findViewById(R.id.editTextRecompensa);
        btnPostarFavor = findViewById(R.id.btnPostarFavor);
        //btnMenuPrincipal = findViewById(R.id.btnMenuPrincipal);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        inputLayoutEndereco = findViewById(R.id.layoutEndereco);

        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextHora.setOnClickListener(v -> showTimePicker());

        btnPostarFavor.setOnClickListener(v -> {
            if (validarCampos()) {
                salvarFavorNoFirestore();
            }
        });
    }

    private void salvarFavorNoFirestore() {
        String titulo = editTextTitulo.getText().toString().trim();
        String descricao = editTextDescricao.getText().toString().trim();
        String recompensa = editTextRecompensa.getText().toString().trim();
        String data = editTextDate.getText().toString().trim();
        String hora = editTextHora.getText().toString().trim();
        String endereco = editTextEndereco.getText().toString().trim();

        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        Map<String, Object> favor = new HashMap<>();
        favor.put("titulo", titulo);
        favor.put("descricao", descricao);
        favor.put("recompensa", recompensa);
        favor.put("data", data);
        favor.put("hora", hora);
        favor.put("endereco", endereco);
        favor.put("idCriador", uid);
        favor.put("idExecutor", null);
        favor.put("status", "pendente");

        db.collection("favores")
                .add(favor)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Favor cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE", "Erro ao cadastrar favor", e);
                    Toast.makeText(this, "Erro ao cadastrar favor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    inputLayoutDate.setError(null);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    editTextHora.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    inputLayoutHora.setError(null);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePicker.show();
    }

    private boolean validarCampos() {
        boolean valido = true;

        if (editTextTitulo.getText().toString().trim().isEmpty()) {
            ((TextInputLayout) editTextTitulo.getParent().getParent()).setError("Título obrigatório");
            valido = false;
        }

        if (editTextDescricao.getText().toString().trim().isEmpty()) {
            ((TextInputLayout) editTextDescricao.getParent().getParent()).setError("Descrição obrigatória");
            valido = false;
        }

        if (editTextDate.getText().toString().isEmpty()) {
            inputLayoutDate.setError("Data obrigatória");
            valido = false;
        }

        if (editTextHora.getText().toString().isEmpty()) {
            inputLayoutHora.setError("Horário obrigatório");
            valido = false;
        }

        if (calendar.getTime().before(new Date())) {
            inputLayoutDate.setError("Data deve ser futura");
            inputLayoutHora.setError("Horário deve ser futuro");
            valido = false;
        }
        if (editTextEndereco.getText().toString().trim().isEmpty()) {
            inputLayoutEndereco.setError("Endereço obrigatório");
            valido = false;
        }

        return valido;
    }
}