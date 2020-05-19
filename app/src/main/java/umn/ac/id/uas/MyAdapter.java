package umn.ac.id.uas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    private List<Wallet> wallets;
    private List<Wallet> walletsCopy;
    private List<Transaksi> transaksi;
    private Context context;
    private int img = R.drawable.wallet_money_png_icon_7;
    private OnItemClick listener;

    public MyAdapter(Context context) {
        this.context = context;
        wallets = new ArrayList<>();
        transaksi = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView background, cardViewImgWallet, cardViewEdit, cardViewDelete;
        ImageView imgWallet;
        TextView namaWallet, saldoWallet;
        ProgressBar barWallet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWallet = itemView.findViewById(R.id.imgWallet);
            namaWallet = itemView.findViewById(R.id.namaWallet);
            saldoWallet = itemView.findViewById(R.id.saldoWallet);
            background = itemView.findViewById(R.id.backgroundCardView);
            cardViewImgWallet = itemView.findViewById(R.id.cardViewImgWallet);
            cardViewEdit = itemView.findViewById(R.id.cardViewEdit);
            cardViewDelete = itemView.findViewById(R.id.cardViewDelete);
            barWallet = itemView.findViewById(R.id.barWallet);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.OnItemClickListener(wallets.get(position));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.wallet_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int progress=0;
        Wallet wallet = wallets.get(position);
        holder.imgWallet.setBackgroundResource(img);
        holder.namaWallet.setText(wallet.getName());
        holder.saldoWallet.setText("Rp. "+wallet.getBalance());
        holder.background.setCardBackgroundColor(wallet.getColor());
        holder.cardViewImgWallet.setCardBackgroundColor(wallet.getColor() - 10101010);
        holder.barWallet.setMax(wallet.getInitBalance());
        holder.barWallet.setProgress(progress);

        for(Transaksi ts: transaksi){
            if(wallet.getIdWallet() == ts.getIdCreatorWallet()){
                if(ts.getCategory().equals("Expense")) progress += ts.getNominal();
                else if (ts.getCategory().equals("Income")) progress -= ts.getNominal();
            }
        }
        holder.barWallet.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
        walletsCopy = new ArrayList<>(wallets);
        notifyDataSetChanged();
    }

    public void setTransaksi(List<Transaksi> transaksi) {
        this.transaksi = transaksi;
        notifyDataSetChanged();
    }

    public Wallet getWallet(int position) {
        return wallets.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Wallet> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(walletsCopy);
            } else {
                String fillterPattern = constraint.toString().toLowerCase().trim();

                for (Wallet item : walletsCopy) {
                    if (item.getName().toLowerCase().contains(fillterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            wallets.clear();
            wallets.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClick{
        void OnItemClickListener(Wallet wallet);
    }

    public void SetOnItemClick(OnItemClick listener){
        this.listener = listener;
    }
}
