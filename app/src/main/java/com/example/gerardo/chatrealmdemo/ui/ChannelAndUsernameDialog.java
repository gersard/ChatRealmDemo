package com.example.gerardo.chatrealmdemo.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gerardo.chatrealmdemo.Application;
import com.example.gerardo.chatrealmdemo.Constants;
import com.example.gerardo.chatrealmdemo.Funciones;
import com.example.gerardo.chatrealmdemo.R;
import com.example.gerardo.chatrealmdemo.model.Usuario;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by Gerardo on 19/02/2017.
 */

public class ChannelAndUsernameDialog extends Dialog {

    @BindView(R.id.dialog_edit_username)
    EditText editUsername;
    @BindView(R.id.txt_nombre_usuario)
    TextView txtTitleUsername;
    @BindView(R.id.dialog_btn_cancelar)
    Button btnCancelar;
    @BindView(R.id.dialog_btn_aceptar)
    Button btnAceptar;

    SharedPreferences prefs;
    int dialogType;

    private IupdateRecyclers mIupdateRecyclers;
    MainActivity activity;

    public ChannelAndUsernameDialog(Context context, int dialogType, MainActivity activity) {
        super(context);
        this.dialogType = dialogType;
        this.activity = activity;
        mIupdateRecyclers = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureDialog();
    }

    private void configureDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_channel_and_username);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);

        prefs = getContext().getSharedPreferences("prefs_chat_realm", Context.MODE_PRIVATE);

        if (dialogType == Constants.DIALOG_USERNAME){
            txtTitleUsername.setText("Nombre de usuario");
            editUsername.setHint("Su de usuario");
            if (!prefs.getString("username", "").equals("")) {
                editUsername.setText(prefs.getString("username", ""));
                editUsername.setSelection(editUsername.getText().length());
            }
        }else{
            txtTitleUsername.setText("Crear nuevo canal");
            editUsername.setHint("Nombre del canal");
        }

    }


    @OnClick(R.id.dialog_btn_cancelar)
    public void btnCancelar() {
        dismiss();
    }

    @OnClick(R.id.dialog_btn_aceptar)
    public void btnAceptar() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (dialogType == Constants.DIALOG_USERNAME){
                    Usuario user = new Usuario();
                    user.setIdUsuario();
                    user.setNombreUsuario(editUsername.getText().toString());

                    realm.copyToRealm(user);

                    prefs.edit().putInt("id_username", Application.usuarioId.get()).apply();
                    prefs.edit().putString("username", editUsername.getText().toString()).apply();
                } else if (dialogType == Constants.DIALOG_CHANNEL){
                    Funciones.crearCanal(realm,editUsername.getText().toString());
                    mIupdateRecyclers = (IupdateRecyclers) activity;
                    mIupdateRecyclers.updateRecyclerChannel();
                }

            }
        });

        dismiss();
    }

    public interface IupdateRecyclers{
        void updateRecyclerChannel();
    }

}
