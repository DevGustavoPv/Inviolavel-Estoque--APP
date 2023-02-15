package com.devgusta.inviolavelestoque.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devgusta.inviolavelestoque.R;
import com.devgusta.inviolavelestoque.adapter.AdapterProdutos;
import com.devgusta.inviolavelestoque.auth.Login;
import com.devgusta.inviolavelestoque.backend.FirebaseHelper;
import com.devgusta.inviolavelestoque.model.Produtos;
import com.devgusta.inviolavelestoque.model.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProdutos.setClick {
    private ImageButton btnLogout;
    private ProgressBar progressBar,progressBar2;
    private  AdapterProdutos adapter;
    private Users users;
    private Produtos produtos;
    private List<Produtos> produtosList = new ArrayList<>();
    private RecyclerView rv;
    private FloatingActionButton floatingActionButton;
    private TextView toolbar_titulo,textSemProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onLoad();
        recuperandoDados();

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar2.setVisibility(View.VISIBLE);
        textSemProduto.setVisibility(View.GONE);
        recuperandoProdutos();
        configReclycle();
    }

    private void configReclycle(){
        rv.setLayoutManager( new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        adapter = new AdapterProdutos(produtosList,this);
        rv.setAdapter(adapter);

    }
    private void onLoad(){
        //loading ids
        btnLogout = findViewById(R.id.btnLogout);
        rv = findViewById(R.id.rv);
        textSemProduto = findViewById(R.id.textSemProduto);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        toolbar_titulo = findViewById(R.id.textView2);
         progressBar = findViewById(R.id.progressLoad);
        progressBar2 = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        //listeners
        onListenersClicks();
    }
    private void onListenersClicks(){
        btnLogout.setOnClickListener(v->{
            FirebaseHelper.getAuth().signOut();
            Toast.makeText(this, "Saindo..", Toast.LENGTH_LONG).show();
          finish();
            startActivity(new Intent(getBaseContext(),Login.class));
        });
        floatingActionButton.setOnClickListener(v-> startActivity(new Intent(this,NovoProduto.class)));
    }
    private void recuperandoProdutos(){
        DatabaseReference ref = FirebaseHelper.getDatabaseReference()
                .child("produtos").child(FirebaseHelper.getIdFirebase());


        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    produtosList.clear();
                    for(DataSnapshot snap : snapshot.getChildren()) {
                        Produtos produtos = snap.getValue(Produtos.class);
                        produtosList.add(produtos);
                        progressBar2.setVisibility(View.GONE);
                        textSemProduto.setVisibility(View.GONE);
                    }
                }else{
                    textSemProduto.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.GONE);
                }
                Collections.reverse(produtosList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void recuperandoDados(){

        DatabaseReference ref = FirebaseHelper.getDatabaseReference()
                .child("usuarios").child(FirebaseHelper.getIdFirebase());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    users = snapshot.getValue(Users.class);
                    toolbar_titulo.setVisibility(View.VISIBLE);
                    toolbar_titulo.setText("Olá, "+users.getNome());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickListener(Produtos produtos) {
        Toast.makeText(this, produtos.getTitulo(), Toast.LENGTH_SHORT).show();
    }
}