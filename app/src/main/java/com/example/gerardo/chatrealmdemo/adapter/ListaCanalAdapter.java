package com.example.gerardo.chatrealmdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gerardo.chatrealmdemo.R;
import com.example.gerardo.chatrealmdemo.model.Canal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by Gerardo on 18/02/2017.
 */

public class ListaCanalAdapter extends RecyclerView.Adapter<ListaCanalAdapter.CanalViewHolder>
        implements View.OnClickListener{

    private Context context;
    private RealmResults<Canal> canales;

    //listener para el evento onclick
    private View.OnClickListener listener;

    public ListaCanalAdapter(Context context) {
        this.context = context;
    }


    @Override
    public CanalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.channel_view_holder,parent,false);
        itemView.setOnClickListener(this);
        return new CanalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CanalViewHolder holder, int position) {
        Canal canal = canales.get(position);

        holder.setNombre(canal.getNombreCanal());
    }

    @Override
    public int getItemCount() {
        return canales.size();
    }

    public void setCanales(RealmResults<Canal> canalesList){
        canales = canalesList;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View view){
        if(listener != null){
            listener.onClick(view);
        }
    }

    public int getCanalId(int pos){
        return canales.get(pos).getIdCanal();
    }

    class CanalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_vw_canal_name)
        TextView nombre;
        @BindView(R.id.txt_vw_canal_count_user)
        TextView countUser;

        public CanalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setNombre(String nombre){
            this.nombre.setText(nombre);
        }

        public void setCountUser(String countUser){
            this.countUser.setText(countUser);
        }

    }
}
