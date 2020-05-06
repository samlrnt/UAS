package umn.ac.id.uas;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Wallet.class}, version = 1, exportSchema = false)
public abstract class WalletDatabase extends RoomDatabase {

    public abstract WalletDao walletDao();
    private static WalletDatabase INSTANCE;

    public static synchronized WalletDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WalletDatabase.class, "wallet_database").
                    fallbackToDestructiveMigration().
                    build();
        }
        return INSTANCE;
    }

}
