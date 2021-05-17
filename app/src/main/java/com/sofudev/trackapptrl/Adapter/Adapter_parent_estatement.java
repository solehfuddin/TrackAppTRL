package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_parent_estatement;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_parent_estatement extends RecyclerView.Adapter<Adapter_parent_estatement.ViewHolder> {
    private Context context;
    private List<Data_parent_estatement> item;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public Adapter_parent_estatement(Context context, List<Data_parent_estatement> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_estatement_parent, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Data_parent_estatement data = item.get(i);

        LinearLayoutManager lm = new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        lm.setInitialPrefetchItemCount(data.getListChildEstatement().size());

        Adapter_child_estatement childAdapter = new Adapter_child_estatement(context, data.getListChildEstatement(), new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {

            }
        });
        holder.recyclerView.setLayoutManager(lm);
        holder.recyclerView.setAdapter(childAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);

        holder.txtTitle.setText(data.getParentTitle());
        holder.txtSubtotal.setText("Rp. " + CurencyFormat(data.getParentSubtotal()));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0,00";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtTitle, txtSubtotal;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.item_parent_estatement_txttitle);
            txtSubtotal = itemView.findViewById(R.id.item_parent_estatement_txtsubtotal);
            recyclerView = itemView.findViewById(R.id.item_parent_estatement_recycler);
        }
    }
}
