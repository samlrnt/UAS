package umn.ac.id.uas;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WalletViewModel extends AndroidViewModel {

    private WalletRepository walletRepository;

    public WalletViewModel(@NonNull Application application) {
        super(application);
        walletRepository = new WalletRepository(application);
    }

    public void addWallet(Wallet wallet){
        walletRepository.addWallet(wallet);
    }

    public LiveData<List<Wallet>> getAllWallet(){
        return walletRepository.getAllWallet();
    }

    public List<Wallet> getAllWalletList(){
        return walletRepository.getWalletList();
    }

    public void deleteAll(){
        walletRepository.deleteAllWallet();
    }

    public void deleteWallet(Wallet wallet){
        walletRepository.deleteWallet(wallet);
    }

    public void updateWallet(Wallet wallet){
        walletRepository.updateWallet(wallet);
    }
}
