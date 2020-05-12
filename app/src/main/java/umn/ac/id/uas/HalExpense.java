package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HalExpense extends AppCompatActivity {
    Button btnExSub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hal_expense);
        btnExSub = findViewById(R.id.submitEx);

        btnExSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKeAddWallet = new Intent(HalExpense.this, HomeActivity.class);
                startActivity(intentKeAddWallet);
            }
        });
    }
}
