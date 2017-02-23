package com.example.gerardo.chatrealmdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gerardo.chatrealmdemo.Constants;
import com.example.gerardo.chatrealmdemo.R;
import com.example.gerardo.chatrealmdemo.model.Mensaje;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by Gerardo on 19/02/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    Context context;
    RealmList<Mensaje> mensajes;
    private int idUser;
    private String lastDate;
    private boolean lastMessageWasMe;

    public ChatAdapter(Context context, int idUser) {
        this.context = context;
        this.idUser = idUser;
        lastDate = "";
        lastMessageWasMe = false;
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = mensajes.get(position);

        if (mensaje.getIdUsuario() == idUser){
            return Constants.MESSAGE_OUTCOMING;
        }else{
            return Constants.MESSAGE_INCOMING;
        }
    }


    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case Constants.MESSAGE_OUTCOMING:
                itemView = LayoutInflater.from(context).inflate(R.layout.message_outcoming_viewholder,parent,false);
                break;
            case Constants.MESSAGE_INCOMING:
                itemView = LayoutInflater.from(context).inflate(R.layout.message_incoming_viewholder,parent,false);
                break;
        }

        return new ChatAdapter.ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        holder.setMessage(mensaje.getContenidoMensaje());

        String timeMessage = mensaje.getFechaEnviado();
        holder.setHora(timeMessage);

        switch (holder.getItemViewType()){
            case Constants.MESSAGE_OUTCOMING:
                if (lastMessageWasMe){
                    if (lastDate.equals(timeMessage)){
                        holder.hideDate();
                    }else{
                        holder.showDate();
                    }
                }else{
                    holder.showDate();
                }
                lastMessageWasMe = true;
                break;
            case Constants.MESSAGE_INCOMING:
                if (!lastMessageWasMe){
                    if (lastDate.equals(timeMessage)){
                        holder.hideDate();
                    }else{
                        holder.showDate();
                    }
                }else{
                    holder.showDate();
                }
                lastMessageWasMe = false;
                break;
        }

        lastDate = timeMessage;
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
        this.mensajes = mensajes;
        if (mensajes.size()!=0){
            notifyDataSetChanged();
        }
    }



    public class ChatViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.vw_chat_message)
        TextView message;
        @BindView(R.id.cardview_chat)
        LinearLayout container;
        @BindView(R.id.vw_chat_fecha)
        TextView txtHora;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setMessage(String message){
            this.message.setText(message);
        }

        public void setHora(String hora){
            txtHora.setText(hora);
        }

        public void hideDate(){
            txtHora.setVisibility(View.GONE);
        }
        public void showDate(){
            txtHora.setVisibility(View.VISIBLE);
        }

    }
}
