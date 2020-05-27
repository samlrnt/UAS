package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button btnex, setting;
    ImageView btn;
    int currentPosition, saldowal, saldoexpense;
    ProgressBar progressBar;
    WalletViewModel walletViewModel;
    TransaksiViewModel transaksiViewModel;
    TextView etSaldoSum, etExpense;
    static String TAG = "test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: ");
        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        btn = findViewById(R.id.button);
        btnex = findViewById(R.id.btnExpense);
        setting = findViewById( R.id.buttonSetting );
        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);
        etSaldoSum = findViewById(R.id.textView3);
        etExpense = findViewById(R.id.textView2);
        //saldowal = findViewById(R.id.saldoWallet);
        //saldowal = saldowallet+saldowallet
        //saldoexpense = expense1+expense1;
        //progessbar = saldowal - saldoexpense;

        //saldoexpense = findViewById(R.id.expense1)
        /*progressValue = progressBar.getProgress();
        currentPosition= saldowal - Expenses;
        int total =saldoWallet.getDuration();
        while(mediaPlayer!=null && currentPosition<total){
            try{
                if(progressEnable){
                    Thread.sleep(1000);
                    currentPosition=mediaPlayer.getCurrentPosition();
                    long pos=1000L * currentPosition/total;
                    Log.d("thread pos", pos+"");
                    progressSeekBar.setProgress((int) pos);
                }
            }
            catch (Exception e) {
            }
        }*/

        setting.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSetting = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(  intentSetting);
            }
        } );

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKeAddWallet = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intentKeAddWallet);
            }
        });

        btnex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKeAddExpense = new Intent(HomeActivity.this, HalExpense.class);
                startActivity(intentKeAddExpense);
            }
        });
        walletViewModel.getAllWallet().observe(this, new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                int progressMax = 0;
                int progressBalance = 0;
                for(Wallet wallet : wallets){
                    progressMax += wallet.getInitBalance();
                    progressBalance += wallet.getBalance();
                }
                progressBar.setMax(progressMax);
                etSaldoSum.setText(String.valueOf(progressBalance));
            }
        });

        transaksiViewModel.getAllTransaksi().observe(this, new Observer<List<Transaksi>>() {
            @Override
            public void onChanged(List<Transaksi> transaksis) {
                int progress = 0;
                int expense = 0;
                for(Transaksi transaksi: transaksis){
                    if(transaksi.getCategory().equals("Expense")){
                        progress += transaksi.getNominal();
                        expense += transaksi.getNominal();
                    }
                    else if(transaksi.getCategory().equals("Income")){
                        progress -= transaksi.getNominal();
                    }
                }
                progressBar.setProgress(progress);
                etExpense.setText(String.valueOf(expense));
                Log.d(TAG, String.valueOf(progressBar.getProgress()));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
