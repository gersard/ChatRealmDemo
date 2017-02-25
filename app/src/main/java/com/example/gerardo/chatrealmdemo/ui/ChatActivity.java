package com.example.gerardo.chatrealmdemo.ui;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.gerardo.chatrealmdemo.Constants;
import com.example.gerardo.chatrealmdemo.Funciones;
import com.example.gerardo.chatrealmdemo.R;
import com.example.gerardo.chatrealmdemo.adapter.ChatAdapter;
import com.example.gerardo.chatrealmdemo.model.Canal;
import com.example.gerardo.chatrealmdemo.model.Mensaje;
import com.example.gerardo.chatrealmdemo.ui.custom.RecyclerViewWithEmptyViewSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_recyclerview)
    RecyclerViewWithEmptyViewSupport recyclerViewChat;
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
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        getSupportActionBar().setTitle("Canal "+idCanal);
        setupRecyclerView(prefs.getInt("id_username",0),idCanal);

    }

    private void setupRecyclerView(int idUser, int idCanal){

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(false);
        recyclerViewChat.setLayoutManager(lm);
        recyclerViewChat.setEmptyView(findViewById(R.id.txt_empty_view_chat));


        RealmList<Mensaje> messages = Canal.getMensajesByCanal(realm,idCanal);
        adapter = new ChatAdapter(this,idUser,messages);
        recyclerViewChat.setAdapter(adapter);
        if (messages != null && messages.size() != 0){
            recyclerViewChat.scrollToPosition(adapter.getItemCount()-1);
            scrollRecyclerViewWhenKeyboardAppears(recyclerViewChat);
        }

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
                    mensaje.setFechaEnviado(Funciones.getCurrentHour());

                    realm.copyToRealm(mensaje);

                    Canal canal = realm.where(Canal.class).equalTo("id",idCanal).findFirst();
                    canal.getMensajes().add(mensaje);

                    editMessage.setText("");
//                    adapter.notifyDataSetChanged();

                    if (adapter.getItemCount() == 1){
                        updateAdapter();
                    }

                    adapter.notifyItemInserted(adapter.getItemCount());

                    recyclerViewChat.scrollToPosition(adapter.getItemCount()-1);
                }
            });
        }

    }

    private void updateAdapter(){
        RealmList<Mensaje> messages = Canal.getMensajesByCanal(realm,idCanal);
        adapter = new ChatAdapter(this,prefs.getInt("id_username",0),messages);
        recyclerViewChat.setAdapter(adapter);
    }

    private void scrollRecyclerViewWhenKeyboardAppears(final RecyclerView recyclerView){

        if (Build.VERSION.SDK_INT >= 11) {
            recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v,
                                           int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(
                                        recyclerView.getAdapter().getItemCount() - 1);
                            }
                        }, 100);
                    }
                }
            });
        }
    }

}
