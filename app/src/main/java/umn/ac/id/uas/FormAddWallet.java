package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class FormAddWallet extends AppCompatActivity {

    Button btnSubmit, btnClrPicker;
    EditText etNama, etSaldo, etNotes, etSaldoGoal;
    CheckBox cbGoal;
    int defColor;
    WalletViewModel walletViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_wallet);

        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        etNama = findViewById(R.id.namaWallet);
        etSaldo = findViewById(R.id.saldoWallet);
        etNotes = findViewById(R.id.notesWallet);
        cbGoal = findViewById(R.id.isGoal);
        etSaldoGoal = findViewById(R.id.saldoTarget);
        btnSubmit = findViewById(R.id.submitForm);
        btnClrPicker = findViewById(R.id.colorpicker);
        etSaldoGoal.setEnabled(false);
        defColor = ContextCompat.getColor(FormAddWallet.this, R.color.colorPrimary);

        btnClrPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBtn();
            }
        });

        cbGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbGoal.isChecked()){
                    etSaldoGoal.setEnabled(true);
                }
                else{
                    etSaldoGoal.setEnabled(false);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etNama.getText().toString().trim().length() <= 0 || etSaldo.getText().toString().length() <= 0){
                    Toast.makeText(getApplicationContext(), "Name and Balance cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else if(cbGoal.isChecked() == true && etSaldoGoal.getText().toString().length() <= 0){
                    Toast.makeText(getApplicationContext(), "Goal balance cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    String namaWallet = etNama.getText().toString().trim();
                    int saldoWallet = Integer.parseInt(etSaldo.getText().toString());
                    boolean isgoal = cbGoal.isChecked();

                    if(etNotes.getText().toString().trim().length() <= 0){
                        etNotes.setText(null);
                    }
                    if(etSaldoGoal.getText().toString().length() <= 0){
                        etSaldoGoal.setText("0");
                    }
                    String notesWallet = etNotes.getText().toString().trim();
                    int saldoTarget = Integer.parseInt(etSaldoGoal.getText().toString());

                    Wallet wallet = new Wallet(namaWallet, saldoWallet, notesWallet, isgoal, saldoTarget, defColor);

                    walletViewModel.addWallet(wallet);

                    setResult(MainActivity.REQ_ADD);
                    finish();

                }
            }
        });
    }

    public void colorBtn(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defColor = color;
                Log.d("warna",String.valueOf(defColor));
                btnClrPicker.setBackgroundColor(defColor);
            }
        });
        colorPicker.show();
    }

}
