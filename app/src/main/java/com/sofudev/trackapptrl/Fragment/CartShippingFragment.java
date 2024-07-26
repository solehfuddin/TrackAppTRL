package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_shipping_type;
import com.sofudev.trackapptrl.Form.ShippingInfoActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import jrizani.jrspinner.JRSpinner;

public class CartShippingFragment extends Fragment {
    Config config = new Config();
    String URLGETTYPESHIPPING = config.Ip_address + config.shipping_getType;

    JRSpinner spinCity;

    ArrayList<Data_shipping_type> listTypeShipping = new ArrayList<>();
    Context myContext;

    public CartShippingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping_cart, container, false);

        RippleView buttonNext = view.findViewById(R.id.fragment_shippingcart_rippleNext);
        spinCity              = view.findViewById(R.id.fragment_shippingcart_spinexpedition);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShippingInfoActivity.goToStepOrangTua();
                Step2Fragment step2Fragment = new Step2Fragment();
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, R.anim.slide_in_from_left, R.anim.slide_out_from_left)
                        .replace(R.id.frame_layout, step2Fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        getShippingType();

        return view;
    }

    private void getShippingType() {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETTYPESHIPPING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (Objects.requireNonNull(jsonObject.names()).get(0).equals("error"))
                        {
                            Toasty.warning(myContext, "Data not found", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Data_shipping_type item = new Data_shipping_type();
                            item.setIdShipping(jsonObject.getInt("idShipping"));
                            item.setKurir(jsonObject.getString("kurir"));
                            item.setIcon(jsonObject.getString("icon"));
                            item.setRajaLabel(jsonObject.getString("rajaLabel"));

                            listTypeShipping.add(item);
                        }
                    }

                    bindingShippingType();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ShippingInfoActivity.class.getSimpleName(), error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void bindingShippingType() {
        String[] sampleArray = new String[listTypeShipping.size()];

        for (int i = 0; i < listTypeShipping.size(); i++)
        {
            sampleArray[i] = listTypeShipping.get(i).getKurir();
        }

        spinCity.setItems(sampleArray);
        spinCity.setTitle("Select expedition");
        spinCity.setMultiple(false);

        spinCity.setOnItemClickListener(new JRSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Toasty.info(myContext, listTypeShipping.get(pos).getRajaLabel(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}