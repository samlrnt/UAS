package umn.ac.id.uas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransaksiDao {

    @Insert
    void addTransaksi(Transaksi transaksi);

    @Query("SELECT * FROM transaksi")
    LiveData<List<Transaksi>> getAllTransaksi();
}
