package com.example.gerardo.chatrealmdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.gerardo.chatrealmdemo.model.Usuario;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Gerardo on 19/02/2017.
 */

public class UsernameDialog extends Dialog {

    @BindView(R.id.dialog_edit_username)
    EditText editUsername;
    @BindView(R.id.dialog_btn_cancelar)
    Button btnCancelar;
    @BindView(R.id.dialog_btn_aceptar)
    Button btnAceptar;

    SharedPreferences prefs;

    public UsernameDialog(Context context) {
        super(context);
        configureDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = (int) convertDpToPixel(200,getContext());
//        getWindow().setAttributes(lp);

//        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(getContext(),R.color.whiteTransparecy));
//        getWindow().setBackgroundDrawable(colorDrawable);

    }

    private void configureDialog(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_username);
        ButterKnife.bind(this);

        prefs = getContext().getSharedPreferences("prefs_chat_realm",Context.MODE_PRIVATE);

        if (!prefs.getString("username","").equals("")){
            editUsername.setText(prefs.getString("username",""));
            editUsername.setSelection(editUsername.getText().length());
        }





    }




    @OnClick(R.id.dialog_btn_cancelar)
    public void btnCancelar(){
        dismiss();
    }

    @OnClick(R.id.dialog_btn_aceptar)
    public void btnAceptar(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Usuario user = new Usuario();
                user.setIdUsuario();
                user.setNombreUsuario(editUsername.getText().toString());
                prefs.edit().putInt("id_username",user.getIdUsuario()).apply();
            }
        });
        prefs.edit().putString("username",editUsername.getText().toString()).apply();
        dismiss();
    }

}
