package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageMultiResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.DashboardActivity;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class FormProfileActivity extends AppCompatActivity{
    Config config = new Config();

    private String DETAILURL   = config.Ip_address + config.profile_user_detail;
    private String CONTACTURL  = config.Ip_address + config.profile_update_contact;
    private String CHECKPASS   = config.Ip_address + config.profile_check_password;
    private String CHANGEPASS  = config.Ip_address + config.profile_update_password;
    private String CHANGEIMG   = config.Ip_address + config.profile_update_image;

    ImageButton btn_back, btn_updateContact, btnDone;
    UniversalFontTextView txt_profile, txt_customer, txt_address, txt_contact, txt_phone, txt_email;
    UniversalFontTextView info_txt_contact, info_txt_phone, info_txt_email, info_txt_oldpass, info_txt_newpass,
                          info_txt_confirmpass;
    BootstrapEditText update_txt_contact, update_txt_phone, update_txt_email, update_txt_oldpass, update_txt_newpass,
                      update_txt_confirmpass;
    BootstrapCircleThumbnail img_profile;
    BootstrapButton btn_changeimg, btn_status, update_btn_update, update_btn_cancel, update_btn_password;
    LinearLayout linear_header;

    private String decrypt_username, decrypt_image, decrypt_customer, decrypt_status, decrypt_address1,
            decrypt_address2, decrypt_city, decrypt_province, decrypt_postcode, decrypt_contact, decrypt_phone,
            decrypt_email;
    private String id_data;
    String filename;
    Uri img_uri;
    private MCrypt mCrypt;
    private ACProgressFlower loading;
    private LovelyCustomDialog lovelyCustomDialog;
    private LayoutInflater layoutInflater;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_profile);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btn_back        = (ImageButton) findViewById(R.id.form_profile_btnback);
        btnDone         = findViewById(R.id.form_profile_btndone);
        txt_profile     = (UniversalFontTextView) findViewById(R.id.form_profile_txtusername);
        txt_customer    = (UniversalFontTextView) findViewById(R.id.form_profile_txtCustomer);
        txt_address     = (UniversalFontTextView) findViewById(R.id.form_profile_txtAddress);
        txt_contact     = (UniversalFontTextView) findViewById(R.id.form_profile_txtContactPerson);
        txt_phone       = (UniversalFontTextView) findViewById(R.id.form_profile_txtPhonePerson);
        txt_email       = (UniversalFontTextView) findViewById(R.id.form_profile_txtEmailPerson);
        img_profile     = (BootstrapCircleThumbnail) findViewById(R.id.form_profile_imgprofile);
        btn_changeimg   = (BootstrapButton) findViewById(R.id.form_profile_btnChangeimg);
        btn_status      = (BootstrapButton) findViewById(R.id.form_profile_btnStatus);
        btn_updateContact = (ImageButton) findViewById(R.id.form_profile_imgbtnEditContact);
        linear_header   = (LinearLayout) findViewById(R.id.form_profile_linearHeader);

        //Initialize control for update password
        update_txt_oldpass  = (BootstrapEditText) findViewById(R.id.form_profile_txtOldPassword);
        update_txt_newpass  = (BootstrapEditText) findViewById(R.id.form_profile_txtNewPassword);
        update_txt_confirmpass = (BootstrapEditText) findViewById(R.id.form_profile_txtConfirmPassword);
        update_btn_password = (BootstrapButton) findViewById(R.id.form_profile_btnUpdatePassword);
        info_txt_oldpass    = (UniversalFontTextView) findViewById(R.id.form_profile_lblOldPassword);
        info_txt_newpass    = (UniversalFontTextView) findViewById(R.id.form_profile_lblNewPassword);
        info_txt_confirmpass= (UniversalFontTextView) findViewById(R.id.form_profile_lblConfirmPassword);

        mCrypt = new MCrypt();
        getUsernameData();
        BackToDashboard();
        showDetailuser();
        imageChooser();
        openDialogContact();

        updatePassword();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.putExtra("idparty", id_data);
        intent.putExtra("username", txt_customer.getText().toString());
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void BackToDashboard()
    {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.putExtra("idparty", id_data);
                intent.putExtra("username", txt_customer.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    private void getUsernameData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            id_data = bundle.getString("idparty");
        }
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(FormProfileActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();
        loading.show();
    }

    private void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        img_status      = (ImageView) dialog.findViewById(R.id.info_status_imageview);
        txt_information = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtInformation);
        txt_message     = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtMessage);
        btn_ok          = (BootstrapButton) dialog.findViewById(R.id.info_status_btnOk);

        img_status.setImageResource(resource);
        txt_information.setText(info);
        txt_message.setText(message);
        btn_ok.setBootstrapBrand(defaultcolorbtn);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDetailuser();
            }
        });

        dialog.show();
    }

    private void informationImage(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        img_status      = (ImageView) dialog.findViewById(R.id.info_status_imageview);
        txt_information = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtInformation);
        txt_message     = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtMessage);
        btn_ok          = (BootstrapButton) dialog.findViewById(R.id.info_status_btnOk);

        img_status.setImageResource(resource);
        txt_information.setText(info);
        txt_message.setText(message);
        btn_ok.setBootstrapBrand(defaultcolorbtn);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             changeImgProfile(filename);
            }
        });

        dialog.show();
    }

    private void showDetailuser()
    {
        showLoading();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DETAILURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info_username = "username";
                String info_image    = "image";
                String info_customer = "customer";
                String info_status   = "status";
                String info_address1 = "address1";
                String info_address2 = "address2";
                String info_city     = "city";
                String info_province = "province";
                String info_postcode = "postcode";
                String info_contact  = "contact";
                String info_phone    = "phone";
                String info_email    = "email";

                try {
                    String encrypt_username = MCrypt.bytesToHex(mCrypt.encrypt(info_username));
                    String encrypt_image    = MCrypt.bytesToHex(mCrypt.encrypt(info_image));
                    String encrypt_customer = MCrypt.bytesToHex(mCrypt.encrypt(info_customer));
                    String encrypt_status   = MCrypt.bytesToHex(mCrypt.encrypt(info_status));
                    String encrypt_address1 = MCrypt.bytesToHex(mCrypt.encrypt(info_address1));
                    String encrypt_address2 = MCrypt.bytesToHex(mCrypt.encrypt(info_address2));
                    String encrypt_city     = MCrypt.bytesToHex(mCrypt.encrypt(info_city));
                    String encrypt_province = MCrypt.bytesToHex(mCrypt.encrypt(info_province));
                    String encrypt_postcode = MCrypt.bytesToHex(mCrypt.encrypt(info_postcode));
                    String encrypt_contact  = MCrypt.bytesToHex(mCrypt.encrypt(info_contact));
                    String encrypt_phone    = MCrypt.bytesToHex(mCrypt.encrypt(info_phone));
                    String encrypt_email    = MCrypt.bytesToHex(mCrypt.encrypt(info_email));

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject   = jsonArray.getJSONObject(i);
                            decrypt_username = new String(mCrypt.decrypt(jsonObject.getString(encrypt_username)));
                            decrypt_image    = new String(mCrypt.decrypt(jsonObject.getString(encrypt_image)));
                            decrypt_customer = new String(mCrypt.decrypt(jsonObject.getString(encrypt_customer)));
                            decrypt_status   = new String(mCrypt.decrypt(jsonObject.getString(encrypt_status)));
                            decrypt_address1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt_address1)));
                            decrypt_address2 = new String(mCrypt.decrypt(jsonObject.getString(encrypt_address2)));
                            decrypt_city     = new String(mCrypt.decrypt(jsonObject.getString(encrypt_city)));
                            decrypt_province = new String(mCrypt.decrypt(jsonObject.getString(encrypt_province)));
                            decrypt_postcode = new String(mCrypt.decrypt(jsonObject.getString(encrypt_postcode)));
                            decrypt_contact  = new String(mCrypt.decrypt(jsonObject.getString(encrypt_contact)));
                            decrypt_phone    = new String(mCrypt.decrypt(jsonObject.getString(encrypt_phone)));
                            decrypt_email    = new String(mCrypt.decrypt(jsonObject.getString(encrypt_email)));

                            txt_profile.setText(decrypt_username);
                            Picasso.with(FormProfileActivity.this).load(decrypt_image).into(img_profile);

                            //area basic profile
                            txt_customer.setText(decrypt_customer);
                            if (decrypt_status.equals("A"))
                            {
                                String active = "Active";
                                btn_status.setText(active);
                                btn_status.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            }
                            else if (decrypt_status.equals("I"))
                            {
                                String inactive = "Inactive";
                                btn_status.setText(inactive);
                                btn_status.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                            }

                            if (decrypt_address2.equals("-"))
                            {
                                decrypt_address2 = "";
                            }
                            if (decrypt_city.equals("-"))
                            {
                                decrypt_city = "";
                            }
                            if (decrypt_province.equals("-"))
                            {
                                decrypt_province = "";
                            }
                            if (decrypt_postcode.equals("-"))
                            {
                                decrypt_postcode = "";
                            }
                            String address = decrypt_address1 + " " + decrypt_address2 + " " + decrypt_city + " " + decrypt_province + " "
                                    + decrypt_postcode;
                            txt_address.setText(address);

                            //area contact profile
                            txt_contact.setText(decrypt_contact);
                            txt_phone.setText(decrypt_phone);
                            txt_email.setText(decrypt_email);
                        }
                        loading.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
                information("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
                        DefaultBootstrapBrand.WARNING);
                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id_data);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void imageChooser()
    {
        btn_changeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.build(new DialogConfiguration()
                        .setTitle("Pilih gambar")
                        .setOptionOrientation(LinearLayoutCompat.HORIZONTAL)
                        .setResultImageDimension(600, 600), new ImageMultiResultListener() {
                    @Override
                    public void onImageResult(ArrayList<ImageResult> arrayList) {
                        Log.e(LOG_TAG, "onImageResult:Number of image picked " + arrayList.size());

                        Toasty.info(getApplicationContext(), arrayList.get(0).getPath(), Toast.LENGTH_LONG).show();

                        img_profile.setImageBitmap(arrayList.get(0).getBitmap());

//                        img_uri = getImageUrl(FormProfileActivity.this, arrayList.get(0).getBitmap());
//                        filename = getPath(img_uri);
//                        changeImgProfile(arrayList.get(0).getPath());
                    }
                }).show(getSupportFragmentManager());
            }
        });

//        img_profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImagePicker.build(new DialogConfiguration()
//                        .setTitle("Pilih gambar")
//                        .setOptionOrientation(LinearLayoutCompat.VERTICAL)
//                        .setResultImageDimension(600, 600), new ImageMultiResultListener() {
//                    @Override
//                    public void onImageResult(ArrayList<ImageResult> arrayList) {
//                        Log.e(LOG_TAG, "onImageResult:Number of image picked " + arrayList.size());
//                        img_profile.setImageBitmap(arrayList.get(0).getBitmap());
//                        Toasty.info(getApplicationContext(), arrayList.get(0).getPath(), Toast.LENGTH_LONG).show();
////                        img_uri = getImageUrl(FormProfileActivity.this, arrayList.get(0).getBitmap());
////                        filename = getPath(img_uri);
////                        changeImgProfile(arrayList.get(0).getPath());
//                    }
//                }).show(getSupportFragmentManager());
//            }
//        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImgProfile(filename);
            }
        });
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }

    private void openDialogContact()
    {
        btn_updateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lovelyCustomDialog = new LovelyCustomDialog(FormProfileActivity.this);
                layoutInflater = getLayoutInflater();
                view = layoutInflater.inflate(R.layout.form_profile_contact, null);
                lovelyCustomDialog.setView(view);
                lovelyCustomDialog.setTopColorRes(R.color.bootstrap_brand_danger);
                lovelyCustomDialog.setTopTitleColor(Color.WHITE);
                lovelyCustomDialog.setTopTitle("Update contact profile");

                info_txt_contact   = (UniversalFontTextView) view.findViewById(R.id.form_profile_contact_lblCp);
                info_txt_phone     = (UniversalFontTextView) view.findViewById(R.id.form_profile_contact_lblPhone);
                info_txt_email     = (UniversalFontTextView) view.findViewById(R.id.form_profile_contact_lblMail);
                update_txt_contact = (BootstrapEditText) view.findViewById(R.id.form_profile_contact_txtCp);
                update_txt_phone   = (BootstrapEditText) view.findViewById(R.id.form_profile_contact_txtPhone);
                update_txt_email   = (BootstrapEditText) view.findViewById(R.id.form_profile_contact_txtMail);
                update_btn_update  = (BootstrapButton) view.findViewById(R.id.form_profile_contact_btnUpdate);
                update_btn_cancel  = (BootstrapButton) view.findViewById(R.id.form_profile_contact_btnCancel);

                //update_btn_update.setEnabled(false);
                update_txt_contact.setText(decrypt_contact);
                update_txt_phone.setText(decrypt_phone);
                update_txt_email.setText(decrypt_email);

                update_txt_contact.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (Pattern.matches("^[\\p{L} .'-]+$", update_txt_contact.getText()))
                        {
                            update_txt_contact.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            info_txt_contact.setVisibility(View.GONE);
                        }
                        else {
                            update_txt_contact.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                            info_txt_contact.setVisibility(View.VISIBLE);
                        }
                    }
                });

                update_txt_phone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (isValidPhone(update_txt_phone.getText().toString()))
                        {
                            update_txt_phone.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            info_txt_phone.setVisibility(View.GONE);
                        }
                        else {
                            String info = "Invalid phone number";
                            update_txt_phone.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                            info_txt_phone.setText(info);
                            info_txt_phone.setVisibility(View.VISIBLE);
                        }
                    }
                });

                update_txt_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (isValidEmail(update_txt_email.getText().toString()))
                        {
                            update_txt_email.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            info_txt_email.setVisibility(View.GONE);
                            //update_btn_update.setEnabled(true);
                        }
                        else {
                            String info = "Invalid email address";
                            update_txt_email.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                            info_txt_email.setText(info);
                            info_txt_email.setVisibility(View.VISIBLE);
                        }
                    }
                });

                update_btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isValidEmail(update_txt_email.getText().toString()) && isValidPhone(update_txt_phone.getText().toString())
                                && Pattern.matches("^[\\p{L} .'-]+$", update_txt_contact.getText()))
                        {
                            final String contact  = update_txt_contact.getText().toString();
                            final String phone    = update_txt_phone.getText().toString();
                            final String email    = update_txt_email.getText().toString();

                            UpdateProfileContact(contact, phone, email);
                        }
                        else {
                            Toasty.error(getApplicationContext(), "Please check your data", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });

                update_btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lovelyCustomDialog.dismiss();
                    }
                });

                lovelyCustomDialog.show();
            }
        });
    }

    private void UpdateProfileContact(final String contact, final String phone, final String email)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CONTACTURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info = "success";
                try {
                    String encrypt_info = MCrypt.bytesToHex(mCrypt.encrypt(info));
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String data = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info)));

                        if (jsonObject.names().get(0).equals(encrypt_info))
                        {
                            Toasty.success(getApplicationContext(), data, Toast.LENGTH_SHORT, true).show();

                            txt_contact.setText(contact.toUpperCase());
                            txt_phone.setText(phone);
                            txt_email.setText(email.toLowerCase());

                            lovelyCustomDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toasty.warning(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id_party", id_data);
                hashMap.put("contact", contact);
                hashMap.put("phone", phone);
                hashMap.put("email", email);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updatePassword()
    {
        update_txt_newpass.setEnabled(false);
        update_txt_confirmpass.setEnabled(false);
        update_btn_password.setEnabled(false);

        update_txt_oldpass.addTextChangedListener(new TextWatcher() {
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
                    update_txt_oldpass.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                    info_txt_oldpass.setVisibility(View.VISIBLE);
                }
                else {
                    update_txt_oldpass.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                    info_txt_oldpass.setVisibility(View.GONE);
                    String pass = update_txt_oldpass.getText().toString();
                    checkPassword(pass);
                }
            }
        });

        update_txt_newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6)
                {
                    String info = "Password length must more 6 characters";
                    update_txt_newpass.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                    info_txt_newpass.setText(info);
                    info_txt_newpass.setVisibility(View.VISIBLE);
                    update_txt_confirmpass.setEnabled(false);
                }
                else {
                    update_txt_newpass.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                    info_txt_newpass.setVisibility(View.GONE);
                    update_txt_confirmpass.setEnabled(true);
                    update_txt_oldpass.setEnabled(false);
                    String newPass = update_txt_newpass.getText().toString();
                    String conPass = update_txt_confirmpass.getText().toString();
                    if (!newPass.equals(conPass))
                    {
                        String info = "New password not match";
                        info_txt_newpass.setText(info);
                        info_txt_newpass.setVisibility(View.VISIBLE);
                    }
                    else {
                        info_txt_newpass.setVisibility(View.GONE);
                        info_txt_confirmpass.setVisibility(View.GONE);
                    }
                }
            }
        });

        update_txt_confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6)
                {
                    String info = "Password length must more 6 characters";
                    update_txt_confirmpass.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                    info_txt_confirmpass.setText(info);
                    info_txt_confirmpass.setVisibility(View.VISIBLE);
                    update_btn_password.setEnabled(false);
                }
                else {
                    update_txt_confirmpass.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                    info_txt_confirmpass.setVisibility(View.GONE);
                    String newPass = update_txt_newpass.getText().toString();
                    String conPass = update_txt_confirmpass.getText().toString();
                    if (newPass.equals(conPass))
                    {
                        info_txt_confirmpass.setVisibility(View.GONE);
                        info_txt_newpass.setVisibility(View.GONE);
                        update_btn_password.setEnabled(true);
                    }
                    else {
                        String info = "Confirm password not match";
                        info_txt_confirmpass.setText(info);
                        info_txt_confirmpass.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        update_btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = update_txt_newpass.getText().toString();
                String conPass = update_txt_confirmpass.getText().toString();
                if (newPass.equals(conPass))
                {
                    changePassword(conPass);
                }
                else
                {
                    Toasty.error(getApplicationContext(), "Please check your input password", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void checkPassword(final String pass)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECKPASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info = "info";
                String success = "success";
                String error   = "error";
                try {
                    String encrypt_info = MCrypt.bytesToHex(mCrypt.encrypt(info));
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String decrypt_info = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info)));

                        if (jsonObject.names().get(0).equals(encrypt_info))
                        {
                            if (decrypt_info.equals(success))
                            {
                                update_txt_oldpass.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                                update_txt_newpass.setFocusable(true);
                                update_txt_newpass.setEnabled(true);
                                info_txt_oldpass.setVisibility(View.GONE);
                            }
                            else if (decrypt_info.equals(error)){
                                String error_pass = "Current password not match";
                                update_txt_newpass.setEnabled(false);
                                update_txt_oldpass.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                                info_txt_oldpass.setText(error_pass);
                                info_txt_oldpass.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", txt_profile.getText().toString());
                hashMap.put("password", pass);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void changePassword(final String newPassword)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHANGEPASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info = "info";
                String success = "success";
                String error = "error";
                try {
                    String encrypt_info = MCrypt.bytesToHex(mCrypt.encrypt(info));
                    try {
                        JSONObject jsonObject =  new JSONObject(response);
                        String decrypt_info = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info)));

                        if (decrypt_info.equals(success))
                        {
                            Toasty.success(getApplicationContext(), "New password has been updated", Toast.LENGTH_SHORT, true).show();
                        }
                        else if (decrypt_info.equals(error))
                        {
                            Toasty.error(getApplicationContext(), "Check your data, password not change", Toast.LENGTH_SHORT, true).show();
                        }

                        clearPassword();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("username", txt_profile.getText().toString());
                hashMap.put("password", newPassword);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void clearPassword()
    {
        update_txt_oldpass.setText("");
        update_txt_newpass.setText("");
        update_txt_confirmpass.setText("");

        update_txt_oldpass.setEnabled(true);
        update_txt_newpass.setEnabled(false);
        update_txt_confirmpass.setEnabled(false);
        update_txt_confirmpass.setFocusable(false);
        update_btn_password.setEnabled(false);

        info_txt_oldpass.setVisibility(View.GONE);
        info_txt_newpass.setVisibility(View.GONE);
        info_txt_confirmpass.setVisibility(View.GONE);

        linear_header.setFocusable(true);
        linear_header.setFocusableInTouchMode(true);
    }

    public Uri getImageUrl(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
                "Title", null);

        return Uri.parse(path);
    }

    private String getPath(Uri content) {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), content, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void changeImgProfile(String file)
    {
        showLoading();
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, CHANGEIMG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info = "info";

                try {
                    String encrypt_info = MCrypt.bytesToHex(mCrypt.encrypt(info));
                    loading.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info)));
                        Toasty.success(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                informationImage("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
                        DefaultBootstrapBrand.WARNING);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        smr.addStringParam("username", txt_profile.getText().toString());
        smr.addFile("image", file);

        AppController.getInstance().addToRequestQueue(smr);
    }
}
