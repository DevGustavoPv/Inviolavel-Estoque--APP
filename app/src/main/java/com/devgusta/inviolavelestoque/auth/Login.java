package com.devgusta.inviolavelestoque.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devgusta.inviolavelestoque.R;
import com.devgusta.inviolavelestoque.activity.MainActivity;
import com.devgusta.inviolavelestoque.backend.FirebaseHelper;

public class Login extends AppCompatActivity {
    private TextView esqueceu,criaracc;
    private AppCompatButton btnLogin;
    private EditText email,senha;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        onLoad();
    }


    private void onLoad(){
        esqueceu = findViewById(R.id.textEsqueceu);
        criaracc = findViewById(R.id.textCadastrar);
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.loginEmail);
        senha = findViewById(R.id.loginSenha);
        progressBar = findViewById(R.id.progressBar2);
        listenersClick();
        onLogin();
    }

    private void listenersClick(){
        esqueceu.setOnClickListener(v -> {startActivity(new Intent(this,EsqueceuSenha.class));});

        criaracc.setOnClickListener(v -> {startActivity(new Intent(this,CriarConta.class));});
    }

    private void onLogin(){
        btnLogin.setOnClickListener(v ->{
            String uEmail,uSenha;
            uEmail = email.getText().toString().trim();
            uSenha = senha.getText().toString().trim();
            if(!uEmail.isEmpty()){
               if(!uSenha.isEmpty()) {
                   progressBar.setVisibility(View.VISIBLE);
                   btnLogin.setText("Entrando..");
                   btnLogin.setClickable(false);
                   onLogar(uEmail,uSenha);
               }else{
                   senha.setError("Digite uma senha antes.");
                   senha.requestFocus();
               }
            }else{
                email.setError("Digite um email antes.");
                email.requestFocus();
            }
        });
    }

    private void onLogar(String uEmail, String uSenha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(uEmail,uSenha)
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        finish();
                        startActivity(new Intent(this, MainActivity.class));
                    }else{
                        String error = FirebaseHelper.validarDadodos(task.getException().getMessage());
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setText("Entrar");
                        btnLogin.setClickable(true);
                    }
                });

    }


}