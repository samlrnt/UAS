package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button btnex;
    ImageView btn;
    int progressMax=0, currentPosition, saldowal, saldoexpense;
    ProgressBar progressBar;
    WalletViewModel walletViewModel;
    TextView etSaldoSum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        btn = findViewById(R.id.button);
        btnex = findViewById(R.id.btnExpense);
        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        etSaldoSum = findViewById(R.id.textView3);
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

//        for(Wallet wallet:allWallets){
//            progressMax += wallet.getBalance();
//        }
//        Log.d("test", String.valueOf(allWallets));
//        progressBar.setMax(progressMax);
//        etSaldoSum.setText(String.valueOf(progressMax));

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
                progressMax = 0;
                for(Wallet wallet : wallets){
                    progressMax += wallet.getBalance();
                }
                progressBar.setMax(progressMax);
                etSaldoSum.setText(String.valueOf(progressMax));
            }
        });
    }


}
