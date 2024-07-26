package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Form.FormSpFrameActivity;
import com.sofudev.trackapptrl.LocalDb.Db.AddFrameSpHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelFrameSp;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_add_framesp extends RecyclerView.Adapter<Adapter_add_framesp.ViewHolder> {
    private Context context;
    private List<ModelFrameSp> item;
    private RecyclerViewOnClickListener itemClick;
    private AddFrameSpHelper addFrameSpHelper;

    public Adapter_add_framesp(Context context, List<ModelFrameSp> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
        addFrameSpHelper = AddFrameSpHelper.getINSTANCE(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, viewGroup, false);
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_framesp, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        final ModelFrameSp data = item.get(i);

//        Picasso.with(context).load(data.getProductImage()).resize(100, 100).into(holder.img_product);
        holder.txt_productid.setText(String.valueOf(data.getProductId()));
        holder.txt_productname.setText(data.getProductName());
//        holder.txt_productname.setText(data.getProductCollect());
        holder.txt_productqty.setText(String.valueOf(data.getProductQty()));
        holder.ed_productqty.setText(String.valueOf(data.getProductQty()));
        holder.txt_productprice.setText("Rp. " + CurencyFormat(String.valueOf(data.getNewProductDiscPrice())));
        holder.txt_productflag.setText(data.getProductFlag());

        final int stock = data.getProductStock();
        if (stock < 0)
        {
            holder.txt_stockkurang.setText("Stok tersisa : " + data.getProductTempStock() + " PCS");
            holder.txt_stockkurang.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.txt_stockkurang.setVisibility(View.GONE);
        }

        if (data.getProductFlag().equals("STORE"))
        {
            holder.txt_productflag.setBackgroundColor(Color.parseColor("#45ac2d"));
        }
        else
        {
            holder.txt_productflag.setBackgroundColor(Color.parseColor("#ff9100"));
        }

        holder.txt_productflag.setTextColor(Color.parseColor("#ffffff"));

        int disc = data.getProductDisc();
        if (disc > 0)
        {
            holder.txt_productdisc.setText(String.valueOf(data.getProductDisc() + " % "));
            holder.txt_producdiscprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txt_producdiscprice.setText("Rp. " + CurencyFormat(String.valueOf(data.getNewProductPrice())));
//            holder.txt_productdisc.setVisibility(View.GONE);
//            holder.txt_producdiscprice.setVisibility(View.GONE);
        }
        else
        {
            holder.txt_productdisc.setVisibility(View.GONE);
            holder.txt_producdiscprice.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1)
        {
            holder.ed_productqty.setFocusableInTouchMode(false);
            holder.ed_productqty.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    holder.ed_productqty.setFocusable(true);
                    holder.ed_productqty.setFocusableInTouchMode(true);
                    return false;
                }
            });
        }

        holder.ed_productqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (holder.ed_productqty.getText().length() < 1)
                    {
                        holder.ed_productqty.setText("1");
                    }

                    if (holder.ed_productqty.getText().charAt(0) == '0')
                    {
                        holder.ed_productqty.setText("1");
                    }

                    addFrameSpHelper.open();
                    ModelFrameSp modelFrameSp = addFrameSpHelper.getAddFrameSp(data.getProductId());

                    int price = modelFrameSp.getProductPrice();
                    int qty   = Integer.parseInt(holder.ed_productqty.getText().toString());
                    int stock = modelFrameSp.getProductStock();
                    int discprice = modelFrameSp.getProductDiscPrice();

                    int newprice = price * qty;
                    int newdiscprice = discprice * qty;

                    stock = stock - qty;
                    Log.d("Stok Change", String.valueOf(stock));
                    modelFrameSp.setProductStock(stock);

                    modelFrameSp.setNewProductPrice(newprice);
                    modelFrameSp.setProductQty(qty);
                    modelFrameSp.setNewProductDiscPrice(newdiscprice);
                    modelFrameSp.setProductFlag(data.getProductFlag());
                    addFrameSpHelper.updateFrameSpQty(modelFrameSp);

                    ((FormSpFrameActivity)context).handleQty(
                            modelFrameSp,
                            i,
                            addFrameSpHelper.countTotalQty(),
                            addFrameSpHelper.countTotalPrice()
                    );
                }
                else
                {
                    ((FormSpFrameActivity)context).disableButtonSubmit();
                }
            }
        });

        holder.ed_productqty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if ( v.hasFocus())
                    {
                        v.clearFocus();
                        if ( v instanceof EditText) {
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1)
                            {
                                holder.ed_productqty.setFocusable(false);
                            }
                        }

                        ((FormSpFrameActivity)context).enableButtonSubmit();
                    }


                    if (holder.ed_productqty.getText().length() < 1)
                    {
                        holder.ed_productqty.setText("1");
                    }

                    if (holder.ed_productqty.getText().charAt(0) == '0')
                    {
                        holder.ed_productqty.setText("1");
                    }

                    addFrameSpHelper.open();
                    ModelFrameSp modelFrameSp = addFrameSpHelper.getAddFrameSp(data.getProductId());

                    int price = modelFrameSp.getProductPrice();
                    int qty   = Integer.parseInt(holder.ed_productqty.getText().toString());
                    int stock = modelFrameSp.getProductStock();
                    int discprice = modelFrameSp.getProductDiscPrice();

                    int newprice = price * qty;
                    int newdiscprice = discprice * qty;

                    stock = stock - qty;
                    Log.d("Stok Change", String.valueOf(stock));
                    modelFrameSp.setProductStock(stock);

                    modelFrameSp.setNewProductPrice(newprice);
                    modelFrameSp.setProductQty(qty);
                    modelFrameSp.setNewProductDiscPrice(newdiscprice);
                    modelFrameSp.setProductFlag(data.getProductFlag());
                    addFrameSpHelper.updateFrameSpQty(modelFrameSp);

                    ((FormSpFrameActivity)context).handleQty(
                            modelFrameSp,
                            i,
                            addFrameSpHelper.countTotalQty(),
                            addFrameSpHelper.countTotalPrice()
                    );
                }

                return false;
            }
        });

        holder.ed_productqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1)
                {
                    holder.btn_plus.setEnabled(false);
                    holder.btn_minus.setEnabled(false);
                }
                else
                {
                    holder.btn_plus.setEnabled(true);
                    holder.btn_minus.setEnabled(true);
                }
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_product, btn_plus, btn_minus;
        BootstrapEditText ed_productqty;
        UniversalFontTextView txt_productname, txt_productid, txt_productqty, txt_productprice, txt_productdisc, txt_productflag,
                txt_producdiscprice, txt_stockkurang, btn_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.item_cartframesp_imageView);
            btn_plus = itemView.findViewById(R.id.item_cartframesp_btnPlus);
            btn_minus= itemView.findViewById(R.id.item_cartframesp_btnMinus);
            txt_productname = itemView.findViewById(R.id.item_cartframesp_txtName);
            txt_productid = itemView.findViewById(R.id.item_cartframesp_txtId);
            txt_productqty = itemView.findViewById(R.id.item_cartframesp_txtQty);
            ed_productqty = itemView.findViewById(R.id.item_cartframesp_edQty);
            txt_productprice = itemView.findViewById(R.id.item_cartframesp_txtPrice);
            txt_producdiscprice = itemView.findViewById(R.id.item_cartframesp_txtPriceDisc);
            txt_productdisc = itemView.findViewById(R.id.item_cartframesp_txtDisc);
            txt_stockkurang = itemView.findViewById(R.id.item_cartframesp_lblStockKurang);
            txt_productflag = itemView.findViewById(R.id.item_cartframesp_lblFlag);
            btn_remove = itemView.findViewById(R.id.item_cartframesp_btnRemove);

            ed_productqty.setOnClickListener(this);
            btn_remove.setOnClickListener(this);
            btn_plus.setOnClickListener(this);
            btn_minus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txt_productid.getText().toString());
        }
    }
}
