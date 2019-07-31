package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Form.AddCartProductActivity;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Db.WishlistHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelAddCart;
import com.sofudev.trackapptrl.LocalDb.Model.ModelWishlist;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Adapter_wishlist extends RecyclerView.Adapter<Adapter_wishlist.ViewHolder> {
    private Context context;
    private List<ModelWishlist> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_wishlist(Context context, List<ModelWishlist> item, RecyclerViewOnClickListener onClick) {
        this.context = context;
        this.item = item;
        this.itemClick = onClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_fragment_wishlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context).load(item.get(position).getProductimg()).fit().into(holder.img_product);
        holder.txt_discprice.setText("Rp. " + CurencyFormat(String.valueOf(item.get(position).getProductprice())));
        holder.txt_productid.setText(String.valueOf(item.get(position).getProductid()));
        holder.txt_discpercent.setText(String.valueOf(item.get(position).getProductdiscount() + " %"));
        holder.txt_productcode.setText(item.get(position).getProductcode());
        holder.txt_productname.setText(item.get(position).getProductname());

        holder.txt_productcode.setVisibility(View.GONE);
        holder.txt_productid.setVisibility(View.GONE);

//        holder.btnMoveToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ModelWishlist modelWishlist = item.get(position);
//
//                int productId       = modelWishlist.getProductid();
//                String productName  = modelWishlist.getProductname();
//                String productCode  = modelWishlist.getProductcode();
//                int productQty      = 1;
//                int productPrice    = modelWishlist.getProductprice();
//                int productDisc     = modelWishlist.getProductdiscount();
//                int productDiscPrice= productPrice - (productDisc / 100 * productPrice);
//                String productImg   = modelWishlist.getProductimg();
//
//                WishlistHelper wishlistHelper = WishlistHelper.getInstance(context);
//                AddCartHelper addCartHelper   = AddCartHelper.getINSTANCE(context);
//
//                wishlistHelper.open();
//                addCartHelper.open();
//
//                int delete = wishlistHelper.deleteWishlist(productId);
//
//                if (delete > 0)
//                {
//                    ModelAddCart modelAddCart = new ModelAddCart();
//                    modelAddCart.setProductId(productId);
//                    modelAddCart.setProductName(productName);
//                    modelAddCart.setProductCode(productCode);
//                    modelAddCart.setProductQty(productQty);
//                    modelAddCart.setProductPrice(productPrice);
//                    modelAddCart.setProductDiscPrice(productDiscPrice);
//                    modelAddCart.setProductDisc(productDisc);
//                    modelAddCart.setProductImage(productImg);
//
//                    long moving = addCartHelper.insertAddCart(modelAddCart);
//
//                    if (moving > 0)
//                    {
//                        Toasty.success(context, "Item has been moved to Cart", Toast.LENGTH_SHORT).show();
//
//                        addCartHelper.open();
//                        int counter  = addCartHelper.countAddCart();
//                    }
//                }
//            }
//        });
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_product, img_remove;
        UniversalFontTextView txt_discpercent, txt_productid, txt_productname, txt_productcode, txt_discprice;
        RippleView btnMoveToCart;

        public ViewHolder(View itemView) {
            super(itemView);

            img_product     = itemView.findViewById(R.id.listitem_wishlist_imgProduct);
            txt_discpercent = itemView.findViewById(R.id.listitem_wishlist_txtDiscpercent);
            txt_productid   = itemView.findViewById(R.id.listitem_wishlist_txtProductid);
            txt_productname = itemView.findViewById(R.id.listitem_wishlist_txtProductname);
            txt_productcode = itemView.findViewById(R.id.listitem_wishlist_txtProductcode);
            txt_discprice   = itemView.findViewById(R.id.listitem_wishlist_txtRealprice);
            btnMoveToCart   = itemView.findViewById(R.id.listitem_wishlist_btnMoveCart);
            btnMoveToCart.setOnClickListener(this);
            img_remove      = itemView.findViewById(R.id.listitem_wishlist_btnRemove);
            img_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, this.getLayoutPosition(), txt_productid.getText().toString());
        }
    }
}
