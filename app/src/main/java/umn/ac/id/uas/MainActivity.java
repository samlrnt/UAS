package umn.ac.id.uas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentManager fragmentManager;
    private LinearLayout btnAddWallet;
    private ConstraintLayout mainLayout;
    private WalletViewModel walletViewModel;
    private TextView whatWallet;
    private SearchView etSearch;
    public static int REQ_ADD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        whatWallet = findViewById(R.id.WhatWallet);
        mainLayout = findViewById(R.id.layoutbiasa);
        btnAddWallet = findViewById(R.id.AddWalletBtn);
        recyclerView = findViewById(R.id.recycler_view);
        etSearch = findViewById(R.id.searchBar);
        layoutManager = new LinearLayoutManager(this);
        fragmentManager = getSupportFragmentManager();
        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);

        adapter = new MyAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnAddWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addWalletIntent = new Intent(MainActivity.this, FormAddWallet.class);
                startActivityForResult(addWalletIntent, REQ_ADD);
            }
        });

        walletViewModel.getAllWallet().observe(this, new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                adapter.setWallets(wallets);
            }
        });

        whatWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletViewModel.deleteAll();
            }
        });

        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT
        | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    walletViewModel.deleteWallet(adapter.getWallet(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "Delete "+adapter.getWallet(viewHolder.getAdapterPosition()).getName()+" successfully", Toast.LENGTH_SHORT).show();
                }
                else if(direction == ItemTouchHelper.RIGHT){
                    Toast.makeText(MainActivity.this, "Edit "+adapter.getWallet(viewHolder.getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if(viewHolder != null){
                    final View foregroundView = ((MyAdapter.MyViewHolder) viewHolder).background;
                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((MyAdapter.MyViewHolder) viewHolder).background;
                getDefaultUIUtil().clearView(foregroundView);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState,
                                    boolean isCurrentlyActive) {
                final View foregroundView = ((MyAdapter.MyViewHolder) viewHolder).background;
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX/4, dY, actionState, isCurrentlyActive);


            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_ADD){
            Toast.makeText(this, "Wallet successfully created", Toast.LENGTH_LONG).show();
        }
    }
}
