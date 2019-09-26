package com.sofudev.trackapptrl.Custom;

public class Config {
    /* Area Ip */
    //Production Server
    public String Ip_address = "http://180.250.96.154/trl-webs/"; /* IP Public */
    //Development Server
    //public String Ip_address = "http://180.250.96.154/trl-dev/";
    //public String Ip_address = "http://192.168.44.143/"; /* Localhost */

    /* Area Login */
    public String login_apps = "index.php/android_main/loginApps";
    public String login_update_isOnline = "index.php/android_main/updateInfoLogin";

    /* Area Dashboard*/
    public String dashboard_user_detail = "index.php/android_dashboard/userDetail";
    public String dashboard_show_image  = "index.php/android_dashboard/showImageHome";
    public String sample_image          = "assets/images/produk/sample_image.png";
    public String dashboard_banner_slide= "index.php/android_dashboard/show_banner";
    public String dashboard_category    = "index.php/android_dashboard/show_categoryhome";
    public String dashboard_product_home= "index.php/android_dashboard/show_productHome";
    public String dashboard_hot_sale    = "index.php/android_dashboard/show_hotsale";
    public String dashboard_brand_random = "index.php/Android_dashboard/show_brandrandom";
    public String dashboard_update_offline = "index.php/android_main/updateLogout";
    public String dashboard_check_userinfo = "index.php/android_dashboard/checkUserDetail";
    public String dashboard_update_phone   = "index.php/android_dashboard/updateMobileNumber";
    public String dashboard_update_email   = "index.php/android_dashboard/updateEmailAddress";
    public String dashboard_verify_mail    = "index.php/android_dashboard/verifyEmailAddress";
    public String dashboard_verify_phone   = "index.php/android_dashboard/verifyPhoneNumber";
    public String dashboard_upload_txtphone= "index.php/android_dashboard/uploadVerifyphoneTxt";
    public String dashboard_search_product = "index.php/android_dashboard/searchProduct";
    public String dashboard_search_producttitle = "index.php/android_dashboard/searchProductTitle";

    /* Area Profile */
    public String profile_user_detail       = "index.php/android_profile/userProfileDetail";
    public String profile_update_contact    = "index.php/android_profile/updateProfileContact";
    public String profile_check_password    = "index.php/android_profile/checkProfilePassword";
    public String profile_update_password   = "index.php/android_profile/updateProfilePassword";
    public String profile_update_image      = "index.php/android_profile/updateImg";

    /* Area UAC*/
    public String uac_filter_user       = "index.php/android_uac/customerUACFilter/5/";
    public String uac_detail_user       = "index.php/android_uac/userUACDetail";
    public String uac_update_user       = "index.php/android_uac/UpdateUACData";
    public String uac_update_password   = "index.php/android_uac/UpdateUACPass";
    public String uac_add_user          = "index.php/android_uac/InsertUACData";

    /* Area TrackOrder */
    public String track_order_optic     = "index.php/Android_checkorder/getOrderByOpticname/8/";
    public String track_order_custname  = "index.php/Android_checkorder/getOrderByCustname/8/";
    public String track_order_reference = "index.php/Android_checkorder/getOrderByReference/8/";
    public String track_order_daterange = "index.php/Android_checkorder/getOrderByRange/8/";

    /* Area Filter Optic Name*/
    public String filter_optic_showall      = "index.php/Android_filteroptic/showAlloptic/8/";
    public String filter_optic_showbyname   = "index.php/Android_filteroptic/showOpticByFilter/8/";

    /* Area Info Order History */
    public String info_order_history    = "index.php/Android_checkorder/getOrderHistoryByStatus";

    /* Area Order Lens*/
    /*public String orderlens_show_lenstypebyid     = "trl-webs/index.php/Android_lensorder/showlenstypeById";
    public String orderlens_filter_lenstype       = "trl-webs/index.php/Android_lensorder/filterLenstypeByType";
    public String orderlens_insert_tmporder       = "trl-webs/index.php/Android_lensorder/insertTemporder";
    public String orderlens_update_lensDetail     = "trl-webs/index.php/Android_lensorder/updateLensDetail";
    public String orderlens_update_facetDetail    = "trl-webs/index.php/Android_lensorder/updateFacetDetail";
    public String orderlens_update_frameDetail    = "trl-webs/index.php/Android_lensorder/updateFrameDetail";
    public String orderlens_upload_txtfile        = "trl-webs/index.php/Android_lensorder/uploadTxt";
    public String orderlens_get_usernameinfo      = "trl-webs/index.php/Android_lensorder/getUserInfo";
    public String orderlens_verifyorder_mail      = "trl-webs/index.php/Android_lensorder/verifyOrderByMail";
    public String orderlens_sentorder_information = "trl-webs/index.php/Android_lensorder/sentOrderInformation";
    public String orderlens_autoreplyorder        = "trl-webs/index.php/Android_lensorder/autoreplyorder";
    public String orderlens_get_corridorinfo      = "trl-webs/index.php/Android_lensorder/getCorridorInfo";
    public String orderlens_get_tintgroup         = "trl-webs/index.php/Android_lensorder/getTintingGroup";
    public String orderlens_get_tinting           = "trl-webs/index.php/Android_lensorder/getTinting";
    public String orderlens_verifyorder_sms       = "trl-webs/index.php/Android_lensorder/verifyOrderBySMS";*/

    public String orderlens_show_lenstypebyid     = "index.php/Android_lensorder_new/showlenstypeById";
    public String orderlens_filter_lenstype       = "index.php/Android_lensorder_new/filterLenstypeByType";
    public String orderlens_insert_tmporder       = "index.php/Android_lensorder_new/insertTemporder";
    public String orderlens_update_lensDetail     = "index.php/Android_lensorder_new/updateLensDetail";
    public String orderlens_update_facetDetail    = "index.php/Android_lensorder_new/updateFacetDetail";
    public String orderlens_update_frameDetail    = "index.php/Android_lensorder_new/updateFrameDetail";
    public String orderlens_upload_txtfile        = "index.php/Android_lensorder_new/uploadTxt";
    public String orderlens_upload_temptxtfile    = "index.php/Android_lensorder_new/uploadTempTxtTest";
    public String orderlens_get_usernameinfo      = "index.php/Android_lensorder_new/getUserInfo";
    public String orderlens_verifyorder_mail      = "index.php/Android_lensorder_new/verifyOrderByMail";
    public String orderlens_sentorder_information = "index.php/Android_lensorder_new/sentOrderInformation";
    public String orderlens_autoreplyorder        = "index.php/Android_lensorder_new/autoreplyorder";
    public String orderlens_autoreplyorderpayment = "index.php/Android_lensorder_new/autoreplyorderpayment";
    public String orderlens_get_corridorinfo      = "index.php/Android_lensorder_new/getCorridorInfo";
    public String orderlens_get_tintgroup         = "index.php/Android_lensorder_new/getTintingGroup";
    public String orderlens_get_tinting           = "index.php/Android_lensorder_new/getTinting";
    public String orderlens_verifyorder_sms       = "index.php/Android_lensorder_new/verifyOrderBySMS";
    public String orderlens_get_coating           = "index.php/Android_lensorder_new/getCoating";
    public String orderlens_get_product           = "index.php/Android_lensorder_new/getProduct";
    public String orderlens_get_lensdiv           = "index.php/Android_lensorder_new/getLensdiv";

    /* Area order dashboard information panel*/
    public String orderdashboard_informationpanel = "index.php/Android_orderdashboard/showInformationPanelStatus";

    /* Area Frame */
    public String frame_bestproduct_showAllData   = "index.php/Android_frameproduct/apiFrameWithLimit";
    public String frame_filterby_group            = "index.php/Android_frameproduct/apiFrameWithGroup";
    public String frame_filterby_price            = "index.php/Android_frameproduct/apiFrameWithPrice";
    public String frame_filterwith_sort           = "index.php/Android_frameproduct/apiFrameWithFilter";
    public String frame_showcolor_filter          = "index.php/android_frameproduct/showColorList";
    public String frame_showbrand_filter          = "index.php/android_frameproduct/showBrandList";
    public String frame_showdetail_product        = "index.php/android_frameproduct/showDetailProduct";
    public String frame_showdetail_color          = "index.php/Android_frameproduct/showDetailColor";
    public String frame_generate_orderId          = "index.php/Android_frameorder/generateId";
    public String frame_insert_header             = "index.php/android_frameorder/insertFrameHeader";
    public String frame_insert_statusnonpayment   = "index.php/android_frameorder/insertStatusNonPayment";
    public String frame_insert_lineitem           = "index.php/android_frameorder/insertLineItem";
    public String frame_getall_frame              = "index.php/Android_orderhistoryframe/getAllFrame";
    public String frame_getframe_byrange          = "index.php/Android_orderhistoryframe/getAllFrameByRange";
    public String frame_getlineitem_frame         = "index.php/Android_orderhistoryframe/getDetailFrame";
    public String frame_getlineship_frame         = "index.php/Android_orderhistoryframe/getDetailFrameShipping";
    public String frame_checkpayment_ornot        = "index.php/Android_orderhistoryframe/checkPaymentOrNot";
    public String frame_showpayment_qr            = "index.php/Android_orderhistoryframe/showInfoPaymentQR";
    public String frame_showpayment_va            = "index.php/Android_orderhistoryframe/showInfoPaymentVA";
    public String frame_showpayment_cc            = "index.php/Android_orderhistoryframe/showInfoPaymentCC";
    public String frame_showpayment_loan          = "index.php/Android_orderhistoryframe/showInfoPaymentLoan";

    /* Area Price Rx dan Stock*/
    public String price_rx_getinfolens            = "index.php/Android_pricerx/paymentGetInfoLens";
    public String price_rx_getitemrx              = "index.php/Android_pricerx/paymentGetItemRX";
    public String price_st_getitemstock           = "index.php/Android_pricerx/paymentGetItemStock";
    public String price_all_getitemprice          = "index.php/Android_pricerx/paymentGetItemPrice";
    public String price_check_stockrl             = "index.php/Android_pricerx/paymentCheckStockRL";

    /* Area Discount Item */
    public String discount_item_getStbItem        = "index.php/Android_discountitem/paymentGetStbStockItem";
    public String discount_item_getDiscount       = "index.php/Android_discountitem/paymentGetDiscount";

    /* Area Facet Item */
    public String facet_item_getFacetItem         = "index.php/Android_facetitem/paymentGetItemFacet";
    public String facet_item_getFacetPrice        = "index.php/Android_facetitem/paymentGetPriceFacet";

    /* Area Tinting Item */
    public String tint_item_getOtherTint          = "index.php/Android_tintitem/paymentGetOtherTintPrice";

    /* Area Spinner Shipment */
    public String spinner_shipment_getAllData     = "index.php/Android_shipment/paymentShipment";
    public String spinner_shipment_getProvinceOptic = "index.php/Android_shipment/getProvinceOptic";
    public String spinner_shipment_getAllProvince = "index.php/Android_shipment/getProvince";
    public String spinner_shipment_getCity        = "index.php/Android_shipment/getCity";
    public String spinner_shipment_updateCity     = "index.php/Android_shipment/updateAddress";

    /* Area Lens Summary */
    public String lens_summary_getLensInfo        = "index.php/Android_lenssummary/getLensInformation";
    public String lens_summary_insertOrder        = "index.php/Android_lenssummary/inputOrder";
    public String lens_summary_insertOrderTemp    = "index.php/Android_lenssummary/inputOrderTemp";
    public String lens_summary_insertDetailOrder  = "index.php/Android_lenssummary/insertDetailOrder";
    public String lens_summary_getOrderNumber     = "index.php/Android_lenssummary/getOrderId";
    public String lens_summary_countTempOrder     = "index.php/Android_lenssummary/countTemp";
    public String lens_summary_sumWeightOrder     = "index.php/Android_lenssummary/sumWeight";
    public String lens_summary_deleteTempOrder    = "index.php/Android_lenssummary/deleteTemp";

    /* Area Order History */
    public String order_history_getPhoneNumber      = "index.php/Android_orderhistory/getPhoneNumber";
    public String order_history_updatePhoneNumber   = "index.php/Android_orderhistory/updatePhoneNumber";
    public String order_history_getOrderToday       = "index.php/Android_orderhistory/getOrderToday";
    public String order_history_searchByOrderNumber = "index.php/Android_orderhistory/searchByOrderNumber";
    public String order_history_searchByDateRange   = "index.php/Android_orderhistory/searchByDateRange";
    public String order_history_chooseOrder         = "index.php/Android_orderhistory/chooseOrder";
    public String order_history_showInfoPaymentQR   = "index.php/Android_orderhistory/showInfoPaymentQR";
    public String order_history_showInfoPaymentVA   = "index.php/Android_orderhistory/showInfoPaymentVA";
    public String order_history_showInfoPaymentCC   = "index.php/Android_orderhistory/showInfoPaymentCC";
    public String order_history_showInfoPaymentLoan = "index.php/Android_orderhistory/showInfoPaymentLoan";

    /* Area VIEW PDF */
    public String view_pdf_showAllData            = "index.php/Android_viewpdf/showData";
    public String view_pdf_showAllDataByFilter    = "index.php/Android_viewpdf/showDataFilter";

    /* Area Payment Method */
    public String payment_method_showAllData      = "index.php/Android_payment/showPayment";
    public String payment_insert_billingQR        = "index.php/Android_payment/postBillingQR";
    public String payment_insert_billingVA        = "index.php/Android_payment/postBillingVA";
    public String payment_insert_billingCC        = "index.php/Android_payment/postBillingCC";
    public String payment_insert_billingKP        = "index.php/Android_payment/postBillingKP";
    public String payment_insert_billingLOAN      = "index.php/Android_payment/postBillingLOAN";
    public String payment_show_expiredDurationQR  = "index.php/Android_payment/showPaymentSessionQR";
    public String payment_show_expiredDurationVA  = "index.php/Android_payment/showPaymentSessionVA";
    public String payment_show_expiredDurationCC  = "index.php/Android_payment/showPaymentSessionCC";
    public String payment_show_expiredDurationKP  = "index.php/Android_payment/showPaymentSessionKP";
    public String payment_show_expiredDurationLOAN= "index.php/Android_payment/showPaymentSessionLOAN";
//    public String payment_method_postBilling      = "http://ortu.izikita.net/soleh/callPACPayment.php";
    public String payment_method_postBilling      = "http://www.timurrayalab.com/webapi/callPACPayment.php";
//    public String payment_method_creditCard       = "http://ortu.izikita.net/soleh/testingccnew.php";
    public String payment_method_creditCard       = "http://www.timurrayalab.com/webapi/testingccnew.php";
    public String payment_method_autoMoveOrder    = "index.php/Android_payment/moveSuccessOrder";
    public String payment_method_updateStatus     = "index.php/Android_payment/moveSuccessOrder1";
    public String payment_method_cancelBilling    = "index.php/Android_payment/cancelpayment/";
//    public String payment_method_cancelBilling    = "index.php/Android_payment/cancelpayment/";
    public String payment_method_kreditPro        = "index.php/Android_payment/postKreditproPayment";
//    public String payment_method_loanSaldo        = "http://ortu.izikita.net/soleh/loanSoleh1.php";
//    public String payment_method_loanKonfirmasi   = "http://ortu.izikita.net/soleh/loanSoleh2.php";
//    public String payment_method_loanOtp          = "http://ortu.izikita.net/soleh/loanSoleh3.php";
    public String payment_method_loanSaldo        = "http://www.timurrayalab.com/webapi/loanSoleh1.php";
    public String payment_method_loanKonfirmasi   = "http://www.timurrayalab.com/webapi/loanSoleh2.php";
    public String payment_method_loanOtp          = "http://www.timurrayalab.com/webapi/loanSoleh3.php";
//    public String payment_check_status            = "http://ortu.izikita.net/soleh/cek_inquiry.php";
    public String payment_check_status            = "http://www.timurrayalab.com/webapi/cek_inquiry.php";
    public String payment_update_loanbprks        = "index.php/Android_payment/updateSaldoLoan";
    public String payment_update_token            = "index.php/Android_payment/updateToken";

    /* AREA WARRANTY */
    public String ewarranty_getListWarranty       = "index.php/Android_warranty/getListWarranty";

    /* AREA CHECK BALANCE*/
    public String check_balance_loanbprks         = "index.php/Android_payment/checkSaldo";
    public String show_historytrx_lens            = "index.php/Android_balance/showHistoryTrxLens";
    public String show_historytrx_lensall         = "index.php/Android_balance/showAllTrxLens";
    public String show_historytrx_frame           = "index.php/Android_balance/showHistoryTrxFrame";
    public String show_historytrx_frameall        = "index.php/Android_balance/showAllTrxFrame";

    /* AREA LENS STOCK */
    public String show_all_stocklens              = "index.php/Android_lensstock/showAllLensStock";
    public String show_sph                        = "index.php/Android_lensstock/showPowerSph";
    public String show_cyl                        = "index.php/Android_lensstock/showPowerCyl";
    public String show_add                        = "index.php/Android_lensstock/showPowerAdd";
    public String show_price                      = "index.php/Android_lensstock/showPrice";

    /* AREA LENS STOCK PARTAI */
    public String orderpartai_generateId          = "index.php/Android_lensstockorder/generateId";
    public String orderpartai_insertHeader        = "index.php/Android_lensstockorder/insertHeader";
    public String orderpartai_insertItem          = "index.php/Android_lensstockorder/insertItem";
    public String orderpartai_show_allhistory     = "index.php/Android_orderhistorypartai/getAllPartai";
    public String orderpartai_show_rangehistory   = "index.php/Android_orderhistorypartai/getAllPartaiByRange";
    public String orderpartai_show_detailitem     = "index.php/Android_orderhistorypartai/getDetailPartai";
    public String orderpartai_show_detailship     = "index.php/Android_orderhistorypartai/getDetailPartaiShip";
    public String orderpartai_checkpayment_ornot  = "index.php/Android_orderhistorypartai/checkPaymentOrNot";
    public String orderpartai_showpayment_qr      = "index.php/Android_orderhistorypartai/showInfoPaymentQR";
    public String orderpartai_showpayment_va      = "index.php/Android_orderhistorypartai/showInfoPaymentVA";
    public String orderpartai_showpayment_cc      = "index.php/Android_orderhistorypartai/showInfoPaymentCC";
    public String orderpartai_showpayment_loan    = "index.php/Android_orderhistorypartai/showInfoPaymentLoan";
}
