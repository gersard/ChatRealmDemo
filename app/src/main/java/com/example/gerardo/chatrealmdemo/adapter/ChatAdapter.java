package com.example.gerardo.chatrealmdemo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gerardo.chatrealmdemo.Funciones;
import com.example.gerardo.chatrealmdemo.R;
import com.example.gerardo.chatrealmdemo.model.Mensaje;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Gerardo on 19/02/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    Context context;
    RealmList<Mensaje> mensajes;
    int idUser;

    public ChatAdapter(Context context, int idUser) {
        this.context = context;
        idUser = idUser;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.mensaje_view_holder,parent,false);

        return new ChatAdapter.ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        if (mensaje.getIdUsuario() == idUser){
            holder.setGravity(1);
        }else{
            holder.setGravity(2);
        }

        holder.setMessage(mensaje.getContenidoMensaje());
    }

    @Override
    public int getItemCount() {
        if (mensajes != null){
            return mensajes.size();
        }else{
            return 0;
        }

    }

    public void addAllMessages(RealmList<Mensaje> mensajes){
        if (mensajes.size()!=0){
            this.mensajes = mensajes;
            notifyDataSetChanged();
        }
    }



    public class ChatViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.vw_chat_message)
        TextView message;
        @BindView(R.id.cardview_chat)
        CardView container;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setMessage(String message){
            this.message.setText(message);
        }

        public void setGravity(int tipo){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    (int) Funciones.convertDpToPixel(250,context), RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (tipo == 1){
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }else if (tipo == 2){
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            this.container.setLayoutParams(params);
        }

    }
}
