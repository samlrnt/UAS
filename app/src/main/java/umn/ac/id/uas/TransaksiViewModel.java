package umn.ac.id.uas;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TransaksiViewModel extends AndroidViewModel {

    private TransaksiRepository transaksiRepository;

    public TransaksiViewModel(@NonNull Application application) {
        super(application);
        transaksiRepository = new TransaksiRepository(application);
    }

    public void addTransaksi(Transaksi transaksi){
        transaksiRepository.addTransaksi(transaksi);
    }

    public LiveData<List<Transaksi>> getAllTransaksi(){
        return transaksiRepository.getAllTransaksi();
    }

    public List<Transaksi> getAllTransaksiList(){
        return transaksiRepository.getAllTransaksiList();
    }

    public void deleteAllTransaksi(){
        transaksiRepository.deleteAllTransaksi();
    }
}
