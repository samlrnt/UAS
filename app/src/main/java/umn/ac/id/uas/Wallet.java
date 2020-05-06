package umn.ac.id.uas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallet")
public class Wallet {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "namaWallet")
    private String name;

    @NonNull
    @ColumnInfo(name = "saldo")
    private int balance;

    @Nullable
    @ColumnInfo(name = "catatan")
    private String notes;

    @ColumnInfo(name = "checkgoal")
    private boolean isGoal;

    @ColumnInfo(name = "target")
    private int saldoGoal;

    @ColumnInfo(name = "color")
    private int color;

    public Wallet(String name, int balance, String notes, boolean isGoal, int saldoGoal, int color){
        this.name = name;
        this.balance = balance;
        this.notes = notes;
        this.isGoal = isGoal;
        this.saldoGoal = saldoGoal;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public int getSaldoGoal() {
        return saldoGoal;
    }

    public String getNotes() {
        return notes;
    }

    public int getColor() {
        return color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setGoal(boolean goal) {
        this.isGoal = goal;
    }

    public void setSaldoGoal(int saldoGoal) {
        this.saldoGoal = saldoGoal;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
