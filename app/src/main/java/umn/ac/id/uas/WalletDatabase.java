package umn.ac.id.uas;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Wallet.class, Transaksi.class}, version = 1, exportSchema = false)
public abstract class WalletDatabase extends RoomDatabase {

    public abstract TransaksiDao transaksiDao();
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

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private WalletDao walletDao;
        private TransaksiDao transaksiDao;

        private PopulateDbAsyncTask(WalletDao walletDao, TransaksiDao transaksiDao){
            this.walletDao = walletDao;
            this.transaksiDao = transaksiDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            walletDao.addWallet(new Wallet("test",1000,"ada apanya", false, 2000, 1));
            walletDao.addWallet(new Wallet("test2",2000,"ada apanyaa", false, 3000, 2));
            walletDao.addWallet(new Wallet("test3",3000,"ada apanyaasd", false, 4000, 3));
            transaksiDao.addTransaksi(new Transaksi(1, "Expense", "makan", 20));
            transaksiDao.addTransaksi(new Transaksi(2, "Income", "gaji", 40));
            transaksiDao.addTransaksi(new Transaksi(3, "Expense", "makan", 60));
            return null;
        }
    }
}
