package com.devgusta.inviolavelestoque.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devgusta.inviolavelestoque.R;
import com.devgusta.inviolavelestoque.activity.MainActivity;
import com.devgusta.inviolavelestoque.backend.FirebaseHelper;
import com.devgusta.inviolavelestoque.model.Users;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class CriarConta extends AppCompatActivity {
    private ImageButton btnVoltar;
    private AppCompatButton btnCadastar;
    private EditText email,senha,nome;
    private String urlPerfil;
    private Users users;
    private ImageView perfil;
    private static final int REQUEST_GALERIA = 100;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        onLoad();
        eventPerfil();
    }

    private void onLoad() {
        btnVoltar = findViewById(R.id.btnVoltar);
        email = findViewById(R.id.editEmail);
        senha = findViewById(R.id.editSenha);
        nome = findViewById(R.id.editNome);
        btnCadastar = findViewById(R.id.btnCadastrar);
        perfil = findViewById(R.id.imgPerfil);
        progressBar = findViewById(R.id.progressBar2);
        listenersClick();

    }

    private void listenersClick(){
        btnVoltar.setOnClickListener(v -> finish());

        btnCadastar.setOnClickListener(v->{
            String uNome,uEmail,uSenha;
            uNome = nome.getText().toString().trim();
            uSenha = senha.getText().toString().trim();
            uEmail = email.getText().toString().trim();

            if(!uNome.isEmpty()){
                if(!uEmail.isEmpty()){
                    if(!uEmail.contains("@")){
                        email.setError("Ops.. Tipo de email inválido");
                        email.requestFocus();

                    }else{
                       if(!uSenha.isEmpty()){
                              if(urlPerfil == null) {
                                  progressBar.setVisibility(View.VISIBLE);
                                  btnCadastar.setText("Entrando..");
                                  btnCadastar.setClickable(false);
                                  if (users == null) users = new Users();
                                  users.setNome(uNome);
                                  users.setEmail(uEmail);
                                  users.setSenha(uSenha);
                                  onCadastrar(users);
                              }else{
                                  progressBar.setVisibility(View.VISIBLE);
                                  btnCadastar.setText("Entrando..");
                                  btnCadastar.setClickable(false);
                                  if (users == null) users = new Users();
                                  users.setNome(uNome);
                                  users.setEmail(uEmail);
                                  users.setSenha(uSenha);
                                  onCadastrar(users);
                                  salvandoImg();
                              }

                       }else{
                           senha.setError("Digite uma senha antes.");
                           senha.requestFocus();
                       }
                    }
                }else{
                    email.setError("Digite um email antes.");
                    email.requestFocus();
                }
            }else{
                nome.setError("Digite um nome antes.");
                nome.requestFocus();
            }
        });

    }
    private void onCadastrar(Users users){
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(users.getEmail(),users.getSenha())
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        String id = task.getResult().getUser().getUid();
                        users.setId(id);
                        users.salvarUser();
                        finish();
                        startActivity(new Intent(this, MainActivity.class));
                    }else{
                        String error = FirebaseHelper.validarDadodos(task.getException().getMessage());
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        btnCadastar.setText("Cadastrar");
                        btnCadastar.setClickable(true);
                    }
                });
    }

    private void eventPerfil(){
        perfil.setOnClickListener(v->{
            PermissionListener listener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    gerandoIntent();
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(CriarConta.this, "Permissao foi negada.", Toast.LENGTH_LONG).show();
                }
            };
            gerandoAlertDialog(listener, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE});

        });
    }

    private void gerandoIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_GALERIA);

    }
    private void gerandoAlertDialog(PermissionListener listener, String[] permission){
        TedPermission.create().setPermissionListener(listener)
                .setDeniedTitle("Permissão Negada.")
                .setDeniedMessage("O acesso à galeria foi negada, deseja permitir?")
                .setDeniedCloseButtonText("Não").setGotoSettingButtonText("Sim")
                .setPermissions(permission).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_GALERIA){
               Uri local = data.getData();
               Bitmap bitmap;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),local);
                    perfil.setImageBitmap(bitmap);
                    urlPerfil = local.toString();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void salvandoImg(){
        StorageReference ref = FirebaseHelper.getStorageReference()
                .child("imagens").child("perfil")
                .child(FirebaseHelper.getIdFirebase()+ ".jpeg");


        UploadTask uploadTask = ref.putFile(Uri.parse(urlPerfil));
        uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task ->{
            String url = task.getResult().toString();
            users.setUrlImg(url);
            users.salvarUser();


        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());




    }
}