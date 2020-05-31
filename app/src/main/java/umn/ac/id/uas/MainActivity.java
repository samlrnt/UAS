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
import android.graphics.Canvas;
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
    private TransaksiViewModel transaksiViewModel;
    private TextView whatWallet;
    private SearchView etSearch;
    public static int REQ_ADD = 1, REQ_EDIT = 2;
    private Wallet walletinit;

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
        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);

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

        transaksiViewModel.getAllTransaksi().observe(this, new Observer<List<Transaksi>>() {
            @Override
            public void onChanged(List<Transaksi> transaksis) {
                adapter.setTransaksi(transaksis);
            }
        });

        whatWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletViewModel.deleteAll();
                transaksiViewModel.deleteAllTransaksi();
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

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT){
                    List<Transaksi> listTransaksi = transaksiViewModel.getAllTransaksiList();
                    for(Transaksi transaksi : listTransaksi){
                        if(transaksi.getIdCreatorWallet() == adapter.getWallet(viewHolder.getAdapterPosition()).getIdWallet()){
                            transaksiViewModel.deleteTransaksi(transaksi);
                        }
                    }
                    walletViewModel.deleteWallet(adapter.getWallet(viewHolder.getAdapterPosition()));

                    Toast.makeText(MainActivity.this, "Delete "+adapter.getWallet(viewHolder.getAdapterPosition()).getName()+" successfully", Toast.LENGTH_SHORT).show();
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
        adapter.SetOnItemClick(new MyAdapter.OnItemClick() {
            @Override
            public void OnItemClickListener(Wallet wallet) {
                Intent intent = new Intent(MainActivity.this, HalamanWallet.class);
                walletinit = wallet;
                intent.putExtra("wallet", wallet);
                startActivityForResult(intent, REQ_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_ADD && resultCode == RESULT_OK){
            String nama = data.getStringExtra("nama");
            int saldo = data.getIntExtra("saldo", 0);
            String notes = data.getStringExtra("notes");
            boolean isgoal = data.getBooleanExtra("isgoal", false);
            int target = data.getIntExtra("saldoTarget", 0);
            int warna = data.getIntExtra("warna", 0);
            Wallet wallet = new Wallet(nama, saldo, notes, isgoal, target, warna);
            walletViewModel.addWallet(wallet);
            Toast.makeText(this, "Wallet successfully created", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == REQ_EDIT && resultCode == RESULT_OK){
            Wallet walletedit = data.getParcelableExtra("databalik");
            if(walletinit.isGoal() != walletedit.isGoal()){
                walletViewModel.updateWallet(walletedit);
                Toast.makeText(this, "wallet updated", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
