package com.example.gerardo.chatrealmdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gerardo.chatrealmdemo.BuildConfig;
import com.example.gerardo.chatrealmdemo.R;
import com.example.gerardo.chatrealmdemo.model.Canal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Gerardo on 18/02/2017.
 */

public class ListaCanalAdapter extends RecyclerView.Adapter<ListaCanalAdapter.CanalViewHolder>
        implements View.OnClickListener{

    private static final int PENDING_REMOVAL_TIMEOUT = 3000;

    private Context context;
    private RealmResults<Canal> canales;
    private List<Canal> channelPendingRemoval;
    boolean undoOn;
    private Handler handler = new Handler();
    HashMap<Canal, Runnable> pendingRunnables;
    private Realm realm;

    //listener para el evento onclick
    private View.OnClickListener listener;

    public ListaCanalAdapter(Context context, Realm realm) {
        this.context = context;
        channelPendingRemoval = new ArrayList<>();
        undoOn = true;
        this.realm = realm;
        pendingRunnables = new HashMap<>();
    }


    @Override
    public CanalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.channel_view_holder,parent,false);
        itemView.setOnClickListener(this);
        return new CanalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CanalViewHolder holder, int position) {
        final Canal canal = canales.get(position);

        if (channelPendingRemoval.contains(canal)){
            holder.itemView.setBackgroundColor(Color.RED);
            holder.hideNombre();
            holder.hideCountUser();
            holder.showUndoBtn();
            holder.btnUndo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(canal);
                    pendingRunnables.remove(canal);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    channelPendingRemoval.remove(canal);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(canales.indexOf(canal));
                }
            });
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.showNombre();
            holder.showCountUser();
            holder.setNombre(canal.getNombreCanal());
            holder.hideUndoBtn();
            holder.btnUndo.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {
        if (canales == null){
            return 0;
        }
        return canales.size();
    }

    public void setCanales(RealmResults<Canal> canalesList){
        canales = canalesList;
        notifyItemRangeInserted(getItemCount()-1,canalesList.size());
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

    public long getCanalId(int pos){
        Log.d("AIDI",canales.get(pos).getIdCanal()+"");
        return canales.get(pos).getIdCanal();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public boolean isPendingRemoval(int position) {
        Canal item = canales.get(position);
        return channelPendingRemoval.contains(item);
    }

    public void pendingRemoval(int position) {
        final Canal item = canales.get(position);
        if (!channelPendingRemoval.contains(item)) {
            channelPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(canales.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);

        }
    }

    public void remove(int position) {
        Canal item = canales.get(position);
        if (channelPendingRemoval.contains(item)) {
            channelPendingRemoval.remove(item);
        }
        if (canales.contains(item)) {
            realm.beginTransaction();
            canales.deleteFromRealm(position);
            realm.commitTransaction();
            notifyItemRemoved(position);
        }
    }

    class CanalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_vw_canal_name)
        TextView nombre;
        @BindView(R.id.txt_vw_canal_count_user)
        TextView countUser;
        @BindView(R.id.undo_button)
        Button btnUndo;

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

        public void hideNombre(){
            nombre.setVisibility(View.INVISIBLE);
        }
        public void showNombre(){
            nombre.setVisibility(View.VISIBLE);
        }
        public void hideCountUser(){
            countUser.setVisibility(View.GONE);
        }
        public void showCountUser(){
            countUser.setVisibility(View.VISIBLE);
        }
        public void hideUndoBtn(){
            btnUndo.setVisibility(View.GONE);
        }
        public void showUndoBtn(){
            btnUndo.setVisibility(View.VISIBLE);
        }

    }
}
