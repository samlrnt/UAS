package umn.ac.id.uas;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

public class WalletRepository {

    private WalletDao walletDao;
    private LiveData<List<Wallet>> allWallet;

    public WalletRepository(Application application){
       WalletDatabase db = WalletDatabase.getInstance(application);
       walletDao = db.walletDao();
       allWallet = walletDao.getAllWallet();
    }

    public void addWallet(Wallet wallet){
        new InsertAsyncTask(walletDao).execute(wallet);
    }

    public void deleteAllWallet(){
        new DeleteAllAsyncTask(walletDao).execute();
    }

    public LiveData<List<Wallet>> getAllWallet(){
        return allWallet;
    }

    public void deleteWallet(Wallet wallet){
        new DeleteAsyncTask(walletDao).execute(wallet);
    }

    private static class InsertAsyncTask extends AsyncTask<Wallet, Void, Void>{
        private WalletDao walletDao;

        private InsertAsyncTask(WalletDao walletDao){
            this.walletDao = walletDao;
        }
        @Override
        protected Void doInBackground(Wallet... wallets) {
            walletDao.addWallet(wallets[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{
        private WalletDao walletDao;

        private DeleteAllAsyncTask(WalletDao walletDao){
            this.walletDao = walletDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            walletDao.deleteAllWallet();
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Wallet, Void, Void>{
        private WalletDao walletDao;

        private DeleteAsyncTask(WalletDao walletDao){
            this.walletDao = walletDao;
        }
        @Override
        protected Void doInBackground(Wallet... wallets) {
            walletDao.deleteWallet(wallets[0]);
            return null;
        }
    }
}
