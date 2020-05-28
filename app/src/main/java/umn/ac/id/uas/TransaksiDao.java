package umn.ac.id.uas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransaksiDao {

    @Insert
    void addTransaksi(Transaksi transaksi);

    @Delete
    void deleteTransaksi(Transaksi transaksi);

    @Query("DELETE FROM transaksi")
    void deleteAllTransaksi();

    @Query("SELECT * FROM transaksi")
    LiveData<List<Transaksi>> getAllTransaksi();

    @Query("SELECT * FROM transaksi")
    List<Transaksi> getAllTransaksiList();

}
