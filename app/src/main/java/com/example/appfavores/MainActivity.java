package com.example.appfavores;

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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private Button btnEntrar, btnIrCadastro, btnEsqueciSenha;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnIrCadastro = findViewById(R.id.btnIrCadastro);
        btnEsqueciSenha = findViewById(R.id.btnEsqueciSenha); // ← botão que você adicionou no XML

        btnEntrar.setOnClickListener(v -> {
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

            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, TelaPrincipal.class));
                            finish();
                        } else {
                            String mensagem = "Erro desconhecido";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                mensagem = "A senha deve ter pelo menos 6 caracteres.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                mensagem = "E-mail ou senha inválidos.";
                            } catch (FirebaseAuthUserCollisionException e) {
                                mensagem = "Este e-mail já está cadastrado.";
                            } catch (Exception e) {
                                mensagem = "Erro: " + e.getMessage();
                            }

                            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnIrCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TelaCadastro.class);
            startActivity(intent);
        });

        btnEsqueciSenha.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu e-mail para redefinir a senha", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_LONG).show();
                        } else {
                            String erro = "Erro ao enviar e-mail.";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "E-mail inválido.";
                            } catch (Exception e) {
                                erro = "Erro: " + e.getMessage();
                            }
                            Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, TelaPrincipal.class));
            finish();
        }
    }
}
