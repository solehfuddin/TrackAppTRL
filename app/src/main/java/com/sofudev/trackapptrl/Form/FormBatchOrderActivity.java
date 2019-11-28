package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_add_partai;
import com.sofudev.trackapptrl.Adapter.Adapter_courier_service;
import com.sofudev.trackapptrl.Adapter.Adapter_lenstype;
import com.sofudev.trackapptrl.Adapter.Adapter_paymentmethod;
import com.sofudev.trackapptrl.Adapter.Adapter_power_add;
import com.sofudev.trackapptrl.Adapter.Adapter_power_cyl;
import com.sofudev.trackapptrl.Adapter.Adapter_power_sph;
import com.sofudev.trackapptrl.Adapter.Adapter_spinner_shipment;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_lenstype;
import com.sofudev.trackapptrl.Data.Data_partai_header;
import com.sofudev.trackapptrl.Data.Data_partai_item;
import com.sofudev.trackapptrl.Data.Data_paymentmethod;
import com.sofudev.trackapptrl.Data.Data_spin_shipment;
import com.sofudev.trackapptrl.LocalDb.Db.LensPartaiHelper;
import com.sofudev.trackapptrl.LocalDb.Model.ModelLensPartai;
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

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;
import viethoa.com.snackbar.BottomSnackBarMessage;

public class FormBatchOrderActivity extends AppCompatActivity {
    Config config = new Config();
    String URLALLLENS = config.Ip_address + config.show_all_stocklens;
    String URLPOWERSPH = config.Ip_address + config.show_sph;
    String URLPOWERCYL = config.Ip_address + config.show_cyl;
    String URLPOWERADD = config.Ip_address + config.show_add;
    String URLGETPRICE = config.Ip_address + config.show_price;
    String URLGETSTBSTOCK = config.Ip_address + config.discount_item_getStbItem;
    String URLGETDISCOUNT = config.Ip_address + config.discount_item_getDiscount;
    String URL_ALLDATA = config.Ip_address + config.spinner_shipment_getAllData;
    String URL_GENERATEID = config.Ip_address + config.orderpartai_generateId;
    String URLPHONE = config.Ip_address + config.order_history_getPhoneNumber;
    String URLINSERTHEADER = config.Ip_address + config.orderpartai_insertHeader;
    String URLINSERTITEM   = config.Ip_address + config.orderpartai_insertItem;
    String URL_UPDATESTOCK = config.Ip_address + config.update_stock_lens_partai;
    String URLNONPAYMENT   = config.Ip_address + config.frame_insert_statusnonpayment;
    String URL_ALLPAYMENT = config.Ip_address + config.payment_method_showAllData;
    String URL_POSTBILLING= config.payment_method_postBilling;
    String URL_INSERTBILLQR = config.Ip_address + config.payment_insert_billingQR;
    String URL_INSERTBILLVA = config.Ip_address + config.payment_insert_billingVA;
    String URL_INSERTBILLCC = config.Ip_address + config.payment_insert_billingCC;
    String URL_INSERTBILLLOAN = config.Ip_address + config.payment_insert_billingLOAN;
    String URL_SHOWSESSIONPAYMENTQR = config.Ip_address + config.payment_show_expiredDurationQR;
    String URL_SHOWSESSIONPAYMENTVA = config.Ip_address + config.payment_show_expiredDurationVA;
    String URL_SHOWSESSIONPAYMENTCC = config.Ip_address + config.payment_show_expiredDurationCC;
    String URL_SHOWSESSIONPAYMENTLOAN = config.Ip_address + config.payment_show_expiredDurationLOAN;

    ACProgressCustom loading;
    ImageView btnBack, btnAddItem;
    BootstrapEditText txtlenstype, txtlensdescription;
    RippleView btnlenstype, btnSph, btnCyl, btnAdd;
    ScalableLayout scalableCourier;
    UniversalFontTextView txtSph, txtCyl, txtAdd, txtSubtotalPrice, txtSubtotalDisc, txtShippingPrice, txtTotalPrice,
        txtTitleShip, txtInfoShipping;
    RecyclerView recyclerView, recyclerCourier;
    CardView cardView, cardContinue;
//    Spinner spinShipping;
    LinearLayout linearPayment, linearLayout;
    Button btnContinue;

    ListView listPayment;
    RippleView btnChoosePayment;

    Adapter_paymentmethod adapter_paymentmethod;
    Adapter_lenstype adapter_lenstype;
    Adapter_power_sph adapter_power_sph;
    Adapter_power_cyl adapter_power_cyl;
    Adapter_power_add adapter_power_add;
    Adapter_add_partai adapter_add_partai;
    Adapter_courier_service adapterCourierService;
//    Adapter_spinner_shipment adapter_spinner_shipment;
    List<Data_lenstype> item_stocklens = new ArrayList<>();
    List<ModelLensPartai> item_partai  = new ArrayList<>();
    List<Data_paymentmethod> paymentmethodList = new ArrayList<>();
    ArrayList<String> item_sph = new ArrayList<>();
    ArrayList<String> item_cyl = new ArrayList<>();
    ArrayList<String> item_add = new ArrayList<>();
    List<Data_spin_shipment> shipmentList = new ArrayList<>();

    String id_lensa, desc_lensa, power_sph, power_cyl, power_add;
    String opticId, opticName, opticProvince, opticUsername, opticCity, opticAddress, opticFlag, shipmentPrice,
            shippingName, shippingService, orderId, phone, orgName;
    String sphpos = null, cylpos = null, addpos = null;
    String selectedItem, kodeBilling, duration, expDate, prodAttrVal;
    int totalPrice, totalDisc, priceDisc, totalAllPrice, shippingId, availableStock;

    LensPartaiHelper lensPartaiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_batch_order);

        lensPartaiHelper = LensPartaiHelper.getINSTANCE(getApplicationContext());
        lensPartaiHelper.open();

        getIdOptic();
        generateOrderNumber(opticUsername);
        getPhoneNumber(opticUsername);

        scalableCourier = findViewById(R.id.form_batchorder_scalableCourier);
        btnBack = findViewById(R.id.form_batchorder_btnback);
        txtlensdescription = findViewById(R.id.form_batchorder_txtlensdesc);
        txtlenstype = findViewById(R.id.form_batchorder_txtlenstype);
        btnlenstype = findViewById(R.id.form_batchorder_btnlenstype);
        btnSph = findViewById(R.id.form_batchorder_ripplebtnsph);
        btnCyl = findViewById(R.id.form_batchorder_ripplebtncyl);
        btnAdd = findViewById(R.id.form_batchorder_ripplebtnadd);
        btnAddItem = findViewById(R.id.form_batchorder_btnadd);
        txtSph = findViewById(R.id.form_batchorder_txtsph);
        txtCyl = findViewById(R.id.form_batchorder_txtcyl);
        txtAdd = findViewById(R.id.form_batchorder_txtadd);
        txtSubtotalPrice = findViewById(R.id.form_batchorder_txtitemprice);
        txtSubtotalDisc  = findViewById(R.id.form_batchorder_txtitemdisc);
//        spinShipping = findViewById(R.id.form_batchorder_spinshipment);
        txtTitleShip = findViewById(R.id.form_batchorder_txttitleship);
        txtShippingPrice = findViewById(R.id.form_batchorder_txtitemship);
        txtInfoShipping = findViewById(R.id.form_batchorder_txtInfoShipping);
        txtTotalPrice = findViewById(R.id.form_batchorder_txttotalprice);
        recyclerView = findViewById(R.id.form_batchorder_recyclerview);
        recyclerCourier = findViewById(R.id.form_batchorder_recyclerCourier);
        cardView = findViewById(R.id.form_batchorder_cardsummary);
        cardContinue = findViewById(R.id.form_batchorder_cardview);
        linearPayment = findViewById(R.id.form_batchorder_linearPayment);
        linearLayout = findViewById(R.id.form_batchorder_linearLayout);
        btnContinue = findViewById(R.id.form_batchorder_btncontinue);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnlenstype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAllLens();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lensPartaiHelper.truncLensPartai();

                int len = item_partai.size();
                List<Boolean> sisanya = new ArrayList<>();

                for (int i = 0; i < len; i++)
                {
                    String item = item_partai.get(i).getProductDesc();
                    int stock = item_partai.get(i).getProductStock();
                    int qty   = item_partai.get(i).getProductQty();
                    int sisa  = stock - qty;

                    Log.d("Stock " + item, " Sisa = " + sisa);

                    if (sisa < 0)
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

                    final Dialog dialog = new Dialog(FormBatchOrderActivity.this);
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

                    dialog.show();
                    dialog.getWindow().setAttributes(lwindow);
                }
                else
                {
                    Log.d("Information Cart", "Aman lanjutkan");

                    if (opticFlag.equals("0"))
                    {
                        Data_partai_header data_header = new Data_partai_header();
                        data_header.setOrderNumber(orderId);
                        data_header.setIdParty(Integer.parseInt(opticId.replace(",", "")));
                        data_header.setOpticName(opticName.replace(",", ""));
                        data_header.setOpticAddress(opticAddress);
                        data_header.setOpticCity(opticCity);
                        data_header.setPhoneNumber(phone);
                        data_header.setProdDivType(orgName);
                        data_header.setShippingId(shippingId);
                        data_header.setShippingName(shippingName);
                        data_header.setOpticProvince(opticProvince);
                        data_header.setShippingService(shippingService);
                        data_header.setShippingPrice(Integer.valueOf(shipmentPrice));
                        data_header.setTotalPrice(totalAllPrice);
                        data_header.setPayment_cashcarry("Non Payment Method");

                        Log.d("Header Partai", "OrderNumber = " + data_header.getOrderNumber());
                        Log.d("Header Partai", "IdParty = " + data_header.getIdParty());
                        Log.d("Header Partai", "OpticName = " + data_header.getOpticName());
                        Log.d("Header Partai", "OpticAddress = " + data_header.getOpticAddress());
                        Log.d("Header Partai", "OpticCity = " + data_header.getOpticCity());
                        Log.d("Header Partai", "Phone = " + data_header.getPhoneNumber());
                        Log.d("Header Partai", "OrgName = " + data_header.getProdDivType());
                        Log.d("Header Partai", "ShippingId = " + data_header.getShippingId());
                        Log.d("Header Partai", "ShippingName = " + data_header.getShippingName());
                        Log.d("Header Partai", "Province = " + data_header.getOpticProvince());
                        Log.d("Header Partai", "Service = " + data_header.getShippingService());
                        Log.d("Header Partai", "ShipPrice = " + data_header.getShippingPrice());
                        Log.d("Header Partai", "TotalPrice  = " + data_header.getTotalPrice());
                        Log.d("Header Partai", "CashCarry = " + data_header.getPayment_cashcarry());

                        insertHeader(data_header);

                        for (int i = 0; i < item_partai.size(); i++)
                        {
                            Data_partai_item data_item = new Data_partai_item();
                            data_item.setOrderNumber(orderId);
                            data_item.setItemId(item_partai.get(i).getProductId());
                            data_item.setItemCode(item_partai.get(i).getProductCode());
                            data_item.setDescription(item_partai.get(i).getProductDesc());
                            data_item.setSide(item_partai.get(i).getProductSide());
                            data_item.setQty(item_partai.get(i).getProductQty());
                            data_item.setPrice(item_partai.get(i).getProductPrice());
                            data_item.setDiscount_name("");
                            data_item.setDiscount(item_partai.get(i).getProductDisc());
                            data_item.setTotal_price(item_partai.get(i).getNewProductDiscPrice());

                            insertItem(data_item);
                        }

                        insertNonPayment(orderId);

//                        Toasty.info(getApplicationContext(), "Congratulation, Order has been success", Toast.LENGTH_SHORT).show();

                        final Dialog dialog = new Dialog(FormBatchOrderActivity.this);
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

                                finish();
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(lwindow);
                    }
                    else
                    {
                        for (int j = 0; j < item_partai.size(); j++)
                        {
                            String item_id = String.valueOf(item_partai.get(j).getProductId());
                            int stock = item_partai.get(j).getProductStock();
                            int qty   = item_partai.get(j).getProductQty();
                            int sisa  = stock - qty;

                            potongStock(item_id, String.valueOf(sisa));
                        }

                        final Dialog dialog = new Dialog(FormBatchOrderActivity.this);
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

                                Data_partai_header data_header = new Data_partai_header();
                                data_header.setOrderNumber(orderId);
                                data_header.setIdParty(Integer.parseInt(opticId.replace(",", "")));
                                data_header.setOpticName(opticName.replace(",", ""));
                                data_header.setOpticAddress(opticAddress);
                                data_header.setOpticCity(opticCity);
                                data_header.setPhoneNumber(phone);
                                data_header.setProdDivType(orgName);
                                data_header.setShippingId(shippingId);
                                data_header.setShippingName(shippingName);
                                data_header.setShippingService(shippingService);
                                data_header.setOpticProvince(opticProvince);
                                data_header.setShippingPrice(Integer.valueOf(shipmentPrice));
                                data_header.setTotalPrice(totalAllPrice);
                                data_header.setPayment_cashcarry("Pending");

                                insertHeader(data_header);

                                for (int i = 0; i < item_partai.size(); i++)
                                {
                                    Data_partai_item data_item = new Data_partai_item();
                                    data_item.setOrderNumber(orderId);
                                    data_item.setItemId(item_partai.get(i).getProductId());
                                    data_item.setItemCode(item_partai.get(i).getProductCode());
                                    data_item.setDescription(item_partai.get(i).getProductDesc());
                                    data_item.setSide(item_partai.get(i).getProductSide());
                                    data_item.setQty(item_partai.get(i).getProductQty());
                                    data_item.setPrice(item_partai.get(i).getProductPrice());
                                    data_item.setDiscount_name("");
                                    data_item.setDiscount(item_partai.get(i).getProductDisc());
                                    data_item.setTotal_price(item_partai.get(i).getNewProductDiscPrice());

                                    insertItem(data_item);
                                }

                                linearLayout.setVisibility(View.VISIBLE);
                                linearPayment.setVisibility(View.GONE);
                                cardContinue.setVisibility(View.GONE);
                                cardView.setVisibility(View.GONE);
                                scalableCourier.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setAttributes(lwindow);
                    }
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager horizonManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCourier.setLayoutManager(horizonManager);
//        adapter_spinner_shipment = new Adapter_spinner_shipment(FormBatchOrderActivity.this, shipmentList);

//        getAllKurir(opticCity, opticProvince);

        totalPrice = lensPartaiHelper.countTotalPrice();
        priceDisc  = lensPartaiHelper.countTotalDiscPrice();
        totalDisc  = totalPrice - priceDisc;

        txtSubtotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
        txtSubtotalDisc.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));

        showCart();

//        spinShipping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!shipmentList.isEmpty())
//                {
//                    shipmentPrice = shipmentList.get(position).getPrice();
//                    shippingId    = shipmentList.get(position).getId();
//                    shippingName  = shipmentList.get(position).getKurir();
//                    txtShippingPrice.setText("Rp. " + CurencyFormat(shipmentPrice));
//
////                    shipmentPrice = "0";
////                    shippingId    = 0;
////                    shippingName  = "";
////                    if (shipmentPrice.equals("0"))
////                    {
////                        txtShippingPrice.setText("Free");
////                    }
//                }
//                else
//                {
//                    shipmentPrice = "0";
//                    shippingId    = 0;
//                    shippingName  = "";
//                    if (shipmentPrice.equals("0"))
//                    {
//                        txtShippingPrice.setText("Free");
//                    }
//                }
//
//                totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
//                txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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
                txtShippingPrice.setText("Rp. " + CurencyFormat(shipmentPrice));
                txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
            }
        });

        btnSph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UniversalFontTextView txtTitle;
                final ListView listView;
                final BootstrapButton btnChoose;
                final Dialog dialog = new Dialog(FormBatchOrderActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                adapter_power_sph = new Adapter_power_sph(FormBatchOrderActivity.this, item_sph, sphpos);

                txtTitle = dialog.findViewById(R.id.dialog_powerstock_txtTitle);
                listView = dialog.findViewById(R.id.dialog_powerstock_listview);
                btnChoose= dialog.findViewById(R.id.dialog_powerstock_btnsave);

                txtTitle.setText("Pilih SPH");
                listView.setAdapter(adapter_power_sph);
                btnChoose.setEnabled(false);
                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        power_sph = item_sph.get(position);

                        sphpos = power_sph;
                        if (id_lensa.isEmpty())
                        {
                            btnChoose.setEnabled(false);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        }
                        else
                        {
                            btnChoose.setEnabled(true);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        }
                    }
                });

                dialog.show();

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtSph.setText(power_sph);
                        dialog.hide();
                    }
                });
            }
        });

        btnCyl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UniversalFontTextView txtTitle;
                final ListView listView;
                final BootstrapButton btnChoose;
                final Dialog dialog = new Dialog(FormBatchOrderActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                adapter_power_cyl = new Adapter_power_cyl(FormBatchOrderActivity.this, item_cyl, cylpos);

                txtTitle = dialog.findViewById(R.id.dialog_powerstock_txtTitle);
                listView = dialog.findViewById(R.id.dialog_powerstock_listview);
                btnChoose= dialog.findViewById(R.id.dialog_powerstock_btnsave);

                txtTitle.setText("Pilih CYL");
                listView.setAdapter(adapter_power_cyl);
                btnChoose.setEnabled(false);
                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        power_cyl = item_cyl.get(position);

                        cylpos = power_cyl;
                        if (id_lensa.isEmpty())
                        {
                            btnChoose.setEnabled(false);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        }
                        else
                        {
                            btnChoose.setEnabled(true);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        }
                    }
                });

                dialog.show();

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtCyl.setText(power_cyl);
                        dialog.hide();
                    }
                });
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UniversalFontTextView txtTitle;
                final ListView listView;
                final BootstrapButton btnChoose;
                final Dialog dialog = new Dialog(FormBatchOrderActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_power_lensstock);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                adapter_power_add = new Adapter_power_add(FormBatchOrderActivity.this, item_add, addpos);

                txtTitle = dialog.findViewById(R.id.dialog_powerstock_txtTitle);
                listView = dialog.findViewById(R.id.dialog_powerstock_listview);
                btnChoose= dialog.findViewById(R.id.dialog_powerstock_btnsave);

                txtTitle.setText("Pilih ADD");
                listView.setAdapter(adapter_power_add);
                btnChoose.setEnabled(false);
                btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        power_add = item_add.get(position);

                        addpos = power_add;
                        if (id_lensa.isEmpty())
                        {
                            btnChoose.setEnabled(false);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        }
                        else
                        {
                            btnChoose.setEnabled(true);
                            btnChoose.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        }
                    }
                });

                dialog.show();

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtAdd.setText(power_add);
                        dialog.hide();
                    }
                });
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kode = txtlenstype.getText().toString();
                String sph  = txtSph.getText().toString();
                String cyl  = txtCyl.getText().toString();
                String add  = txtAdd.getText().toString();

                showPrice(kode, sph, cyl, add);
                getAllKurir(opticCity, opticProvince);
            }
        });
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(FormBatchOrderActivity.this)
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(60)
                .build();
        loading.show();
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

    private void getIdOptic()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            opticId     = bundle.getString("idparty");
            opticName   = bundle.getString("opticname");
            opticProvince = bundle.getString("province");
            opticUsername = bundle.getString("usernameInfo");
            opticCity   = bundle.getString("city");
            opticFlag   = bundle.getString("flag");
            opticAddress= bundle.getString("province_address");
        }
        opticId     = opticId + ",";
        opticName   = opticName + ",";

        //Toast.makeText(getApplicationContext(), opticUsername, Toast.LENGTH_SHORT).show();
    }

    private void getAllPayment()
    {
        paymentmethodList.clear();
        adapter_paymentmethod = new Adapter_paymentmethod(FormBatchOrderActivity.this, paymentmethodList);
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
        //shipmentList.clear();

        if (opticCity == null)
        {
            txtTitleShip.setVisibility(View.GONE);
            txtShippingPrice.setVisibility(View.GONE);
//            spinShipping.setVisibility(View.GONE);

            shipmentPrice = "0";
            totalAllPrice = priceDisc;
            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
        }
        else
        {
            if (opticFlag.equals("0"))
            {
//                txtInfoShipping.setText("Belum termasuk ongkos kirim. Kurir dan tarif pengiriman sesuai kebijakan Timur Raya");
                scalableCourier.setVisibility(View.GONE);
                txtInfoShipping.setText("Belum termasuk ongkos kirim. Kurir dan tarif pengiriman sesuai kebijakan Timur Raya");

//                shipmentPrice = "0";
//                totalAllPrice = (totalPrice - totalDisc) + Integer.valueOf(shipmentPrice);
//                txtShippingPrice.setText("Rp. " + CurencyFormat(shipmentPrice));
//                txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                shipmentPrice = "0";
                totalAllPrice = (totalPrice - totalDisc) + Integer.valueOf(shipmentPrice);
                txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                txtShippingPrice.setText("Rp. " + CurencyFormat(shipmentPrice));
            }
            else
            {
                txtInfoShipping.setVisibility(View.GONE);
                getAllKurir(opticCity, opticProvince);
                scalableCourier.setVisibility(View.VISIBLE);
            }

            txtTitleShip.setVisibility(View.VISIBLE);
            txtShippingPrice.setVisibility(View.VISIBLE);
//            spinShipping.setVisibility(View.VISIBLE);
        }

        lensPartaiHelper.open();
        item_partai = lensPartaiHelper.getAllPartai();
        adapter_add_partai = new Adapter_add_partai(this, item_partai, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                int btn = view.getId();

                switch (btn){
                    case R.id.item_partaiproduct_btnRemove:
                        lensPartaiHelper.deleteLensPartai(item_partai.get(pos).getProductId());

                        item_partai.remove(pos);
                        adapter_add_partai.notifyItemRemoved(pos);

                        if (item_partai.size() > 0)
                        {
                            linearLayout.setVisibility(View.GONE);
                            linearPayment.setVisibility(View.VISIBLE);
                            cardContinue.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.VISIBLE);
//                            scalableCourier.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            linearPayment.setVisibility(View.GONE);
                            cardContinue.setVisibility(View.GONE);
                            cardView.setVisibility(View.GONE);
//                            scalableCourier.setVisibility(View.GONE);
                        }

                        adapter_add_partai.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter_add_partai);

                        totalPrice = lensPartaiHelper.countTotalPrice();
                        priceDisc  = lensPartaiHelper.countTotalDiscPrice();
                        totalDisc  = totalPrice - priceDisc;

                        txtSubtotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtSubtotalDisc.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));

                        if (opticCity == null)
                        {
                            shipmentPrice = "0";
                            totalAllPrice = priceDisc;
                            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }
                        else
                        {
                            totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
                            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }

                        BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(FormBatchOrderActivity.this);
                        snackBarMessage.showWarningMessage("Item has been removed");

                        break;

                    case R.id.item_partaiproduct_btnPlus:
                        ModelLensPartai modelPartai = lensPartaiHelper.getLensPartai(item_partai.get(pos).getProductId());

                        int price = modelPartai.getProductPrice();
                        int qty   = modelPartai.getProductQty();
                        int stock = modelPartai.getProductStock();
                        int discprice = modelPartai.getProductDiscPrice();

                        qty = qty + 1;
                        int newprice = qty * price;
                        int newdiscprice = qty * discprice;
                        stock = stock - qty;

                        Log.d(FormBatchOrderActivity.class.getSimpleName(), "New Price : " + newprice);
                        Log.d(FormBatchOrderActivity.class.getSimpleName(), "New Discount price : " + newdiscprice);

                        modelPartai.setProductQty(qty);
                        modelPartai.setNewProductPrice(newprice);
                        modelPartai.setNewProductDiscPrice(newdiscprice);
                        modelPartai.setProductStock(stock);

                        item_partai.set(pos, modelPartai);
                        lensPartaiHelper.updateLensPartaiQty(modelPartai);
                        adapter_add_partai.notifyDataSetChanged();

                        totalPrice = lensPartaiHelper.countTotalPrice();
                        priceDisc  = lensPartaiHelper.countTotalDiscPrice();
                        totalDisc  = totalPrice - priceDisc;

                        txtSubtotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtSubtotalDisc.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));

                        if (opticCity == null)
                        {
                            shipmentPrice = "0";
                            totalAllPrice = priceDisc;
                            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }
                        else
                        {
                            totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
                            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }

                        break;

                    case R.id.item_partaiproduct_btnMinus:
                        ModelLensPartai modellensPartai = lensPartaiHelper.getLensPartai(item_partai.get(pos).getProductId());

                        int mPrice = modellensPartai.getProductPrice();
                        int mQty   = modellensPartai.getProductQty();
                        int mDiscPrice = modellensPartai.getProductDiscPrice();
                        int mStock = modellensPartai.getProductStock();

                        mQty = mQty - 1;
//                        mStock = mStock + mQty;

                        if (mQty == 0)
                        {
                            BottomSnackBarMessage snackMsg = new BottomSnackBarMessage(FormBatchOrderActivity.this);
                            snackMsg.showWarningMessage("Qty minimal 1 pcs");
                        }
                        else
                        {
                            int mNewPrice = mQty * mPrice;
                            int mNewDiscPrice = mQty * mDiscPrice;

                            modellensPartai.setProductQty(mQty);
                            modellensPartai.setNewProductPrice(mNewPrice);
                            modellensPartai.setNewProductDiscPrice(mNewDiscPrice);
                            modellensPartai.setProductStock(mStock);

                            item_partai.set(pos, modellensPartai);
                            lensPartaiHelper.updateLensPartaiQty(modellensPartai);
                            adapter_add_partai.notifyDataSetChanged();
                        }

                        totalPrice = lensPartaiHelper.countTotalPrice();
                        priceDisc  = lensPartaiHelper.countTotalDiscPrice();
                        totalDisc  = totalPrice - priceDisc;

                        txtSubtotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtSubtotalDisc.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));

                        if (opticCity == null)
                        {
                            shipmentPrice = "0";
                            totalAllPrice = priceDisc;
                            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }
                        else
                        {
                            totalAllPrice = priceDisc + Integer.valueOf(shipmentPrice);
                            txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                        }

                        break;
                }
            }
        });

        if (item_partai.size() > 0)
        {
            linearLayout.setVisibility(View.GONE);
            linearPayment.setVisibility(View.VISIBLE);
            cardContinue.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
//            scalableCourier.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayout.setVisibility(View.VISIBLE);
            linearPayment.setVisibility(View.GONE);
            cardContinue.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
//            scalableCourier.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter_add_partai);
    }

    private void dialogAllLens()
    {
        final ListView listView;
        final BootstrapButton btn_save;
        final Dialog dialog = new Dialog(FormBatchOrderActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_choose_lensstock);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        adapter_lenstype = new Adapter_lenstype(FormBatchOrderActivity.this, item_stocklens);

        listView    =  dialog.findViewById(R.id.choose_lensstock_listview);
        btn_save    =  dialog.findViewById(R.id.choose_lensstock_btnsave);

        getAllStock();
        listView.setAdapter(adapter_lenstype);

        btn_save.setEnabled(false);
        btn_save.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_lensa = item_stocklens.get(position).getLenstype();
                desc_lensa = item_stocklens.get(position).getLensdescription();

                if (id_lensa.isEmpty())
                {
                    btn_save.setEnabled(false);
                    btn_save.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                }
                else
                {
                    btn_save.setEnabled(true);
                    btn_save.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtlenstype.setText(id_lensa);
                txtlensdescription.setText(desc_lensa);

                showSphPower(id_lensa);
                showCylPower(id_lensa);
                showAddPower(id_lensa);

                dialog.hide();
            }
        });

        dialog.show();
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

    private void getAllStock()
    {
        item_stocklens.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLALLLENS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String deskripsi = object.getString("item_desc");
                        String kodelensa = object.getString("item_code_opto");
                        String gambar    = object.getString("image");

                        Data_lenstype dataLenstype = new Data_lenstype();
                        dataLenstype.setImage(gambar);
                        dataLenstype.setLenstype(kodelensa);
                        dataLenstype.setLensdescription(deskripsi);

                        item_stocklens.add(dataLenstype);
                    }

                    adapter_lenstype.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showSphPower(final String kodeLensa)
    {
        item_sph.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLPOWERSPH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String sph = jsonObject.getString("sph");

                        item_sph.add(sph);
                    }

                    String first = item_sph.get(0);
                    txtSph.setText(first);
                    sphpos = first;
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
                hashMap.put("key", kodeLensa);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showCylPower(final String kodeLensa)
    {
        item_cyl.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLPOWERCYL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String cyl = jsonObject.getString("cyl");

                        item_cyl.add(cyl);
                    }

                    String first = item_cyl.get(0);
                    txtCyl.setText(first);
                    cylpos = first;
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
                hashMap.put("key", kodeLensa);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showAddPower(final String kodeLensa)
    {
        item_add.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLPOWERADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String add = jsonObject.getString("add");

                        item_add.add(add);
                    }

                    String first = item_add.get(0);
                    txtAdd.setText(first);
                    addpos = first;
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
                hashMap.put("key", kodeLensa);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showPrice(final String kodeLensa, final String sph, final String cyl, final String add)
    {
        lensPartaiHelper.open();
        //lensPartaiHelper.truncLensPartai();

        StringRequest request = new StringRequest(Request.Method.POST, URLGETPRICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error"))
                        {
                            BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(FormBatchOrderActivity.this);
                            snackBarMessage.showErrorMessage("Maaf, data tidak dapat ditemukan");
                        }
                        else
                        {
                            String productId        = object.getString("item_id");
                            String productCode      = object.getString("item_code");
                            String productDesc      = object.getString("item_desc");
                            String productSph       = object.getString("sph");
                            String productCyl       = object.getString("cyl");
                            String productAdd       = object.getString("add");
                            String productSide      = object.getString("side");
                            String productQty       = "1";
                            String productprice     = object.getString("price");
                            String productDisc      = "0";
                            String productDiscprice = "0";
                            String productNewPrice  = object.getString("price");
                            String productNewDiscprice = "0";
                            String productStock     = object.getString("qty");
                            String productWeight    = object.getString("weight");
                            String productImage     = object.getString("image");
                            orgName                 = object.getString("org_name");
                            availableStock          = object.getInt("qty");
                            prodAttrVal             = object.getString("prod_attr");

                            //Toasty.info(getApplicationContext(), price, Toast.LENGTH_SHORT).show();

                            if (availableStock < 1)
                            {
                                productprice = "0";
                                productNewPrice = "0";
                            }

                            ModelLensPartai itemPartai = new ModelLensPartai();
                            itemPartai.setProductId(Integer.valueOf(productId));
                            itemPartai.setProductCode(productCode);
                            itemPartai.setProductDesc(productDesc);
                            itemPartai.setPowerSph(productSph);
                            itemPartai.setPowerCyl(productCyl);
                            itemPartai.setPowerAdd(productAdd);
                            itemPartai.setProductSide(productSide);
                            itemPartai.setProductQty(Integer.valueOf(productQty));
                            itemPartai.setProductPrice(Integer.valueOf(productprice));
                            itemPartai.setProductDisc(Integer.valueOf(productDisc));
                            itemPartai.setProductDiscPrice(Integer.valueOf(productDiscprice));
                            itemPartai.setNewProductPrice(Integer.valueOf(productNewPrice));
                            itemPartai.setNewProductDiscPrice(Integer.valueOf(productNewDiscprice));
                            itemPartai.setProductStock(Integer.valueOf(productStock));
                            itemPartai.setProductWeight(Integer.valueOf(productWeight));
                            itemPartai.setProductImage(productImage);

                            long status = lensPartaiHelper.insertLensPartai(itemPartai);

                            if (status > 0)
                            {
                                //Log.d(FormBatchOrderActivity.class.getSimpleName(), "Data disimpan ke sqlite");
                                //Toasty.success(getApplicationContext(), "Item has been save in sqlite", Toast.LENGTH_SHORT).show();

                                //showCart();
                                showStbStockItem(itemPartai);

                                BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(FormBatchOrderActivity.this);
                                snackBarMessage.showSuccessMessage("New Item has been added");
                            }
                            else
                            {
                                BottomSnackBarMessage snackBarMessage = new BottomSnackBarMessage(FormBatchOrderActivity.this);
                                snackBarMessage.showWarningMessage("Upps, this item has been added");
                            }
                        }
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
                hashMap.put("lenscode", kodeLensa);
                hashMap.put("sph", sph);
                hashMap.put("cyl", cyl);
                hashMap.put("add", add);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showStbStockItem(final ModelLensPartai partai)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETSTBSTOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    String segment1 = object.getString("segment_1");
                    String segment2 = object.getString("segment_2");
                    String segment3 = object.getString("segment_3");
                    String segment4 = object.getString("segment_4");
                    String segment5 = object.getString("segment_5");
                    String segment6 = object.getString("segment_6");
                    String segment7 = object.getString("segment_7");
                    String segment8 = object.getString("segment_8");

                    StringBuilder sb = new StringBuilder();
                    sb.append(segment1).append('.')
                            .append(segment2).append('.')
                            .append(segment3).append('.')
                            .append(segment4).append('.')
                            .append(segment5).append('.')
                            .append(segment6).append('.')
                            .append(segment7).append('.')
                            .append(segment8);

                    String prod_attr_val = sb.toString();

                    getDiscountItem(prod_attr_val, opticUsername, partai);
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
                hashMap.put("item_id", String.valueOf(partai.getProductId()));
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDiscountItem(final String prodAttr, final String username, final ModelLensPartai partai)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETDISCOUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    int diskon;

                    if (object.names().get(0).equals("error"))
                    {
                        diskon = 0;
                    }
                    else
                    {
                        diskon = object.getInt("operand");
                    }

                    Log.d(FormBatchOrderActivity.class.getSimpleName(), "Diskon = " + diskon);

                    int price     = partai.getProductPrice();
                    Log.d(FormBatchOrderActivity.class.getSimpleName(), "Harga item = " + price);
                    int discprice = diskon * price / 100;
                    Log.d(FormBatchOrderActivity.class.getSimpleName(), "Harga diskon = " + discprice);
                    int newprice = price - discprice;
                    Log.d(FormBatchOrderActivity.class.getSimpleName(), "Harga setelah diskon = " + newprice);

                    ModelLensPartai data = new ModelLensPartai();
                    data.setProductId(Integer.valueOf(partai.getProductId()));
                    data.setProductDisc(diskon);
                    data.setProductDiscPrice(newprice);
                    data.setNewProductDiscPrice(newprice);

                    lensPartaiHelper.open();
                    int status = lensPartaiHelper.updateLensPartaiDisc(data);
                    //Toasty.info(getApplicationContext(), "Diskon = " + diskon, Toast.LENGTH_SHORT).show();
                    if (status > 0)
                    {
                        //shipmentList.clear();
                        showCart();
                        //getAllKurir(opticCity);

                        totalPrice = lensPartaiHelper.countTotalPrice();
                        priceDisc  = lensPartaiHelper.countTotalDiscPrice();
                        totalDisc  = totalPrice - priceDisc;

                        txtSubtotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalPrice)));
                        txtSubtotalDisc.setText("Rp. - " + CurencyFormat(String.valueOf(totalDisc)));
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
                hashMap.put("prod_attr", prodAttr);
                hashMap.put("customer_number", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getAllKurir(final String city, final String prov)
    {
        shipmentList.clear();
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
                        String estimasi = jsonObject.getString("estimasi");

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
                    txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
                    txtShippingPrice.setText("Rp. " + CurencyFormat(shipmentPrice));
                    txtTotalPrice.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));

                    adapterCourierService.notifyDataSetChanged();
                    recyclerCourier.setAdapter(adapterCourierService);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                adapter_spinner_shipment.notifyDataSetChanged();
//                spinShipping.setAdapter(adapter_spinner_shipment);
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

    private void generateOrderNumber(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GENERATEID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    orderId = object.getString("lastnumber");
                    Log.d(FormBatchOrderActivity.class.getSimpleName(), "Order Number " + orderId);
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
                hashMap.put("user", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getPhoneNumber(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLPHONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    phone = jsonObject.getString("phone");
                }
                catch (JSONException e)
                {
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
                hashMap.put("username", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertHeader(final Data_partai_header item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLINSERTHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.d(FormBatchOrderActivity.class.getSimpleName(), "Success Insert Header");
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
                hashMap.put("order_number", item.getOrderNumber());
                hashMap.put("id_party", String.valueOf(item.getIdParty()));
                hashMap.put("optic_name", item.getOpticName());
                hashMap.put("optic_address", item.getOpticAddress());
                hashMap.put("optic_city", item.getOpticCity());
                hashMap.put("phone_number", item.getPhoneNumber());
                hashMap.put("prod_div_type", orgName);
                hashMap.put("shipping_id", String.valueOf(item.getShippingId()));
                hashMap.put("shipping_name", item.getShippingName());
                hashMap.put("optic_province", item.getOpticProvince());
                hashMap.put("shipping_service", item.getShippingService());
                hashMap.put("shipping_price", String.valueOf(item.getShippingPrice()));
                hashMap.put("total_price", String.valueOf(item.getTotalPrice()));
                hashMap.put("payment_cashcarry", item.getPayment_cashcarry());
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertItem(final Data_partai_item item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLINSERTITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        //lensPartaiHelper.open();

                        int status = lensPartaiHelper.truncLensPartai();

                        if (status > 0)
                        {

                        }
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
                hashMap.put("order_number", item.getOrderNumber());
                hashMap.put("item_id", String.valueOf(item.getItemId()));
                hashMap.put("item_code", item.getItemCode());
                hashMap.put("description", item.getDescription());
                hashMap.put("side", item.getSide());
                hashMap.put("qty", String.valueOf(item.getQty()));
                hashMap.put("price", String.valueOf(item.getPrice()));
                hashMap.put("discount_name", item.getDiscount_name());
                hashMap.put("discount", String.valueOf(item.getDiscount()));
                hashMap.put("total_price", String.valueOf(item.getTotal_price()));
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertNonPayment(final String orderId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLNONPAYMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.d(FormBatchOrderActivity.class.getSimpleName(), "Success Insert Non Payment");
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
                hashmap.put("order_id", orderId);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void postBillingQR(final String paymentType, final String grossAmount, final String orderId)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.hide();
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
                loading.hide();
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

    private void postBillingVA(final String paymentType, final String grossAmount, final String orderId)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.hide();
                    JSONObject jsonObject = new JSONObject(response);

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
                hashMap.put("orderId", orderId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void postBillingLoan(final String paymentType, final String grossAmount, final String order)
    {
        //showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //loading.hide();
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

    private void createBillingCC(final String orderNumber, final String paymentType)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLCC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.hide();
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Call ShowSessionPayment
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
                        Intent intent = new Intent(FormBatchOrderActivity.this, FormPaymentQR.class);
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
                        Intent intent = new Intent(FormBatchOrderActivity.this, FormPaymentVA.class);
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
                        Intent intent = new Intent(FormBatchOrderActivity.this, FormPaymentCC.class);
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
                        Intent intent = new Intent(FormBatchOrderActivity.this, FormPaymentLoanActivity.class);
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
}
