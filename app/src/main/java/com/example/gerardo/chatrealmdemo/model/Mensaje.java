package com.example.gerardo.chatrealmdemo.model;

import com.example.gerardo.chatrealmdemo.Application;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Gerardo on 18/02/2017.
 */
public class Mensaje extends RealmObject {

    @PrimaryKey
    private int id;
    private String contenidoMensaje;
    private Date fechaEnviado;

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

    public Date getFechaEnviado() {
        return fechaEnviado;
    }

    public void setFechaEnviado(Date fechaEnviado) {
        this.fechaEnviado = fechaEnviado;
    }
}
