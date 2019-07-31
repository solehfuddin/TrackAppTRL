package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sofudev.trackapptrl.Adapter.Adapter_wishlist;
import com.sofudev.trackapptrl.Custom.GridSpacingItemDecoration;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Db.WishlistHelper;
import com.sofudev.trackapptrl.LocalDb.DbTrlHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelAddCart;
import com.sofudev.trackapptrl.LocalDb.Model.ModelWishlist;
import com.sofudev.trackapptrl.MainActivity;
import com.sofudev.trackapptrl.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.midtrans.sdk.corekit.utilities.Utils.dpToPx;

public class WishlistProductActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnStartShopping;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    WishlistHelper wishlistHelper;
    AddCartHelper addCartHelper;
    List<ModelWishlist> itemWishlist;
    Adapter_wishlist adapter_wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_product);

        btnBack = findViewById(R.id.wishlist_product_btnback);
        btnStartShopping = findViewById(R.id.wishlist_product_btnStartShopping);
        recyclerView = findViewById(R.id.wishlist_product_recyclerview);
        linearLayout = findViewById(R.id.wishlist_product_linearLayout);

        RecyclerView.LayoutManager verticalGrid = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(verticalGrid);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        wishlistHelper = WishlistHelper.getInstance(getApplicationContext());
        addCartHelper = AddCartHelper.getINSTANCE(getApplicationContext());
        wishlistHelper.open();

        showWishlist();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter  = wishlistHelper.countWishlist();

                Intent intent = new Intent();
                intent.putExtra("counter", counter);
                setResult(1, intent);

                finish();
            }
        });

        btnStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter  = wishlistHelper.countWishlist();

                Intent intent = new Intent();
                intent.putExtra("counter", counter);
                setResult(1, intent);

                finish();
            }
        });
    }

    private void showWishlist()
    {
        itemWishlist = wishlistHelper.getAllWishlist();
        adapter_wishlist = new Adapter_wishlist(this, itemWishlist, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
//                Toasty.success(getApplicationContext(), itemWishlist.get(sphpos).getProductid().toString(),
//                        Toast.LENGTH_SHORT).show();
                int btn = view.getId();

                switch (btn)
                {
                    case R.id.listitem_wishlist_btnRemove:

                        wishlistHelper.deleteWishlist(itemWishlist.get(pos).getProductid());

                        itemWishlist.remove(pos);
                        adapter_wishlist.notifyItemRemoved(pos);
//                itemWishlist = wishlistHelper.getAllWishlist();

                        if (itemWishlist.size() > 0)
                        {
                            linearLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                        adapter_wishlist.notifyDataSetChanged();

                        recyclerView.setAdapter(adapter_wishlist);

                        int counter  = wishlistHelper.countWishlist();

                        Intent intent = new Intent();
                        intent.putExtra("counter", counter);
                        setResult(1, intent);

                        break;

                    case R.id.listitem_wishlist_btnMoveCart:
                        wishlistHelper.open();
                        addCartHelper.open();

                        ModelWishlist modelWishlist = wishlistHelper.getWishlist(itemWishlist.get(pos).getProductid());

                        int delete = wishlistHelper.deleteWishlist(itemWishlist.get(pos).getProductid());

                        if (delete > 0)
                        {
                            int productId       = modelWishlist.getProductid();
                            String productName  = modelWishlist.getProductname();
                            String productCode  = modelWishlist.getProductcode();
                            int productQty      = 1;
                            int productPrice    = modelWishlist.getProductprice();
                            int productDisc     = modelWishlist.getProductdiscount();
                            int productDiscPrice= productPrice - (productDisc / 100 * productPrice);
                            String productImg   = modelWishlist.getProductimg();

                            ModelAddCart modelAddCart = new ModelAddCart();
                            modelAddCart.setProductId(productId);
                            modelAddCart.setProductName(productName);
                            modelAddCart.setProductCode(productCode);
                            modelAddCart.setProductQty(productQty);
                            modelAddCart.setProductPrice(productPrice);
                            modelAddCart.setProductDiscPrice(productDiscPrice);
                            modelAddCart.setProductDisc(productDisc);
                            modelAddCart.setNewProductPrice(productPrice);
                            modelAddCart.setNewProductDiscPrice(productDiscPrice);
                            modelAddCart.setProductImage(productImg);

                            long moving = addCartHelper.insertAddCart(modelAddCart);

                            if (moving > 0)
                            {
                                Toasty.success(getApplicationContext(), "Item has been moved to Cart", Toast.LENGTH_SHORT).show();

                                itemWishlist.remove(pos);
                                adapter_wishlist.notifyItemRemoved(pos);

                                if (itemWishlist.size() > 0)
                                {
                                    linearLayout.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }

                                adapter_wishlist.notifyDataSetChanged();

                                recyclerView.setAdapter(adapter_wishlist);

                                int countWish  = wishlistHelper.countWishlist();

                                Intent mIntent = new Intent();
                                mIntent.putExtra("counter", countWish);
                                setResult(1, mIntent);

//                                Intent cartIntent = new Intent(WishlistProductActivity.this, AddCartProductActivity.class);
//                                startActivity(cartIntent);

//                                finish();
                            }
                        }

//                        Toasty.info(getApplicationContext(), modelWishlist.getProductname(), Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        if (itemWishlist.size() > 0)
        {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter_wishlist);
    }
}
