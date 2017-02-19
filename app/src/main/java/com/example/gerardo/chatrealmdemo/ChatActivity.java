package com.example.gerardo.chatrealmdemo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.gerardo.chatrealmdemo.adapter.ChatAdapter;
import com.example.gerardo.chatrealmdemo.model.Canal;
import com.example.gerardo.chatrealmdemo.model.Mensaje;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_recyclerview)
    RecyclerView recyclerViewChat;
    @BindView(R.id.edit_message)
    EditText editMessage;
    @BindView(R.id.btn_send)
    ImageButton btnSend;

    Realm realm;
    int idCanal;
    SharedPreferences prefs;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        Bundle b = getIntent().getExtras();
        idCanal = 0;
        if (b.getInt("id_canal") != 0){
            idCanal = b.getInt("id_canal");
        }
        prefs = getSharedPreferences("prefs_chat_realm", MODE_PRIVATE);
        getSupportActionBar().setTitle("Canal "+idCanal);
        setupRecyclerView(prefs.getInt("id_username",0),idCanal);

    }

    private void setupRecyclerView(int idUser, int idCanal){

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(false);
        recyclerViewChat.setLayoutManager(lm);

        adapter = new ChatAdapter(this,idUser);
        recyclerViewChat.setAdapter(adapter);

        adapter.addAllMessages(Canal.getMensajesByCanal(realm,idCanal));
    }

    @OnClick(R.id.btn_send)
    public void btnSendMessage(){
        if (editMessage.getText().length() != 0){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setIdMensaje();
                    mensaje.setContenidoMensaje(editMessage.getText().toString());
                    mensaje.setIdUsuario(prefs.getInt("id_username",0));

                    realm.copyToRealm(mensaje);

                    Canal canal = realm.where(Canal.class).equalTo("id",idCanal).findFirst();
                    canal.getMensajes().add(mensaje);

                    editMessage.setText("");
                    adapter.notifyDataSetChanged();
                    recyclerViewChat.scrollToPosition(adapter.getItemCount()-1);
                }
            });
        }

    }


}
