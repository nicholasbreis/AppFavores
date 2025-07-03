package com.example.appfavores;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class TelaCadastro extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private Button btnCadastrar, btnVoltar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.editTextEmail);
        edtSenha = findViewById(R.id.editTextSenha);
        btnCadastrar = findViewById(R.id.buttonCadastrar);
        btnVoltar = findViewById(R.id.btnVoltar);

        btnCadastrar.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish(); // volta para tela de login
                        } else {
                            String mensagem = "Erro desconhecido";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                mensagem = "A senha deve ter pelo menos 6 caracteres.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                mensagem = "E-mail inválido.";
                            } catch (FirebaseAuthUserCollisionException e) {
                                mensagem = "Este e-mail já está cadastrado.";
                            } catch (Exception e) {
                                mensagem = "Erro: " + e.getMessage();
                            }

                            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(TelaCadastro.this, MainActivity.class));
            finish();
        });
    }
}
