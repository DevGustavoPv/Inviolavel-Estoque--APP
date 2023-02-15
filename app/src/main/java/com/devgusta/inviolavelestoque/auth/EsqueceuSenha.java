package com.devgusta.inviolavelestoque.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.devgusta.inviolavelestoque.R;
import com.devgusta.inviolavelestoque.backend.FirebaseHelper;

public class EsqueceuSenha extends AppCompatActivity {
    private AppCompatButton btnEnviar;
    private EditText editEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);
        btnEnviar = findViewById(R.id.btnEsqueceu);
        editEmail = findViewById(R.id.editEmailEsqueceu);
        click();
    }
    private void click(){
        btnEnviar.setOnClickListener(v ->{
            String email = editEmail.getText().toString().trim();
            if(email.isEmpty()){
                editEmail.setError("Digite um email antes.");
                editEmail.requestFocus();
            }else{
                btnEnviar.setText("Enviando..");
                btnEnviar.setClickable(false);
                sendEmail(email);
            }
        });
    }

    private void sendEmail(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(task ->{
                   if(task.isSuccessful()){
                       Toast.makeText(this, "Enviado para: "+email, Toast.LENGTH_LONG).show();
                       finish();
                   }else{
                       btnEnviar.setText("Enviar");
                       btnEnviar.setLinksClickable(true);
                       String error = FirebaseHelper.validarDadodos(task.getException().getMessage());
                       Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                   }
                });
    }
}