package umn.ac.id.uas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.MyViewHolder> {

    private Context context;
    private List<Transaksi> allTransaksi;
    int id;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView etCategory, etNotes, etNominal;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            etCategory = itemView.findViewById(R.id.category);
            etNotes = itemView.findViewById(R.id.notes);
            etNominal = itemView.findViewById(R.id.nominal);

        }
    }

    public TransaksiAdapter(Context context){
        this.context = context;
        allTransaksi = new ArrayList<>();
        id = -1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaksi_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(allTransaksi.get(position).getIdCreatorWallet() == id){
            holder.etCategory.setText(allTransaksi.get(position).getCategory());
            holder.etNotes.setText(allTransaksi.get(position).getDescription());
            holder.etNominal.setText(String.valueOf(allTransaksi.get(position).getNominal()));
        }
    }

    @Override
    public int getItemCount() {
        return allTransaksi.size();
    }

    public void setAllTransaksi(List<Transaksi> allTransaksi, int id) {
        this.allTransaksi = allTransaksi;
        this.id = id;
        notifyDataSetChanged();
    }
}
