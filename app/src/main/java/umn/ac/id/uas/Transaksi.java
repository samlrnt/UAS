package umn.ac.id.uas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaksi")
public class Transaksi {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_transaksi")
    private int idTransaksi;

    @ColumnInfo(name = "id_wallet")
    private int idCreatorWallet;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "keterangan")
    private String description;

    @ColumnInfo(name = "jumlah")
    private int nominal;

    public Transaksi(int idCreatorWallet, String category, String description, int nominal){
        this.idCreatorWallet = idCreatorWallet;
        this.category = category;
        this.description = description;
        this.nominal = nominal;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public int getIdCreatorWallet() {
        return idCreatorWallet;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getNominal() {
        return nominal;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public void setIdCreatorWallet(int idCreatorWallet) {
        this.idCreatorWallet = idCreatorWallet;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
