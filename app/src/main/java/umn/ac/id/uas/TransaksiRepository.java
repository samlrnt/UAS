package umn.ac.id.uas;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransaksiRepository {

    private TransaksiDao transaksiDao;
    private LiveData<List<Transaksi>> allTransaksi;
    private List<Transaksi> allTransaksiList;

    public TransaksiRepository(Application application){
        WalletDatabase db = WalletDatabase.getInstance(application);
        transaksiDao = db.transaksiDao();
        allTransaksi = transaksiDao.getAllTransaksi();
        allTransaksiList = transaksiDao.getAllTransaksiList();
    }

    public void addTransaksi(Transaksi transaksi){
        new AddTransaksiAsyncTask(transaksiDao).execute(transaksi);
    }

    public void deleteAllTransaksi(){
        new DeleteAllTransaksiAsyncTask(transaksiDao).execute();
    }

    public LiveData<List<Transaksi>> getAllTransaksi() {
        return allTransaksi;
    }

    public List<Transaksi> getAllTransaksiList(){
        return allTransaksiList;
    }

    private class AddTransaksiAsyncTask extends AsyncTask<Transaksi, Void, Void>{
        private TransaksiDao transaksiDao;

        private AddTransaksiAsyncTask(TransaksiDao transaksiDao){
            this.transaksiDao = transaksiDao;
        }
        @Override
        protected Void doInBackground(Transaksi... transaksis) {
            transaksiDao.addTransaksi(transaksis[0]);
            return null;
        }
    }

    private class DeleteAllTransaksiAsyncTask extends AsyncTask<Void, Void, Void>{

        private TransaksiDao transaksiDao;

        private DeleteAllTransaksiAsyncTask(TransaksiDao transaksiDao){
            this.transaksiDao = transaksiDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            transaksiDao.deleteAllTransaksi();
            return null;
        }
    }

}
