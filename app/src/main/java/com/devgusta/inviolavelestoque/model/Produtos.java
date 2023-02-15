package com.devgusta.inviolavelestoque.model;

import com.devgusta.inviolavelestoque.backend.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

public class Produtos {
    private String titulo;
    private String id;
    private String valor;
    private String urlImg;
    private String unidades;

    public Produtos() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }

    public void salvarProduto(){
        DatabaseReference ref = FirebaseHelper.getDatabaseReference()
                .child("produtos").child(FirebaseHelper.getIdFirebase())
                .child(this.id);
        ref.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }
}
