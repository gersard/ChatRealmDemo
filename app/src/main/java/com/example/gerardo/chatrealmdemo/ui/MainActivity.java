package com.example.gerardo.chatrealmdemo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import android.widget.Toast;
import com.example.gerardo.chatrealmdemo.*;
import com.example.gerardo.chatrealmdemo.adapter.ListaCanalAdapter;
import com.example.gerardo.chatrealmdemo.model.Canal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.*;

public class MainActivity extends AppCompatActivity implements ChannelAndUsernameDialog.IupdateRecyclers, SyncUser.Callback {

    private final String TAG = getClass().getSimpleName();

    private Realm realm;
    @BindView(R.id.rv_channels)
    RecyclerView recyclerViewChannel;
    @BindView(R.id.txt_rv_empty)
    TextView txtRecyclerViewEmpty;
    ListaCanalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);
        dialog.show();

        SyncUser.loginAsync(SyncCredentials.usernamePassword("user", "user", false), Application.AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                UserManager.setActiveUser(user);
                dialog.dismiss();

                realm = Realm.getDefaultInstance();
                setRecyclerView(MainActivity.this);

                validarUsernameExist();
            }

            @Override
            public void onError(ObjectServerError error) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this,error.getErrorMessage(),Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelAndUsernameDialog dialog = new ChannelAndUsernameDialog(MainActivity.this
                        , Constants.DIALOG_CHANNEL,MainActivity.this);
                dialog.show();
            }
        });




    }

    private void validarUsernameExist() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
        if (prefs.getString("username","").equals("")){
            ChannelAndUsernameDialog dialog = new ChannelAndUsernameDialog(this, Constants.DIALOG_USERNAME,
                    MainActivity.this);
            dialog.show();
        }
    }

    private void setRecyclerView(Context context){
        adapter = new ListaCanalAdapter(context,realm);
        LinearLayoutManager lm = new LinearLayoutManager(context);

        recyclerViewChannel.setLayoutManager(lm);
        recyclerViewChannel.setAdapter(adapter);
        RealmResults<Canal> canales = Canal.getCanales(realm);
        checkContentRecyclerView(canales);
        adapter.setCanales(canales);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("id_canal",adapter.getCanalId(recyclerViewChannel.getChildAdapterPosition(view)));
                startActivity(intent);
            }
        });
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        canales.addChangeListener(new RealmChangeListener<RealmResults<Canal>>() {
            @Override
            public void onChange(RealmResults<Canal> element) {
                adapter.setCanales(element);
                recyclerViewChannel.setAdapter(adapter);
                checkContentRecyclerView(element);
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
        if (id == R.id.action_editusername) {
            ChannelAndUsernameDialog dialog = new ChannelAndUsernameDialog(this, Constants.DIALOG_USERNAME,
                    MainActivity.this);
            dialog.show();
            return true;
        }else if (id == R.id.action_user_online){

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

    @Override
    public void updateRecyclerChannel() {
        if (adapter != null){
            adapter.notifyItemInserted(adapter.getItemCount());
            recyclerViewChannel.scrollToPosition(adapter.getItemCount());
        }
    }

    // FUNCIONALIDAD DE ELIMINAR CANALES MEDIANTE "SWIPE"

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) MainActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                ListaCanalAdapter testAdapter = (ListaCanalAdapter)recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ListaCanalAdapter adapter = (ListaCanalAdapter)recyclerViewChannel.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerViewChannel);
    }

    private void setUpAnimationDecoratorHelper() {
        recyclerViewChannel.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    private void checkContentRecyclerView(RealmResults<Canal> canales){
        if (canales != null){
            if (canales.size() == 0){
                recyclerViewChannel.setVisibility(View.GONE);
                txtRecyclerViewEmpty.setVisibility(View.VISIBLE);
            }else{
                recyclerViewChannel.setVisibility(View.VISIBLE);
                txtRecyclerViewEmpty.setVisibility(View.GONE);
            }
        }
    }

    //CALLBACK'S REALM SYNC USER
    @Override
    public void onSuccess(SyncUser user) {

    }

    @Override
    public void onError(ObjectServerError error) {

    }
}
