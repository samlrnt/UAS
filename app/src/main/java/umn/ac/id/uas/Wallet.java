package umn.ac.id.uas;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallet")
public class Wallet implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_wallet")
    private int idWallet;

    private String accountCreateor;

    @NonNull
    @ColumnInfo(name = "namaWallet")
    private String name;

    @NonNull
    @ColumnInfo(name = "saldoAwal")
    private int initBalance;

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
        this.initBalance = balance;
        this.balance = balance;
        this.notes = notes;
        this.isGoal = isGoal;
        this.saldoGoal = saldoGoal;
        this.color = color;
    }


    protected Wallet(Parcel in) {
        idWallet = in.readInt();
        accountCreateor = in.readString();
        name = in.readString();
        initBalance = in.readInt();
        balance = in.readInt();
        notes = in.readString();
        isGoal = in.readByte() != 0;
        saldoGoal = in.readInt();
        color = in.readInt();
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    public int getIdWallet() {
        return idWallet;
    }

    public String getName() {
        return name;
    }

    public int getInitBalance() {
        return initBalance;
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

    public String getAccountCreateor() {
        return accountCreateor;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setInitBalance(int initBalance) {
        this.initBalance = initBalance;
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

    public void setAccountCreateor(String accountCreateor) {
        this.accountCreateor = accountCreateor;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idWallet);
        dest.writeString(accountCreateor);
        dest.writeString(name);
        dest.writeInt(initBalance);
        dest.writeInt(balance);
        dest.writeString(notes);
        dest.writeByte((byte) (isGoal ? 1 : 0));
        dest.writeInt(saldoGoal);
        dest.writeInt(color);
    }
}
