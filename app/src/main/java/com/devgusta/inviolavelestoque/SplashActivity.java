package com.devgusta.inviolavelestoque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.devgusta.inviolavelestoque.activity.MainActivity;
import com.devgusta.inviolavelestoque.auth.Login;
import com.devgusta.inviolavelestoque.backend.FirebaseHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         new Handler(Looper.getMainLooper()).postDelayed(this::verificarLogin,3000);
    }

    private void verificarLogin(){
        if(!FirebaseHelper.getAutenticado()){
            startActivity( new Intent(getApplicationContext(), Login.class));
        }
      startActivity( new Intent(getApplicationContext(), MainActivity.class));


    }
}