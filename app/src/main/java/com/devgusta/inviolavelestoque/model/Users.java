package com.devgusta.inviolavelestoque.model;

import com.devgusta.inviolavelestoque.backend.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Users {

    private String id;
    private String nome;
    private String urlImg;
    private String email;
    private String senha;


    public void salvarUser(){
        DatabaseReference ref = FirebaseHelper.getDatabaseReference()
                .child("usuarios").child(FirebaseHelper.getIdFirebase());
        ref.setValue(this);
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
