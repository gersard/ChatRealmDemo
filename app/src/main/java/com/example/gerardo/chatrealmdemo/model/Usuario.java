package com.example.gerardo.chatrealmdemo.model;

import com.example.gerardo.chatrealmdemo.Application;
import com.example.gerardo.chatrealmdemo.Funciones;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Gerardo on 18/02/2017.
 */
public class Usuario extends RealmObject {

    @PrimaryKey
    private int id;
    private String nombreUsuario;
    private RealmList<Mensaje> mensajes;

    public Usuario() {
    }

    public int getIdUsuario() {
        return id;
    }

    public void setIdUsuario() {
        this.id = (int) Funciones.crearIdLong();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public RealmList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(RealmList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
