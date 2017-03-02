package com.example.gerardo.chatrealmdemo;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

/**
 * Created by assertsoft on 1/3/17.
 */
public class UserManager {



    public static void setActiveUser(SyncUser user){
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user,Application.REALM_URL)
                .build();
        Realm.setDefaultConfiguration(defaultConfig);
    }


}
