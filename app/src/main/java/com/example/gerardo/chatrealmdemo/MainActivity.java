package com.example.gerardo.chatrealmdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gerardo.chatrealmdemo.adapter.ListaCanalAdapter;
import com.example.gerardo.chatrealmdemo.model.Canal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    @BindView(R.id.rv_channels)
    RecyclerView recyclerViewChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        realm = Realm.getDefaultInstance();

        Funciones.crearCanales(realm);
        setRecyclerView(this);

    }

    private void setRecyclerView(Context context){
        final ListaCanalAdapter adapter = new ListaCanalAdapter(context);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        //PROPIEDAD PARA EL CHAT
//        lm.setReverseLayout(true);
        recyclerViewChannel.setLayoutManager(lm);
        recyclerViewChannel.setAdapter(adapter);
        adapter.setCanales(Canal.getCanales(realm));

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("id_canal",adapter.getCanalId(recyclerViewChannel.getChildAdapterPosition(view)));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()){
            realm.close();
        }
    }
}
