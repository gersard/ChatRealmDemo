package com.example.gerardo.chatrealmdemo.model;

import com.example.gerardo.chatrealmdemo.Application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Gerardo on 18/02/2017.
 */
public class Mensaje extends RealmObject {

    @PrimaryKey
    private int id;
    private String contenidoMensaje;
    private String fechaEnviado;
    private int idUsuario;

    public Mensaje() {
    }

    public int getIdMensaje() {
        return id;
    }

    public void setIdMensaje() {
        this.id = Application.mensajeId.incrementAndGet();;
    }

    public String getContenidoMensaje() {
        return contenidoMensaje;
    }

    public void setContenidoMensaje(String contenidoMensaje) {
        this.contenidoMensaje = contenidoMensaje;
    }

    public String getFechaEnviado() {
        return fechaEnviado;
    }

    public void setFechaEnviado(String fechaEnviado) {
        this.fechaEnviado = fechaEnviado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
