package com.example.gerardo.chatrealmdemo;

import com.example.gerardo.chatrealmdemo.model.Canal;
import com.example.gerardo.chatrealmdemo.model.Mensaje;
import com.example.gerardo.chatrealmdemo.model.Usuario;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Gerardo on 18/02/2017.
 */

public class Application extends android.app.Application {

    public static AtomicInteger canalId;
    public static AtomicInteger usuarioId;
    public static AtomicInteger mensajeId;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(0)
//                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        Realm realm = Realm.getDefaultInstance();
        canalId = getIdByTable(realm, Canal.class);
        usuarioId = getIdByTable(realm, Usuario.class);
        mensajeId = getIdByTable(realm, Mensaje.class);
        realm.close();

    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size()>0) ?  new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();

    }

}
