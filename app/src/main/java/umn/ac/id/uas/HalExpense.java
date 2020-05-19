package umn.ac.id.uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HalExpense extends AppCompatActivity {
    Button btnExSub;
    Spinner sWallet, sCat;
    List<Wallet> allWallets;
    WalletViewModel walletViewModel;
    TransaksiViewModel transaksiViewModel;
    String[] category = {"Expense","Income"};
    Wallet chsWalet;
    String cat;
    EditText etNominal, etDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hal_expense);

        btnExSub = findViewById(R.id.submitEx);
        sWallet = findViewById(R.id.spinnerAddEx);
        sCat = findViewById(R.id.spinnerCat);
        etNominal = findViewById(R.id.nominal);
        etDesc = findViewById(R.id.desc);

        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);

        allWallets = new ArrayList<>();
        final ArrayAdapter<Wallet> adapterWallet = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, allWallets);
        final ArrayAdapter<String> adapterCat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        adapterWallet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sWallet.setAdapter(adapterWallet);
        sCat.setAdapter(adapterCat);
        sWallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chsWalet = (Wallet) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnExSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKeAddWallet = new Intent(HalExpense.this, HomeActivity.class);
                startActivity(intentKeAddWallet);
            }
        });

        walletViewModel.getAllWallet().observe(this, new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                for(Wallet wallet:wallets){
                    allWallets.add(wallet);
                }
                adapterWallet.notifyDataSetChanged();
            }
        });
        btnExSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allWallets.isEmpty())
                    Toast.makeText(HalExpense.this, "you don't have a wallet", Toast.LENGTH_SHORT).show();
                else if(etNominal.getText().toString().trim().length() <= 0 || etDesc.getText().toString().trim().length() <= 0)
                    Toast.makeText(HalExpense.this, "Nominal and Description cannot be empty", Toast.LENGTH_SHORT).show();
                else{
                    int nominal = Integer.parseInt(etNominal.getText().toString().trim());
                    String description = etDesc.getText().toString().trim();
                    if(nominal <= 0)
                        Toast.makeText(HalExpense.this, "nominal value must be greater than zero", Toast.LENGTH_SHORT).show();
                    else{
                        Transaksi transaksi = new Transaksi(chsWalet.getIdWallet(), cat, description, nominal);
                        if(cat.equals(category[0])){
                            chsWalet.setBalance(chsWalet.getBalance() - nominal);
                        }
                        else if(cat.equals(category[1])){
                            chsWalet.setBalance(chsWalet.getBalance() + nominal);
                        }
                        transaksiViewModel.addTransaksi(transaksi);
                        walletViewModel.updateWallet(chsWalet);
                        finish();
                    }
                }
            }
        });
    }
}
