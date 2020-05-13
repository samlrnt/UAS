package umn.ac.id.uas;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Transaksi.class}, version = 2, exportSchema = false)
public abstract class TransaksiDatabase extends RoomDatabase {

    public abstract TransaksiDao transaksiDao();
    private static TransaksiDatabase INSTANCE;

    public static synchronized TransaksiDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TransaksiDatabase.class, "transaksi_database").
                    fallbackToDestructiveMigration().
                    build();
        }
        return INSTANCE;
    }
}
