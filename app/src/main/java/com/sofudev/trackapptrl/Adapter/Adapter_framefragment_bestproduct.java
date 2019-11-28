package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.LocalDb.Db.WishlistHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelWishlist;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class Adapter_framefragment_bestproduct extends RecyclerView.Adapter<Adapter_framefragment_bestproduct.FragmentViewHolder> {

    private Context context;
    private List<Data_fragment_bestproduct> item;
    private RecyclerViewOnClickListener itemClick;
    WishlistHelper wishlistHelper;
    int click = 0;
    String TAG;

    public Adapter_framefragment_bestproduct(Context context, List<Data_fragment_bestproduct> item, RecyclerViewOnClickListener onClick, String TAG) {
        this.context = context;
        this.item = item;
        this.itemClick = onClick;
        wishlistHelper = WishlistHelper.getInstance(context);
        this.TAG = TAG;
    }

    @Override
    public FragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fragment_bestproduct, parent, false);
        FragmentViewHolder fragmentViewHolder = new FragmentViewHolder(view);
        return fragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(final FragmentViewHolder holder, final int position) {
        Picasso.with(context).load(item.get(position).getProduct_image()).into(holder.img_product);
//        Glide.with(context).load(item.get(position).getProduct_image()).into(holder.img_product);

        if (TAG.equals("main"))
        {
            holder.txt_productid.setText(item.get(position).getProduct_id());
            holder.txt_productcode.setText(item.get(position).getProduct_code());
            holder.txt_productname.setText(item.get(position).getProduct_name());
            holder.txt_brand.setText(item.get(position).getProduct_brand());
            //holder.txt_discprice.setText(item.get(position).getProduct_discprice());
            holder.txt_realprice.setText(item.get(position).getProduct_realprice());
            holder.txt_realprice.setVisibility(View.GONE);
            //holder.txt_discpercent.setText(item.get(position).getProduct_discprice());

//        holder.txt_discprice.setVisibility(View.GONE);
            holder.txt_productid.setVisibility(View.GONE);
            holder.txt_productcode.setVisibility(View.GONE);
//            String discount = item.get(position).getProduct_discpercent();
//            if (discount.contentEquals("null"))
//            {
//                holder.txt_discpercent.setVisibility(View.GONE);
//                holder.txt_discpercent.setPadding(20, 30, 20, 30);
//            }
//            else if (discount.contentEquals("0 %"))
//            {
//                holder.txt_discpercent.setVisibility(View.GONE);
//                holder.txt_discpercent.setPadding(20, 30, 20, 30);
//            }
//            else
//            {
//                holder.txt_discpercent.setText(item.get(position).getProduct_discpercent());
//            }

            int discount = Integer.parseInt(item.get(position).getProduct_discpercent());
            if (discount > 0)
            {
                String disc = String.valueOf(discount) + " %";
                holder.txt_discpercent.setVisibility(View.VISIBLE);
                holder.txt_discpercent.setText(disc);
            }
            else
            {
                holder.txt_discpercent.setVisibility(View.GONE);
//                holder.txt_discpercent.setPadding(20, 30, 20, 30);
            }

            int qty = Integer.parseInt(item.get(position).getProduct_qty());
            if (qty > 0)
            {
                holder.txt_available.setText("Stok Tersedia");
                holder.txt_available.setTextColor(Color.parseColor("#45ac2d"));
            }
            else
            {
                holder.txt_available.setText("Stok Habis");
                holder.txt_available.setTextColor(Color.parseColor("#f90606"));
            }

            holder.btn_wish.setVisibility(View.GONE);
            holder.btn_wish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (click == 0)
                    {
                        click = 1;
                        holder.btn_wish.setInactiveImage(R.drawable.ic_favorite);

                        int realprice = Integer.parseInt(removeRupiah(item.get(position).getProduct_realprice()));
                        int disc      = Integer.parseInt(removeDiskon(item.get(position).getProduct_discpercent()));
                        int discprice = realprice - (realprice * disc/100);

//                Toasty.info(context, "Price = " + realprice + " , disc " + disc + " , discprice " + discprice, Toast.LENGTH_SHORT).show();

                        wishlistHelper.open();

                        ModelWishlist modelWishlist = new ModelWishlist();
                        modelWishlist.setProductid(Integer.parseInt(item.get(position).getProduct_id()));
                        modelWishlist.setProductcode(item.get(position).getProduct_code());
                        modelWishlist.setProductname(item.get(position).getProduct_name());
                        modelWishlist.setProductimg(item.get(position).getProduct_image());
                        modelWishlist.setProductprice(discprice);
                        modelWishlist.setProductdiscount(disc);

                        long status = wishlistHelper.insertWishlist(modelWishlist);

                        if (status > 0)
                        {
                            Toasty.success(context, "Item has been added to wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        click = 0;

                        holder.btn_wish.setInactiveImage(R.drawable.ic_favorite_border);

                        int status = wishlistHelper.deleteWishlist(Integer.parseInt(item.get(position).getProduct_id()));
                        if (status > 0)
                        {
                            Toasty.info(context, "Item has been removed from wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else
        {
            holder.txt_productid.setText(item.get(position).getProduct_id());
            holder.txt_productcode.setText(item.get(position).getProduct_code());
            holder.txt_productname.setText(item.get(position).getProduct_name());
            holder.txt_brand.setText(item.get(position).getProduct_brand());
            //holder.txt_discprice.setText(item.get(position).getProduct_discprice());
            holder.txt_realprice.setText(item.get(position).getProduct_realprice());
            //holder.txt_discpercent.setText(item.get(position).getProduct_discprice())

//        holder.txt_discprice.setVisibility(View.GONE);
            holder.txt_productid.setVisibility(View.GONE);
            holder.txt_productcode.setVisibility(View.GONE);
//            String discount = item.get(position).getProduct_discpercent();
//            if (discount.contentEquals("null"))
//            {
//                holder.txt_discpercent.setVisibility(View.GONE);
//                holder.txt_discpercent.setPadding(20, 30, 20, 30);
//            }
//            else if (discount.contentEquals("0 %"))
//            {
//                holder.txt_discpercent.setVisibility(View.GONE);
//                holder.txt_discpercent.setPadding(20, 30, 20, 30);
//            }
//            else
//            {
//                holder.txt_discpercent.setText(item.get(position).getProduct_discpercent());
//            }

            int discount = Integer.parseInt(item.get(position).getProduct_discpercent());
            if (discount > 0)
            {
                String disc = String.valueOf(discount) + " %";
                holder.txt_discpercent.setVisibility(View.VISIBLE);
                holder.txt_discpercent.setText(disc);
            }
            else
            {
                holder.txt_discpercent.setVisibility(View.GONE);
//                holder.txt_discpercent.setPadding(20, 30, 20, 30);
            }

            int qty = Integer.parseInt(item.get(position).getProduct_qty());
            if (qty > 0)
            {
                holder.txt_available.setText("Stok Tersedia");
                holder.txt_available.setTextColor(Color.parseColor("#45ac2d"));
            }
            else
            {
                holder.txt_available.setText("Stok Habis");
                holder.txt_available.setTextColor(Color.parseColor("#f90606"));
            }

            holder.btn_wish.setVisibility(View.GONE);
            holder.btn_wish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (click == 0)
                    {
                        click = 1;
                        holder.btn_wish.setInactiveImage(R.drawable.ic_favorite);

                        int realprice = Integer.parseInt(removeRupiah(item.get(position).getProduct_realprice()));
                        int disc      = Integer.parseInt(removeDiskon(item.get(position).getProduct_discpercent()));
                        int discprice = realprice - (realprice * disc/100);

//                Toasty.info(context, "Price = " + realprice + " , disc " + disc + " , discprice " + discprice, Toast.LENGTH_SHORT).show();

                        wishlistHelper.open();

                        ModelWishlist modelWishlist = new ModelWishlist();
                        modelWishlist.setProductid(Integer.parseInt(item.get(position).getProduct_id()));
                        modelWishlist.setProductcode(item.get(position).getProduct_code());
                        modelWishlist.setProductname(item.get(position).getProduct_name());
                        modelWishlist.setProductimg(item.get(position).getProduct_image());
                        modelWishlist.setProductprice(discprice);
                        modelWishlist.setProductdiscount(disc);

                        long status = wishlistHelper.insertWishlist(modelWishlist);

                        if (status > 0)
                        {
                            Toasty.success(context, "Item has been added to wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        click = 0;

                        holder.btn_wish.setInactiveImage(R.drawable.ic_favorite_border);

                        int status = wishlistHelper.deleteWishlist(Integer.parseInt(item.get(position).getProduct_id()));
                        if (status > 0)
                        {
                            Toasty.info(context, "Item has been removed from wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    private String removeRupiah(String price)
    {
        String output;
        output = price.replace("Rp ", "").replace("Rp", "").replace(".","").trim();

        return output;
    }

    private String removeDiskon(String diskon)
    {
        String output;
        output = diskon.replace("%", "").trim();

        return output;
    }


    class FragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView img_product;
        UniversalFontTextView txt_productid, txt_discpercent, txt_productname, txt_productcode, txt_realprice, txt_discprice,
                                txt_brand, txt_available;
        SparkButton btn_wish;

        public FragmentViewHolder(View itemView) {
            super(itemView);

            btn_wish        = itemView.findViewById(R.id.listitem_fragment_bestproduct_btnWish);
            img_product     = (ImageView) itemView.findViewById(R.id.listitem_fragment_bestproduct_imgProduct);
            txt_productid   = (UniversalFontTextView) itemView.findViewById(R.id.listitem_fragment_bestproduct_txtProductid);
            txt_discpercent = (UniversalFontTextView) itemView.findViewById(R.id.listitem_fragment_bestproduct_txtDiscpercent);
            txt_productname = (UniversalFontTextView) itemView.findViewById(R.id.listitem_fragment_bestproduct_txtProductname);
            txt_productcode = (UniversalFontTextView) itemView.findViewById(R.id.listitem_fragment_bestproduct_txtProductcode);
            txt_realprice   = (UniversalFontTextView) itemView.findViewById(R.id.listitem_fragment_bestproduct_txtRealprice);
            txt_brand       = itemView.findViewById(R.id.listitem_fragment_bestproduct_txtBrand);
            txt_available   = itemView.findViewById(R.id.listitem_fragment_bestproduct_txtProductavailable);
//            txt_discprice   = (UniversalFontTextView) itemView.findViewById(R.id.listitem_fragment_bestproduct_txtDiscprice);

            //txt_realprice.setPaintFlags(txt_realprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            itemView.setOnClickListener(this);
//            btn_wish.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txt_productid.getText().toString());

//            switch (v.getId())
//            {
//                case R.id.listitem_fragment_bestproduct_btnWish:
//                    wishlistHelper.insertWishlist()
//                    break;
//            }
        }
    }
}
