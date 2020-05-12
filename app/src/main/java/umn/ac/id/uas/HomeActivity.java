package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class HomeActivity extends AppCompatActivity {

    Button btn;
    int progressValue, currentPosition, saldowal, saldoexpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProgressBar progressBar =(ProgressBar) findViewById(R.id.progressBar);
        btn = findViewById(R.id.button);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKeAddWallet = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intentKeAddWallet);
            }
        });

    }


}
