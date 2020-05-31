package umn.ac.id.uas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button btnex, setting;
    ImageView btn;
    int progressMax, progressBalance, progress, expense;
    ProgressBar progressBar;
    WalletViewModel walletViewModel;
    TransaksiViewModel transaksiViewModel;
    TextView etSaldoSum, etExpense, tvEmail;
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
    private Context context = this;

    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Bikin Shared Preference buat notif settings gitu-gitu */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean NotifPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION_SWITCH, false);
        Boolean FingerprintPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_FINGERPRINT_SWITCH, false);

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
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
        View view = navigationView.getHeaderView(0);
        tvEmail = view.findViewById(R.id.email);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
                    backup();
                }
                else if(menuItem.getTitle().equals("Restore")){
                    restore();
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

        /*Bikin Notification Channel*/
        CreateNotificationChannel();

        /* Bikin Notification Intent jalan tiap jam 8 malem */
        if(NotifPref){
            Intent intent = new Intent(HomeActivity.this, NotificationBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, intent, 0);

            AlarmManager alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);

            assert alarmManager != null;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
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
            navigationView.getMenu().findItem(R.id.sign_in_button).setVisible(false);
            email = account.getEmail();

            emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
           tvEmail.setText(email);
        }
        else{
            navigationView.getMenu().findItem(R.id.backup).setVisible(false);
            navigationView.getMenu().findItem(R.id.restore).setVisible(false);
            navigationView.getMenu().findItem(R.id.sign_out_button).setVisible(false);
            navigationView.getMenu().findItem(R.id.sign_in_button).setVisible(true);
            tvEmail.setText("User");
        }
    }

    private void restore(){
        walletViewModel.deleteAll();
        transaksiViewModel.deleteAllTransaksi();

        walletRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    int idWallet = Integer.parseInt(dataSnapshot1.child("idWallet").getValue().toString());
                    String nama = dataSnapshot1.child("name").getValue().toString();
                    int balance = Integer.parseInt(dataSnapshot1.child("balance").getValue().toString());
                    String notes = dataSnapshot1.child("notes").getValue().toString();
                    boolean isgoal = Boolean.parseBoolean(dataSnapshot1.child("goal").getValue().toString());
                    int saldoGoal = Integer.parseInt(dataSnapshot1.child("saldoGoal").getValue().toString());
                    int color = Integer.parseInt(dataSnapshot1.child("color").getValue().toString());
                    Wallet wallet = new Wallet(nama, balance, notes, isgoal, saldoGoal, color);
                    wallet.setIdWallet(idWallet);
                    wallet.setAccountCreateor(emailKey);
                    walletViewModel.addWallet(wallet);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "there's a problem with the Database, try again", Toast.LENGTH_SHORT).show();
            }
        });

        transRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    int idTransaksi = Integer.parseInt(dataSnapshot1.child("idTransaksi").getValue().toString());
                    String desc = dataSnapshot1.child("description").getValue().toString();
                    int idCreatorWallet = Integer.parseInt(dataSnapshot1.child("idCreatorWallet").getValue().toString());
                    String category = dataSnapshot1.child("category").getValue().toString();
                    int nominal = Integer.parseInt(dataSnapshot1.child("nominal").getValue().toString());
                    Transaksi transaksi = new Transaksi(idCreatorWallet,category,desc,nominal);
                    transaksi.setIdTransaksi(idTransaksi);
                    transaksiViewModel.addTransaksi(transaksi);
                }
                Toast.makeText(context, "wallet has been restored", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "there's a problem with the Database, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreateNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Swallow Notification Channel";
            String description = "Channel for Swallow Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notifySwallow", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void backup(){
        walletRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                walletRef.removeValue();
                transRef.removeValue();
                for(Wallet wallet: allWallet){
                    wallet.setAccountCreateor(emailKey);
                    walletRef.push().setValue(wallet);
                }
                for(Transaksi transaksi: allTransaksi){
                    transRef.push().setValue(transaksi);
                }
                Toast.makeText(context, "backup successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Something error with the Database, Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

    }
}
