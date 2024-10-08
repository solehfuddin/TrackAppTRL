package com.sofudev.trackapptrl.Custom;

public class Config {
    /* Area Ip */
    //New Production Server
//    public String Ip_address = "https://leinz.org/trl-webs/"; /* IP Public */
//    public String Ip_21express = "https://leinz.org/trl_ds_exp/";
//    public String Ip_tiki    = "https://leinz.org/trl_tiki/";

    //Production Server
    public String Ip_address = "http://180.250.96.154/trl-webs/"; /* IP Public */
//    public String Ip_address = "http://180.250.96.154/trl-dev/"; /* IP Public */
    public String Ip_21express = "http://180.250.96.154/trl_ds_exp/";
    public String Ip_tiki    = "http://180.250.96.154/trl_tiki/";

    //Development Server
//    public String Ip_addressdev = "http://180.250.96.154/trl-dev/";
//    public String Ip_address = "http://192.168.44.21/trl-dev/";
//    public String Ip_address = "http://192.168.44.143/trl-webs/"; /* Localhost */

    public String Username21 = "TIMURRAYALESTARI";
    public String Password21 = "606c0c0e3cb22";
    public String ClientId   = "ebf4b72c9ec86e51e5827ab352cc1aa9";
    public String ClientSecret = "1b8fc7a3a55de16ee21904d2d84cfb66";

    /*public String UsernameTiki = "TIMURRAYA";
    public String PasswordTiki = "RAYALES785";*/

    public String UsernameTiki = "17464515123871067514";
    public String PasswordTiki = "2a35487c74b28b7548ed8f5a0b359f37aa6e1dcd";

    /* Area App Setting */
    public String version_apps = "index.php/android_appsetting/getVersion";
    public String maintenance_mode = "index.php/android_appsetting/getMaintenanceMode";
    public String frame_price_mode = "index.php/android_appsetting/getPriceFrameMode";
    public String frame_stock_mode = "index.php/Android_appsetting/getStockFrameMode";
    public String shipping_mode    = "index.php/Android_appsetting/getShippingMode";

    /* Area Login */
    public String login_apps = "index.php/android_main/loginApps";
    public String login_update_isOnline = "index.php/android_main/updateInfoLogin";
    public String login_update_fcmtoken = "index.php/android_main/updateToken";
    public String login_update_fcmtokenbyCust = "index.php/android_main/updateTokenByCustname";
    public String get_sic_code = "index.php/android_main/getSicCode";
    public String set_sic_code = "index.php/android_main/setSicCode";
    public String verify_siccode = "index.php/android_main/verifySicCode";
    public String status_user  = "index.php/android_main/cekStatusUser";
    public String get_user_byid= "index.php/android_main/getUserById";
    public String get_user_bycustname= "index.php/android_main/getUserByCustname";

    /* Area Dashboard*/
    public String dashboard_user_detail = "index.php/android_dashboard/userDetail";
    public String dashboard_user_detailByUsername = "index.php/android_dashboard/userDetailByUsername";
    public String dashboard_show_image  = "index.php/android_dashboard/showImageHome";
    public String sample_image          = "assets/images/produk/sample_image.png";
    public String dashboard_banner_slide= "index.php/android_dashboard/show_banner";
    public String dashboard_category    = "index.php/android_dashboard/show_categoryhome";
    public String dashboard_product_home= "index.php/android_dashboard/show_productHome";
    public String dashboard_hot_sale    = "index.php/android_dashboard/show_hotsale";
    public String dashboard_brand_random = "index.php/Android_dashboard/show_brandrandom";
    public String dashboard_more_frame  = "index.php/Android_dashboard/show_moreframe";
    public String dashboard_update_offline = "index.php/android_main/updateLogout";
    public String dashboard_check_userinfo = "index.php/android_dashboard/checkUserDetail";
    public String dashboard_update_phone   = "index.php/android_dashboard/updateMobileNumber";
    public String dashboard_update_email   = "index.php/android_dashboard/updateEmailAddress";
    public String dashboard_verify_mail    = "index.php/android_dashboard/verifyEmailAddress";
    public String dashboard_verify_phone   = "index.php/android_dashboard/verifyPhoneNumber";
    public String dashboard_upload_txtphone= "index.php/android_dashboard/uploadVerifyphoneTxt";
    public String dashboard_search_product = "index.php/android_dashboard/searchProduct";
    public String dashboard_search_producttitle = "index.php/android_dashboard/searchProductTitle";
    public String dashboard_getpromo_banner= "index.php/Android_dashboard/getPromoBanner";
    public String dashboard_another_product= "index.php/Android_dashboard/show_anotherproduct";
    public String dashboard_popular_frame  = "index.php/Android_dashboard/show_popularframe";

    /* Area Profile */
    public String profile_user_detail       = "index.php/android_profile/userProfileDetail";
    public String profile_update_contact    = "index.php/android_profile/updateProfileContact";
    public String profile_check_password    = "index.php/android_profile/checkProfilePassword";
    public String profile_update_password   = "index.php/android_profile/updateProfilePassword";
    public String profile_update_image      = "index.php/android_profile/updateImg";

    /* Area UAC*/
    public String uac_filter_user       = "index.php/android_uac/customerUACFilter/5/";
    /*public String uac_detail_user       = "index.php/android_uac/userUACDetail";*/
    public String uac_detail_user       = "index.php/android_uac/userUACDetailNew";
    /*public String uac_update_user       = "index.php/android_uac/UpdateUACData";*/
    public String uac_update_user       = "index.php/android_uac/UpdateUACDataNew";
    public String uac_update_password   = "index.php/android_uac/UpdateUACPass";
    /*public String uac_add_user          = "index.php/android_uac/InsertUACData";*/
    public String uac_add_user          = "index.php/android_uac/InsertUACDataNew";

    /* Area TrackOrder */
    public String track_order_optic     = "index.php/Android_checkorder/getOrderByOpticname/8/";
    public String track_order_stock_optic       = "index.php/Android_checkorder/getOrderStockByOpticname/8/";
    public String track_order_custname  = "index.php/Android_checkorder/getOrderByCustname/8/";
    public String track_order_reference = "index.php/Android_checkorder/getOrderByReference/8/";
    public String track_order_stock_search      = "index.php/Android_checkorder/searchOrderStockByOpticname/8/";
    public String track_order_daterange = "index.php/Android_checkorder/getOrderByRange/8/";
    public String track_order_stock_daterange   = "index.php/Android_checkorder/getOrderStockByRange/8/";
    public String track_order_getFrame  = "index.php/Android_checkorder/getFrameBrand";
    public String track_order_getType   = "index.php/Android_checkorder/getFrameType";
    public String track_order_itemstock = "index.php/Android_checkorder/getItemLensStock";

    /* Area Filter Optic Name*/
    public String filter_optic_showall      = "index.php/Android_filteroptic/showAlloptic/8/";
    public String filter_optic_showallcustom  = "index.php/Android_filteroptic/showAllopticCustom/8/";
    public String filter_optic_showbyname   = "index.php/Android_filteroptic/showOpticByFilter/8/";
    public String filter_optic_showbynamecustom = "index.php/Android_filteroptic/showOpticCustomByFilter/8/";
    public String filter_optic_getbyshipnumber = "index.php/Android_filteroptic/showOpticByShipname";

    /* Area Filter Sales Name */
    public String filter_sales_showall      = "index.php/Android_Filtersales/showAllsales/8/";
    public String filter_sales_showbyname   = "index.php/Android_Filtersales/showSalesByFilter/8/";

    /* Area Info Order History */
    public String info_order_history    = "index.php/Android_checkorder/getOrderHistoryByStatus";
    public String info_order_historypos = "index.php/Android_checkorder/getOrderHistoryByPos";
    public String info_order_historyjob = "index.php/Android_checkorder/getOrderHistoryByJob";
    public String info_order_historyStock = "index.php/Android_checkorder/getOrderStockHistoryByInv";

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
    public String orderlens_get_corridorinfoAlias = "index.php/Android_lensorder_new/getCorridorInfoAlias";
    public String orderlens_get_tintgroup         = "index.php/Android_lensorder_new/getTintingGroup";
    public String orderlens_get_tinting           = "index.php/Android_lensorder_new/getTinting";
    public String orderlens_verifyorder_sms       = "index.php/Android_lensorder_new/verifyOrderBySMS";
    public String orderlens_get_coating           = "index.php/Android_lensorder_new/getCoating";
    public String orderlens_get_product           = "index.php/Android_lensorder_new/getProduct";
    public String orderlens_get_lensdiv           = "index.php/Android_lensorder_new/getLensdiv";
    public String orderlens_get_detailaccount     = "index.php/Android_lensorder_new/getDetailAccount";
    public String orderlens_check_reminder        = "index.php/Android_lensorder_new/checkReminder";
    public String orderlens_send_reminder         = "index.php/Android_lensorder_new/reminderPaymentByMail";

    /* Area order dashboard information panel*/
    public String orderdashboard_informationpanel = "index.php/Android_orderdashboard/showInformationPanelStatus";

    /* Area Frame */
    public String frame_bestproduct_showAllData   = "index.php/Android_frameproduct/apiFrameWithLimit";
    public String frame_filterby_group            = "index.php/Android_frameproduct/apiFrameWithGroup";
//    public String frame_filterby_category         = "index.php/Android_frameproduct/apiFrameWithCategory";
//    public String frame_filterby_keyword          = "index.php/Android_frameproduct/apiFrameWithSearch";
    public String frame_filterby_price            = "index.php/Android_frameproduct/apiFrameWithPrice";
    public String frame_filterwith_sort           = "index.php/Android_frameproduct/apiFrameWithFilter";
    public String frame_showcolor_filter          = "index.php/android_frameproduct/showColorList";
    public String frame_showbrand_filter          = "index.php/android_frameproduct/showBrandList";
    public String frame_showdetail_product        = "index.php/android_frameproduct/showDetailProduct";
    public String frame_showdetail_color          = "index.php/Android_frameproduct/showDetailColor";
    public String frame_generate_orderId          = "index.php/Android_frameorder/generateId";
    public String frame_getoracle_id              = "index.php/Android_frameorder/getOracleId";
    public String frame_insert_header             = "index.php/android_frameorder/insertFrameHeader";
    public String frame_insert_statusnonpayment   = "index.php/android_frameorder/insertStatusNonPayment";
    public String frame_insert_lineitem           = "index.php/android_frameorder/insertLineItem";
//    public String frame_check_stockframe          = "index.php/android_frameorder/checkStockFrame";
    public String frame_update_stockframe         = "index.php/android_frameorder/updateStockFrame";
    public String frame_getall_frame              = "index.php/Android_orderhistoryframe/getAllFrame";
    public String frame_getframe_byrange          = "index.php/Android_orderhistoryframe/getAllFrameByRange";
    public String frame_getlineitem_frame         = "index.php/Android_orderhistoryframe/getDetailFrame";
    public String frame_getlineship_frame         = "index.php/Android_orderhistoryframe/getDetailFrameShipping";
    public String frame_getheader_frame           = "index.php/Android_orderhistoryframe/getDetailFrameHeader";
    public String frame_getitem_frame             = "index.php/Android_orderhistoryframe/getDetailFrameItem";
    public String frame_checkpayment_ornot        = "index.php/Android_orderhistoryframe/checkPaymentOrNot";
    public String frame_showpayment_qr            = "index.php/Android_orderhistoryframe/showInfoPaymentQR";
    public String frame_showpayment_va            = "index.php/Android_orderhistoryframe/showInfoPaymentVA";
    public String frame_showpayment_cc            = "index.php/Android_orderhistoryframe/showInfoPaymentCC";
    public String frame_showpayment_loan          = "index.php/Android_orderhistoryframe/showInfoPaymentLoan";
    public String frame_showpayment_deposit       = "index.php/Android_orderhistoryframe/showInfoPaymentDeposit";

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
    public String facet_item_getLensFacet         = "index.php/Android_facetitem/paymentGetLensFacet";

    /* Area Tinting Item */
    public String tint_item_getOtherTint          = "index.php/Android_tintitem/paymentGetOtherTintPrice";

    /* Area Spinner Shipment */
    public String spinner_shipment_getAllData     = "index.php/Android_shipment/paymentShipment";
    public String spinner_shipment_getProvinceOptic = "index.php/Android_shipment/getProvinceOptic";
    public String spinner_shipment_getAllProvince = "index.php/Android_shipment/getProvince";
    public String spinner_shipment_getCity        = "index.php/Android_shipment/getCity";
    public String spinner_shipment_updateCity     = "index.php/Android_shipment/updateAddress";
    public String shipment_checkMember            = "index.php/Android_shipment/check_member";

    /* Area Lens Summary */
    public String lens_summary_getLensInfo        = "index.php/Android_lenssummary/getLensInformation";
    public String lens_summary_insertOrder        = "index.php/Android_lenssummary/inputOrder";
    public String lens_summary_insertOrderTemp    = "index.php/Android_lenssummary/inputOrderTemp";
    public String lens_summary_insertDetailOrder  = "index.php/Android_lenssummary/insertDetailOrder";
    public String lens_summary_getOrderNumber     = "index.php/Android_lenssummary/getOrderId";
    public String lens_summary_countTempOrder     = "index.php/Android_lenssummary/countTemp";
    public String lens_summary_sumWeightOrder     = "index.php/Android_lenssummary/sumWeight";
    public String lens_summary_deleteTempOrder    = "index.php/Android_lenssummary/deleteTemp";
    public String lens_summary_getDiscSale        = "index.php/Android_lenssummary/getDiscFlashSale";

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
    public String order_history_showInfoPaymentDepo = "index.php/Android_orderhistory/showInfoPaymentDeposit";

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
    public String payment_insert_billingDeposit   = "index.php/Android_payment/postBillingDeposit";
    public String payment_show_expiredDurationQR  = "index.php/Android_payment/showPaymentSessionQR";
    public String payment_show_expiredDurationVA  = "index.php/Android_payment/showPaymentSessionVA";
    public String payment_show_expiredDurationCC  = "index.php/Android_payment/showPaymentSessionCC";
    public String payment_show_expiredDurationKP  = "index.php/Android_payment/showPaymentSessionKP";
    public String payment_show_expiredDurationLOAN= "index.php/Android_payment/showPaymentSessionLOAN";
    public String payment_show_expiredDurationDeposit = "index.php/Android_payment/showPaymentSessionDeposit";
//    public String payment_method_postBilling      = "http://timurrayalab.com/devapi/callPACPayment.php";
    public String payment_method_postBilling      = "https://www.timurrayalab.com/webapi/callPACPayment.php";
//    public String payment_method_creditCard       = "http://ortu.izikita.net/soleh/testingccnew.php";
    public String payment_method_creditCard       = "https://www.timurrayalab.com/webapi/testingccnew.php";
    public String payment_method_deposit          = "index.php/Android_payment/payWithDeposit";
    public String payment_method_autoMoveOrder    = "index.php/Android_payment/moveSuccessOrder1";
    public String payment_method_updateStatus     = "index.php/Android_payment/moveSuccessOrder1";
    public String payment_method_cancelBilling    = "index.php/Android_payment/cancelpayment/";
//    public String payment_method_cancelBilling    = "index.php/Android_payment/cancelpayment/";
    public String payment_method_kreditPro        = "index.php/Android_payment/postKreditproPayment";
//    public String payment_method_loanSaldo        = "http://ortu.izikita.net/soleh/loanSoleh1.php";
//    public String payment_method_loanKonfirmasi   = "http://ortu.izikita.net/soleh/loanSoleh2.php";
//    public String payment_method_loanOtp          = "http://ortu.izikita.net/soleh/loanSoleh3.php";
    public String payment_method_loanSaldo        = "https://www.timurrayalab.com/webapi/loanSoleh1.php";
    public String payment_method_loanKonfirmasi   = "https://www.timurrayalab.com/webapi/loanSoleh2.php";
    public String payment_method_loanOtp          = "https://www.timurrayalab.com/webapi/loanSoleh3.php";
//    public String payment_check_status            = "http://ortu.izikita.net/soleh/cek_inquiry.php";
    public String payment_check_status            = "https://www.timurrayalab.com/webapi/cek_inquiry.php";
    public String payment_update_loanbprks        = "index.php/Android_payment/updateSaldoLoan";
//    public String payment_update_token            = "index.php/Android_payment/updateToken";

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
    public String show_price_sp                   = "index.php/Android_apisppartai/showPrice";
    public String update_stock_lens_partai        = "index.php/Android_lensstock/updateStockLens";

    /* AREA LENS STOCK PARTAI */
    public String orderpartai_generateId          = "index.php/Android_lensstockorder/generateId";
    public String orderpartai_insertHeader        = "index.php/Android_lensstockorder/insertHeader";
    public String orderpartai_insertItem          = "index.php/Android_lensstockorder/insertItem";
    public String orderpartai_nonpayment          = "index.php/Android_lensstockorder/insertNonPayment";
    public String orderpartai_getPrice            = "index.php/Android_lensstockorder/getPricePartai";
    public String orderpartai_show_allhistory     = "index.php/Android_orderhistorypartai/getAllPartai";
    public String orderpartai_show_rangehistory   = "index.php/Android_orderhistorypartai/getAllPartaiByRange";
    public String orderpartai_show_detailitem     = "index.php/Android_orderhistorypartai/getDetailPartai";
    public String orderpartai_show_detailship     = "index.php/Android_orderhistorypartai/getDetailPartaiShip";
    public String orderpartai_checkpayment_ornot  = "index.php/Android_orderhistorypartai/checkPaymentOrNot";
    public String orderpartai_showpayment_qr      = "index.php/Android_orderhistorypartai/showInfoPaymentQR";
    public String orderpartai_showpayment_va      = "index.php/Android_orderhistorypartai/showInfoPaymentVA";
    public String orderpartai_showpayment_cc      = "index.php/Android_orderhistorypartai/showInfoPaymentCC";
    public String orderpartai_showpayment_loan    = "index.php/Android_orderhistorypartai/showInfoPaymentLoan";
    public String orderpartai_showpayment_deposit = "index.php/Android_orderhistorypartai/showInfoPaymentDeposit";
    public String orderpartai_detail_header       = "index.php/Android_orderhistorypartai/getDetailPartaiHeader";
    public String orderpartai_detail_item         = "index.php/Android_orderhistorypartai/getDetailPartaiItem";

    /* AREA INTEGRASI LENS DENGAN WEB ECOMMERCE*/
    public String orderlens_insert_lensorder      = "index.php/Android_lensorder_web/insertLensorder";
    public String orderlens_insert_lensorderitem  = "index.php/Android_lensorder_web/insertLensorderItem";
    public String orderlens_insert_statusentry    = "index.php/Android_lensorder_web/insertStatusEntry";
    public String orderlens_confirm_entrynonpay   = "index.php/Android_lensorder_web/confirmEntryNonPayment";
    public String orderlens_confirm_entrypay      = "index.php/Android_lensorder_web/confirmEntryPayment";
    public String orderlens_complete_info         = "index.php/Android_lensorder_web/checkCompleteInfo";
    public String orderlens_insert_checkstatus    = "index.php/Android_lensorder_web/checkStatusEntry";
    public String orderlens_get_orderid           = "index.php/Android_lensorder_web/getOrderId";
    public String orderlens_get_infolens          = "index.php/Android_lensorder_web/getInfoLens";
    public String orderlens_get_sidelensrx        = "index.php/Android_lensorder_web/getSideLensRX";
    public String orderlens_get_sidelensstock     = "index.php/Android_lensorder_web/getSideLensStock";
    public String orderlens_check_stock           = "index.php/Android_lensorder_web/checkStockLens";
    public String orderlens_update_stock          = "index.php/Android_lensorder_web/updateStockLens";

    /* AREA INTEGRASI HISTORY LENS DENGAN WEB ECOMMERCE */
    public String lenshistory_getOrderTodayWeb    = "index.php/Android_orderhistory/getOrderTodayWeb";
    public String lenshistory_getOrderByPatient   = "index.php/Android_orderhistory/getOrderByPatient";
    public String lenshistory_getOrderByRange     = "index.php/Android_orderhistory/getOrderByDate";
    public String lenshistory_checkpayment_ornot  = "index.php/Android_orderhistory/checkPaymentOrNot";
    public String lenshistory_chooselens          = "index.php/Android_orderhistory/chooseOrderLens";
    public String lenshistory_chooselensheader    = "index.php/Android_orderhistory/chooseOrderLensHeader";
    public String lenshistory_chooselensitem      = "index.php/Android_orderhistory/chooseOrderLensItem";

    /* AREA INTEGRASI DENGAN APLIKASI SP */
    public String ordersp_get_spId                = "index.php/Android_apisp/getSpId";
    public String ordersp_get_spIdFrame           = "index.php/Android_apisp/getSpIdFrame";
    public String ordersp_get_spIdPameran         = "index.php/Android_apisp/getPameran";
    public String ordersp_get_opticInfo           = "index.php/Android_apisp/getOpticInfo";
    public String ordersp_insert_spHeader         = "index.php/Android_apisp/insertSp";
    public String ordersp_insert_samTemp          = "index.php/Android_apisp/insertSamTemp";
    public String ordersp_update_photo            = "index.php/Android_apisp/updatePhoto";
    public String ordersp_update_digitalsigned    = "index.php/Android_apisp/updateSigned";
    public String ordersp_update_excel            = "index.php/Android_apisp/updateExcel";
    public String ordersp_autosent_photo          = "index.php/Android_apisp/uploadPhoto";
    public String ordersp_get_spHeader            = "index.php/Android_apisp/getSpHeader";
    public String ordersp_get_listSp              = "index.php/Android_apisp/getSp/8/";
    public String ordersp_get_listSpByOptic       = "index.php/Android_apisp/getSpByOptic/8/";
    public String ordersp_get_searchSp            = "index.php/Android_apisp/getSpSearch/8/";
    public String ordersp_get_searchSpByOptic     = "index.php/Android_apisp/getSpSearchByOptic/8/";
    public String ordersp_get_rangeSp             = "index.php/Android_apisp/getSpRange/8/";
    public String ordersp_get_rangeSpByOptic      = "index.php/Android_apisp/getSpRangeByOptic/8/";
    public String ordersp_get_statusSp            = "index.php/Android_apisp/getLastStatus";
    public String ordersp_get_detailSp            = "index.php/Android_apisp/trackingSp";
    public String ordersp_get_inv                 = "index.php/Android_apisp/getInv";
    public String ordersp_insert_duration         = "index.php/Android_apisp/insertDuration";
    public String ordersp_insert_durationbin      = "index.php/Android_apisp/insertDurationBin";
    public String ordersp_get_approvalsp          = "index.php/Android_apisp/getApprovalSp/8/";
    public String ordersp_search_approvalsp       = "index.php/Android_apisp/getSpApprovalSearch/1/";
    public String ordersp_update_approvalsp       = "index.php/Android_apisp/approvalByAreaManager";

    public String ordersp_update_approvalsphold   = "index.php/Android_apisp/approvalHoldByAreaManager";
    public String ordersp_insert_arapprove        = "index.php/Android_apisp/approveByAR";
    public String ordersp_insert_arhold           = "index.php/Android_apisp/holdByAR";
    public String ordersp_update_arrelease        = "index.php/Android_apisp/releaseByAR";
    public String ordersp_insert_arreject         = "index.php/Android_apisp/rejectByAR";
    public String ordersp_update_rejectsp         = "index.php/Android_apisp/rejectByAreaManager";
    public String ordersp_update_rejectsphold     = "index.php/Android_apisp/rejectHoldByAreaManager";
    public String ordersp_item_detailsp           = "index.php/Android_apisp/getItemSpFrame";
    public String ordersp_unprocess_frame         = "index.php/Android_apisp/getUnproccessSp/8/";
    public String ordersp_process_frame           = "index.php/Android_apisp/getProccessSp/8/";
    public String ordersp_unprocess_frame_search  = "index.php/Android_apisp/getUnproccessSpSearch/8/";
    public String ordersp_process_frame_search    = "index.php/Android_apisp/getProccessSpSearch/8/";
    public String ordersp_holdunprocess_frame     = "index.php/Android_apisp/getHoldUnproccessSp/8/";
    public String ordersp_holdunprocess_frame_search  = "index.php/Android_apisp/getHoldUnproccessSpSearch/8/";
    public String ordersp_hold_frame              = "index.php/Android_apisp/getHoldSpFrame/8/";
    public String ordersp_reject_frame            = "index.php/Android_apisp/getRejectSpFrame/8/";
    public String ordersp_laststatus_frame        = "index.php/Android_apisp/getLastStatusSpFrame/8/";

    /* AREA FLASH SALE */
    public String flashsale_getActiveSale         = "index.php/Android_flashsale/getActiveSale";
    public String flashsale_getImagePromo         = "index.php/Android_flashsale/gambarPromo";

    /* AREA MEMBER FLAG */
    public String memberflag_getStatus            = "index.php/Android_memberflag/getStatus";

    /* AREA LOG FLAG */
    public String inser_logger                    = "index.php/Android_logger/createLog";

    /* AREA ON HAND */
    public String masteronhand_getitemname        = "index.php/Android_masteronhand/getItemname";
    public String masteronhand_getitemframe       = "index.php/Android_masteronhand/getBrandFrame";
    public String masteronhand_getbyorgid         = "index.php/Android_masteronhand/getOnHandByOrgid";
    public String masteronhand_getbysorting       = "index.php/Android_masteronhand/getOnHandBySorting";
    public String masteronhand_getbyframesorting  = "index.php/Android_masteronhand/getOnHandFrameBySorting";
    public String masteronhand_getbyframebinsorting = "index.php/Android_masteronhand/getOnHandFrameBinBySorting";
    public String masteronhand_getbyqty           = "index.php/Android_masteronhand/getOnHandByQty";
    public String masteronhand_getsuggestion      = "index.php/Android_masteronhand/searchSuggestion";
    public String masteronhand_search             = "index.php/Android_masteronhand/searchOnHand";
    public String masteronhand_searchFrame        = "index.php/Android_masteronhand/searchOnHandFrame";
    public String masteronhand_searchFrameBin     = "index.php/Android_masteronhand/searchOnHandFrameBin";
    public String masteronhand_gettranslation     = "index.php/Android_masteronhand/getCategoryTranslasi";
    public String masteronhand_getbytranslation   = "index.php/Android_masteronhand/getOnHandByTranslasi";

    /* AREA SP FRAME */
    public String spframe_get_framebrand          = "index.php/Android_apispframe/getBrand";
    public String spframe_get_byframe             = "index.php/Android_apispframe/getFrameByBrand";
    public String spframe_get_byframe_flag        = "index.php/Android_apispframe/getFrameByBrandFlag";
    public String spframe_get_searchframe         = "index.php/Android_apispframe/getFrameBySearch";
    public String spframe_get_searchframebrand    = "index.php/Android_apispframe/getFrameBrandBySearch";
    public String spframe_get_searchframebrandflag= "index.php/Android_apispframe/getFrameBrandBySearchBin";
    public String spframe_get_searchbarcode       = "index.php/Android_apispframe/getFrameByBarcode";
    public String spframe_get_searchbarcodeflag   = "index.php/Android_apispframe/getFrameByBarcodeBin";
    public String spframe_get_byitemid            = "index.php/Android_apispframe/getFrameByItemId";
    public String spframe_get_byitemidflag        = "index.php/Android_apispframe/getFrameByItemIdBin";
    public String spframe_get_qtybyitemflag       = "index.php/Android_apispframe/getQtyFrameByItem";

    /* AREA DEPOSIT */
    public String depo_getsaldo                   = "index.php/Android_deposit/getSaldo";
    public String depo_getrecentsaldo             = "index.php/Android_deposit/getRecentMutasi";
    public String depo_getrecentpending           = "index.php/Android_deposit/getRecentPending";
    public String depo_getfiltersaldo             = "index.php/Android_deposit/getMutasi";
    public String depo_getfilterpending           = "index.php/Android_deposit/getPending";
    public String depo_getfiltertotal             = "index.php/Android_deposit/getTotalByDate";

    /* AREA ESTATEMENT */
    public String estatement_getdata              = "index.php/Android_estatement/showStatement";
    public String estatement_getdatarange         = "index.php/Android_estatement/showStatementRange";
    public String estatement_getcategory          = "index.php/Android_estatement/showCategory";
    public String estatement_getcategoryrange     = "index.php/Android_estatement/showCategoryRange";
    public String estatement_getsummarylast       = "index.php/Android_estatement/getTotalSummaryLast";
    public String estatement_getdetailsummary     = "index.php/Android_estatement/getDetailSummaryLast";

    /* AREA EINVOICE */
    public String einvoice_getdata                = "index.php/Android_einvoice/showInvoice";
    public String einvoice_getcategory            = "index.php/Android_einvoice/showCategory";
    public String einvoice_getDetail              = "index.php/Android_einvoice/showDetailData";

    /* AREA EINVOICE PAID */
    public String einvoicepaid_getdata            = "index.php/Android_einvoice_paid/showInvoice";
    public String einvoicepaid_getcategory        = "index.php/Android_einvoice_paid/showCategory";
    public String einvoicepaid_getDetail          = "index.php/Android_einvoice_paid/showDetailData";

    /* AREA DELIVERY TRACK */
    public String deliverytrack_bydate            = "index.php/Android_deliverytrack/showDataByDate";
    public String deliverytrack_counter           = "index.php/Android_deliverytrack/countData";
    public String deliverytrack_byawbnumber       = "index.php/Android_deliverytrack/showDataByAwb";

    /* AREA TRACKING 21 EXPRESS */
    public String generate21_token                = "index.php/Texp/req_access_token";
    public String getstatus21_info                = "index.php/Texp/get_info_status";
    public String receive21_info                  = "index.php/Texp/get_received_status";

    /* AREA TIKI */
    public String generatetiki_token              = "index.php/Texp/req_access_token";
    public String getconnote_info                 = "index.php/Texp/connote_info";
    public String getconnote_status               = "index.php/Texp/connote_history";

    /* AREA PARENT CHILD CHECK */
    public String getparent_info                  = "index.php/Android_parentchild/getParent";
    public String getchild_data                   = "index.php/Android_parentchild/showChild";
    public String searchchild_data                = "index.php/Android_parentchild/searchChild";

    /* AREA TRACKING DPODK  */
    public String dpodk_getcourierbydate          = "index.php/Android_apidpodk/getCourierByDate";
    public String dpodk_getcourierbydaterange     = "index.php/Android_apidpodk/getCourierByDateRange";
    public String dpodk_getcourierbysearch        = "index.php/Android_apidpodk/getCourierBySearch";
    public String dpodk_getinvbyidtrx             = "index.php/Android_apidpodk/getInvproccessByDpodk";
    public String dpodk_changestatus              = "index.php/Android_apidpodk/changeStatusCourier";
    public String dpodk_searchbyinvnumber         = "index.php/Android_apidpodk/getCourierBySearchNoTrx";
    public String dpodk_historybydaterange        = "index.php/Android_apidpodk/getHistoryByDateRange";
    public String dpodk_historybysearchidtrx      = "index.php/Android_apidpodk/getHistoryBySearcNoinv";
    public String dpodk_processbysearch           = "index.php/Android_apidpodk/getProcessBySearch";
    public String dpodk_historybyidtrx            = "index.php/Android_apidpodk/getHistoryByNotrx";
    public String dpodk_processbyidtrx            = "index.php/Android_apidpodk/getProccessByNotrx";
    public String dpodk_historybysearchinvnumber  = "index.php/Android_apidpodk/getHistoryBySearcNoinv";
    public String dpodk_processbysearchinvnumber  = "index.php/Android_apidpodk/getProcessBySearcNoinv";
    public String dpodk_processbydaterange        = "index.php/Android_apidpodk/getProcessByDateRange";
    public String dpodk_statuscheckbyoptic        = "index.php/Android_apidpodk/getProcessCourierByOptic";
    public String dpodk_statuscounterbyoptic      = "index.php/Android_apidpodk/countProcessCourierByOptic";
    public String dpodk_getprocessbyoptic         = "index.php/Android_apidpodk/getProcessByOptic";
    public String dpodk_getdeliveredbyoptic       = "index.php/Android_apidpodk/getDeliveredByOptic";
    public String dpodk_processcounterbyoptic     = "index.php/Android_apidpodk/countProcessByOptic";
    public String dpodk_deliveredcounterbyoptic   = "index.php/Android_apidpodk/countDeliveredByOptic";

    /* AREA NEWS */
    public String news_getdatawithlimit           = "index.php/Android_Post/getNewsExtenal/3/";
    public String news_getbycategorylimit         = "index.php/Android_Post/getNewsExtenalByCategory/5/";
    public String news_getcategory                = "index.php/Android_Post/getCategories";

    /* AREA PRODUCT COMPARE */
    public String compare_getbyId                 = "index.php/Android_Compare/getCompareById";
    public String compare_getcategory             = "index.php/Android_Compare/getTitles";

    /* AREA NOTIFICATION */
    public String notification_getdata            = "index.php/Notification/showNotification";
    public String notification_updateIsRead       = "index.php/Notification/updateNotifIsRead";
    public String notification_countUnread        = "index.php/Notification/counterNotifUnseen";

    /* AREA ASSIGN BIN */
    public String assignbin_getheader             = "index.php/Android_assignbin/getDataHeader/5/";
    public String assignbin_getline               = "index.php/Android_assignbin/getDataLine";
    public String assignbin_updateline            = "index.php/Android_assignbin/updateDataLine";
    public String assignbin_insertretur           = "index.php/Android_assignbin/insertReturBin";
    public String assignbin_getdataretur          = "index.php/Android_assignbin/getDataRetur/5/";
    public String assignbin_getlineretur          = "index.php/Android_assignbin/getLineRetur";

    /* AREA SHIPPING */
    public String shipping_getType                = "index.php/Android_shipping/getShippingType";
    public String shipping_getProvince_tiki       = "index.php/Android_shipping/getProvinceTiki";
}
