package umn.ac.id.uas;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransaksiRepository {

    private TransaksiDao transaksiDao;
    private LiveData<List<Transaksi>> allTransaksi;

    public TransaksiRepository(Application application){
        TransaksiDatabase db = TransaksiDatabase.getInstance(application);
        transaksiDao = db.transaksiDao();
        allTransaksi = transaksiDao.getAllTransaksi();
    }

    public void addTransaksi(Transaksi transaksi){
        new AddTransaksiAsyncTask(transaksiDao).execute(transaksi);
    }

    public LiveData<List<Transaksi>> getAllTransaksi() {
        return allTransaksi;
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
}
