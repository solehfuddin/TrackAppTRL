package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_paymentmethod;
import com.sofudev.trackapptrl.Adapter.Adapter_spinner_shipment;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_paymentmethod;
import com.sofudev.trackapptrl.Data.Data_spin_shipment;
import com.sofudev.trackapptrl.LocalDb.Model.ModelDetailOrderHistory;
import com.sofudev.trackapptrl.LocalDb.Model.ModelOrderHistory;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;

public class FormLensSummaryActivity extends AppCompatActivity {
    Config config = new Config();
    String URL_ALLDATA = config.Ip_address + config.spinner_shipment_getAllData;
    String URL_LENSINFO= config.Ip_address + config.lens_summary_getLensInfo;
    String URL_INSERTDT= config.Ip_address + config.lens_summary_insertOrder;
    String URL_INSERTDTTEMP = config.Ip_address + config.lens_summary_insertOrderTemp;
    String URL_INSERTDETAILORDER = config.Ip_address + config.lens_summary_insertDetailOrder;
    String URL_ALLPAYMENT = config.Ip_address + config.payment_method_showAllData;
    String URL_INSERTBILLQR = config.Ip_address + config.payment_insert_billingQR;
    String URL_INSERTBILLVA = config.Ip_address + config.payment_insert_billingVA;
    String URL_INSERTBILLCC = config.Ip_address + config.payment_insert_billingCC;
    String URL_INSERTBILLLOAN = config.Ip_address + config.payment_insert_billingLOAN;
    String URL_INSERTBILLKP = config.Ip_address + config.payment_insert_billingKP;
    String URL_SHOWSESSIONPAYMENTQR = config.Ip_address + config.payment_show_expiredDurationQR;
    String URL_SHOWSESSIONPAYMENTVA = config.Ip_address + config.payment_show_expiredDurationVA;
    String URL_SHOWSESSIONPAYMENTCC = config.Ip_address + config.payment_show_expiredDurationCC;
    String URL_SHOWSESSIONPAYMENTLOAN = config.Ip_address + config.payment_show_expiredDurationLOAN;
    String URL_SHOWSESSIONPAYMENTKP = config.Ip_address + config.payment_show_expiredDurationKP;
    String URL_GETUSERINFO  = config.Ip_address + config.profile_user_detail;
    String URL_POSTBILLING= config.payment_method_postBilling;

    ACProgressCustom loading;
    Button btnCheckout;
    UniversalFontTextView txtLensDescr, txtPriceLens, txtPriceDisc, txtPriceFacet, txtPriceTinting, txtPriceShipping,
                            txtPriceTotal, txtItemWeight, txtLensModel, txtSide, txtShippingMethod;
    ImageView imgLensModel;
    ListView listPayment;
    String orderNumber, opticUsername, hargaLensa, deskripsiLensa, diskonLensa, facetLensa, tintingLensa, totalPrice,
            tempTotal, cityOptic, itemWeight, lensCategory, date1, date2, addTemp, flagShipping, patientName, idParty,
            shippingMethod, kodeBilling, duration, expDate, ownerPhone;
    String itemCodeR, descR, powerR, uom, qtyR, priceR, totalPriceR, marginR, extraMarginR, itemCodeL, descL, powerL,
            qtyL, priceL, totalPriceL, marginL, extraMarginL, discountItem, discountR, discountL, extraMarginDiscount,
            itemFacetCode, facetDescription, facetqty, facetPrice, facetTotal, facetMargin, facetExtraMargin,
            itemTintingCode, tintingDescription, tintingqty, tintingPrice, tintingTotal, tintingMargin, tintingExtraMargin;

    int totalWeight = 0;
    Spinner spinShipment;
    private static final String[] shipment = new String[] {"Shipping Method"};
    private static final String[] payment = new String[] {"QR Code", "Virtual Account", "Credit Card", "Loan"};
    List<Data_paymentmethod> paymentmethodList = new ArrayList<>();
    List<Data_spin_shipment> shipmentList = new ArrayList<>();
    ArrayAdapter adapterListShipment;

    Adapter_paymentmethod adapter_paymentmethod;
    Adapter_spinner_shipment adapter_spinner_shipment;
    String selectedItem;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lens_summary);
        showLoading();

        btnCheckout     = (Button) findViewById(R.id.activity_lenssummary_btn_checkout);
        txtLensDescr    = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_lensdesc);
        txtPriceLens    = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_pricelens);
        txtPriceDisc    = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_pricedisc);
        txtPriceFacet   = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_pricefacet);
        txtPriceTinting = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_pricetinting);
        txtPriceShipping= (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_priceshipment);
        txtPriceTotal   = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_pricetotal);
        txtItemWeight   = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_itemweight);
        txtLensModel    = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_lensmodel);
        txtSide         = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_side);
        txtShippingMethod = (UniversalFontTextView) findViewById(R.id.activity_lenssummary_txt_shippingmethod);
        spinShipment    = (Spinner) findViewById(R.id.activity_lenssummary_spin_shipment);
        imgLensModel    = (ImageView) findViewById(R.id.activity_lenssummary_img_logo);
        listPayment     = (ListView) findViewById(R.id.activity_lenssummary_listview_paymentMethod);

        gettingPrice();

        btnCheckout.setBackgroundColor(Color.LTGRAY);
        btnCheckout.setTextColor(Color.BLACK);
        btnCheckout.setEnabled(false);

        //adapterListShipment = new ArrayAdapter(this, R.layout.listview_payment, payment);

        txtLensDescr.setText(deskripsiLensa);
        txtPriceLens.setText(hargaLensa);
        txtPriceDisc.setText(diskonLensa);
        txtPriceFacet.setText(facetLensa);
        txtPriceTinting.setText(tintingLensa);
        txtItemWeight.setText(itemWeight + " gram");

        txtPriceTotal.setText("Rp " + totalPrice);

        listPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem  = paymentmethodList.get(i).getPaymentMethod();

                if (selectedItem.contentEquals("Kreditpro") || selectedItem.equals("Kreditpro")
                        || selectedItem.contains("Kreditpro"))
                {
                    btnCheckout.setBackgroundColor(Color.LTGRAY);
                    btnCheckout.setTextColor(Color.BLACK);
                    btnCheckout.setEnabled(false);
                }
//                else if (selectedItem.contentEquals("Virtual Account") || selectedItem.equals("Virtual Account")
//                        || selectedItem.contains("Virtual Account"))
//                {
//                    btnCheckout.setBackgroundColor(Color.LTGRAY);
//                    btnCheckout.setTextColor(Color.BLACK);
//                    btnCheckout.setEnabled(false);
//                }
//                else if (selectedItem.contentEquals("Loan") || selectedItem.equals("Loan")
//                        || selectedItem.contains("Loan"))
//                {
//                    btnCheckout.setBackgroundColor(Color.LTGRAY);
//                    btnCheckout.setTextColor(Color.BLACK);
//                    btnCheckout.setEnabled(false);
//                }
                else
                {
                    btnCheckout.setBackgroundColor(getResources().getColor(R.color.colorFirst));
                    btnCheckout.setTextColor(Color.WHITE);
                    btnCheckout.setEnabled(true);
                }
            }
        });

        //spinShipment.setAdapter(new ArrayAdapter<>(FormLensSummaryActivity.this, R.layout.spin_shipment_title, shipment));

        getAllPayment();
        getAllKurir(cityOptic);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showLoading();
                insertLokalDb();

                if (selectedItem.contentEquals("QR Code") || selectedItem.equals("QR Code") || selectedItem.contains("QR Code"))
                {
                    String paymentType = "internetBanking";
                    String grossAmount = removeRupiah(txtPriceTotal.getText().toString());
                    grossAmount = grossAmount.replace(",00", "");
                    String orderId     = orderNumber;

                    postBillingQR(paymentType, grossAmount, orderId);

                }
                else if (selectedItem.contentEquals("Virtual Account") || selectedItem.equals("Virtual Account") ||
                        selectedItem.contains("Virtual Account"))
                {
                    String paymentType = "bankTransfer";
                    String grossAmount = removeRupiah(txtPriceTotal.getText().toString());
                    grossAmount = grossAmount.replace(",00", "");
                    String orderId     = orderNumber;

                    postBillingVA(paymentType, grossAmount, orderId);
                }
                else if (selectedItem.contentEquals("Credit Card") || selectedItem.equals("Credit Card") ||
                        selectedItem.contains("Credit Card"))
                {
                    createBillingCC(orderNumber, "creditCard");
                }
                else if (selectedItem.contentEquals("Loan") || selectedItem.equals("Loan") ||
                        selectedItem.contains("Loan"))
                {
//                    createBillingLoan(orderNumber, "loanKS");
                    String paymentType = "loanKS";
                    String grossAmount = removeRupiah(txtPriceTotal.getText().toString());
                    grossAmount = grossAmount.replace(",00", "");
                    String orderId     = orderNumber;

//                    Toasty.info(getApplicationContext(), "Total amount : " + grossAmount, Toast.LENGTH_SHORT).show();

                    postBillingLoan(paymentType, grossAmount, orderId);
                }
                else if (selectedItem.contentEquals("Kreditpro") || selectedItem.equals("Kreditpro") ||
                        selectedItem.contains("Kreditpro"))
                {
//                    Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentKreditpro.class);
//                    startActivity(intent);

                    createBillingKP(orderNumber, "kreditPro");
                }

                loading.dismiss();
            }
        });

        spinShipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String shipmentPrice;

                if (!shipmentList.isEmpty())
                {
                    shipmentPrice = shipmentList.get(position).getPrice();
                    shippingMethod= "Tarif Pengiriman " + shipmentList.get(position).getKurir();
                    txtPriceShipping.setText(CurencyFormat(shipmentPrice));
                }
                else
                {
                    shipmentPrice = "0,00";
                    shippingMethod= "Gratis Tarif Pengiriman";
                    txtPriceShipping.setText(shipmentPrice);
                }

                Float addNew = Float.parseFloat(tempTotal) + Float.parseFloat(shipmentPrice);
                addTemp = CurencyFormat(addNew.toString());
                txtPriceTotal.setText("Rp " + addTemp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loading.dismiss();
    }

    private void disableButton() {
        btnCheckout.setEnabled(false);
        btnCheckout.setBackgroundColor(Color.GRAY);
        btnCheckout.setTextColor(Color.BLACK);
    }

    private void enableButton() {
        btnCheckout.setEnabled(true);
        btnCheckout.setBackgroundColor(Color.parseColor("#ff8800"));
        btnCheckout.setTextColor(Color.BLACK);
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(FormLensSummaryActivity.this)
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(60)
                .build();
        loading.show();
    }

    private void postBillingQR(final String paymentType, final String grossAmount, final String orderId)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Post qr : " + response);

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
                //loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
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

    private void postBillingVA(final String paymentType, final String grossAmount, final String orderId)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Post va : " + response);

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
                    //loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
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
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Create cc : " + response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Call ShowSessionPayment
                        showSessionPaymentCC(orderNumber);
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.warning(getApplicationContext(), "Data harus diisi", Toast.LENGTH_SHORT).show();
                    }
                    //loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
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

    private void postBillingLoan(final String paymentType, final String grossAmount, final String orderId)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POSTBILLING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Post loan : " + response);

                    String statusCode   = jsonObject.getString("responseCode");
                    String description  = jsonObject.getString("responseDescription");

                    if (statusCode.equals("00") || statusCode.contains("00") || statusCode.contentEquals("00"))
                    {
                        kodeBilling = jsonObject.getString("billidId");

                        String paymentType = "loanKS";

                        createBillingLoan(orderId, paymentType, kodeBilling);
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), description + " (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }
                    //loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
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

    private void createBillingLoan(final String orderNumber, final String paymentType, final String billingId)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLLOAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Post Bill success", Toast.LENGTH_SHORT).show();
                        showSessionPaymentLoan(orderNumber);
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
                hashMap.put("billing_id", billingId);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void createBillingKP(final String orderNumber, final String paymentType)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLKP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Call ShowSessionPayment
                        showSessionPaymentKP(orderNumber);
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

//        AppController.getInstance().addToRequestQueue(stringRequest);
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void showSessionPaymentQR(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTQR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Respon qr : " + response);

                    String amount      = "Rp " + addTemp;
                    duration = jsonObject.getString("duration");
                    expDate  = jsonObject.getString("exp_date");

                    Toasty.success(getApplicationContext(), "Total amount : " + amount, Toast.LENGTH_LONG).show();

                    if (duration != null)
                    {
                        Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentQR.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("billingCode", kodeBilling);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    }

                    finish();
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
                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Respon VA : " + response);

                    String amount      = "Rp " + addTemp;
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

                    if (duration != null)
                    {
                        Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentVA.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("billingCode", kodeBilling);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    }

                    finish();
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

                    String amount = addTemp;
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

                    if (duration != null)
                    {
                        Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentCC.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Please, Try Again !", Toast.LENGTH_SHORT).show();
                    }

                    finish();
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

                    Log.d(FormLensSummaryActivity.class.getSimpleName(), "Respon loan : " + response);

                    String amount      = addTemp;
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

//                    Toasty.success(getApplicationContext(), "Total amount : " + amount, Toast.LENGTH_LONG).show();

                    if (duration != null)
                    {
                        Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentLoanActivity.class);
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

                    finish();
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

    private void showSessionPaymentKP(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTKP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String amount = addTemp;
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

                    if (duration != null)
                    {
                        Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentKreditpro.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("amount", amount);
                        intent.putExtra("duration", duration);
                        intent.putExtra("expDate", expDate);
                        intent.putExtra("ownerPhone", ownerPhone);
                        startActivity(intent);
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Please, Try Again !", Toast.LENGTH_SHORT).show();
                    }

                    finish();
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

//        AppController.getInstance().addToRequestQueue(stringRequest);
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void getAllPayment()
    {
        adapter_paymentmethod = new Adapter_paymentmethod(FormLensSummaryActivity.this, paymentmethodList);
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

    private void getAllKurir(final String city)
    {
        adapter_spinner_shipment = new Adapter_spinner_shipment(FormLensSummaryActivity.this, shipmentList);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ALLDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String kurir = jsonObject.getString("kurir");
                        String icon  = jsonObject.getString("icon");
                        String price = jsonObject.getString("price");

                        Data_spin_shipment data_spin_shipment = new Data_spin_shipment();
                        data_spin_shipment.setKurir(kurir);
                        data_spin_shipment.setIcon(icon);
                        data_spin_shipment.setPrice(price);

                        shipmentList.add(data_spin_shipment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter_spinner_shipment.notifyDataSetChanged();
                spinShipment.setAdapter(adapter_spinner_shipment);
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
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0,00";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###.00";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private String removeRupiah(String price)
    {
        String output;
        output = price.replace("Rp ", "").replace("Rp", "").replace(".","").trim();

        return output;
    }

    private void gettingPrice()
    {
        Bundle bundle = getIntent().getExtras();
        Integer a = bundle.getInt("price_lens");
        String b  = bundle.getString("description_lens");
        Float c   = bundle.getFloat("discount_lens");
        Integer d = bundle.getInt("facet_lens");
        Integer e = bundle.getInt("tinting_lens");
        cityOptic = bundle.getString("city_info");
        Integer f = bundle.getInt("item_weight");
        String h  = bundle.getString("flag_pasang");

        Float g   = bundle.getFloat("total_price");
        opticUsername = bundle.getString("username_info");
        flagShipping  = bundle.getString("flag_shipping");
        orderNumber   = bundle.getString("order_number");
        patientName   = bundle.getString("patient_name");
        idParty       = bundle.getString("id_party");

        hargaLensa = CurencyFormat(a.toString());
        deskripsiLensa = b;
        diskonLensa = CurencyFormat(c.toString());
        facetLensa  = CurencyFormat(d.toString());
        tintingLensa = CurencyFormat(e.toString());
        tempTotal  = g.toString();
        totalPrice = CurencyFormat(g.toString());
        itemWeight = f.toString();

        //AREA R
        itemCodeR   = bundle.getString("itemcode_R");
        descR       = bundle.getString("description_R");
        powerR      = bundle.getString("power_R");
        uom         = "PCS";
        qtyR        = bundle.getString("qty_R");
        Integer pr  = bundle.getInt("itemprice_R");
        priceR      = CurencyFormat(pr.toString());
        Integer ptr = bundle.getInt("itemtotal_R");
        totalPriceR = CurencyFormat(ptr.toString());
        marginR     = bundle.getString("margin_lens");
        extraMarginR= bundle.getString("extramargin_lens");

        //AREA L
        itemCodeL   = bundle.getString("itemcode_L");
        descL       = bundle.getString("description_L");
        powerL      = bundle.getString("power_L");
        uom         = "PCS";
        qtyL        = bundle.getString("qty_L");
        Integer pl  = bundle.getInt("itemprice_L");
        priceL      = CurencyFormat(pl.toString());
        Integer ptl = bundle.getInt("itemtotal_L");
        totalPriceL = CurencyFormat(ptl.toString());
        marginL     = bundle.getString("margin_lens");
        extraMarginL= bundle.getString("extramargin_lens");

        //Area diskon
        discountItem= bundle.getString("description_diskon");
        Float disR  = bundle.getFloat("discount_r");
        discountR   = CurencyFormat(disR.toString());
        if (discountR.equals(",00") || discountR.equals(",00"))
        {
            discountR = "-";
        }
        else {
            discountR = "-" + discountR;
        }
        Float disL  = bundle.getFloat("discount_l");
        discountL   = CurencyFormat(disL.toString());
        if (discountL.equals(",00") || discountL.equals(",00"))
        {
            discountL = "-";
        }
        else {
            discountL = "-" + discountL;
        }
        extraMarginDiscount = bundle.getString("extra_margin_discount");

        //Area facet
        itemFacetCode   = bundle.getString("itemcode_facet");
        facetDescription= bundle.getString("description_facet");
        facetqty        = bundle.getString("qty_facet");
        Integer fp      = bundle.getInt("price_facet");
        facetPrice      = CurencyFormat(fp.toString());
        Integer tf      = bundle.getInt("total_facet");
        facetTotal      = CurencyFormat(tf.toString());
        facetMargin     = bundle.getString("margin_facet");
        facetExtraMargin= bundle.getString("extra_margin_facet");

        //Area tinting
        itemTintingCode     = bundle.getString("itemcode_tinting");
        tintingDescription  = bundle.getString("description_tinting");
        tintingqty          = bundle.getString("qty_tinting");
        Integer tintp       = bundle.getInt("price_tinting");
        tintingPrice        = CurencyFormat(tintp.toString());
        Integer tintt       = bundle.getInt("total_tinting");
        tintingTotal        = CurencyFormat(tintt.toString());
        tintingMargin       = bundle.getString("margin_tinting");
        tintingExtraMargin  = bundle.getString("extra_margin_tinting");

        //Toasty.info(getApplicationContext(), flagShipping, Toast.LENGTH_SHORT).show();
//        if (flagShipping.equals("0") | flagShipping.contentEquals("0") | flagShipping.contains("0"))
//        {
//            txtShippingMethod.setVisibility(View.GONE);
//            spinShipment.setVisibility(View.GONE);
//            txtPriceShipping.setText("free");
//            addTemp = totalPrice;
//        }
//        else
//        {
//            txtShippingMethod.setVisibility(View.VISIBLE);
//            spinShipment.setVisibility(View.VISIBLE);
//            txtPriceShipping.setText("0,00");
//        }



        if (h.equals("2") | h.contentEquals("2") | h.contains("2"))
        {
            txtSide.setText("RL");
        }
        else
        {
            txtSide.setText("R");
        }

        getLensInformation();
    }

    private void getLensInformation()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LENSINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String catLens  = jsonObject.getString("category_lens");
                    lensCategory    = jsonObject.getString("type_lensa");

                    //txtLensModel.setText(catLens);
                    if (catLens.equals("R") | catLens.contentEquals("R") | catLens.contains("R"))
                    {
                        txtLensModel.setText("RX LENS");
                    }
                    else if (catLens.equals("S") | catLens.contentEquals("S") | catLens.contains("S"))
                    {
                        txtLensModel.setText("STOCK LENS");
                    }

                    Picasso.with(getApplicationContext()).load(jsonObject.getString("icon")).into(imgLensModel);
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
                hashMap.put("lens_description", deskripsiLensa);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void insertLokalDb()
    {
        disableButton();
        ModelOrderHistory data = new ModelOrderHistory();

        SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        date1 = sdf1.format(calendar.getTime());
        date2 = sdf2.format(calendar.getTime());

        data.setOrder_number(orderNumber);
        data.setLens_description(deskripsiLensa);
        data.setLens_category(lensCategory);
        data.setPrice_lens(hargaLensa);
        data.setPrice_discount(diskonLensa);
        data.setPrice_facet(facetLensa);
        data.setPrice_tinting(tintingLensa);
        data.setPrice_shipment(txtPriceShipping.getText().toString());
        data.setPrice_total(addTemp);
        data.setStatus("Pending");
        data.setWeight(itemWeight);
        data.setDate(date2);

        //AREA R
        ModelDetailOrderHistory dataR = new ModelDetailOrderHistory();
        dataR.setOrderNumber(orderNumber);
        dataR.setItemCode(itemCodeR);
        dataR.setDescription(descR);
        dataR.setPower(powerR);
        dataR.setUom(uom);
        dataR.setQty(qtyR);
        dataR.setPrice(priceR);
        dataR.setTotal(totalPriceR);
        dataR.setMargin(marginR);
        dataR.setExtraMargin(extraMarginR);

        //AREA Diskon R
        ModelDetailOrderHistory diskonR = new ModelDetailOrderHistory();
        diskonR.setOrderNumber(orderNumber);
        diskonR.setDescription(discountItem);
        diskonR.setTotal(discountR);
        diskonR.setExtraMargin(extraMarginDiscount);

        //AREA L
        ModelDetailOrderHistory dataL = new ModelDetailOrderHistory();
        dataL.setOrderNumber(orderNumber);
        dataL.setItemCode(itemCodeL);
        dataL.setDescription(descL);
        dataL.setPower(powerL);
        dataL.setUom(uom);
        dataL.setQty(qtyL);
        dataL.setPrice(priceL);
        dataL.setTotal(totalPriceL);
        dataL.setMargin(marginL);
        dataL.setExtraMargin(extraMarginL);

        //AREA Diskon L
        ModelDetailOrderHistory diskonL = new ModelDetailOrderHistory();
        diskonL.setOrderNumber(orderNumber);
        diskonL.setDescription(discountItem);
        diskonL.setTotal(discountL);
        diskonL.setExtraMargin(extraMarginDiscount);

        //AREA Facet
        ModelDetailOrderHistory facet = new ModelDetailOrderHistory();
        facet.setOrderNumber(orderNumber);
        facet.setItemCode(itemFacetCode);
        facet.setDescription(facetDescription);
        facet.setUom(uom);
        facet.setQty(facetqty);
        facet.setPrice(facetPrice);
        facet.setTotal(facetTotal);
        facet.setMargin(facetMargin);
        facet.setExtraMargin(facetExtraMargin);

        //AREA Tinting
        ModelDetailOrderHistory tint = new ModelDetailOrderHistory();
        tint.setOrderNumber(orderNumber);
        tint.setItemCode(itemTintingCode);
        tint.setDescription(tintingDescription);
        tint.setUom(uom);
        tint.setQty(tintingqty);
        tint.setPrice(tintingPrice);
        tint.setTotal(tintingTotal);
        tint.setMargin(tintingMargin);
        tint.setExtraMargin(tintingExtraMargin);

        //AREA Shipment
        ModelDetailOrderHistory shipping = new ModelDetailOrderHistory();
        shipping.setOrderNumber(orderNumber);
        shipping.setItemCode(shippingMethod);
        shipping.setTotal(txtPriceShipping.getText().toString());

        insertExternalDb(data);
        insertExternalDbTemp(data);

        if (dataR.getItemCode() != null && dataR.getDescription() != null)
        {
            if (diskonR.getDescription() != null) {
                insertDetailOrder(dataR);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                insertDetailOrder(diskonR);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                dataR.setExtraMargin("15");
                dataR.setMargin("15");
                insertDetailOrder(dataR);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (dataL.getItemCode() != null && dataL.getDescription() != null)
        {
            if (diskonR.getDescription() != null) {
                insertDetailOrder(dataR);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                insertDetailOrder(diskonR);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                dataR.setExtraMargin("15");
                dataR.setMargin("15");
                insertDetailOrder(dataR);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (facet.getItemCode() != null && facet.getDescription() != null)
        {
            insertDetailOrder(facet);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (tint.getItemCode() != null && tint.getDescription() != null)
        {
            insertDetailOrder(tint);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (shipping.getItemCode() != null)
        {
            insertDetailOrder(shipping);
        }
        else
        {
            shipping.setItemCode("Gratis Tarif Pengiriman");
            shipping.setTotal("Free");

            insertDetailOrder(shipping);
        }


        if (orderNumber != null) {
            Toasty.info(getApplicationContext(), orderNumber, Toast.LENGTH_SHORT).show();
        }
        else
        {
            enableButton();
        }
    }

    private void insertExternalDb(final ModelOrderHistory item)
    {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        final String dateFilter     = sdf3.format(calendar.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTDT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.info(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("order_number", item.getOrder_number());
                hashMap.put("id_party", idParty);
                hashMap.put("lens_description", item.getLens_description());
                hashMap.put("lens_category", item.getLens_category());
                hashMap.put("price_lens", item.getPrice_lens());
                hashMap.put("price_discount", item.getPrice_discount());
                hashMap.put("price_facet", item.getPrice_facet());
                hashMap.put("price_tinting", item.getPrice_tinting());
                hashMap.put("price_shipment", item.getPrice_shipment());
                hashMap.put("price_total", item.getPrice_total());
                hashMap.put("status", item.getStatus());
                hashMap.put("weight", item.getWeight());
                hashMap.put("date", item.getDate());
                hashMap.put("date_filter", dateFilter);
                hashMap.put("patient_name", patientName);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void insertExternalDbTemp(final ModelOrderHistory item)
    {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        final String dateFilter     = sdf3.format(calendar.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTDTTEMP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.info(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("order_number", item.getOrder_number());
                hashMap.put("id_party", idParty);
                hashMap.put("lens_description", item.getLens_description());
                hashMap.put("lens_category", item.getLens_category());
                hashMap.put("price_lens", item.getPrice_lens());
                hashMap.put("price_discount", item.getPrice_discount());
                hashMap.put("price_facet", item.getPrice_facet());
                hashMap.put("price_tinting", item.getPrice_tinting());
                hashMap.put("price_shipment", item.getPrice_shipment());
                hashMap.put("price_total", item.getPrice_total());
                hashMap.put("status", item.getStatus());
                hashMap.put("weight", item.getWeight());
                hashMap.put("date", item.getDate());
                hashMap.put("date_filter", dateFilter);
                hashMap.put("patient_name", patientName);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void insertDetailOrder(final ModelDetailOrderHistory item)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTDETAILORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Toasty.info(getApplicationContext(), "Success insert record", Toast.LENGTH_SHORT).show();
                    }
                    else if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(getApplicationContext(), "Failed to insert record", Toast.LENGTH_SHORT).show();
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
                hashMap.put("item_code", item.getItemCode());
                hashMap.put("description", item.getDescription());
                hashMap.put("power", item.getPower());
                hashMap.put("uom", item.getUom());
                hashMap.put("qty", item.getQty());
                hashMap.put("price", item.getPrice());
                hashMap.put("total", item.getTotal());
                hashMap.put("margin", item.getMargin());
                hashMap.put("extra_margin", item.getExtraMargin());

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
