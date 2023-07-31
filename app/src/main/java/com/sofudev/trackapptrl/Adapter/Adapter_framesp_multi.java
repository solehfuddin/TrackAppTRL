package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.MultiSelectSpFrame;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Form.FormSpFrameActivity;
import com.sofudev.trackapptrl.Fragment.BannerFragment;
import com.sofudev.trackapptrl.Fragment.PhotoZoomFragment;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.ZoomImageActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoomConfig;

public class Adapter_framesp_multi extends RecyclerView.Adapter<Adapter_framesp_multi.ViewHolder> {
    private Context context;
    private List<Data_fragment_bestproduct> item;
    private ArrayList<Data_fragment_bestproduct> returnItem = new ArrayList<>();
    private MultiSelectSpFrame callback;
    Adapter_slider_product adapter_slider_product;
    List<String> allImg = new ArrayList<>();

    public Adapter_framesp_multi(Context context, List<Data_fragment_bestproduct> item, MultiSelectSpFrame callback) {
        this.context = context;
        this.item = item;
        this.callback = callback;

        UniversalFontComponents.init(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.recycler_onhand, parent, false);

        View v = LayoutInflater.from(context).inflate(R.layout.recycler_onhand_with_thumb, parent, false);

        return new Adapter_framesp_multi.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Data_fragment_bestproduct dt = item.get(i);
        boolean isSameDate;

        viewHolder.txtItemId.setText(dt.getProduct_id());
        viewHolder.txtItemCode.setText(dt.getProduct_code());
        viewHolder.txtItemName.setText(dt.getProduct_name());
        viewHolder.txtItemImg.setText(dt.getProduct_image());
        viewHolder.txtItemPrice.setText(dt.getProduct_realprice());
        viewHolder.txtItemDisc.setText(dt.getProduct_discprice());
        viewHolder.txtItemBrand.setText(dt.getProduct_brand());
        viewHolder.txtCounter.setText(dt.getProduct_qty());
        viewHolder.txtItemWeight.setText(dt.getProduct_weight());

        if (!dt.getProduct_collect().isEmpty())
        {
            if (i == 0) {
                isSameDate = true;
            } else {
                final String date = dt.getProduct_entry();
                final String prevDate = item.get(i - 1).getProduct_entry();

                Log.d("index date : ", date);
                Log.d("Prev date : ", prevDate);

                isSameDate = !date.equals(prevDate);
            }

            Log.d("Issame date : ", String.valueOf(isSameDate));
            viewHolder.txtItemCollection.setText(dt.getProduct_collect());
            viewHolder.txtItemSubqty.setText(dt.getProduct_qtysubtotal() + " Pcs");
            viewHolder.txtItemCollection.setVisibility(isSameDate ? View.VISIBLE : View.GONE);
            viewHolder.txtItemSubqty.setVisibility(isSameDate ? View.VISIBLE : View.GONE);
        }
        else
        {
            viewHolder.txtItemCollection.setVisibility(View.GONE);
            viewHolder.txtItemSubqty.setVisibility(View.GONE);
        }

        int count = Integer.parseInt(dt.getProduct_qty());
        if (count > 0)
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#45ac2d"));
        }
        else
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#f90606"));
        }

        viewHolder.imgSelect.setVisibility(item.get(i).isChecked() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.linearSelect.setBackgroundResource(item.get(i).isChecked() ? R.drawable.border_select : R.drawable.border_black);

        viewHolder.bind(item.get(i));

//        adapter_slider_product = new Adapter_slider_product(allImg, this.context);
//        allImg.add(item.get(i).getProduct_image());
//        adapter_slider_product.notifyDataSetChanged();

        viewHolder.imgChooser.setVisibility(View.VISIBLE);
        viewHolder.imgZoom.setVisibility(View.GONE);
        viewHolder.imgPager.setVisibility(View.GONE);
//        viewHolder.imgPager.setAdapter(adapter_slider_product);

        Picasso.with(context).
                cancelRequest(viewHolder.imgChooser);
        if (!item.get(i).getProduct_image().isEmpty())
        {
            viewHolder.setIsRecyclable(false);
            Picasso.with(context)
                    .load(item.get(i).getProduct_image())
                    .fit()
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.pic_holder)
                    .error(R.drawable.pic_holder)
                    .into(viewHolder.imgChooser);
        }
        else
        {
            viewHolder.setIsRecyclable(true);
            viewHolder.imgChooser.setImageResource(R.drawable.pic_holder);
        }

        viewHolder.imgChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.get(i).getProduct_image().isEmpty()) {
                    Intent intent = new Intent(context, ZoomImageActivity.class);
                    intent.putExtra("image_url", dt.getProduct_image());
                    intent.putExtra("frame_description", dt.getProduct_name());
                    context.startActivity(intent);
                }
                else
                {
                    Toasty.warning(context, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Picasso.with(context).cancelRequest(viewHolder.imgZoom);
//        if (!item.get(i).getProduct_image().isEmpty())
//        {
//            viewHolder.setIsRecyclable(false);
//            Picasso.with(context)
//                    .load(item.get(i).getProduct_image())
//                    .error(R.drawable.pic_holder)
//                    .fit()
//                    .networkPolicy(NetworkPolicy.NO_CACHE)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .into(viewHolder.imgZoom);
//        }
//        else
//        {
//            viewHolder.setIsRecyclable(true);
//            viewHolder.imgZoom.setImageResource(R.drawable.pic_holder);
//        }
//
//        viewHolder.imgZoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!item.get(i).getProduct_image().isEmpty()) {
//                    Intent intent = new Intent(context, ZoomImageActivity.class);
//                    intent.putExtra("image_url", dt.getProduct_image());
//                    context.startActivity(intent);
//                }
//                else
//                {
//                    Toasty.warning(context, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        Fragment fragment = new PhotoZoomFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("imgname", item.get(i).getProduct_image());
//        fragment.setArguments(bundle);
//        viewHolder.setFragment(fragment);

//        FragmentTransaction fT = fM.beginTransaction();
//        fT.replace(R.id.fragment_photo_image, fragment);
////        fT.addToBackStack(null);
//        fT.commit();
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtItemId, txtItemCode, txtItemName, txtItemImg, txtItemPrice, txtItemDisc,
                txtItemBrand, txtCounter, txtItemWeight, txtItemCollection, txtItemSubqty;
        FrameLayout frameLayout;
        ImageView imgSelect;
        ImageView imgChooser;
        ImageViewZoom imgZoom;
        ViewPager imgPager;
        LinearLayout linearTitle;
        ConstraintLayout linearSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemId   = itemView.findViewById(R.id.recycler_onhand_txtItemId);
            txtItemName = itemView.findViewById(R.id.recycler_onhand_txtItemCode);
            txtItemCode = itemView.findViewById(R.id.recycler_onhand_txtItemName);
            txtItemImg  = itemView.findViewById(R.id.recycler_onhand_txtItemImg);
            txtItemPrice= itemView.findViewById(R.id.recycler_onhand_txtItemPrice);
            txtItemDisc = itemView.findViewById(R.id.recycler_onhand_txtItemDisc);
            txtItemBrand= itemView.findViewById(R.id.recycler_onhand_txtItemBrand);
            txtCounter  = itemView.findViewById(R.id.recycler_onhand_txtCounter);
            txtItemWeight = itemView.findViewById(R.id.recycler_onhand_txtItemWeight);
            txtItemCollection = itemView.findViewById(R.id.recycler_onhand_txtItemCollection);
            txtItemSubqty = itemView.findViewById(R.id.recycler_onhand_txtItemQty);
            imgSelect   = itemView.findViewById(R.id.recycler_onhand_imgSelect);
            linearSelect= itemView.findViewById(R.id.recycler_onhand_linearselect);
            linearTitle = itemView.findViewById(R.id.recycler_onhand_lineartitle);

            frameLayout = itemView.findViewById(R.id.recycler_onhand_fragment);
            imgChooser = itemView.findViewById(R.id.recycler_onhand_imgChooser);
            imgZoom    = itemView.findViewById(R.id.recycler_onhand_imageZoom);
            imgPager   = itemView.findViewById(R.id.recycler_onhand_viewPager);
        }

        void setFragment(Fragment fragment){
//            FragmentManager fm = ((FormSpFrameActivity) context).getSupportFragmentManager();
//            FragmentTransaction fT = fragment.getChildFragmentManager();
//            fT.replace(frameLayout.getId(), fragment);
//            fT.commit();

            FragmentManager manager=((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction Ft=manager.beginTransaction();
            Ft.replace(R.id.recycler_onhand_fragment, fragment);

            Ft.commit();
        }

        void bind(final Data_fragment_bestproduct item){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(item.getProduct_qty()) > 0)
                    {
                        item.setChecked(!item.isChecked());
                        imgSelect.setVisibility(item.isChecked() ? View.VISIBLE : View.INVISIBLE);
                        linearSelect.setBackgroundResource(item.isChecked() ? R.drawable.border_select : R.drawable.border_black);
                        if (item.isChecked()){
                            returnItem.add(item);
                        }
                        else
                        {
                            returnItem.remove(item);
                        }
                    }
                    else
                    {
                        Toasty.warning(context, "Item tidak tersedia saat ini", Toast.LENGTH_SHORT).show();
                    }

                    callback.passResult(returnItem);
                }
            });
        }
    }
}
