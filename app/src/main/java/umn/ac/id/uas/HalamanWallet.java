package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HalamanWallet extends AppCompatActivity {

    TextView tvNama, tvTabungan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_halaman_wallet );
        tvNama = findViewById(R.id.namaWallet);
        tvTabungan = findViewById(R.id.tabungan);
        Intent getIntent = getIntent();
        String tabungan = String.valueOf(getIntent.getIntExtra("tabungan",0));
        String nama = getIntent.getStringExtra("nama");
        tvNama.setText(nama);
        tvTabungan.setText(tabungan);
    }
}
