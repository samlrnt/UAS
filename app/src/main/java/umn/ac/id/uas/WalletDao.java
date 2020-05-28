package umn.ac.id.uas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addWallet(Wallet wallet);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateWallet(Wallet wallet);

    @Delete
    void deleteWallet(Wallet wallet);

    @Query("DELETE FROM wallet")
    void deleteAllWallet();

    @Query("SELECT * FROM wallet")
    LiveData<List<Wallet>> getAllWallet();

    @Query("SELECT * FROM wallet")
    List<Wallet> getAllWalletList();
}
