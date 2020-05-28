package umn.ac.id.uas;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WalletRepository {

    private WalletDao walletDao;
    private LiveData<List<Wallet>> allWallet;
    private List<Wallet> walletList;

    public WalletRepository(Application application){
       WalletDatabase db = WalletDatabase.getInstance(application);
       walletDao = db.walletDao();
       allWallet = walletDao.getAllWallet();
       walletList = walletDao.getAllWalletList();
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

    public List<Wallet> getWalletList(){
        return walletList;
    }

    public void deleteWallet(Wallet wallet){
        new DeleteAsyncTask(walletDao).execute(wallet);
    }

    public void updateWallet(Wallet wallet){
        new UpdateAsyncTask(walletDao).execute(wallet);
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

    private static class UpdateAsyncTask extends AsyncTask<Wallet, Void, Void>{

        private WalletDao walletDao;

        private UpdateAsyncTask(WalletDao walletDao){
            this.walletDao = walletDao;
        }
        @Override
        protected Void doInBackground(Wallet... wallets) {
            walletDao.updateWallet(wallets[0]);
            return null;
        }
    }

    /*private class ListAsyncTask extends AsyncTask<Void, Void, List<Wallet>>{

        private WalletDao walletDao;

        private ListAsyncTask(WalletDao walletDao){
            this.walletDao = walletDao;
        }

        @Override
        protected List<Wallet> doInBackground(Void... voids) {
            return walletDao.getAllWalletList();
        }

        @Override
        protected void onPostExecute(List<Wallet> wallets) {
            walletList = wallets;
        }
    }*/
}
