package com.example.gerardo.chatrealmdemo.model;

import com.example.gerardo.chatrealmdemo.Application;

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
    private int id;
    private RealmList<Usuario> usuarios;
    private RealmList<Mensaje> mensajes;
    private String nombreCanal;

    public Canal() {
    }

    public int getIdCanal() {
        return id;
    }

    public void setIdCanal() {
        this.id = Application.canalId.incrementAndGet();
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

    public static RealmList<Mensaje> getMensajesByCanal(Realm realm, int idCanal){
        Canal mensajes = realm.where(Canal.class).equalTo("id",idCanal)
//                .findAllSorted("fechaEnviado", Sort.ASCENDING)
                .findFirst();

        return mensajes.getMensajes();
    }

}
