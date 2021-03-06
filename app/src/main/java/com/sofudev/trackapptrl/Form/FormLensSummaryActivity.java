package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.LongDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sofudev.trackapptrl.Adapter.Adapter_courier_service;
import com.sofudev.trackapptrl.Adapter.Adapter_paymentmethod;
import com.sofudev.trackapptrl.Adapter.Adapter_spinner_shipment;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.DashboardActivity;
import com.sofudev.trackapptrl.Data.Data_lensorderweb;
import com.sofudev.trackapptrl.Data.Data_lensorderweb_item;
import com.sofudev.trackapptrl.Data.Data_paymentmethod;
import com.sofudev.trackapptrl.Data.Data_spheader;
import com.sofudev.trackapptrl.Data.Data_spin_shipment;
import com.sofudev.trackapptrl.LocalDb.Model.ModelDetailOrderHistory;
import com.sofudev.trackapptrl.LocalDb.Model.ModelOrderHistory;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;
import com.ssomai.android.scalablelayout.ScalableLayout;

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
    String URL_INSERTBILLDEPOSIT = config.Ip_address + config.payment_insert_billingDeposit;
    String URL_INSERTBILLKP = config.Ip_address + config.payment_insert_billingKP;
    String URL_SHOWSESSIONPAYMENTQR = config.Ip_address + config.payment_show_expiredDurationQR;
    String URL_SHOWSESSIONPAYMENTVA = config.Ip_address + config.payment_show_expiredDurationVA;
    String URL_SHOWSESSIONPAYMENTCC = config.Ip_address + config.payment_show_expiredDurationCC;
    String URL_SHOWSESSIONPAYMENTLOAN = config.Ip_address + config.payment_show_expiredDurationLOAN;
    String URL_SHOWSESSIONPAYMENTDEPOSIT = config.Ip_address + config.payment_show_expiredDurationDeposit;
    String URL_SHOWSESSIONPAYMENTKP = config.Ip_address + config.payment_show_expiredDurationKP;
    String URL_POSTBILLING= config.payment_method_postBilling;
    String URL_CHECKMEMBER     = config.Ip_address + config.shipment_checkMember;
    String URL_INSERTLENSORDER = config.Ip_address + config.orderlens_insert_lensorder;
    String URL_INSERTLENSITEM  = config.Ip_address + config.orderlens_insert_lensorderitem;
    String URL_UPDATESTOCK     = config.Ip_address + config.orderlens_update_stock;
    String URL_INSERTORDERENTRY= config.Ip_address + config.orderlens_insert_statusentry;
    String URL_GETSIDERX       = config.Ip_address + config.orderlens_get_sidelensrx;
    String URL_GETSIDESTOCK    = config.Ip_address + config.orderlens_get_sidelensstock;
    String URL_GETMEMBERFLAG   = config.Ip_address + config.memberflag_getStatus;
    String URL_GETDISCSALE     = config.Ip_address + config.lens_summary_getDiscSale;
    String URL_GETACTIVESALE   = config.Ip_address + config.flashsale_getActiveSale;
    String URL_GETSPHEADER     = config.Ip_address + config.ordersp_get_spHeader;
    String URL_INSERTSPHEADER = config.Ip_address + config.ordersp_insert_spHeader;
    String URL_INSERTSAMTEMP  = config.Ip_address + config.ordersp_insert_samTemp;
    String URL_INSERTTRXHEADER= config.Ip_address + config.ordersp_insert_trxHeader;
    String URL_INSERTDURATION = config.Ip_address + config.ordersp_insert_duration;

    ACProgressCustom loading;
    Button btnCheckout;
    UniversalFontTextView txtLensDescr, txtPriceLens, txtPriceDisc, txtPriceDiscSale ,txtPriceFacet, txtPriceTinting, txtPriceShipping,
                            txtPriceTotal, txtItemWeight, txtLensModel, txtSide, txtShippingMethod, txtInfoShipping;
    ScalableLayout scalableCourier;
    RecyclerView recyclerCourier;
    ImageView imgLensModel;
    ListView listPayment;
    CardView cardPayment;
    LinearLayout linearFlashSale;
    String headerNoSp, headerTipeSp, headerSales, headerCustName, headerAddress, headerCity, headerOrderVia, headerDisc,
            headerCondition, headerInstallment, headerStartInstallment, headerShippingAddress, headerStatus, headerImage;
    String orderNumber, opticUsername, hargaLensa, deskripsiLensa, diskonLensa, facetLensa, tintingLensa, totalPrice,
            tempTotal, cityOptic, provinceOptic ,itemWeight, lensCategory, date1, date2, addTemp, flagShipping, patientName, idParty,
            shippingMethod, kodeBilling, duration, expDate, ownerPhone, memberFlag, opticName, opticAddress, phoneNumber,
            note, lenstype, lensdesc, opticFlag;
    String itemCodeR, descR, powerR, uom, qtyR, priceR, totalPriceR, marginR, extraMarginR, itemCodeL, descL, powerL,
            qtyL, priceL, totalPriceL, marginL, extraMarginL, discountItem, discountR, discountL, extraMarginDiscount,
            itemFacetCode, facetDescription, facetqty, facetPrice, facetTotal, facetMargin, facetExtraMargin,
            itemTintingCode, tintingDescription, tintingqty, tintingPrice, tintingTotal, tintingMargin, tintingExtraMargin;
    String sphR, sphL, cylR, cylL, axsR, axsL, addR, addL, coatCode, coatDesc, tintCode, tintDesc, corridor, mpdR, mpdL,
            pv, wrap, panto, vd, facetInfo, frameModel, dbl, hor, ver, frameCode, province, shipCourier, shipPrice, shipService, shipId,
            sideR, sideL, itemIdR, itemIdL, discOperandR, discNameR, discOperandL, discNameL, prod_attr_valR, prod_attr_valL,
            divName, categoryLens;

    Integer pr, ptr, pl, ptl, tintp;
    int flagPayment, discSaleR, discSaleL, headerDp;
    Double disR, disL, valTotalDiscSale;
    Double valDiscSaleR = 0d;
    Double valDiscSaleL = 0d;

//    Spinner spinShipment;
//    private static final String[] shipment = new String[] {"Shipping Method"};
//    private static final String[] payment = new String[] {"QR Code", "Virtual Account", "Credit Card", "Loan"};
    List<Data_paymentmethod> paymentmethodList = new ArrayList<>();
    List<Data_spin_shipment> shipmentList = new ArrayList<>();
//    ArrayAdapter adapterListShipment;
    Data_lensorderweb dataHeader = new Data_lensorderweb();
    Data_lensorderweb_item dataItemR = new Data_lensorderweb_item();
    Data_lensorderweb_item dataItemL = new Data_lensorderweb_item();
    Data_spheader dataSpHeader = new Data_spheader();

    Adapter_courier_service adapterCourierService;
    Adapter_paymentmethod adapter_paymentmethod;
//    Adapter_spinner_shipment adapter_spinner_shipment;
    String selectedItem, flashsaleNote, isSp;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lens_summary);
        showLoading();

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        scalableCourier =  findViewById(R.id.activity_lenssummary_scalableCourier);
        recyclerCourier =  findViewById(R.id.activity_lenssummary_recyclerCourier);
        btnCheckout     =  findViewById(R.id.activity_lenssummary_btn_checkout);
        txtLensDescr    =  findViewById(R.id.activity_lenssummary_txt_lensdesc);
        txtPriceLens    =  findViewById(R.id.activity_lenssummary_txt_pricelens);
        txtPriceDisc    =  findViewById(R.id.activity_lenssummary_txt_pricedisc);
        txtPriceDiscSale=  findViewById(R.id.activity_lenssummary_txt_pricediscsale);
        txtPriceFacet   =  findViewById(R.id.activity_lenssummary_txt_pricefacet);
        txtPriceTinting =  findViewById(R.id.activity_lenssummary_txt_pricetinting);
        txtPriceShipping=  findViewById(R.id.activity_lenssummary_txt_priceshipment);
        txtInfoShipping =  findViewById(R.id.activity_lenssummary_txtInfoShipping);
        txtPriceTotal   =  findViewById(R.id.activity_lenssummary_txt_pricetotal);
        txtItemWeight   =  findViewById(R.id.activity_lenssummary_txt_itemweight);
        txtLensModel    =  findViewById(R.id.activity_lenssummary_txt_lensmodel);
        txtSide         =  findViewById(R.id.activity_lenssummary_txt_side);
        txtShippingMethod =  findViewById(R.id.activity_lenssummary_txt_shippingmethod);
//        spinShipment    =  findViewById(R.id.activity_lenssummary_spin_shipment);
        imgLensModel    =  findViewById(R.id.activity_lenssummary_img_logo);
        listPayment     =  findViewById(R.id.activity_lenssummary_listview_paymentMethod);
        cardPayment     =  findViewById(R.id.activity_lenssummary_card_payment);
        linearFlashSale =  findViewById(R.id.activity_lenssummary_linear_flashsale);

        LinearLayoutManager horizonManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCourier.setLayoutManager(horizonManager);

        gettingPrice();
        getActiveSale();
        getAllKurir(cityOptic, province);

        //adapterListShipment = new ArrayAdapter(this, R.layout.listview_payment, payment);

        txtLensDescr.setText(deskripsiLensa);
        txtPriceLens.setText(hargaLensa);
        txtPriceDisc.setText(" - " + diskonLensa);
//        txtPriceDiscSale.setText(" - " + valTotalDiscSale);
        txtPriceFacet.setText(facetLensa);
        txtPriceTinting.setText(tintingLensa);
        txtItemWeight.setText(itemWeight + " gram");

//        txtPriceTotal.setText("Rp " + totalPrice);

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

        checkMember(opticUsername);

//        if (opticFlag.equals("0"))
//        try {
//            Thread.sleep(1000);
//
//            if (flagPayment == 0)
//            {
//                scalableCourier.setVisibility(View.GONE);
//                txtInfoShipping.setText("Belum termasuk ongkos kirim. Kurir dan tarif pengiriman sesuai kebijakan Timur Raya");
//
//                shipPrice = "0";
//                String price = CurencyFormat(shipPrice.toString());
//                Float addNew = Float.parseFloat(tempTotal) + Float.parseFloat(shipPrice) - valTotalDiscSale;
//                addTemp = CurencyFormat(addNew.toString());
//                txtPriceShipping.setText(String.valueOf(price));
//                txtPriceTotal.setText("Rp " + String.valueOf(addTemp));
//            }
//            else
//            {
//                getAllKurir(cityOptic, province);
//                scalableCourier.setVisibility(View.VISIBLE);
//                txtInfoShipping.setVisibility(View.GONE);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Checkout Klik", "Checkout jangan force close");
                //showLoading();
                DecimalFormat toDecimal = new DecimalFormat("#.##");

//                if (memberFlag.contentEquals("1"))
                if (flagPayment == 1)
                {
                    Log.d("Checkout Klik", "Order With Payment");
//                    insertLokalDb();
//
                    if (selectedItem.contentEquals("QR Code") || selectedItem.equals("QR Code") || selectedItem.contains("QR Code"))
                    {
                        String paymentType = "internetBanking";
                        String grossAmount = removeRupiah(txtPriceTotal.getText().toString());
//                        grossAmount = grossAmount.replace(",00", "");
                        grossAmount = grossAmount.replace(",", ".");
                        String orderId     = orderNumber;

                        postBillingQR(paymentType, grossAmount, orderId);

                    }
                    else if (selectedItem.contentEquals("Virtual Account") || selectedItem.equals("Virtual Account") ||
                            selectedItem.contains("Virtual Account"))
                    {
                        String paymentType = "bankTransfer";
                        String grossAmount = removeRupiah(txtPriceTotal.getText().toString());
//                        grossAmount = grossAmount.replace(",00", "");
                        grossAmount = grossAmount.replace(",", ".");
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
//                        grossAmount = grossAmount.replace(",00", "");
                        grossAmount = grossAmount.replace(",", ".");
                        String orderId     = orderNumber;

//                    Toasty.info(getApplicationContext(), "Total amount : " + grossAmount, Toast.LENGTH_SHORT).show();

                        postBillingLoan(paymentType, grossAmount, orderId);
                    }
                    else if (selectedItem.contentEquals("Deposit") || selectedItem.equals("Deposit") ||
                            selectedItem.contains("Deposit"))
                    {
                        createBillingDeposit(orderNumber, "deposit");
                    }
//                    else if (selectedItem.contentEquals("Kreditpro") || selectedItem.equals("Kreditpro") ||
//                            selectedItem.contains("Kreditpro"))
//                    {
////                    Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentKreditpro.class);
////                    startActivity(intent);
//
//                        createBillingKP(orderNumber, "kreditPro");
//                    }
//
                    dataHeader.setOrderNumber(orderNumber);
                    dataHeader.setCustomerId(Integer.valueOf(idParty));
                    dataHeader.setOpticName(opticName);
                    dataHeader.setOpticAddress(opticAddress);
                    dataHeader.setOpticCity(cityOptic);
                    dataHeader.setPatientName(patientName);
                    dataHeader.setPhoneNumber(phoneNumber);
                    dataHeader.setNote(note);
                    dataHeader.setProdDivType(divName);
                    dataHeader.setIdLensa(lenstype);
                    dataHeader.setDescription(lensdesc);
//
                    String sphr, sphl, cylr, cyll;

                    if (!sphR.isEmpty())
                    {
                        if (Double.valueOf(sphR) > 0)
                        {
                            sphr = "+" + String.valueOf(sphR);
                        }
                        else
                        {
                            sphr = String.valueOf(sphR);
                        }
                    }
                    else
                    {
                        sphr = "";
                    }

                    if (!sphL.isEmpty())
                    {
                        if (Double.valueOf(sphL) > 0)
                        {
                            sphl = "+" + String.valueOf(sphL);
                        }
                        else
                        {
                            sphl = String.valueOf(sphL);
                        }
                    }
                    else
                    {
                        sphl = "";
                    }

                    if (!cylR.isEmpty())
                    {
                        if (Double.valueOf(cylR) > 0)
                        {
                            cylr = "+" + String.valueOf(cylR);
                        }
                        else
                        {
                            cylr = String.valueOf(cylR);
                        }
                    }
                    else
                    {
                        cylr = "";
                    }

                    if (!cylL.isEmpty())
                    {
                        if (Double.valueOf(cylL) > 0)
                        {
                            cyll = "+" + String.valueOf(cylL);
                        }
                        else
                        {
                            cyll = String.valueOf(cylL);
                        }
                    }
                    else
                    {
                        cyll = "";
                    }

                    if (corridor.equals("None") || corridor.contains("None"))
                    {
                        corridor = "";
                    }

                    if (frameModel.contentEquals("-- Model --") || frameModel.contains("-- Model --")
                            || frameModel.equals("-- Model --"))
                    {
                        frameModel = "";
                    }

                    dataHeader.setSphR(sphr);
                    dataHeader.setSphL(sphl);
                    dataHeader.setCylR(cylr);
                    dataHeader.setCylL(cyll);
                    dataHeader.setAxsR(axsR);
                    dataHeader.setAxsL(axsL);
                    dataHeader.setAddR(addR);
                    dataHeader.setAddL(addL);
                    dataHeader.setPrisR("-");
                    dataHeader.setPrisL("-");
//
                    dataHeader.setCoatCode(coatCode);
                    dataHeader.setCoatDesc(coatDesc);
                    dataHeader.setTintCode(tintCode);
                    dataHeader.setTintDesc(tintDesc);
                    dataHeader.setCorridor(corridor);
                    Log.d("HEADER Corridor : ", dataHeader.getCorridor());
                    dataHeader.setMpdR(mpdR);
                    dataHeader.setMpdL(mpdL);
                    dataHeader.setPv(pv);
                    dataHeader.setWrap(wrap);
                    dataHeader.setPanto(panto);
                    dataHeader.setVd(vd);
                    dataHeader.setFacetInfo(facetInfo);
                    dataHeader.setFrameModel(frameModel);
                    dataHeader.setDbl(dbl);
                    dataHeader.setHor(hor);
                    dataHeader.setVer(ver);
                    dataHeader.setFrameCode(frameCode);
                    dataHeader.setShippingId(Integer.valueOf(shipId));
                    dataHeader.setShippingCourier(shipCourier);
                    dataHeader.setShippingService(shipService);
                    dataHeader.setShippingCity(cityOptic);
                    dataHeader.setShippingProvince(province);
                    dataHeader.setShippingPrice(Integer.valueOf(shipPrice));
                    dataHeader.setFacetPrice(Integer.valueOf(facetLensa.replace(",00", "").replace(".", "")));

                    String totalAlla = removeRupiah(txtPriceTotal.getText().toString()).replace(",", ".");
//                    String grandtotal = toDecimal.format(totalAlla);
//                    Log.d("GrandTotal", " = " + grandtotal);
                    dataHeader.setGrandTotal(totalAlla);

                    dataHeader.setCash_carry("Pending");
                    dataHeader.setFlash_note(flashsaleNote);
                    dataHeader.setOrderSp(isSp);

                    dataItemR.setOrderNumber(orderNumber);
                    dataItemR.setItemId(itemIdR);
                    dataItemR.setItemCode(itemCodeR);
                    dataItemR.setDescription(descR);
                    dataItemR.setRl(sideR);
                    dataItemR.setQty(Integer.parseInt(qtyR));
                    dataItemR.setUnitStandardPrice(pr);
                    dataItemR.setUnitStandardWeight(20);
                    dataItemR.setDiscountName(discNameR);
                    dataItemR.setDiscount(Double.valueOf(discOperandR));
                    dataItemR.setDiscountSale(discSaleR);
                    dataItemR.setTotalWeight(20);
                    dataItemR.setTintingPrice(tintp);
                    dataItemR.setAmount(ptr);

                    dataItemL.setOrderNumber(orderNumber);
                    dataItemL.setItemId(itemIdL);
                    dataItemL.setItemCode(itemCodeL);
                    dataItemL.setDescription(descL);
                    dataItemL.setRl(sideL);
                    dataItemL.setQty(Integer.parseInt(qtyL));
                    dataItemL.setUnitStandardPrice(pl);
                    dataItemL.setUnitStandardWeight(20);
                    dataItemL.setDiscountName(discNameL);
                    dataItemL.setDiscount(Double.valueOf(discOperandL));
                    dataItemL.setDiscountSale(discSaleL);
                    dataItemL.setTotalWeight(20);
                    dataItemL.setTintingPrice(tintp);
                    dataItemL.setAmount(ptl);
//
                    insertLensOrderHeader(dataHeader);

                    if (itemIdR.contentEquals(itemIdL) && itemIdR.contains(itemIdL))
                    {
                        dataItemR.setQty(2);
                        int total = ptr * 2;
                        int totalDiscFlash = dataItemR.getDiscountSale() * 2;
                        double totalDisc      = dataItemR.getDiscount() * 2;
                        dataItemR.setDiscount(totalDisc);
                        dataItemR.setDiscountSale(totalDiscFlash);
                        dataItemR.setAmount(total);
                        insertLensOrderItem(dataItemR);

                        if (categoryLens.contentEquals("S") || categoryLens.contains("S"))
                        {
                            if (isSp.equals("1"))
                            {
                                insertItemDurr(orderNumber, itemIdR, "2");
                            }
                            else
                            {
                                potongStock(itemIdR, "2");
                            }
                        }
                    }
                    else
                    {
                        if (!itemCodeR.isEmpty())
                        {
                            insertLensOrderItem(dataItemR);

                            if (categoryLens.contentEquals("S") || categoryLens.contains("S"))
                            {
                                if (isSp.equals("1"))
                                {
                                    insertItemDurr(orderNumber, itemIdR, "1");
                                }
                                else
                                {
                                    potongStock(itemIdR, "1");
                                }
                            }
                        }

                        if (!itemCodeL.isEmpty())
                        {
                            insertLensOrderItem(dataItemL);

                            if (categoryLens.contentEquals("S") || categoryLens.contains("S"))
                            {
                                if (isSp.equals("1"))
                                {
                                    insertItemDurr(orderNumber, itemIdL, "1");
                                }
                                else
                                {
                                    potongStock(itemIdL, "1");
                                }
                            }
                        }
                    }
                }
                else
                {
                    Log.d("Checkout Klik", "Order Non Payment");
////                    Toasty.info(getApplicationContext(), "Insert non payment", Toast.LENGTH_SHORT).show();
//
                    dataHeader.setOrderNumber(orderNumber);
                    Log.d("HEADER OrderNumber : ", dataHeader.getOrderNumber());
                    dataHeader.setCustomerId(Integer.valueOf(idParty));
                    Log.d("HEADER CustomerId : ", String.valueOf(dataHeader.getCustomerId()));
                    dataHeader.setOpticName(opticName);
                    Log.d("HEADER OpticName : ", dataHeader.getOpticName());
                    dataHeader.setOpticAddress(opticAddress);
                    Log.d("HEADER OpticAddress : ", dataHeader.getOpticAddress());
                    dataHeader.setOpticCity(cityOptic);
                    Log.d("HEADER OpticCity : ", dataHeader.getOpticCity());
                    dataHeader.setPatientName(patientName);
                    Log.d("HEADER PatientName : ", dataHeader.getPatientName());
                    dataHeader.setPhoneNumber(phoneNumber);
                    Log.d("HEADER PhoneNumber : ", dataHeader.getPhoneNumber());
                    dataHeader.setNote(note);
                    Log.d("HEADER Note : ", dataHeader.getNote());
                    dataHeader.setProdDivType(divName);
                    Log.d("HEADER ProdDiv : ", dataHeader.getProdDivType());
                    dataHeader.setIdLensa(lenstype);
                    Log.d("HEADER IdLensa : ", dataHeader.getIdLensa());
                    dataHeader.setDescription(lensdesc);
                    Log.d("HEADER Description : ", dataHeader.getDescription());
//
                    String sphr, sphl, cylr, cyll;

                    if (!sphR.isEmpty())
                    {
                        if (Double.valueOf(sphR) > 0)
                        {
                            sphr = "+" + String.valueOf(sphR);
                        }
                        else
                        {
                            sphr = String.valueOf(sphR);
                        }
                    }
                    else
                    {
                        sphr = "0";
                    }

                    if (!sphL.isEmpty())
                    {
                        if (Double.valueOf(sphL) > 0)
                        {
                            sphl = "+" + String.valueOf(sphL);
                        }
                        else
                        {
                            sphl = String.valueOf(sphL);
                        }
                    }
                    else
                    {
                        sphl = "0";
                    }

                    if (!cylR.isEmpty())
                    {
                        if (Double.valueOf(cylR) > 0)
                        {
                            cylr = "+" + String.valueOf(cylR);
                        }
                        else
                        {
                            cylr = String.valueOf(cylR);
                        }
                    }
                    else
                    {
                        cylr = "0";
                    }

                    if (!cylL.isEmpty())
                    {
                        if (Double.valueOf(cylL) > 0)
                        {
                            cyll = "+" + String.valueOf(cylL);
                        }
                        else
                        {
                            cyll = String.valueOf(cylL);
                        }
                    }
                    else
                    {
                        cyll = "0";
                    }

                    if (axsR.isEmpty())
                    {
                        axsR = "0";
                    }

                    if (axsL.isEmpty())
                    {
                        axsL = "0";
                    }

                    if (addR.isEmpty())
                    {
                        addR = "0";
                    }

                    if (addL.isEmpty())
                    {
                        addL = "0";
                    }

                    if (corridor.equals("None") || corridor.contains("None"))
                    {
                        corridor = "";
                    }

                    if (frameModel.contentEquals("-- Model --") || frameModel.contains("-- Model --")
                            || frameModel.equals("-- Model --"))
                    {
                        frameModel = "";
                    }

                    dataHeader.setSphR(sphr);
                    Log.d("HEADER SphR : ", dataHeader.getSphR());
                    dataHeader.setSphL(sphl);
                    Log.d("HEADER SphL : ", dataHeader.getSphL());
                    dataHeader.setCylR(cylr);
                    Log.d("HEADER CylR : ", dataHeader.getCylR());
                    dataHeader.setCylL(cyll);
                    Log.d("HEADER CylL : ", dataHeader.getCylL());
                    dataHeader.setAxsR(axsR);
                    Log.d("HEADER AxsR : ", dataHeader.getAxsR());
                    dataHeader.setAxsL(axsL);
                    Log.d("HEADER AxsL : ", dataHeader.getAxsL());
                    dataHeader.setAddR(addR);
                    Log.d("HEADER AddR : ", dataHeader.getAddR());
                    dataHeader.setAddL(addL);
                    Log.d("HEADER AddL : ", dataHeader.getAddL());
                    dataHeader.setPrisR("0");
                    dataHeader.setPrisL("0");

                    dataHeader.setCoatCode(coatCode);
                    Log.d("HEADER CoatCode : ", dataHeader.getCoatCode());
                    dataHeader.setCoatDesc(coatDesc);
                    Log.d("HEADER CoatDesc : ", dataHeader.getCoatDesc());
                    dataHeader.setTintCode(tintCode);
                    Log.d("HEADER TintCode : ", dataHeader.getTintCode());
                    dataHeader.setTintDesc(tintDesc);
                    Log.d("HEADER TintDesc : ", dataHeader.getTintDesc());
                    dataHeader.setCorridor(corridor);
                    Log.d("HEADER Corridor : ", dataHeader.getCorridor());
                    dataHeader.setMpdR(mpdR);
                    Log.d("HEADER MpdR : ", dataHeader.getMpdR());
                    dataHeader.setMpdL(mpdL);
                    Log.d("HEADER MpdL : ", dataHeader.getMpdL());
                    dataHeader.setPv(pv);
                    Log.d("HEADER Pv : ", dataHeader.getPv());
                    dataHeader.setWrap(wrap);
                    Log.d("HEADER Wrap : ", dataHeader.getWrap());
                    dataHeader.setPanto(panto);
                    Log.d("HEADER Panto : ", dataHeader.getPanto());
                    dataHeader.setVd(vd);
                    Log.d("HEADER Vertex : ", dataHeader.getVd());
                    dataHeader.setFacetInfo(facetInfo);
                    Log.d("HEADER FacetInfo : ", dataHeader.getFacetInfo());
                    dataHeader.setFrameModel(frameModel);
                    Log.d("HEADER FrameModel : ", dataHeader.getFrameModel());
                    dataHeader.setDbl(dbl);
                    Log.d("HEADER Dbl : ", dataHeader.getDbl());
                    dataHeader.setHor(hor);
                    Log.d("HEADER hor : ", dataHeader.getHor());
                    dataHeader.setVer(ver);
                    Log.d("HEADER ver : ", dataHeader.getVer());
                    dataHeader.setFrameCode(frameCode);
                    Log.d("HEADER FrameCode : ", dataHeader.getFrameCode());
                    dataHeader.setShippingId(0);
                    Log.d("HEADER ShippingId : ", String.valueOf(dataHeader.getShippingId()));
                    dataHeader.setShippingCourier("");
                    Log.d("HEADER ShipCourier : ", dataHeader.getShippingCourier());
                    dataHeader.setShippingService("");
                    Log.d("HEADER ShipService : ", dataHeader.getShippingService());
                    dataHeader.setShippingCity(cityOptic);
                    Log.d("HEADER ShipCity : ", dataHeader.getShippingCity());
                    dataHeader.setShippingProvince(province);
                    Log.d("HEADER ShipProv : ", dataHeader.getShippingProvince());
                    dataHeader.setShippingPrice(Integer.valueOf(shipPrice));
                    Log.d("HEADER ShipPrice : ", String.valueOf(dataHeader.getShippingPrice()));
                    dataHeader.setFacetPrice(Integer.valueOf(facetLensa.replace(",00", "").replace(".", "")));
                    Log.d("HEADER FacetPrice : ", String.valueOf(dataHeader.getFacetPrice()));

                    String totalAllb = removeRupiah(txtPriceTotal.getText().toString()).replace(",", ".");
//                    double totalAllb = Double.valueOf(removeRupiah());
//                    String grandtotal = toDecimal.format(val);
                    dataHeader.setGrandTotal(totalAllb);

                    Log.d("HEADER GrantTotal : ", String.valueOf(dataHeader.getGrandTotal()));
                    dataHeader.setCash_carry("Non Payment Method");
                    Log.d("HEADER CashCarry : ", dataHeader.getCash_carry());
                    dataHeader.setFlash_note(flashsaleNote);
                    if (dataHeader.getFlash_note() != null)
                    {
                        Log.d("HEADER FlashNote : ", dataHeader.getFlash_note());
                    }
                    dataHeader.setOrderSp(isSp);

                    dataItemR.setOrderNumber(orderNumber);
                    Log.d("ITEM OrderNumber R : ", dataItemR.getOrderNumber());
                    dataItemR.setItemId(itemIdR);
                    Log.d("ITEM ItemId R : ", dataItemR.getItemId());
                    dataItemR.setItemCode(itemCodeR);
                    Log.d("ITEM ItemCode R : ", dataItemR.getItemCode());
                    dataItemR.setDescription(descR);
                    Log.d("ITEM Descr R : ", dataItemR.getDescription());
                    if (sideR == null)
                    {
                        sideR = "";
                    }
                    dataItemR.setRl(sideR);
                    Log.d("ITEM Side R : ", dataItemR.getRl());
                    dataItemR.setQty(Integer.parseInt(qtyR));
                    Log.d("ITEM Qty R : ", String.valueOf(dataItemR.getQty()));
                    dataItemR.setUnitStandardPrice(pr);
                    Log.d("ITEM Price R : ", String.valueOf(dataItemR.getUnitStandardPrice()));
                    dataItemR.setUnitStandardWeight(20);
                    Log.d("ITEM Weight R : ", String.valueOf(dataItemR.getUnitStandardWeight()));
                    dataItemR.setDiscountName(discNameR);
                    Log.d("ITEM DiscName R : ", dataItemR.getDiscountName());
                    dataItemR.setDiscount(Double.valueOf(discOperandR));
                    Log.d("ITEM Disc R : ", String.valueOf(dataItemR.getDiscount()));
                    dataItemR.setDiscountSale(discSaleR);
                    Log.d("ITEM DiscSale R :", String.valueOf(dataItemR.getDiscountSale()));
                    dataItemR.setTotalWeight(20);
                    Log.d("ITEM TotalWeight R : ", String.valueOf(dataItemR.getTotalWeight()));
                    dataItemR.setTintingPrice(tintp);
                    Log.d("ITEM Tinting R : ", String.valueOf(dataItemR.getTintingPrice()));
                    dataItemR.setAmount(ptr);
                    Log.d("ITEM Amount R : ", String.valueOf(dataItemR.getAmount()));

                    dataItemL.setOrderNumber(orderNumber);
                    Log.d("ITEM OrderNumber L : ", dataItemL.getOrderNumber());
                    dataItemL.setItemId(itemIdL);
                    Log.d("ITEM ItemId L : ", dataItemL.getItemId());
                    dataItemL.setItemCode(itemCodeL);
                    Log.d("ITEM ItemCode L : ", dataItemL.getItemCode());
                    dataItemL.setDescription(descL);
                    Log.d("ITEM Descr L : ", dataItemL.getDescription());
                    if (sideL == null)
                    {
                        sideL = "";
                    }
                    dataItemL.setRl(sideL);
                    Log.d("ITEM Side L : ", dataItemL.getRl());
                    dataItemL.setQty(Integer.parseInt(qtyL));
                    Log.d("ITEM Qty L : ", String.valueOf(dataItemL.getQty()));
                    dataItemL.setUnitStandardPrice(pl);
                    Log.d("ITEM Price L : ", String.valueOf(dataItemL.getUnitStandardPrice()));
                    dataItemL.setUnitStandardWeight(20);
                    Log.d("ITEM Weigh L : ", String.valueOf(dataItemL.getUnitStandardWeight()));
                    dataItemL.setDiscountName(discNameL);
                    Log.d("ITEM DiscName L : ", dataItemL.getDiscountName());
                    dataItemL.setDiscount(Double.valueOf(discOperandL));
                    Log.d("ITEM Disc L : ", String.valueOf(dataItemL.getDiscount()));
                    dataItemL.setDiscountSale(discSaleL);
                    Log.d("ITEM DiscSale L :", String.valueOf(dataItemL.getDiscountSale()));
                    dataItemL.setTotalWeight(20);
                    Log.d("ITEM TotalWeight L : ", String.valueOf(dataItemL.getTotalWeight()));
                    dataItemL.setTintingPrice(tintp);
                    Log.d("ITEM Tinting L : ", String.valueOf(dataItemL.getTintingPrice()));
                    dataItemL.setAmount(ptl);
                    Log.d("ITEM Amount L : ", String.valueOf(dataItemL.getAmount()));

                    insertLensOrderHeader(dataHeader);

                    if (isSp.equals("1"))
                    {
                        insertSpHeader(dataSpHeader);
                        insertSP(URL_INSERTSAMTEMP, dataSpHeader);
                        insertSP(URL_INSERTTRXHEADER, dataSpHeader);

                        Intent intent = new Intent("finishLs");
                        sendBroadcast(intent);
                        finish();
                    }

                    if (itemIdR.contentEquals(itemIdL) && itemIdR.contains(itemIdL))
                    {
                        dataItemR.setQty(2);
                        int total = ptr * 2;
                        int totalDiscFlash = dataItemR.getDiscountSale() * 2;
                        double totalDisc      = dataItemR.getDiscount() * 2;
                        dataItemR.setDiscount(totalDisc);
                        dataItemR.setDiscountSale(totalDiscFlash);
                        dataItemR.setAmount(total);
                        insertLensOrderItem(dataItemR);
                    }
                    else
                    {
                        if (!itemCodeR.isEmpty())
                        {
                            insertLensOrderItem(dataItemR);
                        }

                        if (!itemCodeL.isEmpty())
                        {
                            insertLensOrderItem(dataItemL);
                        }
                    }

                    insertStatusEntry(orderNumber);

                }

                loading.dismiss();
            }
        });

        adapterCourierService = new Adapter_courier_service(this, shipmentList, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                shipPrice   = shipmentList.get(pos).getPrice();
                shipId      = String.valueOf(shipmentList.get(pos).getId());
                shipCourier = shipmentList.get(pos).getKurir();
                shipService = shipmentList.get(pos).getService();
                shipPrice   = shipmentList.get(pos).getPrice();

                String price = CurencyFormat(shipPrice.toString());

                Log.d("Callback Price", "Shipment : " + shipPrice);

                Double addNew = Double.parseDouble(tempTotal) + Double.parseDouble(shipPrice) - valTotalDiscSale;
                addTemp = CurencyFormat(addNew.toString());
                txtPriceShipping.setText(String.valueOf(price));
                txtPriceTotal.setText("Rp " + String.valueOf(addTemp));

//                totalAllPrice = (totalPrice - totalDisc) + Integer.valueOf(shipmentPrice);
//                txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(totalAllPrice)));
            }
        });

//        spinShipment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String shipmentPrice;
//
//                if (!shipmentList.isEmpty())
//                {
//                    shipId = String.valueOf(shipmentList.get(position).getId());
//                    shipmentPrice = shipmentList.get(position).getPrice();
//                    shipPrice = shipmentList.get(position).getPrice();
//                    shipCourier = shipmentList.get(position).getKurir();
//                    shippingMethod= "Tarif Pengiriman " + shipmentList.get(position).getKurir();
//                    txtPriceShipping.setText(CurencyFormat(shipmentPrice));
//                }
//                else
//                {
//                    shipId = "0";
//                    shipPrice = "0";
//                    shipCourier = "";
//                    shipmentPrice = "0,00";
//                    shippingMethod= "Gratis Tarif Pengiriman";
//                    txtPriceShipping.setText(shipmentPrice);
//                }
//
//                Float addNew = Float.parseFloat(tempTotal) + Float.parseFloat(shipmentPrice);
//                addTemp = CurencyFormat(addNew.toString());
//                txtPriceTotal.setText("Rp " + addTemp);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        loading.dismiss();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
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

    private void createBillingDeposit(final String orderNumber, final String paymentType)
    {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTBILLDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loading.hide();
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        //Call ShowSessionPayment
//                        autoInsertOrder();
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

                        intent.putExtra("username", opticUsername);
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

    private void showSessionPaymentDeposit(final String orderNumber)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SHOWSESSIONPAYMENTDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String grossAmount = removeRupiah(txtPriceTotal.getText().toString());
//                        grossAmount = grossAmount.replace(",00", "");
                    grossAmount = grossAmount.replace(",", ".");
//                    String amount = String.valueOf(totalAllPrice);
                    duration = jsonObject.getString("dur");
                    expDate  = jsonObject.getString("exp_date");

                    if (duration != null)
                    {
                        Intent intent = new Intent(FormLensSummaryActivity.this, FormPaymentDeposit.class);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("amount", grossAmount);
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

    private void getAllKurir(final String city, final String prov)
    {
//        adapter_spinner_shipment = new Adapter_spinner_shipment(FormLensSummaryActivity.this, shipmentList);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ALLDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id       = jsonObject.getString("idShip");
                        String kurir    = jsonObject.getString("kurir");
                        String service  = jsonObject.getString("type");
                        String icon     = jsonObject.getString("icon");
                        String city     = jsonObject.getString("city");
                        String province = jsonObject.getString("province");
                        String price    = jsonObject.getString("price");
                        String estimasi = jsonObject.getString("estimasi");

                        Data_spin_shipment data_spin_shipment = new Data_spin_shipment();
                        data_spin_shipment.setId(Integer.valueOf(id));
                        data_spin_shipment.setKurir(kurir);
                        data_spin_shipment.setService(service);
                        data_spin_shipment.setIcon(icon);
                        data_spin_shipment.setPrice(price);
                        data_spin_shipment.setCity(city);
                        data_spin_shipment.setProvince(province);
                        data_spin_shipment.setEstimasi(estimasi);

                        shipmentList.add(data_spin_shipment);
                    }

                    shipPrice   = shipmentList.get(0).getPrice();
                    shipId      = String.valueOf(shipmentList.get(0).getId());
                    shipCourier = shipmentList.get(0).getKurir();
                    shipService = shipmentList.get(0).getService();
                    shipPrice   = shipmentList.get(0).getPrice();

                    String price = CurencyFormat(shipPrice.toString());

                    Log.d("Callback Price", "Shipment : " + shipPrice);

                    Double addNew = Double.parseDouble(tempTotal) + Double.parseDouble(shipPrice) - valTotalDiscSale;
                    addTemp = CurencyFormat(addNew.toString());
                    txtPriceShipping.setText(String.valueOf(price));
                    txtPriceTotal.setText("Rp " + String.valueOf(addTemp));

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
        Double c   = bundle.getDouble("discount_lens");
        Integer d = bundle.getInt("facet_lens");
        Integer e = bundle.getInt("tinting_lens");
        cityOptic = bundle.getString("city_info");
        Integer f = bundle.getInt("item_weight");
        String h  = bundle.getString("flag_pasang");
        opticFlag = bundle.getString("optic_flag");
        isSp      = bundle.getString("isSp");

        if (isSp.equals("1"))
        {
//            getSp();

            headerNoSp       = bundle.getString("headerNoSp");
            headerTipeSp     = bundle.getString("headerTipeSp");
            headerSales      = bundle.getString("headerSales");
            headerCustName   = bundle.getString("headerCustName");
            headerAddress    = bundle.getString("headerAddress");
            headerCity       = bundle.getString("headerCity");
            headerOrderVia   = bundle.getString("headerOrderVia");
            headerDp            = bundle.getInt("headerDp");
            headerDisc       = bundle.getString("headerDisc");
            headerCondition  = bundle.getString("headerCondition");
            headerInstallment = bundle.getString("headerInstallment");
            headerStartInstallment = bundle.getString("headerStartInstallment");
            headerShippingAddress  = bundle.getString("headerShippingAddress");
            headerStatus     = bundle.getString("headerStatus");
            headerStatus     = "";
            headerImage      = bundle.getString("headerImage");

            Log.d("headerNoSp", headerNoSp);
            Log.d("headerTipeSp", headerTipeSp);
            Log.d("headerSales", headerSales);
            Log.d("headerCustName", headerCustName);
            Log.d("headerAddress", headerAddress);
            Log.d("headerCity", headerCity);
            Log.d("headerOrderVia", headerOrderVia);
            Log.d("headerDp", String.valueOf(headerDp));
            Log.d("headerDisc", headerDisc);
            Log.d("headerCondition", headerCondition);
            Log.d("headerInstallment", headerInstallment);
            Log.d("headerStartInstallment", headerStartInstallment);
            Log.d("headerShippingAddress", headerShippingAddress);
            Log.d("headerStatus", headerStatus);
            Log.d("headerImage", headerImage);

            dataSpHeader.setNoSp(headerNoSp);
            dataSpHeader.setTypeSp(headerTipeSp);
            dataSpHeader.setSales(headerSales);
            dataSpHeader.setCustName(headerCustName);
            dataSpHeader.setAddress(headerAddress);
            dataSpHeader.setCity(headerCity);
            dataSpHeader.setOrderVia(headerOrderVia);
            dataSpHeader.setDp(headerDp);
            dataSpHeader.setDisc(headerDisc);
            dataSpHeader.setCondition(headerCondition);
            dataSpHeader.setInstallment(headerInstallment);
            dataSpHeader.setStartInstallment(headerStartInstallment);
            dataSpHeader.setShipAddress(headerShippingAddress);
            dataSpHeader.setPhoto(headerImage);
            dataSpHeader.setStatus(headerStatus);
        }

        Double g   = bundle.getDouble("total_price");
        opticUsername = bundle.getString("username_info");
        flagShipping  = bundle.getString("flag_shipping");
        orderNumber   = bundle.getString("order_number");
        patientName   = bundle.getString("patient_name");
        idParty       = bundle.getString("id_party");
        phoneNumber   = bundle.getString("phone_number");
        note          = bundle.getString("note");
        divName       = bundle.getString("prodDivType");
        lenstype      = bundle.getString("lenstype");
        lensdesc      = bundle.getString("lensdesc");
        prod_attr_valR= bundle.getString("prod_attr_valR");
        prod_attr_valL= bundle.getString("prod_attr_valL");

        sphR          = bundle.getString("sphR");
        sphL          = bundle.getString("sphL");
        cylR          = bundle.getString("cylR");
        cylL          = bundle.getString("cylL");
        axsR          = bundle.getString("axsR");
        axsL          = bundle.getString("axsL");
        addR          = bundle.getString("addR");
        addL          = bundle.getString("addL");
        coatCode      = bundle.getString("coatCode");
        coatDesc      = bundle.getString("coatDesc");
        tintCode      = bundle.getString("tintCode");
        tintDesc      = bundle.getString("tintDesc");
        corridor      = bundle.getString("corridor");
        mpdR          = bundle.getString("mpdR");
        mpdL          = bundle.getString("mpdL");
        pv            = bundle.getString("pv");
        wrap          = bundle.getString("wrap");
        panto         = bundle.getString("panto");
        vd            = bundle.getString("vd");
        facetInfo     = bundle.getString("facetInfo");
        frameModel    = bundle.getString("frameModel");
        dbl           = bundle.getString("dbl");
        hor           = bundle.getString("hor");
        ver           = bundle.getString("ver");
        frameCode     = bundle.getString("frameCode");
        categoryLens  = bundle.getString("categoryLens");


        hargaLensa = CurencyFormat(a.toString());
        deskripsiLensa = b;
        if (c > 0)
        {
            diskonLensa = CurencyFormat(c.toString());
        }
        else
        {
            diskonLensa = c.toString();
        }
        facetLensa  = CurencyFormat(d.toString());
        tintingLensa = CurencyFormat(e.toString());
        tempTotal  = g.toString();
        totalPrice = CurencyFormat(g.toString());
        itemWeight = f.toString();

        //AREA R
        itemIdR     = bundle.getString("itemid_R");
        itemCodeR   = bundle.getString("itemcode_R");
        descR       = bundle.getString("description_R");
        powerR      = bundle.getString("power_R");
        uom         = "PCS";
        qtyR        = bundle.getString("qty_R");

        if (qtyR == null)
        {
            qtyR = "0";
        }

        if (itemIdR == null)
        {
            itemIdR = "0";
        }

        if (itemCodeR == null)
        {
            itemCodeR = "";
        }

        if (descR == null)
        {
            descR = "";
        }

        if (powerR == null)
        {
            powerR = "";
        }

        if (uom == null)
        {
            uom = "";
        }

        if (qtyR == null)
        {
            qtyR = "";
        }

        pr          = bundle.getInt("itemprice_R");
        priceR      = CurencyFormat(pr.toString());
        ptr         = bundle.getInt("itemtotal_R");
        totalPriceR = CurencyFormat(ptr.toString());
        marginR     = bundle.getString("margin_lens");
        extraMarginR= bundle.getString("extramargin_lens");

        //AREA L
        itemIdL     = bundle.getString("itemid_L");
        itemCodeL   = bundle.getString("itemcode_L");
        descL       = bundle.getString("description_L");
        powerL      = bundle.getString("power_L");
        uom         = "PCS";
        qtyL        = bundle.getString("qty_L");

        if (qtyL == null)
        {
            qtyL = "0";
        }

        if (itemIdL == null)
        {
            itemIdL = "0";
        }

        if (itemCodeL == null)
        {
            itemCodeL = "";
        }

        if (descL == null)
        {
            descL = "";
        }

        if (powerL == null)
        {
            powerL = "";
        }

        if (uom == null)
        {
            uom = "";
        }

        if (qtyL == null)
        {
            qtyL = "";
        }

        pl          = bundle.getInt("itemprice_L");
        priceL      = CurencyFormat(pl.toString());
        ptl         = bundle.getInt("itemtotal_L");
        totalPriceL = CurencyFormat(ptl.toString());
        marginL     = bundle.getString("margin_lens");
        extraMarginL= bundle.getString("extramargin_lens");

        //Area diskon
        discountItem= bundle.getString("description_diskon");
        disR        = bundle.getDouble("discount_r");
        discountR   = CurencyFormat(disR.toString());
        if (discountR.equals(",00") || discountR.equals(",00"))
        {
            discountR = "-";
        }
        else {
            discountR = "-" + discountR;
        }
        disL        = bundle.getDouble("discount_l");
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
        tintp               = bundle.getInt("price_tinting");
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

        if (categoryLens.equals("R") || categoryLens.contentEquals("R"))
        {
            if (itemIdR != null)
            {
                getSideRXR(itemIdR);
                getDiscSaleR(itemIdR);
            }

            if (itemIdL != null)
            {
                getSideRXL(itemIdL);
                getDiscSaleL(itemIdL);
            }
        }
        else
        {
            if (itemIdR != null)
            {
                getSideSTOCKR(itemIdR);
                getDiscSaleR(itemIdR);
            }

            if (itemIdL != null)
            {
                getSideSTOCKL(itemIdL);
                getDiscSaleL(itemIdL);
            }
        }

        Toasty.info(getApplicationContext(), "Category : " + categoryLens, Toast.LENGTH_SHORT).show();

        getLensInformation();
//        getInfoLensR(itemCodeR);
//        getInfoLensL(itemCodeL);
        getDiscountItemR(prod_attr_valR, opticUsername);
        getDiscountItemL(prod_attr_valL, opticUsername);
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

    private void checkMember(final String opticUsername)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_CHECKMEMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    memberFlag = object.getString("memberFlag");
                    opticName  = object.getString("cust_name");
                    opticAddress = object.getString("address1");
                    province   = object.getString("province");


                    Log.d("MEMBER FLAG ", memberFlag);
                    getPaymentOrNot(memberFlag);

//                    if (memberFlag.contentEquals("1"))
//                    {
//                        cardPayment.setVisibility(View.VISIBLE);
//
//                        btnCheckout.setBackgroundColor(Color.LTGRAY);
//                        btnCheckout.setTextColor(Color.BLACK);
//                        btnCheckout.setEnabled(false);
//                    }
//                    else
//                    {
//                        cardPayment.setVisibility(View.GONE);
//
//                        btnCheckout.setBackgroundColor(getResources().getColor(R.color.colorFirst));
//                        btnCheckout.setTextColor(Color.WHITE);
//                        btnCheckout.setEnabled(true);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CHECK MEMBER ERROR", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", opticUsername);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSideRXR(final String itemId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSIDERX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    sideR   = object.getString("side");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null)
                {
                    Log.d("ERROR INFO LENS", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("itemId", itemId);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSideRXL(final String itemId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSIDERX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    sideL   = object.getString("side");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null)
                {
                    Log.d("ERROR INFO LENS", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("itemId", itemId);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSideSTOCKR(final String itemId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSIDESTOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    sideR   = object.getString("side");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null)
                {
                    Log.d("ERROR INFO LENS", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("itemId", itemId);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSideSTOCKL(final String itemId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSIDESTOCK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    sideL   = object.getString("side");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null)
                {
                    Log.d("ERROR INFO LENS", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put("itemId", itemId);
                return hashmap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertLensOrderHeader(final Data_lensorderweb header)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTLENSORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Toasty.info(getApplicationContext(), "Order has been sent", Toast.LENGTH_SHORT).show();

                        finish();
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
                    Log.d("INSERT LENSORDER WEB", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderNumber", header.getOrderNumber());
                hashMap.put("customerId", String.valueOf(header.getCustomerId()));
                hashMap.put("opticName", header.getOpticName());
                hashMap.put("opticAddress", header.getOpticAddress());
                hashMap.put("opticCity", header.getOpticCity());
                hashMap.put("patientName", header.getPatientName());
                hashMap.put("phoneNumber", header.getPhoneNumber());
                hashMap.put("note", header.getNote());
                hashMap.put("prodDivType", header.getProdDivType());
                hashMap.put("idLensa", header.getIdLensa());
                hashMap.put("description", header.getDescription());
                hashMap.put("sphR", header.getSphR());
                hashMap.put("sphL", header.getSphL());
                hashMap.put("cylR", header.getCylR());
                hashMap.put("cylL", header.getCylL());
                hashMap.put("axsR", header.getAxsR());
                hashMap.put("axsL", header.getAxsL());
                hashMap.put("addR", header.getAddR());
                hashMap.put("addL", header.getAddL());
                hashMap.put("prisR", header.getPrisR());
                hashMap.put("prisL", header.getPrisL());
                hashMap.put("coatCode", header.getCoatCode());
                hashMap.put("coatDesc", header.getCoatDesc());
                hashMap.put("tintCode", header.getTintCode());
                hashMap.put("tintDesc", header.getTintDesc());
                hashMap.put("corridor", corridor);
                hashMap.put("mpdL", header.getMpdL());
                hashMap.put("mpdR", header.getMpdR());
                hashMap.put("pv", header.getPv());
                hashMap.put("wrap", header.getWrap());
                hashMap.put("panto", header.getPanto());
                hashMap.put("vd", header.getVd());
                hashMap.put("facetInfo", header.getFacetInfo());
                hashMap.put("frameModel", header.getFrameModel());
                hashMap.put("dbl", header.getDbl());
                hashMap.put("hor", header.getHor());
                hashMap.put("ver", header.getVer());
                hashMap.put("frameCode", header.getFrameCode());
                hashMap.put("shippingId", String.valueOf(header.getShippingId()));
                hashMap.put("shippingCourier", header.getShippingCourier());
                hashMap.put("shippingCity", header.getShippingCity());
                hashMap.put("shippingProv", header.getShippingProvince());
                hashMap.put("shippingService", header.getShippingService());
                hashMap.put("shippingPrice", String.valueOf(header.getShippingPrice()));
                hashMap.put("facetPrice", String.valueOf(header.getFacetPrice()));
                hashMap.put("grandTotal", header.getGrandTotal());
                hashMap.put("cashCarry", header.getCash_carry());
                hashMap.put("flash_note", header.getFlash_note());
                hashMap.put("order_sp", header.getOrderSp());
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertLensOrderItem(final Data_lensorderweb_item item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTLENSITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Toasty.info(getApplicationContext(), "Order has been sent", Toast.LENGTH_SHORT).show();

                        finish();
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
                    Log.d("ITEM ERROR ", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderNumber", item.getOrderNumber());
                hashMap.put("itemId", item.getItemId());
                hashMap.put("itemCode", item.getItemCode());
                hashMap.put("description", item.getDescription());
                hashMap.put("rl", item.getRl());
                hashMap.put("qty", String.valueOf(item.getQty()));
                hashMap.put("unitStandardPrice", String.valueOf(item.getUnitStandardPrice()));
                hashMap.put("unitStandardWeight", String.valueOf(item.getUnitStandardWeight()));
                hashMap.put("discountName", item.getDiscountName());
                hashMap.put("discount", String.valueOf(item.getDiscount()));
                hashMap.put("discount_sale", String.valueOf(item.getDiscountSale()));
                hashMap.put("totalWeight", String.valueOf(item.getTotalWeight()));
                hashMap.put("tintingPrice", String.valueOf(item.getTintingPrice()));
                hashMap.put("amount", String.valueOf(item.getAmount()));
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertStatusEntry(final String orderNumber)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTORDERENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
//                        Toasty.info(getApplicationContext(), "Order has been sent", Toast.LENGTH_SHORT).show();
//
//                        finish();

                        Log.d("SUCCESS STATUS ENTRY", "success");
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
                    Log.d("ERROR ENTRY STATUS", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderNumber", orderNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDiscountItemR(final String prodAttrR, final String custNumber)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.discount_item_getDiscount;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    discOperandR    = jsonObject.getString("operand");
                    String listLine = jsonObject.getString("list_line");
                    String name     = jsonObject.getString("name");
                    discNameR       = name + "." + listLine;
                    //Toasty.success(getApplicationContext(), "Diskon = " + oprDiscount + "%", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    discOperandR = "0";
                    discNameR    = "";
//                    Toasty.error(getApplicationContext(), oprDiscount.toString(), Toast.LENGTH_SHORT).show();
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
                hashMap.put("prod_attr", prodAttrR);
                hashMap.put("customer_number", custNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void getDiscountItemL(final String prodAttrL, final String custNumber)
    {
        Config config = new Config();
        String URL = config.Ip_address + config.discount_item_getDiscount;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    discOperandL    = jsonObject.getString("operand");
                    String listLine = jsonObject.getString("list_line");
                    String name     = jsonObject.getString("name");
                    discNameL       = name + "." + listLine;
                    //Toasty.success(getApplicationContext(), "Diskon = " + oprDiscount + "%", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                    discOperandL = "0";
                    discNameL    = "";
//                    Toasty.error(getApplicationContext(), oprDiscount.toString(), Toast.LENGTH_SHORT).show();
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
                hashMap.put("prod_attr", prodAttrL);
                hashMap.put("customer_number", custNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
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

    private void insertItemDurr(final String orderNumber, final String item_id, final String qty)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTDURATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {

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
                hashMap.put("no_sp", orderNumber);
                hashMap.put("item_id", item_id);
                hashMap.put("qty", qty);
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
                        Log.d("Status Payment", "Data not found");
                    }
                    else
                    {
                        flagPayment = object.getInt("order_satuan");

                        if (flagPayment == 1)
                        {
                            Log.d("Status Payment", "With Payment");

                            cardPayment.setVisibility(View.VISIBLE);

                            btnCheckout.setBackgroundColor(Color.LTGRAY);
                            btnCheckout.setTextColor(Color.BLACK);
                            btnCheckout.setEnabled(false);

                            scalableCourier.setVisibility(View.VISIBLE);
                            txtInfoShipping.setVisibility(View.GONE);
                        }
                        else
                        {
                            Log.d("Status Payment", "Non Payment");

                            shipPrice = "0";
                            String price = CurencyFormat(shipPrice.toString());
                            Double addNew = Double.parseDouble(tempTotal) + Double.parseDouble(shipPrice) - valTotalDiscSale;
                            addTemp = CurencyFormat(addNew.toString());
                            txtPriceShipping.setText(String.valueOf(price));
                            txtPriceTotal.setText("Rp " + String.valueOf(addTemp));


                            cardPayment.setVisibility(View.GONE);

                            btnCheckout.setBackgroundColor(getResources().getColor(R.color.colorFirst));
                            btnCheckout.setTextColor(Color.WHITE);
                            btnCheckout.setEnabled(true);

                            scalableCourier.setVisibility(View.GONE);
                            txtInfoShipping.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null || !error.getMessage().isEmpty())
                {
                    Log.d("Status Payment", error.getMessage());
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
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETACTIVESALE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("error"))
                    {
                        Log.d("Status sale", "Tidak ada flashsale");

//                        flagDiscSale = 0;
                    }
                    else
                    {
                        Log.d("Status sale", "Ada flashsale");

                        flashsaleNote = object.getString("title");
//                        flagDiscSale = 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!error.getMessage().isEmpty() || error.getMessage().equals(null))
                {
                    Log.d("Error Active Sale", error.getMessage());
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDiscSaleR(final String itemId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETDISCSALE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    discSaleR = object.getInt("disc_sale");
                    String saleR = String.valueOf(discSaleR);
                    valDiscSaleR = Double.parseDouble(saleR) * pr / 100;
//                    valDiscSaleL = 0F;

                    if (saleR.equals("0"))
                    {
                        linearFlashSale.setVisibility(View.GONE);
                    }

                    valTotalDiscSale = valDiscSaleR + valDiscSaleL;

                    Log.d("DISC FLASHSALE R", String.valueOf(discSaleR));
                    Log.d("Value FlashSale R", String.valueOf(valDiscSaleR));
                    Log.d("Total FlashSale", String.valueOf(valTotalDiscSale));

                    txtPriceDiscSale.setText(" - " + CurencyFormat(String.valueOf(valTotalDiscSale)));

//                    int newTotal = total - valTotalDiscSale;
//                    txtPriceTotal.setText(CurencyFormat(String.valueOf(newTotal)));

                    try {
                        Thread.sleep(1000);

                        if (flagPayment == 0)
                        {
                            scalableCourier.setVisibility(View.GONE);
                            txtInfoShipping.setText("Belum termasuk ongkos kirim. Kurir dan tarif pengiriman sesuai kebijakan Timur Raya");

                            shipPrice = "0";
                            String price = CurencyFormat(shipPrice.toString());
                            Double addNew = Double.parseDouble(tempTotal) + Double.parseDouble(shipPrice) - valTotalDiscSale;
                            addTemp = CurencyFormat(addNew.toString());
                            txtPriceShipping.setText(String.valueOf(price));
                            txtPriceTotal.setText("Rp " + String.valueOf(addTemp));
                        }
                        else
                        {
//                            getAllKurir(cityOptic, province);
                            scalableCourier.setVisibility(View.VISIBLE);
                            txtInfoShipping.setVisibility(View.GONE);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error.getMessage() != null || !error.getMessage().isEmpty())
//                {
                    Log.d("DISC FLASHSALE R", error.getMessage());
//                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDiscSaleL(final String itemId)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETDISCSALE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    discSaleL = object.getInt("disc_sale");
                    String saleL = String.valueOf(discSaleL);
                    valDiscSaleL =  Double.parseDouble(saleL) * pl / 100;

//                    if (valDiscSaleR == null)
//                    {
//                        valDiscSaleR = 0F;
//                    }

                    if (saleL.equals("0"))
                    {
                        linearFlashSale.setVisibility(View.GONE);
                    }

                    valTotalDiscSale = valDiscSaleR + valDiscSaleL;

                    Log.d("DISC FLASHSALE L", String.valueOf(discSaleL));
                    Log.d("Value FlashSale L", String.valueOf(valDiscSaleL));
                    Log.d("Total FlashSale", String.valueOf(valTotalDiscSale));

                    txtPriceDiscSale.setText(" - " + CurencyFormat(String.valueOf(valTotalDiscSale)));

//                    int newTotal = total - valTotalDiscSale;
//                    txtPriceTotal.setText(CurencyFormat(String.valueOf(newTotal)));

                    try {
                        Thread.sleep(1000);

                        if (flagPayment == 0)
                        {
                            scalableCourier.setVisibility(View.GONE);
                            txtInfoShipping.setText("Belum termasuk ongkos kirim. Kurir dan tarif pengiriman sesuai kebijakan Timur Raya");

                            shipPrice = "0";
                            String price = CurencyFormat(shipPrice.toString());
                            Double addNew = Double.parseDouble(tempTotal) + Double.parseDouble(shipPrice) - valTotalDiscSale;
                            addTemp = CurencyFormat(addNew.toString());
                            txtPriceShipping.setText(String.valueOf(price));
                            txtPriceTotal.setText("Rp " + String.valueOf(addTemp));
                        }
                        else
                        {
//                            getAllKurir(cityOptic, province);
                            scalableCourier.setVisibility(View.VISIBLE);
                            txtInfoShipping.setVisibility(View.GONE);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error.getMessage() != null || !error.getMessage().isEmpty())
//                {
                    Log.d("DISC FLASHSALE L", error.getMessage());
//                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("item_id", itemId);

                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSp()
    {
        if (isSp.equals("1")) {
            StringRequest request = new StringRequest(Request.Method.POST, URL_GETSPHEADER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);

                        dataSpHeader.setNoSp(object.getString("no_sp"));
                        dataSpHeader.setTypeSp(object.getString("type_sp"));
                        dataSpHeader.setSales(object.getString("sales"));
                        dataSpHeader.setCustName(object.getString("cust_name"));
                        dataSpHeader.setAddress(object.getString("address"));
                        dataSpHeader.setCity(object.getString("city"));
                        dataSpHeader.setOrderVia(object.getString("order_via"));
                        dataSpHeader.setDp(object.getInt("down_payment"));
                        dataSpHeader.setDisc(object.getString("disc"));
                        dataSpHeader.setCondition(object.getString("condition"));
                        dataSpHeader.setInstallment(object.getString("installment"));
                        dataSpHeader.setStartInstallment(object.getString("start_installment"));
                        dataSpHeader.setShipAddress(object.getString("shipping_address"));
                        dataSpHeader.setPhoto(object.getString("path"));
                        dataSpHeader.setStatus(object.getString("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() != null)
                    {
                        Log.d("GET SP", error.getMessage());
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("no_sp", orderNumber);
                    return hashMap;
                }
            };

            AppController.getInstance().addToRequestQueue(request);
        }
    }

    private void insertSpHeader(final Data_spheader item) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERTSPHEADER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Log.d("INSERT SP", "Success");

//                        updatePhoto(dataHeader, URL_UPDATEPHOTO);
                    }
                    else if (object.names().get(0).equals("error"))
                    {
                        Log.d("INSERT SP", "Error");
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
                    Log.d("INSERT SP", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("no_sp", item.getNoSp());
                hashMap.put("type_sp", item.getTypeSp());
                hashMap.put("sales", item.getSales());
                hashMap.put("customer_name", item.getCustName());
                hashMap.put("address", item.getAddress());
                hashMap.put("city", item.getCity());
                hashMap.put("order_via", "ANDROID");
                hashMap.put("down_payment", String.valueOf(item.getDp()));
                hashMap.put("discount", String.valueOf(item.getDisc()));
                hashMap.put("conditions", item.getCondition());
                hashMap.put("installment", item.getInstallment());
                hashMap.put("start_installment", item.getStartInstallment());
                hashMap.put("shipping_address", item.getShipAddress());
                hashMap.put("photo", "http://180.250.96.154/trl-dev/assets/images/ordersp/" + item.getPhoto());
                hashMap.put("path", item.getPhoto());
                hashMap.put("status", item.getStatus());
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertSP(final String URL, final Data_spheader item)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success"))
                    {
                        Log.d("INSERT SP", "Data has been save");
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
                    Log.d("INSERT SP", error.getMessage());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("type_sp", item.getTypeSp());
                hashMap.put("sales", item.getSales());
                hashMap.put("no_sp", item.getNoSp());
                hashMap.put("customer_name", item.getCustName());
                hashMap.put("address", item.getAddress());
                hashMap.put("city", item.getCity());
                hashMap.put("order_via", item.getOrderVia());
                hashMap.put("down_payment", String.valueOf(item.getDp()));
                hashMap.put("discount", String.valueOf(item.getDisc()));
                hashMap.put("conditions", item.getCondition());
                hashMap.put("installment", item.getInstallment());
                hashMap.put("start_installment", item.getStartInstallment());
                hashMap.put("shipping_address", item.getShipAddress());
                hashMap.put("photo", "\\\\192.168.44.21\\ordertxt\\orderandroid\\Foto SP\\foto\\" + item.getPhoto());
                hashMap.put("status", item.getStatus());
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
