package com.devgusta.inviolavelestoque.backend;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FirebaseHelper {

    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;

    public static StorageReference getStorageReference(){
        if(storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

    public static String getIdFirebase(){
        return getAuth().getUid();
    }

    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static FirebaseAuth getAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static boolean getAutenticado(){
        return getAuth().getCurrentUser() != null;
    }
    public static String validarDadodos(String error){
        String msg = "";

        if(error.contains("There is no user record corresponding to this identifier")){
            msg = "Este email não corresponde a nenhum válido em nosso sistema";
        }else if(error.contains("The email address is badly formatted.")){
            msg = "Email inválido, verifique se está digitado corretamente";
        }else if(error.contains("The password is invalid or the user does not have a password.")){
            msg = "Ops.. parece que a senha está incorreta.";
        }else if(error.contains("The email address is already in use by another account.")){
            msg = "Este email ja está sendo usado.";
        }else if (error.contains("The given password is invalid.")){
            msg = "Senha muito fraca, mínimo exigido com 6 caracteres.";
        }

       return msg;
    }
}
