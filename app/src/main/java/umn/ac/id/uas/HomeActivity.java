package umn.ac.id.uas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button btnex, setting;
    ImageView btn;
    int progressMax, progressBalance, progress, expense;
    ProgressBar progressBar;
    WalletViewModel walletViewModel;
    TransaksiViewModel transaksiViewModel;
    TextView etSaldoSum, etExpense;
    private List<Wallet> allWallet;
    private List<Transaksi> allTransaksi;
    private GoogleSignInClient mGoogleSignInClient;
    static int RC_SIGN_IN = 1;
    private GoogleSignInAccount account;
    private FirebaseDatabase mDatabase;
    private DatabaseReference emailRef, walletRef, transRef;
    private String emailKey;
    private String email;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        btn = findViewById(R.id.button);
        btnex = findViewById(R.id.btnExpense);
        setting = findViewById( R.id.buttonSetting );
        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);
        etSaldoSum = findViewById(R.id.textView3);
        etExpense = findViewById(R.id.textView2);
        allWallet = walletViewModel.getAllWalletList();
        allTransaksi = transaksiViewModel.getAllTransaksiList();
        navigationView = findViewById(R.id.nav_view);
        mDatabase = FirebaseDatabase.getInstance();
        emailRef = mDatabase.getReference("email");
        walletRef = mDatabase.getReference("wallet");
        transRef = mDatabase.getReference("transaction");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
                //updateProgress();
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
                //updateProgress();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getTitle().equals("Backup")){

                }
                else if(menuItem.getTitle().equals("Restore")){

                }
                else if(menuItem.getTitle().equals("Sign In")){
                    signIn();
                }
                else if(menuItem.getTitle().equals("Sign Out")){
                    signOut();
                }
                return true;
            }
        });
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("test", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    protected void updateUI(@NonNull GoogleSignInAccount account){
        if(account != null){
            navigationView.getMenu().findItem(R.id.backup).setVisible(true);
            navigationView.getMenu().findItem(R.id.restore).setVisible(true);
            navigationView.getMenu().findItem(R.id.sign_out_button).setVisible(true);
            email = account.getEmail();

            emailRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int ada = 0;
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.getValue().equals(email)){
                            emailKey = dataSnapshot1.getKey();
                            ada = 1;
                        }
                    }
                    if(ada == 0){
                        String ref = emailRef.push().getKey();
                        emailRef.child(ref).setValue(email);
                        emailKey = ref;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HomeActivity.this, "Something error with the Database, try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            navigationView.getMenu().findItem(R.id.backup).setVisible(false);
            navigationView.getMenu().findItem(R.id.restore).setVisible(false);
            navigationView.getMenu().findItem(R.id.sign_out_button).setVisible(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }
}
