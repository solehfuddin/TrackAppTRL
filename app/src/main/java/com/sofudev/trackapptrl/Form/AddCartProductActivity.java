package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_add_cart;
import com.sofudev.trackapptrl.Adapter.Adapter_courier_service;
import com.sofudev.trackapptrl.Adapter.Adapter_paymentmethod;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_frame_header;
import com.sofudev.trackapptrl.Data.Data_frame_item;
import com.sofudev.trackapptrl.Data.Data_paymentmethod;
import com.sofudev.trackapptrl.Data.Data_spin_shipment;
import com.sofudev.trackapptrl.LocalDb.Db.AddCartHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelAddCart;
import com.sofudev.trackapptrl.R;
import com.ssomai.android.scalablelayout.ScalableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import es.dmoral.toasty.Toasty;

public class AddCartProductActivity extends AppCompatActivity {
    Config config = new Config();
    String URL_ALLDATA = config.Ip_address + config.spinner_shipment_getAllData;
    String URL_GENID   = config.Ip_address + config.frame_generate_orderId;
    String URL_GETORAID = config.Ip_address + config.frame_getoracle_id;
    String URL_INSERTHEADER = config.Ip_address + config.frame_insert_header;
    String URL_INSERTNONTPAYMENT = config.Ip_address + config.frame_insert_statusnonpayment;
    String URL_INSERTLINETITEM   = config.Ip_address + config.frame_insert_lineitem;
    String URL_UPDATESTOCK = config.Ip_address + config.frame_update_stockframe;
    String URL_ALLPAYMENT = config.Ip_address + config.payment_method_showAllData;
    String URL_POSTBILLING= config.payment_method_postBilling;
    String URL_INSERTBILLQR = config.Ip_address + config.payment_insert_billingQR;
    String URL_INSERTBILLVA = config.Ip_address + config.payment_insert_billingVA;
    String URL_INSERTBILLCC = config.Ip_address + config.payment_insert_billingCC;
    String URL_INSERTBILLLOAN = config.Ip_address + config.payment_insert_billingLOAN;
    String URL_INSERTBILLDEPOSIT = config.Ip_address + config.payment_insert_billingDeposit;
    String URL_SHOWSESSIONPAYMENTQR = config.Ip_address + config.payment_show_expiredDurationQR;
    String URL_SHOWSESSIONPAYMENTVA = config.Ip_address + config.payment_show_expiredDurationVA;
    String URL_SHOWSESSIONPAYMENTCC = config.Ip_address + config.payment_show_expiredDurationCC;
    String URL_SHOWSESSIONPAYMENTLOAN = config.Ip_address + config.payment_show_expiredDurationLOAN;
    String URL_SHOWSESSIONPAYMENTDEPOSIT = config.Ip_address + config.payment_show_expiredDurationDeposit;
    String URL_GETMEMBERFLAG        = config.Ip_address + config.memberflag_getStatus;
    String GETACTIVESALE_URL        = config.Ip_address + config.flashsale_getActiveSale;

    ImageView btn_back;
    ScrollView nestedDetail;
    CardView cardView, cardViewNonPayment, cardViewPayment;
    ScalableLayout scalableCourier;
    LinearLayout linearLayout, linearPayment;
    RecyclerView recyclerView, recyclerCourier;
    UniversalFontTextView txtPrice, txtDisc, txtTitleShip, txtShipping, txtTotal, txtInfoShipping;
    UniversalFontTextView txtPricePayment, txtDiscPayment, txtTitleShipPayment, txtShippingPayment, txtTotalPayment;
//    Spinner spinShipment;
    Button btnContinueShop, btnContinue;

    ListView listPayment;
    RippleView btnChoosePayment;
    CustomLoading customLoading;

    AddCartHelper addCartHelper;
    List<ModelAddCart> itemCart;
    List<Data_spin_shipment> shipmentList = new ArrayList<>();
    List<Data_paymentmethod> paymentmethodList = new ArrayList<>();
    Adapter_add_cart adapterAddCart;
    Adapter_courier_service adapterCourierService;
//    Adapter_spinner_shipment adapter_spinner_shipment;
    Adapter_paymentmethod adapter_paymentmethod;

    Data_frame_header dataFrameHeader;
    Data_frame_item dataFrameItem;

    int totalPrice, totalDisc, priceDisc, totalAllPrice, shippingId, flagPayment;
    String opticId, opticName, opticProvince, opticUsername, opticCity, opticFlag, opticAddress, shipmentPrice, shippingName,
            shippingService, orderId, subcustId, subcustLocId, estimasi, flashSaleInfo, opticLevel, salesName;
    String selectedItem, kodeBilling, duration, expDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart_product);

        UniversalFontComponents.init(this);
        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        customLoading = new CustomLoading(this);

        btn_back = findViewById(R.id.addcart_product_btnback);
        nestedDetail = findViewById(R.id.addcart_product_nestedscroll);
        recyclerView = findViewById(R.id.addcart_product_recycler);
        recyclerCourier = findViewById(R.id.addcart_product_recyclerCourier);
        scalableCourier = findViewById(R.id.addcart_product_scalableCourier);
        txtPrice = findViewById(R.id.addcart_product_txtitemprice);
        txtPricePayment = findViewById(R.id.addcart_product_txtitemprice_payment);
        txtDisc = findViewById(R.id.addcart_product_txtitemdisc);
        txtDiscPayment = findViewById(R.id.addcart_product_txtitemdisc_payment);
        txtTitleShip = findViewById(R.id.addcart_product_txttitleship);
        txtTitleShipPayment = findViewById(R.id.addcart_product_txttitleship_payment);
        txtShipping = findViewById(R.id.addcart_product_txtitemship);
        txtShippingPayment = findViewById(R.id.addcart_product_txtitemship_payment);
        txtInfoShipping = findViewById(R.id.addcart_product_txtInfoShipping);
        txtTotal = findViewById(R.id.addcart_product_txttotalprice);
        txtTotalPayment = findViewById(R.id.addcart_product_txttotalprice_payment);
        linearLayout = findViewById(R.id.addcart_product_linearLayout);
        linearPayment = findViewById(R.id.addcart_product_linearPayment);
        btnContinueShop = findViewById(R.id.addcart_product_btnContinueShopping);
        cardView = findViewById(R.id.addcart_product_cardview);
//        spinShipment = findViewById(R.id.addcart_product_spinshipment);
        cardViewPayment    = findViewById(R.id.addcart_product_cardsummary_payment);
        cardViewNonPayment = findViewById(R.id.addcart_product_cardsummary);
        btnContinue = findViewById(R.id.addcart_product_btncontinue);

        addCartHelper = AddCartHelper.getINSTANCE(getApplicationContext());
        addCartHelper.open();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager horizonManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCourier.setLayoutManager(horizonManager);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCartHelper.open();
                int counter  = addCartHelper.countAddCart();

                Intent intent = new Intent();
                intent.putExtra("counter", counter);
                setResult(2, intent);

                finish();
            }
        });

        btnContinueShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCartHelper.open();
                int counter  = addCartHelper.countAddCart();

                Intent intent = new Intent();
                intent.putExtra("counter", counter);
                setResult(2, intent);

                finish();
            }
        });

        getIdOptic();
        generateId(opticUsername);
        getActiveSale();

        totalPrice = addCartHelper.countTotalPrice();
        priceDisc = addCartHelper.countTotalDiscPrice();

        totalDisc  = totalPrice - priceDisc;

        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtPricePayment.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtDisc.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));
        txtDiscPayment.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));


//        spinShipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (!shipmentList.isEmpty())
//                {
//                    shipmentPrice = shipmentList.get(i).getPrice();
//                    shippingId    = shipmentList.get(i).getId();
//                    shippingName  = shipmentList.get(i).getKurir();
//                    txtShipping.setText("Rp. " + CurencyFormat(shipmentPrice));
//
////                    shipmentPrice = "0";
////                    shippingId    = 0;
////                    shippingName  = "";
////                    if (shipmentPrice.equals("0"))
////                    {
////                        txtShipping.setText("Free");
////                    }
//                }
//                else
//                {
//                    shipmentPrice = "0";
//                    shippingId    = 0;
//                    shippingName  = "";
//                    if (shipmentPrice.equals("0"))
//                    {
//                        txtShipping.setText("Free");
//                    }
//                }
//
//                totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
//                txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toasty.info(getApplicationContext(), opticId, Toast.LENGTH_SHORT).show();
                if (!opticId.equals("null,"))
                {
//                    Toasty.info(getApplicationContext(), orderId, Toast.LENGTH_SHORT).show();

//                    opticFlag = "0";
                    if (flagPayment == 0)
                    {
//                        Toasty.info(getApplicationContext(), "Pembayaran Non Tunai", Toast.LENGTH_SHORT).show();
                        dataFrameHeader = new Data_frame_header();
                        dataFrameHeader.setOrderId(orderId);
                        dataFrameHeader.setIdParty(Integer.parseInt(opticId.replace(",", "")));
                        dataFrameHeader.setShippingId(shippingId);
                        dataFrameHeader.setShippingName("");
                        dataFrameHeader.setShippingService("");
                        dataFrameHeader.setOpticCity(opticCity);
                        dataFrameHeader.setOpticProvince(opticProvince);
                        dataFrameHeader.setShippingPrice(Integer.parseInt(shipmentPrice));
                        dataFrameHeader.setTotalPrice(totalAllPrice);
                        dataFrameHeader.setOpticName(opticName.replace(",", ""));
                        dataFrameHeader.setOpticAddress(opticAddress);
                        dataFrameHeader.setPaymentCashCarry("Non Payment Method");

                        Log.d("Data Header", "OrderId = " + dataFrameHeader.getOrderId());
                        Log.d("Data Header", "IdParty = " + dataFrameHeader.getIdParty());
                        Log.d("Data Header", "ShippingId = " + dataFrameHeader.getShippingId());
                        Log.d("Data Header", "ShippingName = " + dataFrameHeader.getShippingName());
                        Log.d("Data Header", "ShippingService = " + dataFrameHeader.getShippingService());
                        Log.d("Data Header", "OpticCity = " + dataFrameHeader.getOpticCity());
                        Log.d("Data Header", "OpticProvince = " + dataFrameHeader.getOpticProvince());
                        Log.d("Data Header", "ShippingPrice = " + dataFrameHeader.getShippingPrice());
                        Log.d("Data Header", "TotalPrice = " + dataFrameHeader.getTotalPrice());
                        Log.d("Data Header", "OpticName = " + dataFrameHeader.getOpticName());
                        Log.d("Data Header", "OpticAddress = " + dataFrameHeader.getOpticAddress());
                        Log.d("Data Header", "CashCarry = " + dataFrameHeader.getPaymentCashCarry());
                        Log.d("Data Header", "Subcustid = " + subcustId);
                        Log.d("Data Header", "Subcustlocid = " + subcustLocId);

                        insertHeader(dataFrameHeader, subcustId, subcustLocId, flashSaleInfo);
                        insertNonPaymentStatus(orderId);

                        for (int i = 0; i < itemCart.size(); i++)
                        {
                            dataFrameItem = new Data_frame_item();
                            dataFrameItem.setOrderId(orderId);
                            dataFrameItem.setLineNumber(i);
                            dataFrameItem.setFrameId(itemCart.get(i).getProductId());
                            dataFrameItem.setFrameCode(itemCart.get(i).getProductCode());
                            dataFrameItem.setFrameName(itemCart.get(i).getProductName());
                            dataFrameItem.setFrameQty(itemCart.get(i).getProductQty());
//                                dataFrameItem.setFrameRealPrice(itemCart.get(i).getProductDiscPrice());
                            dataFrameItem.setFrameRealPrice(itemCart.get(i).getProductPrice());
                            dataFrameItem.setFrameDisc(itemCart.get(i).getProductDisc());

//                            int discPrice = itemCart.get(i).getProductDisc() / 100 * itemCart.get(i).getProductPrice();
//                            dataFrameItem.setFrameDiscPrice(discPrice);

                            dataFrameItem.setFrameDiscPrice(itemCart.get(i).getNewProductDiscPrice());

                            insertLineItem(dataFrameItem);
                        }

//                        Toasty.error(getApplicationContext(), "total item cart : " + itemCart.size(), Toast.LENGTH_SHORT).show();
                        int trunc = addCartHelper.truncCart();

                        if (trunc > 0)
                        {
//                                Toasty.success(getApplicationContext(), "Success, Order telah diterima timur raya", Toast.LENGTH_SHORT).show();

                            final Dialog dialog = new Dialog(AddCartProductActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                            dialog.setContentView(R.layout.dialog_warning);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            lwindow.copyFrom(dialog.getWindow().getAttributes());
                            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                            ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                            ImageView imgIcon  = dialog.findViewById(R.id.dialog_warning_imgIcon);
                            UniversalFontTextView txtTitle = dialog.findViewById(R.id.dialog_warning_txtInfo);
                            txtTitle.setText("Success, order anda telah diterima Timur Raya");
                            imgIcon.setImageResource(R.drawable.success_outline);
                            imgClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                    addCartHelper.open();
                                    int counter  = addCartHelper.countAddCart();

                                    Intent intent = new Intent();
                                    intent.putExtra("counter", counter);
                                    setResult(2, intent);

                                    finish();
                                }
                            });

                            dialog.getWindow().setAttributes(lwindow);
                            if (!isFinishing())
                            {
                                dialog.show();
                            }
                        }
                    }
                    else
                    {
//                        Toasty.success(getApplicationContext(), "Pembayaran Tunai", Toast.LENGTH_SHORT).show();
                        int len = itemCart.size();
                        List<Boolean> sisanya = new ArrayList<>();

                        for (int i = 0; i < len; i++)
                        {
                            String item = itemCart.get(i).getProductName();
                            int stock = itemCart.get(i).getProductStock();
                            int qty   = itemCart.get(i).getProductQty();
                            int sisa  = stock - qty;

                            Log.d("Stock " + item, " Sisa = " + stock);

                            if (stock < 0)
                            {
                                sisanya.add(i, false);
                            }
                            else
                            {
                                sisanya.add(i, true);
                            }
                        }

                        boolean cek = sisanya.contains(false);

                        if (cek)
                        {
                            Log.d("Information Cart", "Ada item yang minus");

                            final Dialog dialog = new Dialog(AddCartProductActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                            dialog.setContentView(R.layout.dialog_warning);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            lwindow.copyFrom(dialog.getWindow().getAttributes());
                            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                            ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
                            imgClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.getWindow().setAttributes(lwindow);
                            if (!isFinishing())
                            {
                                dialog.show();
                            }
                        }
                        else
                        {
                            Log.d("Information Cart", "Aman lanjutkan");

                            final Dialog dialog = new Dialog(AddCartProductActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

                            dialog.setContentView(R.layout.dialog_choose_payment);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            lwindow.copyFrom(dialog.getWindow().getAttributes());
                            lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

                            listPayment = dialog.findViewById(R.id.dialog_choosepayment_listview);
                            btnChoosePayment = dialog.findViewById(R.id.dialog_choosepayment_btnChoose);

                            getAllPayment();

                            listPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    selectedItem  = paymentmethodList.get(i).getPaymentMethod();

                                    if (selectedItem.contentEquals("Kreditpro") || selectedItem.equals("Kreditpro")
                                            || selectedItem.contains("Kreditpro"))
                                    {
                                        btnChoosePayment.setBackgroundColor(Color.LTGRAY);
                                        btnChoosePayment.setEnabled(false);
                                    }
//                                    else if (selectedItem.contentEquals("Virtual Account") || selectedItem.equals("Virtual Account")
//                                            || selectedItem.contains("Virtual Account"))
//                                    {
//                                        btnChoosePayment.setBackgroundColor(Color.LTGRAY);
//                                        btnChoosePayment.setEnabled(false);
//                                    }
//                                    else if (selectedItem.contentEquals("Loan") || selectedItem.equals("Loan")
//                                            || selectedItem.contains("Loan"))
//                                    {
//                                        btnChoosePayment.setBackgroundColor(Color.LTGRAY);
//                                        btnChoosePayment.setEnabled(false);
//                                    }
                                    else
                                    {
                                        btnChoosePayment.setBackgroundColor(getResources().getColor(R.color.colorFirst));
                                        btnChoosePayment.setEnabled(true);
                                    }
                                }
                            });

                            btnChoosePayment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //showLoading();

                                    if (selectedItem.contentEquals("QR Code") || selectedItem.equals("QR Code") || selectedItem.contains("QR Code"))
                                    {
                                        String paymentType = "internetBanking";
                                        String grossAmount = String.valueOf(totalAllPrice);
                                        String orderNumber = orderId;

                                        postBillingQR(paymentType, grossAmount, orderNumber);

                                    }
                                    else if (selectedItem.contentEquals("Virtual Account") || selectedItem.equals("Virtual Account") ||
                                            selectedItem.contains("Virtual Account"))
                                    {
                                        String paymentType = "bankTransfer";
                                        String grossAmount = String.valueOf(totalAllPrice);
                                        String orderNumber = orderId;

                                        postBillingVA(paymentType, grossAmount, orderNumber);
                                    }
                                    else if (selectedItem.contentEquals("Credit Card") || selectedItem.equals("Credit Card") ||
                                            selectedItem.contains("Credit Card"))
                                    {
                                        createBillingCC(orderId, "creditCard");
                                    }
                                    else if (selectedItem.contentEquals("Loan") || selectedItem.equals("Loan") ||
                                            selectedItem.contains("Loan"))
                                    {

                                        String paymentType = "loanKS";
                                        String grossAmount = String.valueOf(totalAllPrice);
                                        String orderNumber = orderId;

                                        //Toasty.info(getApplicationContext(), "Order Number : " + orderNumber, Toast.LENGTH_SHORT).show();

                                        postBillingLoan(paymentType, grossAmount, orderNumber);
                                    }
                                    else if (selectedItem.contentEquals("Deposit") || selectedItem.equals("Deposit") ||
                                            selectedItem.contains("Deposit"))
                                    {
//                                        Toasty.info(getApplicationContext(), "Bayar Dengan Deposit", Toast.LENGTH_SHORT).show();

//                                        String paymentType = "deposit";
//                                        String grossAmount = String.valueOf(totalAllPrice);
//                                        String orderNumber = orderId;

                                        createBillingDeposit(orderId, "deposit");
                                    }
//                                else if (selectedItem.contentEquals("Kreditpro") || selectedItem.equals("Kreditpro") ||
//                                        selectedItem.contains("Kreditpro"))
//                                {
////                    Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentKreditpro.class);
////                    startActivity(intent);
//
//                                    createBillingKP(orderNumber, "kreditPro");
//                                }

                                    //loading.dismiss();
                                    dialog.dismiss();


                                }
                            });

                            dialog.getWindow().setAttributes(lwindow);
                            if (!isFinishing())
                            {
                                dialog.show();
                            }

//                        Toasty.error(getApplicationContext(), "total item cart : " + itemCart.size(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toasty.error(getApplicationContext(), "Please login for order !", Toast.LENGTH_SHORT).show();
                }
//
//                for (int j = 0; j < sisanya.size(); j++)
//                {
//                    Log.d("Sisa item ", " = " + sisanya.get(j));
//
//                    if (sisanya.get(j) > 0)
//                    {
//                        Toasty.info(getApplicationContext(), "Lanjutkan ke pembayaran", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toasty.error(getApplicationContext(), "Itemnya kurang", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        addCartHelper.open();
        int counter  = addCartHelper.countAddCart();

        Intent intent = new Intent();
        intent.putExtra("counter", counter);
        setResult(2, intent);

        finish();
    }

    private void autoInsertOrder() {
        dataFrameHeader = new Data_frame_header();
        dataFrameHeader.setOrderId(orderId);
        dataFrameHeader.setIdParty(Integer.parseInt(opticId.replace(",", "")));
        dataFrameHeader.setShippingId(shippingId);
        dataFrameHeader.setShippingName(shippingName);
        dataFrameHeader.setShippingService(shippingService);
        dataFrameHeader.setOpticCity(opticCity);
        dataFrameHeader.setOpticProvince(opticProvince);
        dataFrameHeader.setShippingPrice(Integer.parseInt(shipmentPrice));
        dataFrameHeader.setTotalPrice(totalAllPrice);
        dataFrameHeader.setOpticName(opticName.replace(",", ""));
        dataFrameHeader.setOpticAddress(opticAddress);
        dataFrameHeader.setPaymentCashCarry("Pending");

        Log.d("Data Header", "OrderId = " + dataFrameHeader.getOrderId());
        Log.d("Data Header", "IdParty = " + dataFrameHeader.getIdParty());
        Log.d("Data Header", "ShippingId = " + dataFrameHeader.getShippingId());
        Log.d("Data Header", "ShippingName = " + dataFrameHeader.getShippingName());
        Log.d("Data Header", "ShippingService = " + dataFrameHeader.getShippingService());
        Log.d("Data Header", "OpticCity = " + dataFrameHeader.getOpticCity());
        Log.d("Data Header", "OpticProvince = " + dataFrameHeader.getOpticProvince());
        Log.d("Data Header", "ShippingPrice = " + dataFrameHeader.getShippingPrice());
        Log.d("Data Header", "TotalPrice = " + dataFrameHeader.getTotalPrice());
        Log.d("Data Header", "OpticName = " + dataFrameHeader.getOpticName());
        Log.d("Data Header", "OpticAddress = " + dataFrameHeader.getOpticAddress());
        Log.d("Data Header", "CashCarry = " + dataFrameHeader.getPaymentCashCarry());
        Log.d("Data Header", "Subcustid = " + subcustId);
        Log.d("Data Header", "Subcustlocid = " + subcustLocId);

        insertHeader(dataFrameHeader, subcustId, subcustLocId, flashSaleInfo);

        for (int i = 0; i < itemCart.size(); i++)
        {
            dataFrameItem = new Data_frame_item();
            dataFrameItem.setOrderId(orderId);
            dataFrameItem.setLineNumber(i);
            dataFrameItem.setFrameId(itemCart.get(i).getProductId());
            dataFrameItem.setFrameCode(itemCart.get(i).getProductCode());
            dataFrameItem.setFrameName(itemCart.get(i).getProductName());
            dataFrameItem.setFrameQty(itemCart.get(i).getProductQty());
//                                        dataFrameItem.setFrameRealPrice(itemCart.get(i).getProductDiscPrice());
            dataFrameItem.setFrameRealPrice(itemCart.get(i).getProductPrice());
            dataFrameItem.setFrameDisc(itemCart.get(i).getProductDisc());

//            int discPrice = itemCart.get(i).getProductDisc() / 100 * itemCart.get(i).getProductPrice();
//            dataFrameItem.setFrameDiscPrice(discPrice);

            dataFrameItem.setFrameDiscPrice(itemCart.get(i).getNewProductDiscPrice());

            insertLineItem(dataFrameItem);
        }

        for (int j = 0; j < itemCart.size(); j++)
        {
            String item_id = String.valueOf(itemCart.get(j).getProductId());
            int stock = itemCart.get(j).getProductStock();
            int qty   = itemCart.get(j).getProductQty();
//                                int sisa  = stock - qty;

            potongStock(item_id, String.valueOf(qty));
        }

        linearLayout.setVisibility(View.VISIBLE);
        linearPayment.setVisibility(View.GONE);
        nestedDetail.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);

        int trunc = addCartHelper.truncCart();

        if (trunc > 0)
        {
//                            Toasty.success(getApplicationContext(), "Success, masuk ke metode pembayaran", Toast.LENGTH_SHORT).show();

            Log.d("INFO FRAME", "Order telah dihapus");
        }
    }

    private void getIdOptic()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            opticId     = bundle.getString("idparty");
            opticName   = bundle.getString("opticname");
            opticProvince = bundle.getString("province");
            opticAddress = bundle.getString("province_address");
            opticUsername = bundle.getString("usernameInfo");
            opticCity   = bundle.getString("city");
            opticFlag   = bundle.getString("flag");
            opticLevel  = bundle.getString("level");

            getOraId(opticId);
            getPaymentOrNot(opticFlag);

            if (opticLevel.equals("1"))
            {
                salesName = bundle.getString("sales");
                assert salesName != null;
                Log.d("Sales Name orderlens : ", salesName);
            }
            else
            {
                salesName = "";
                Log.d("Sales Name orderlens : ", salesName);
            }
        }
        opticId     = opticId + ",";
        opticName   = opticName + ",";

        //Toast.makeText(getApplicationContext(), opticUsername, Toast.LENGTH_SHORT).show();
    }

    private void potongStock(final String itemId, final String qty)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATESTOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("Success"))
                    {
                        Log.d("Update Stock", itemId + " Berhasil Dipotong sisa : " + qty);
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Log.d("Update Stock", itemId + " GAGAL DIUBAH");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("Error Potong Stok", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);
                hashMap.put("qty", qty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getAllPayment()
    {
        paymentmethodList.clear();
        adapter_paymentmethod = new Adapter_paymentmethod(AddCartProductActivity.this, paymentmethodList);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ALLPAYMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("payment_method");
                        String image = jsonObject.getString("payment_image");

                        Data_paymentmethod data_paymentmethod = new Data_paymentmethod();
                        data_paymentmethod.setPaymentMethod(title);
                        data_paymentmethod.setPaymentImage(image);

                        paymentmethodList.add(data_paymentmethod);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter_paymentmethod.notifyDataSetChanged();
                listPayment.setAdapter(adapter_paymentmethod);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showCart()
    {
        if (opticCity == null)
        {
            txtTitleShip.setVisibility(View.GONE);
            txtTitleShipPayment.setVisibility(View.GONE);
            txtShipping.setVisibility(View.GONE);
            txtShippingPayment.setVisibility(View.GONE);
//            spinShipment.setVisibility(View.GONE);

            shipmentPrice = "0";
            totalAllPrice = priceDisc;
            txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
            txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
        }
        else
        {
//            getAllKurir(opticCity, opticProvince);

//            if (opticFlag.equals("0"))
            Log.d("Flag Payment", "Flag value = " + flagPayment);
            if (flagPayment == 0)
            {
                scalableCourier.setVisibility(View.GONE);
                txtInfoShipping.setText("Belum termasuk ongkos kirim. Kurir dan tarif pengiriman sesuai kebijakan Timur Raya");

                shipmentPrice = "0";
                totalAllPrice = (totalPrice - totalDisc) + Integer.valueOf(shipmentPrice);
                txtShipping.setText("Rp. " + CurencyFormat(shipmentPrice));
                txtShippingPayment.setText("Rp. " + CurencyFormat(shipmentPrice));
                txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
            }
            else if(flagPayment == 1)
            {
//                txtInfoShipping.setText("Estimasi pengiriman order anda sekitar " + estimasi);
                txtInfoShipping.setVisibility(View.GONE);
                getAllKurir(opticCity, opticProvince);
                scalableCourier.setVisibility(View.VISIBLE);
            }

            txtTitleShip.setVisibility(View.VISIBLE);
            txtTitleShipPayment.setVisibility(View.VISIBLE);
            txtShipping.setVisibility(View.VISIBLE);
            txtShippingPayment.setVisibility(View.VISIBLE);
//            spinShipment.setVisibility(View.VISIBLE);
        }

        itemCart = addCartHelper.getAllCart();
        Log.d("Item Cart", itemCart.toString());

        adapterAddCart = new Adapter_add_cart(this, itemCart, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                int btn = view.getId();

                switch (btn)
                {
                    case R.id.item_cartproduct_btnRemove:
                        addCartHelper.deleteWishlist(itemCart.get(pos).getProductId());

                        itemCart.remove(pos);
                        adapterAddCart.notifyItemRemoved(pos);

                        if (itemCart.size() > 0)
                        {
                            linearLayout.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.VISIBLE);
                            nestedDetail.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            linearPayment.setVisibility(View.GONE);
                            nestedDetail.setVisibility(View.GONE);
                            cardView.setVisibility(View.GONE);
                        }

                        adapterAddCart.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterAddCart);

                        addCartHelper.open();

                        totalPrice = addCartHelper.countTotalPrice();
                        priceDisc = addCartHelper.countTotalDiscPrice();

                        totalDisc  = totalPrice - priceDisc;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtPricePayment.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtDiscPayment.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));

                        if (opticCity == null)
                        {
                            shipmentPrice = "0";
                            totalAllPrice = priceDisc;
                            txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                            txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }
                        else
                        {
                            totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
                            txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                            txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }

//                        totalAllPrice = priceDisc - totalDisc + Integer.valueOf(shipmentPrice);
//                        txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                        addCartHelper.open();
                        int counter  = addCartHelper.countAddCart();

                        Intent intent = new Intent();
                        intent.putExtra("counter", counter);
                        setResult(2, intent);

                        break;

                    case R.id.item_cartproduct_btnPlus:
                        addCartHelper.open();
                        ModelAddCart modelAddCart = addCartHelper.getAddCart(itemCart.get(pos).getProductId());

                        int price = modelAddCart.getProductPrice();
                        int qty   = modelAddCart.getProductQty();
                        int stock = modelAddCart.getProductStock();
                        int discprice = modelAddCart.getProductDiscPrice();

                        qty = qty + 1;
                        int newprice = price * qty;
                        int newdiscprice = discprice * qty;

                        if (flagPayment == 1)
                        {
                            stock = stock - qty;

                            //Toasty.info(getApplicationContext(), String.valueOf(qty), Toast.LENGTH_SHORT).show();

                            modelAddCart.setProductStock(stock);
                        }

                        modelAddCart.setNewProductPrice(newprice);
                        modelAddCart.setProductQty(qty);
                        modelAddCart.setNewProductDiscPrice(newdiscprice);
                        itemCart.set(pos, modelAddCart);
                        addCartHelper.updateCartQty(modelAddCart);
                        adapterAddCart.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapterAddCart);

                        addCartHelper.open();
                        totalPrice = addCartHelper.countTotalPrice();
                        priceDisc = addCartHelper.countTotalDiscPrice();

                        totalDisc  = totalPrice - priceDisc;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtPricePayment.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtDiscPayment.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));

                        if (opticCity == null)
                        {
                            shipmentPrice = "0";
                            totalAllPrice = priceDisc;
                            txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                            txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }
                        else
                        {
                            totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
                            txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                            txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }

//                        totalAllPrice = priceDisc - totalDisc + Integer.valueOf(shipmentPrice);
//                        txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                        int mCounter  = addCartHelper.countAddCart();

                        Intent mIntent = new Intent();
                        mIntent.putExtra("counter", mCounter);
                        setResult(2, mIntent);
                        break;

                    case R.id.item_cartproduct_btnMinus:
//                        aaddCartHelper.open();
                        ModelAddCart mModelAddCart = addCartHelper.getAddCart(itemCart.get(pos).getProductId());

                        int mPrice = mModelAddCart.getProductPrice();
                        int mQty   = mModelAddCart.getProductQty();
                        int mDiscprice = mModelAddCart.getProductDiscPrice();
                        int mStock = mModelAddCart.getProductStock();
//                        int tempStock = 0;

                        mQty = mQty - 1;

                        if (flagPayment == 1)
                        {
                            if (mQty == 0)
                            {
                                mQty = mQty + 1;
                            }

                            mStock = mStock - mQty;
                            Log.d("Informasi Stok", String.valueOf(mStock));
                            mModelAddCart.setProductStock(mStock);

                            itemCart.set(pos, mModelAddCart);
                            addCartHelper.updateCartQty(mModelAddCart);
                            adapterAddCart.notifyDataSetChanged();
                        }

                        if (mQty == 0)
                        {
                            Toasty.info(getApplicationContext(), "Minimal Order 1 pcs", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            int mNewPrice = mPrice * mQty;
                            int mNewdiscprice = mDiscprice * mQty;

                            //Toasty.info(getApplicationContext(), String.valueOf(qty), Toast.LENGTH_SHORT).show();

                            mModelAddCart.setNewProductPrice(mNewPrice);
                            mModelAddCart.setProductQty(mQty);
                            mModelAddCart.setNewProductDiscPrice(mNewdiscprice);

                            itemCart.set(pos, mModelAddCart);
                            addCartHelper.updateCartQty(mModelAddCart);
                            adapterAddCart.notifyDataSetChanged();
//                        recyclerView.setAdapter(adapterAddCart);
                        }

                        addCartHelper.open();
                        totalPrice = addCartHelper.countTotalPrice();
                        priceDisc = addCartHelper.countTotalDiscPrice();

                        totalDisc  = totalPrice - priceDisc;

                        txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtPricePayment.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtDisc.setText("- Rp. " + CurencyFormat(String.valueOf(totalDisc)));
                        txtDiscPayment.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));

                        if (opticCity == null)
                        {
                            shipmentPrice = "0";
                            totalAllPrice = priceDisc;
                            txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                            txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }
                        else
                        {
                            totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
                            txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                            txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }

//                        totalAllPrice = priceDisc - totalDisc + Integer.valueOf(shipmentPrice);
//                        txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                        int nCounter  = addCartHelper.countAddCart();

                        Intent nIntent = new Intent();
                        nIntent.putExtra("counter", nCounter);
                        setResult(2, nIntent);
                        break;
                }
            }
        });

        adapterCourierService = new Adapter_courier_service(this, shipmentList, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                shipmentPrice = shipmentList.get(pos).getPrice();
                shippingId    = shipmentList.get(pos).getId();
                shippingName  = shipmentList.get(pos).getKurir();
                shippingService = shipmentList.get(pos).getService();
                shipmentPrice = shipmentList.get(pos).getPrice();

                Log.d("Callback Price", "Shipment : " + shipmentPrice);
                totalAllPrice = (totalPrice - totalDisc) + Integer.valueOf(shipmentPrice);
                txtShipping.setText("Rp. " + CurencyFormat(shipmentPrice));
                txtShippingPayment.setText("Rp. " + CurencyFormat(shipmentPrice));
                txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
            }
        });

        if (itemCart.size() > 0)
        {
            linearLayout.setVisibility(View.GONE);
            linearPayment.setVisibility(View.VISIBLE);
            nestedDetail.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayout.setVisibility(View.VISIBLE);
            linearPayment.setVisibility(View.GONE);
            nestedDetail.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapterAddCart);
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

    private void dialogError(){
        final Dialog dialog = new Dialog(AddCartProductActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lwindow = new WindowManager.LayoutParams();

        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        lwindow.copyFrom(dialog.getWindow().getAttributes());
        lwindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        lwindow.height= WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView imgClose = dialog.findViewById(R.id.dialog_warning_imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        UniversalFontTextView txtInfo = dialog.findViewById(R.id.dialog_warning_txtInfo);
        txtInfo.setText("Terjadi kesalahan silahkan coba kembali nanti");

        dialog.getWindow().setAttributes(lwindow);
        if (!isFinishing())
        {
            dialog.show();
        }
    }

    private void generateId(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GENID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    orderId = jsonObject.getString("lastnumber");

                    Toasty.info(getApplicationContext(), orderId, Toast.LENGTH_SHORT).show();
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
//                hashMap.put("user", username + "s");
                hashMap.put("user", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertHeader(final Data_frame_header item, final String subcustid, final String subcustlocid, final String flashSale)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("order_id", item.getOrderId());
                hashMap.put("id_party", String.valueOf(item.getIdParty()));
                hashMap.put("shipping_id", String.valueOf(item.getShippingId()));
                hashMap.put("shipping_name", item.getShippingName());
                hashMap.put("shipping_service", item.getShippingService());
                hashMap.put("optic_city", item.getOpticCity());
                hashMap.put("optic_province", item.getOpticProvince());
                hashMap.put("shipping_price", String.valueOf(item.getShippingPrice()));
                hashMap.put("total_price", String.valueOf(item.getTotalPrice()));
                hashMap.put("optic_name", item.getOpticName());
                hashMap.put("optic_address", item.getOpticAddress());
                hashMap.put("payment_cashcarry", item.getPaymentCashCarry());
                hashMap.put("subcust_id", subcustid);
                hashMap.put("subcust_loc_id", subcustlocid);
                hashMap.put("flashSaleInfo", flashSale);
                hashMap.put("salesname", salesName);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertNonPaymentStatus(final String transNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTNONTPAYMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.names().get(0).equals("success")){
//                        Toasty.warning(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
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
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("order_id", transNumber);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertLineItem(final Data_frame_item item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTLINETITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
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
                hashMap.put("order_id", item.getOrderId());
                hashMap.put("line_number", String.valueOf(item.getLineNumber()));
                hashMap.put("frame_id", String.valueOf(item.getFrameId()));
                hashMap.put("frame_code", item.getFrameCode());
                hashMap.put("frame_name", item.getFrameName());
                hashMap.put("frame_qty", String.valueOf(item.getFrameQty()));
                hashMap.put("frame_realprice", String.valueOf(item.getFrameRealPrice()));
                hashMap.put("frame_disc", String.valueOf(item.getFrameDisc()));
                hashMap.put("frame_discprice", String.valueOf(item.getFrameDiscPrice()));

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getAllKurir(final String city, final String prov)
    {
//        adapter_spinner_shipment = new Adapter_spinner_shipment(AddCartProductActivity.this, shipmentList);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ALLDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idShip   = jsonObject.getString("idShip");
                        String kurir    = jsonObject.getString("kurir");
                        String service  = jsonObject.getString("type");
                        String icon     = jsonObject.getString("icon");
                        String city     = jsonObject.getString("city");
                        String province = jsonObject.getString("province");
                        String price    = jsonObject.getString("price");
                        estimasi = jsonObject.getString("estimasi");

                        Data_spin_shipment data_spin_shipment = new Data_spin_shipment();
                        data_spin_shipment.setId(Integer.valueOf(idShip));
                        data_spin_shipment.setKurir(kurir);
                        data_spin_shipment.setService(service);
                        data_spin_shipment.setIcon(icon);
                        data_spin_shipment.setPrice(price);
                        data_spin_shipment.setCity(city);
                        data_spin_shipment.setProvince(province);
                        data_spin_shipment.setEstimasi(estimasi);

                        shipmentList.add(data_spin_shipment);
                    }

                    shipmentPrice = shipmentList.get(0).getPrice();
                    shippingName  = shipmentList.get(0).getKurir();
                    shippingService = shipmentList.get(0).getService();
                    shippingId    = shipmentList.get(0).getId();

                    totalAllPrice = (totalPrice - totalDisc) + Integer.valueOf(shipmentPrice);
                    txtShipping.setText("Rp. " + CurencyFormat(shipmentPrice));
                    txtShippingPayment.setText("Rp. " + CurencyFormat(shipmentPrice));
                    txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                    txtTotalPayment.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                    adapterCourierService.notifyDataSetChanged();
                    recyclerCourier.setAdapter(adapterCourierService);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                adapter_spinner_shipment.notifyDataSetChanged();
//                spinShipment.setAdapter(adapter_spinner_shipment);
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
                hashMap.put("city", city);
                hashMap.put("province", prov);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void postBillingQR(final String paymentType, final String grossAmount, final String orderId)
    {
//        showLoading();
        customLoading.showLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    loading.dismiss();
                    customLoading.dismissLoadingDialog();
                    JSONObject jsonObject = new JSONObject(response);

                    String statusCode   = jsonObject.getString("responseCode");
                    String description  = jsonObject.getString("responseDescription");

                    if (statusCode.equals("00") || statusCode.contains("00") || statusCode.contentEquals("00"))
                    {
                        kodeBilling = jsonObject.getString("billidId");

                        String paymentType = "internetBanking";

                        createBillingQR(orderId, paymentType, kodeBilling);
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), description + " (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//                loading.dismiss();
//                loading.dismiss();
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("paymentType", paymentType);
                hashMap.put("grossAmount", grossAmount);
                hashMap.put("orderId", orderId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void createBillingQR(final String orderNumber, final String paymentType, final String billingId)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLQR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.info(getApplicationContext(), "Post Bill success", Toast.LENGTH_SHORT).show();
                        autoInsertOrder();
                        showSessionPaymentQR(orderNumber);
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), "No order harus diisi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("order_number", orderNumber);
                hashMap.put("payment_type", paymentType);
                hashMap.put("billing_id", billingId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showSessionPaymentQR(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTQR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(AddCartProductActivity.class.getSimpleName(), "Respon : " + response);

                    String amount      = "Rp. " + CurencyFormat(String.valueOf(totalAllPrice));
                    duration = jsonObject.getString("duration");
                    expDate  = jsonObject.getString("exp_date");

                    Toasty.success(getApplicationContext(), "Total amount : " + amount, Toast.LENGTH_LONG).show();

                    if (duration != null)
                    {
                        Intent intent = new Intent(AddCartProductActivity.this, FormPaymentQR.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("billingCode", kodeBilling);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", opticUsername);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    }

                    //finish();
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
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void postBillingVA(final String paymentType, final String grossAmount, final String orderId)
    {
//        showLoading();
        customLoading.showLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    loading.dismiss();
                    customLoading.dismissLoadingDialog();
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.length() > 0)
                    {
                        String statusCode   = jsonObject.getString("responseCode");
                        String description  = jsonObject.getString("responseDescription");

                        if (statusCode.equals("00") || statusCode.contains("00") || statusCode.contentEquals("00"))
                        {
                            kodeBilling = jsonObject.getString("virtualAccount");
                            String paymentType = "bankTransfer";

                            createBillingVA(orderId, paymentType, kodeBilling);
                        }
                        else
                        {
                            Toasty.error(getApplicationContext(), description + " (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        dialogError();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    dialogError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("paymentType", paymentType);
                hashMap.put("grossAmount", grossAmount);
                hashMap.put("orderId", orderId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void createBillingVA(final String orderNumber, final String paymentType, final String virtualAccount)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLVA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.info(getApplicationContext(), "Post Bill success", Toast.LENGTH_SHORT).show();
                        autoInsertOrder();
                        showSessionPaymentVA(orderNumber);
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), "No order harus diisi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ordernumber", orderNumber);
                hashMap.put("payment_type", paymentType);
                hashMap.put("va", virtualAccount);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showSessionPaymentVA(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTVA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String amount      = "Rp " + CurencyFormat(String.valueOf(totalAllPrice));;
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

                    if (duration != null)
                    {
                        Intent intent = new Intent(AddCartProductActivity.this, FormPaymentVA.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("billingCode", kodeBilling);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", opticUsername);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    }
                    //finish();
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
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void createBillingCC(final String orderNumber, final String paymentType)
    {
//        showLoading();
        customLoading.showLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLCC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    loading.dismiss();
                    customLoading.dismissLoadingDialog();
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Call ShowSessionPayment
                        autoInsertOrder();
                        showSessionPaymentCC(orderNumber);
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), "Data harus diisi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("ordernumber", orderNumber);
                hashMap.put("payment_type", paymentType);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showSessionPaymentCC(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTCC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String amount = String.valueOf(totalAllPrice);
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

                    if (duration != null)
                    {
                        Intent intent = new Intent(AddCartProductActivity.this, FormPaymentCC.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", opticUsername);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Please, Try Again !", Toast.LENGTH_SHORT).show();
                    }

//                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void postBillingLoan(final String paymentType, final String grossAmount, final String order)
    {
//        showLoading();
        customLoading.showLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    loading.dismiss();
                    customLoading.dismissLoadingDialog();
                    JSONObject jsonObject = new JSONObject(response);

                    String statusCode   = jsonObject.getString("responseCode");
                    String description  = jsonObject.getString("responseDescription");

                    if (statusCode.equals("00") || statusCode.contains("00") || statusCode.contentEquals("00"))
                    {
                        kodeBilling = jsonObject.getString("billidId");

                        String paymentType = "loanKS";

                        createBillingLoan(order, paymentType, kodeBilling);
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), description + " (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("paymentType", paymentType);
                hashMap.put("grossAmount", grossAmount);
                hashMap.put("orderId", order);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void createBillingLoan(final String order, final String paymentType, final String billingId)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLLOAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Post Bill success", Toast.LENGTH_SHORT).show();
                        autoInsertOrder();
                        showSessionPaymentLoan(order);
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), "No order harus diisi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ordernumber", order);
                hashMap.put("payment_type", paymentType);
                hashMap.put("billing_id", billingId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showSessionPaymentLoan(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTLOAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String amount = String.valueOf(totalAllPrice);
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

//                    Toasty.success(getApplicationContext(), "Total amount : " + amount, Toast.LENGTH_LONG).show();

                    if (duration != null)
                    {
                        Intent intent = new Intent(AddCartProductActivity.this, FormPaymentLoanActivity.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("billingCode", kodeBilling);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);
                        intent.putExtra("username", opticUsername);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    }

                    //finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //loading.dismiss();
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
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void createBillingDeposit(final String orderNumber, final String paymentType)
    {
//        showLoading();
        customLoading.showLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    loading.dismiss();
                    customLoading.dismissLoadingDialog();
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Call ShowSessionPayment
                        autoInsertOrder();
                        showSessionPaymentDeposit(orderNumber);
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), "Data harus diisi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("ordernumber", orderNumber);
                hashMap.put("payment_type", paymentType);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showSessionPaymentDeposit(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String amount = String.valueOf(totalAllPrice);
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

                    if (duration != null)
                    {
                        Intent intent = new Intent(AddCartProductActivity.this, FormPaymentDeposit.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);

                        intent.putExtra("username", opticUsername);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Please, Try Again !", Toast.LENGTH_SHORT).show();
                    }

//                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("order_number", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getOraId(final String idParty) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETORAID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    subcustId = object.getString("subcust_id");
                    subcustLocId = object.getString("subcust_loc_id");

                    if (subcustId.equals("null"))
                    {
                        subcustId = "0";
                    }

                    if (subcustLocId.equals("null"))
                    {
                        subcustLocId = "0";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null)
                {
                    Log.d("ERROR GET ORAID : ", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", idParty);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getPaymentOrNot(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETMEMBERFLAG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error"))
                    {
                        Log.d("Status Payment", "Data Not Found");
                    }
                    else
                    {
                        flagPayment = object.getInt("order_frame");

                        if (flagPayment == 1)
                        {
                            Log.d("Status Payment", "With Payment");
                            cardViewPayment.setVisibility(View.VISIBLE);
                            cardViewNonPayment.setVisibility(View.GONE);
                        }
                        else
                        {
                            Log.d("Status Payment", "Non Payment");
                            cardViewPayment.setVisibility(View.GONE);
                            cardViewNonPayment.setVisibility(View.VISIBLE);
                        }

                        showCart();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!error.getMessage().isEmpty())
                {
                    Log.d("Error Get Status", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", id);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getActiveSale()
    {
        StringRequest request = new StringRequest(Request.Method.POST, GETACTIVESALE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error"))
                    {
                        flashSaleInfo = "";
                    }
                    else
                    {
                        flashSaleInfo = object.getString("title");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}
