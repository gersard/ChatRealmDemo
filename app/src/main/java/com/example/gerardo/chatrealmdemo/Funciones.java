package com.example.gerardo.chatrealmdemo;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.gerardo.chatrealmdemo.model.Canal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Gerardo on 18/02/2017.
 */

public class Funciones {



    public static void crearCanales(Realm realm){
        RealmResults<Canal> canales = realm.where(Canal.class).findAll();

        if (canales.size() == 0){
            for (int i = 1; i <= 6 ; i++) {
                final int finalI = i;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Canal canal = new Canal();
                        canal.setIdCanal();
                        canal.setNombreCanal("Canal "+ finalI);
                        realm.copyToRealm(canal);
                    }
                });
            }
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


}
