package com.devgusta.inviolavelestoque.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devgusta.inviolavelestoque.R;
import com.devgusta.inviolavelestoque.backend.FirebaseHelper;
import com.devgusta.inviolavelestoque.model.Produtos;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class NovoProduto extends AppCompatActivity {
    private AppCompatButton btnSalvar;
    private EditText editTitulo,editQtd,editValor;
    private ProgressBar progressBar;
    private ImageView imgProduto;
    private static final int REQUEST_GALERIA = 100;
    private String urlImg;
    private Produtos produtos;
    private TextView textView3;
    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);
        onLoad();
    }


    private void onLoad(){
        editQtd = findViewById(R.id.editQtd);
        editTitulo = findViewById(R.id.editTitulo);
        editValor = findViewById(R.id.editValor);
        btnSalvar = findViewById(R.id.btnSalvar);
        imgProduto = findViewById(R.id.imgProduto);
        progressBar = findViewById(R.id.progressBar3);
        textView3 = findViewById(R.id.textView3);
        btnVoltar = findViewById(R.id.btnVoltar);
        listenersClick();
    }
    private void listenersClick(){
        btnVoltar.setOnClickListener(v-> finish());

        btnSalvar.setOnClickListener(v->{
      String titulo,valor,qtd;
      titulo = editTitulo.getText().toString().trim();
      valor = editValor.getText().toString().trim();
      qtd = editQtd.getText().toString().trim();

      if(!titulo.isEmpty()){
          if(!qtd.isEmpty()){
              if(!valor.isEmpty()){
                  if(urlImg == null){
                      Toast.makeText(this, "Adicione uma imagem antes..", Toast.LENGTH_LONG).show();
                  }else{
                      progressBar.setVisibility(View.VISIBLE);
                      btnSalvar.setText("Salvando...");
                      btnSalvar.setClickable(false);
                     if(produtos == null) produtos = new Produtos();
                      produtos.setTitulo(titulo);
                      produtos.setUnidades(qtd);
                      produtos.setValor(valor);
                      salvarTudo(produtos);
                  }
              }else{
                  editValor.setError("Digite um valor antes.");
                  editValor.requestFocus();
              }
          }else{
              editQtd.setError("Digite uma quantidade em estoque antes.");
              editQtd.requestFocus();
          }
      }else{
          editTitulo.setError("Digite um titulo antes.");
          editTitulo.requestFocus();
      }
        });

        imgProduto.setOnClickListener(v ->{
            PermissionListener permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    gerarIntent();
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(NovoProduto.this, "Acesso negado!", Toast.LENGTH_LONG).show();
                }
            };
            gerarAlert(permissionListener,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE});
        });

    }

    private void gerarIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_GALERIA);
    }
    private void gerarAlert(PermissionListener listener,String[] permission ){
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
                    imgProduto.setImageBitmap(bitmap);
                    urlImg = local.toString();
                    textView3.setVisibility(View.GONE);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private void salvarTudo(Produtos produtos) {
        StorageReference ref = FirebaseHelper.getStorageReference()
                .child("imagens").child("produtos")
                .child(FirebaseHelper.getIdFirebase()).child(urlImg+".jpeg");

        UploadTask uploadTask = ref.putFile(Uri.parse(urlImg));
        uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task ->{
          String url = task.getResult().toString();
          produtos.setUrlImg(url);
          produtos.salvarProduto();
          new Handler(Looper.getMainLooper()).postDelayed(this::finish,2000);


        })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            btnSalvar.setText("Salvar");
            btnSalvar.setClickable(true);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }


}