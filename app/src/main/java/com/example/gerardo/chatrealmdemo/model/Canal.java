package com.example.gerardo.chatrealmdemo.model;

import com.example.gerardo.chatrealmdemo.Application;
import com.example.gerardo.chatrealmdemo.Funciones;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Gerardo on 18/02/2017.
 */

public class Canal extends RealmObject {

    @PrimaryKey
    private long id;
    private RealmList<Usuario> usuarios;
    private RealmList<Mensaje> mensajes;
    private String nombreCanal;

    public Canal() {
    }

    public long getIdCanal() {
        return id;
    }

    public void setIdCanal() {
        this.id = Funciones.crearIdLong();
    }

    public RealmList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(RealmList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getNombreCanal() {
        return nombreCanal;
    }

    public void setNombreCanal(String nombreCanal) {
        this.nombreCanal = nombreCanal;
    }

    public RealmList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(RealmList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    //METODOS

    public static RealmResults<Canal> getCanales(Realm realm){
        return realm.where(Canal.class).findAll();
    }

    public static RealmResults<Mensaje> getMensajesByCanal(Realm realm, long idCanal){
//        Canal canal = realm.where(Canal.class).equalTo("id",idCanal)
////                .findAllSorted("fechaEnviado", Sort.ASCENDING)
//                .findFirst();

        RealmResults<Mensaje> mensajes = realm.where(Mensaje.class).equalTo("idCanal",idCanal)
                .findAll();

        return mensajes;
    }

}
