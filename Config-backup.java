package com.sofudev.trackapptrl.Custom;

public class Config {
    /* Area Ip */
    public String Ip_address = "http://180.250.96.154/"; /* IP Public */
    //public String Ip_address = "http://192.168.44.143/"; /* Localhost */

    /* Area Login */
    public String login_apps = "trl-webs/index.php/android_main/loginApps";
    public String login_update_isOnline = "trl-webs/index.php/android_main/updateInfoLogin";

    /* Area Dashboard*/
    public String dashboard_user_detail = "trl-webs/index.php/android_dashboard/userDetail";
    public String dashboard_show_image  = "trl-webs/index.php/android_dashboard/showImageHome";
    public String sample_image          = "trl-webs/assets/images/produk/sample_image.png";
    public String dashboard_banner_slide= "trl-webs/index.php/android_dashboard/show_banner";
    public String dashboard_category    = "trl-webs/index.php/android_dashboard/show_categoryhome";
    public String dashboard_product_home= "trl-webs/index.php/android_dashboard/show_productHome";
    public String dashboard_update_offline = "trl-webs/index.php/android_main/updateLogout";
    public String dashboard_check_userinfo = "trl-webs/index.php/android_dashboard/checkUserDetail";
    public String dashboard_update_phone   = "trl-webs/index.php/android_dashboard/updateMobileNumber";
    public String dashboard_update_email   = "trl-webs/index.php/android_dashboard/updateEmailAddress";
    public String dashboard_verify_mail    = "trl-webs/index.php/android_dashboard/verifyEmailAddress";
    public String dashboard_verify_phone   = "trl-webs/index.php/android_dashboard/verifyPhoneNumber";
    public String dashboard_upload_txtphone= "trl-webs/index.php/android_dashboard/uploadVerifyphoneTxt";

    /* Area Profile */
    public String profile_user_detail       = "trl-webs/index.php/android_profile/userProfileDetail";
    public String profile_update_contact    = "trl-webs/index.php/android_profile/updateProfileContact";
    public String profile_check_password    = "trl-webs/index.php/android_profile/checkProfilePassword";
    public String profile_update_password   = "trl-webs/index.php/android_profile/updateProfilePassword";
    public String profile_update_image      = "trl-webs/index.php/android_profile/updateImg";

    /* Area UAC*/
    public String uac_filter_user       = "trl-webs/index.php/android_uac/customerUACFilter/5/";
    public String uac_detail_user       = "trl-webs/index.php/android_uac/userUACDetail";
    public String uac_update_user       = "trl-webs/index.php/android_uac/UpdateUACData";
    public String uac_update_password   = "trl-webs/index.php/android_uac/UpdateUACPass";
    public String uac_add_user          = "trl-webs/index.php/android_uac/InsertUACData";

    /* Area TrackOrder */
    public String track_order_optic     = "trl-webs/index.php/Android_checkorder/getOrderByOpticname/8/";
    public String track_order_custname  = "trl-webs/index.php/Android_checkorder/getOrderByCustname/8/";
    public String track_order_reference = "trl-webs/index.php/Android_checkorder/getOrderByReference/8/";
    public String track_order_daterange = "trl-webs/index.php/Android_checkorder/getOrderByRange/8/";

    /* Area Filter Optic Name*/
    public String filter_optic_showall      = "trl-webs/index.php/Android_filteroptic/showAlloptic/8/";
    public String filter_optic_showbyname   = "trl-webs/index.php/Android_filteroptic/showOpticByFilter/8/";

    /* Area Info Order History */
    public String info_order_history    = "trl-webs/index.php/Android_checkorder/getOrderHistoryByStatus";

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

    public String orderlens_show_lenstypebyid     = "trl-webs/index.php/Android_lensorder_new/showlenstypeById";
    public String orderlens_filter_lenstype       = "trl-webs/index.php/Android_lensorder_new/filterLenstypeByType";
    public String orderlens_insert_tmporder       = "trl-webs/index.php/Android_lensorder_new/insertTemporder";
    public String orderlens_update_lensDetail     = "trl-webs/index.php/Android_lensorder_new/updateLensDetail";
    public String orderlens_update_facetDetail    = "trl-webs/index.php/Android_lensorder_new/updateFacetDetail";
    public String orderlens_update_frameDetail    = "trl-webs/index.php/Android_lensorder_new/updateFrameDetail";
    public String orderlens_upload_txtfile        = "trl-webs/index.php/Android_lensorder_new/uploadTxt";
    public String orderlens_upload_temptxtfile    = "trl-webs/index.php/Android_lensorder_new/uploadTempTxtTest";
    public String orderlens_get_usernameinfo      = "trl-webs/index.php/Android_lensorder_new/getUserInfo";
    public String orderlens_verifyorder_mail      = "trl-webs/index.php/Android_lensorder_new/verifyOrderByMail";
    public String orderlens_sentorder_information = "trl-webs/index.php/Android_lensorder_new/sentOrderInformation";
    public String orderlens_autoreplyorder        = "trl-webs/index.php/Android_lensorder_new/autoreplyorder";
    public String orderlens_autoreplyorderpayment = "trl-webs/index.php/Android_lensorder_new/autoreplyorderpayment";
    public String orderlens_get_corridorinfo      = "trl-webs/index.php/Android_lensorder_new/getCorridorInfo";
    public String orderlens_get_tintgroup         = "trl-webs/index.php/Android_lensorder_new/getTintingGroup";
    public String orderlens_get_tinting           = "trl-webs/index.php/Android_lensorder_new/getTinting";
    public String orderlens_verifyorder_sms       = "trl-webs/index.php/Android_lensorder_new/verifyOrderBySMS";
    public String orderlens_get_coating           = "trl-webs/index.php/Android_lensorder_new/getCoating";
    public String orderlens_get_product           = "trl-webs/index.php/Android_lensorder_new/getProduct";
    public String orderlens_get_lensdiv           = "trl-webs/index.php/Android_lensorder_new/getLensdiv";

    /* Area order dashboard information panel*/
    public String orderdashboard_informationpanel = "trl-webs/index.php/Android_orderdashboard/showInformationPanelStatus";

    /* Area Frame */
    public String frame_bestproduct_showAllData   = "trl-webs/index.php/Android_frameproduct/showDataFrame";

    /* Area Price Rx dan Stock*/
    public String price_rx_getinfolens            = "trl-webs/index.php/Android_pricerx/paymentGetInfoLens";
    public String price_rx_getitemrx              = "trl-webs/index.php/Android_pricerx/paymentGetItemRX";
    public String price_st_getitemstock           = "trl-webs/index.php/Android_pricerx/paymentGetItemStock";
    public String price_all_getitemprice          = "trl-webs/index.php/Android_pricerx/paymentGetItemPrice";
    public String price_check_stockrl             = "trl-webs/index.php/Android_pricerx/paymentCheckStockRL";

    /* Area Discount Item */
    public String discount_item_getStbItem        = "trl-webs/index.php/Android_discountitem/paymentGetStbStockItem";
    public String discount_item_getDiscount       = "trl-webs/index.php/Android_discountitem/paymentGetDiscount";

    /* Area Facet Item */
    public String facet_item_getFacetItem         = "trl-webs/index.php/Android_facetitem/paymentGetItemFacet";
    public String facet_item_getFacetPrice        = "trl-webs/index.php/Android_facetitem/paymentGetPriceFacet";

    /* Area Tinting Item */
    public String tint_item_getOtherTint          = "trl-webs/index.php/Android_tintitem/paymentGetOtherTintPrice";

    /* Area Spinner Shipment */
    public String spinner_shipment_getAllData     = "trl-webs/index.php/Android_shipment/paymentShipment";

    /* Area Lens Summary */
    public String lens_summary_getLensInfo        = "trl-webs/index.php/Android_lenssummary/getLensInformation";
    public String lens_summary_insertOrder        = "trl-webs/index.php/Android_lenssummary/inputOrder";
    public String lens_summary_insertOrderTemp    = "trl-webs/index.php/Android_lenssummary/inputOrderTemp";
    public String lens_summary_insertDetailOrder  = "trl-webs/index.php/Android_lenssummary/insertDetailOrder";
    public String lens_summary_getOrderNumber     = "trl-webs/index.php/Android_lenssummary/getOrderId";
    public String lens_summary_countTempOrder     = "trl-webs/index.php/Android_lenssummary/countTemp";
    public String lens_summary_sumWeightOrder     = "trl-webs/index.php/Android_lenssummary/sumWeight";
    public String lens_summary_deleteTempOrder    = "trl-webs/index.php/Android_lenssummary/deleteTemp";

    /* Area Order History */
    public String order_history_getOrderToday       = "trl-webs/index.php/Android_orderhistory/getOrderToday";
    public String order_history_searchByOrderNumber = "trl-webs/index.php/Android_orderhistory/searchByOrderNumber";
    public String order_history_searchByDateRange   = "trl-webs/index.php/Android_orderhistory/searchByDateRange";
    public String order_history_chooseOrder         = "trl-webs/index.php/Android_orderhistory/chooseOrder";
    public String order_history_showInfoPaymentQR     = "trl-webs/index.php/Android_orderhistory/showInfoPaymentQR";
    public String order_history_showInfoPaymentVA     = "trl-webs/index.php/Android_orderhistory/showInfoPaymentVA";

    /* Area VIEW PDF */
    public String view_pdf_showAllData            = "trl-webs/index.php/Android_viewpdf/showData";
    public String view_pdf_showAllDataByFilter    = "trl-webs/index.php/Android_viewpdf/showDataFilter";

    /* Area Payment Method */
    public String payment_method_showAllData      = "trl-webs/index.php/Android_payment/showPayment";
    public String payment_insert_billingQR        = "trl-webs/index.php/Android_payment/postBillingQR";
    public String payment_insert_billingVA        = "trl-webs/index.php/Android_payment/postBillingVA";
    public String payment_show_expiredDurationQR  = "trl-webs/index.php/Android_payment/showPaymentSessionQR";
    public String payment_show_expiredDurationVA  = "trl-webs/index.php/Android_payment/showPaymentSessionVA";
    public String payment_method_postBilling      = "http://ortu.izikita.net/soleh/callPACPayment.php";
    public String payment_method_cancelBilling    = "trl-webs/index.php/Android_payment/cancelpayment/";
}
