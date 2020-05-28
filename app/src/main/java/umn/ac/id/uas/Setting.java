package umn.ac.id.uas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.mortbay.util.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class Setting extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton btnSignIn;
    private Button btnSignOut, btnDisconnect, btnBackup, btnRestore;
    private GoogleSignInAccount account;
    static int RC_SIGN_IN = 1;
    static String TAG = "test";
    private Context context = this;
    private WalletViewModel walletViewModel;
    private TransaksiViewModel transaksiViewModel;
    private List<String> emailId = new ArrayList<>();
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /*private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "src\\main\\res\\raw\\credentials.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";*/
    private FirebaseStorage storage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference emailRef, walletRef, transRef;
    private List<Wallet> walletList;
    private List<Transaksi> transaksiList;
    private String emailKey;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignOut = findViewById(R.id.sign_out_button);
        btnDisconnect = findViewById(R.id.disconnect_button);
        btnBackup = findViewById(R.id.backup_button);
        btnRestore = findViewById(R.id.restore_button);
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        emailRef = mDatabase.getReference("email");
        walletRef = mDatabase.getReference("wallet");
        transRef = mDatabase.getReference("transaction");
        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);
        walletList = walletViewModel.getAllWalletList();
        transaksiList = transaksiViewModel.getAllTransaksiList();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        walletRef.removeValue();
                        transRef.removeValue();
                        for(Wallet wallet: walletList){
                            wallet.setAccountCreateor(emailKey);
                            walletRef.push().setValue(wallet);
                        }
                        for(Transaksi transaksi: transaksiList){
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
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
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

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    protected void updateUI(@NonNull GoogleSignInAccount account){
        if(account != null){
            btnSignOut.setVisibility(View.VISIBLE);
            btnDisconnect.setVisibility(View.VISIBLE);
            btnBackup.setVisibility(View.VISIBLE);
            btnRestore.setVisibility(View.VISIBLE);
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
                    Toast.makeText(Setting.this, "Something error with the Database, try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            btnSignOut.setVisibility(View.INVISIBLE);
            btnDisconnect.setVisibility(View.INVISIBLE);
            btnBackup.setVisibility(View.INVISIBLE);
            btnRestore.setVisibility(View.INVISIBLE);
        }
    }
}
