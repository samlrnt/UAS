package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HalamanWallet extends AppCompatActivity {

    TextView tvNama, tvTabungan, tvGoals;
    CheckBox cbGoals;
    TransaksiViewModel transaksiViewModel;
    TransaksiAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressBar probar;
    Wallet wallet;
    List<Transaksi> allTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_halaman_wallet );
        tvNama = findViewById(R.id.namaWallet);
        tvTabungan = findViewById(R.id.tabungan);
        tvGoals = findViewById(R.id.Goals);
        recyclerView = findViewById(R.id.history);
        probar = findViewById(R.id.probar);
        cbGoals = findViewById(R.id.checkBox);

        allTransaksi = new ArrayList<>();
        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);
        adapter = new TransaksiAdapter(this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Intent getIntent = getIntent();
        wallet = getIntent.getParcelableExtra("wallet");
        tvNama.setText(wallet.getName());
        tvTabungan.setText(String.valueOf(wallet.getBalance()));
        tvGoals.setText(String.valueOf(wallet.getSaldoGoal()));
        cbGoals.setChecked(wallet.isGoal());
        probar.setMax(wallet.getInitBalance());
        showGoals();

        transaksiViewModel.getAllTransaksi().observe(this, new Observer<List<Transaksi>>() {
            @Override
            public void onChanged(List<Transaksi> transaksis) {
                adapter.setAllTransaksi(transaksis, wallet.getIdWallet());
                for(Transaksi transaksi:transaksis){
                    allTransaksi.add(transaksi);
                }
                setProgress();
            }
        });

        cbGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoals();
                wallet.setGoal(cbGoals.isChecked());
                setProgress();
            }
        });
    }

    void showGoals(){
        if(cbGoals.isChecked())
            tvGoals.setVisibility(View.VISIBLE);
        else
            tvGoals.setVisibility(View.INVISIBLE);
    }

    void setProgress(){
        int progress = 0;
        probar.setProgress(progress);
        if(cbGoals.isChecked()){
            probar.setMax(wallet.getSaldoGoal());
            for(Transaksi transaksi: allTransaksi){
                if(transaksi.getCategory().equals("Income")) progress += transaksi.getNominal();
            }
        }
        else{
            probar.setMax(wallet.getInitBalance());
            for(Transaksi transaksi: allTransaksi){
                if(transaksi.getCategory().equals("Expense")) progress += transaksi.getNominal();
            }
        }
        probar.setProgress(progress);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("databalik", wallet);
        setResult(RESULT_OK, data);
        super.finish();
    }
}