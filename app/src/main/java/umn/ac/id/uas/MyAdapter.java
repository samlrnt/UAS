package umn.ac.id.uas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    private List<Wallet> wallets;
    private List<Wallet> walletsCopy;
    private Context context;
    private int img = R.drawable.wallet_money_png_icon_7;

    public MyAdapter(Context context) {
        this.context = context;
        wallets = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView background, cardViewImgWallet, cardViewEdit, cardViewDelete;
        ImageView imgWallet;
        TextView namaWallet, saldoWallet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWallet = itemView.findViewById(R.id.imgWallet);
            namaWallet = itemView.findViewById(R.id.namaWallet);
            saldoWallet = itemView.findViewById(R.id.saldoWallet);
            background = itemView.findViewById(R.id.backgroundCardView);
            cardViewImgWallet = itemView.findViewById(R.id.cardViewImgWallet);
            cardViewEdit = itemView.findViewById(R.id.cardViewEdit);
            cardViewDelete = itemView.findViewById(R.id.cardViewDelete);

            cardViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                }
            });

           background.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
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
        Wallet wallet = wallets.get(position);
        holder.imgWallet.setBackgroundResource(img);
        holder.namaWallet.setText(wallet.getName());
        holder.saldoWallet.setText(String.valueOf(wallet.getBalance()));
        holder.background.setCardBackgroundColor(wallet.getColor());
        holder.cardViewImgWallet.setCardBackgroundColor(wallet.getColor() - 10101010);
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

}
