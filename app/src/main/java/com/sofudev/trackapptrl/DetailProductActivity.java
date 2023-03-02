package com.sofudev.trackapptrl;

import android.content.Intent;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.OnBadgeCounter;
import com.sofudev.trackapptrl.Fragment.DetailFrameFragment;
import com.sofudev.trackapptrl.Fragment.HomeFragment;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Db.WishlistHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelWishlist;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DetailProductActivity extends AppCompatActivity implements OnBadgeCounter {

    Config config = new Config();
    String URLDETAILPRODUCT = config.Ip_address + config.frame_showdetail_product;

    int id, counter1, counter2, frameId;
    int click = 0;

    ImageView btnBack;
    SparkButton btnWishlist;

    AddCartHelper addCartHelper;
    WishlistHelper wishlistHelper;

    String frameName, frameSku, frameImage, framePrice, frameDisc, frameDiscPrice, frameAvailibilty,
            frameBrand, frameColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnBack     = findViewById(R.id.fragment_detailproduct_btnback);
        btnWishlist = findViewById(R.id.fragment_detailproduct_btnWishlist);

        getData();

        DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", "1");
        bundle.putString("product_id", String.valueOf(id));
        detailFrameFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.detailproduct_fragment_container, detailFrameFragment, "detail");
        fragmentTransaction.commit();

        addCartHelper = AddCartHelper.getINSTANCE(getApplicationContext());
        wishlistHelper = WishlistHelper.getInstance(getApplicationContext());

        showProductDetail(String.valueOf(id));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (from.contentEquals("0"))
//                {
//                    getActivity().getSupportFragmentManager().popBackStack();
//                }
//                else
//                {
//                    getActivity().finish();
//                }

//                int counter2  = addCartHelper.countAddCart();
//                int counter1 = wishlistHelper.countWishlist();
//
//                Intent intent2 = new Intent();
//                intent2.putExtra("counter", counter2);
//                getActivity().setResult(2, intent2);
//
//                Intent intent1 = new Intent();
//                intent1.putExtra("counter", counter1);
//                getActivity().setResult(1, intent1);

                wishlistHelper.open();
                addCartHelper.open();

                int count1 = wishlistHelper.countWishlist();
                int count2 = addCartHelper.countAddCart();

//                if (count1 > 0)
//                {
                    Intent intent1 = new Intent();
                    intent1.putExtra("counter1", count1);
                    intent1.putExtra("counter2", count2);
                    setResult(HomeFragment.RESULT_CODE, intent1);

                    Log.d("Sent Detail Wish : ", String.valueOf(count1));
                    Log.d("Sent Detail Cart : ", String.valueOf(count2));
//                }

//                if (count2 > 0)
//                {
//                    Intent intent2 = new Intent();
//                    intent2.putExtra("counter2", count2);
//                    setResult(HomeFragment.RESULT_CODE_CART, intent2);
//
//                    Log.d("Sent Detail Cart : ", String.valueOf(count2));
//                }

                finish();
            }
        });

        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click == 0)
                {
                    btnWishlist.setInactiveImage(R.drawable.ic_favorite_white);
                    click = 1;

                    wishlistHelper.open();

                    //Toasty.info(getApplicationContext(), String.valueOf(frameId), Toast.LENGTH_SHORT).show();

                    ModelWishlist item = new ModelWishlist();
                    item.setProductid(frameId);
                    item.setProductcode(frameSku);
                    item.setProductname(frameName);
                    item.setProductimg(frameImage);
                    item.setProductprice(Integer.valueOf(removeRupiah(framePrice)));
                    item.setProductdiscount(Integer.valueOf(removeDiskon(frameDisc)));

//                Toasty.info(getApplicationContext(), removeRupiah(frameDiscPrice) + " " +
//                        removeDiskon(frameDisc) + " " + String.valueOf(frameId), Toast.LENGTH_SHORT).show();

                    long status = wishlistHelper.insertWishlist(item);

                    if (status > 0)
                    {
//                   Toasty.success(getApplicationContext(), "Item " + String.valueOf(frameId)
//                           + " has add to wishlist", Toast.LENGTH_SHORT).show();

                        Toasty.success(getApplicationContext(), "Item has been added to wishlist", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    btnWishlist.setInactiveImage(R.drawable.ic_favorite_border_white);
                    click = 0;

                    int status = wishlistHelper.deleteWishlist(frameId);
                    if (status > 0)
                    {
                        Toasty.info(getApplicationContext(), "Item has been removed from wishlist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1)
//        {
//            counter1 = data.getIntExtra("counter", 0);
////            txt_countwishlist.setText(" " + counter + " ");
//            Log.d("DETAIL Wishlist : ", String.valueOf(counter1));
//        }
//        else if (requestCode == 2)
//        {
//            counter2 = data.getIntExtra("counter", 0);
////            txt_countCart.setText(" " + counter + " ");
//            Log.d("DETAIL CartList : ", String.valueOf(counter2));
//        }
//    }

    private void showProductDetail(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDETAILPRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        frameId     = jsonObject.getInt("frame_id");
                        frameName   = jsonObject.getString("frame_name");
                        frameSku    = jsonObject.getString("frame_sku");
                        frameImage  = jsonObject.getString("frame_image");
                        framePrice  = jsonObject.getString("frame_price");
                        frameDisc   = jsonObject.getString("frame_disc");
                        frameDiscPrice = jsonObject.getString("frame_disc_price");
                        frameAvailibilty = jsonObject.getString("frame_availibilty");
                        frameBrand  = jsonObject.getString("frame_brand");
                        frameColor  = jsonObject.getString("frame_color");

//                        allImg.add(frameImage);
                    }

                    frameDisc = frameDisc + " % ";
                    frameDiscPrice = "Rp. " + CurencyFormat(frameDiscPrice);
                    framePrice     = "Rp. " + CurencyFormat(framePrice);
//                    String stock = "IN STOCK";

//                    txtLenscode.setText(frameName);
//                    txtDiscPrice.setText(frameDiscPrice);
//                    txtRealPrice.setPaintFlags(txtRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    txtRealPrice.setText(framePrice);
//                    txtDisc.setText(frameDisc);
//                    txtBrand.setText(frameBrand);
//                    txtAvailability.setText(stock);
//                    txtDescription.setText(frameSku);

//                    Data_recent_view dataRecentView = new Data_recent_view();
//                    dataRecentView.setId(frameId);
//                    dataRecentView.setImage(frameImage);
//
//                    insertRecentSearch(dataRecentView);
//
////                    if (frameColor.contains("[") && frameColor.contains("]"))
////                    {
////                        frameColor.replace("[", "").replace("]", "");
////                    }
//
////                    Toasty.info(getApplicationContext(), "Color : " + frameColor, Toast.LENGTH_SHORT).show();
//
//                    showProductColor(frameColor);
//
//                    adapter_slider_product.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_lensa", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
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

    @Override
    public void countWishlist(int counter) {
        counter1 = counter;
        Log.d("DETAIL Wishlist : ", String.valueOf(counter));
    }

    @Override
    public void countCartlist(int counter) {
        counter2 = counter;
        Log.d("DETAIL CartList : ", String.valueOf(counter));
    }
}
