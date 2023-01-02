package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sofudev.trackapptrl.Form.CourierHistoryActivity;
import com.sofudev.trackapptrl.Form.CourierTaskActivity;
import com.sofudev.trackapptrl.Form.FormOrderSpActivity;
import com.sofudev.trackapptrl.R;

public class CourierFragment extends Fragment {

    LinearLayout linearCourierTask, linearCourierHistory;
    Context myContext;
    String ACTIVITY_TAG, PARTYSITEID, USERNAME, CUSTNAME, STATUS, LEVEL, ADDRESS, CITY, PROVINCE, EMAIL, MOBNUMBER, MEMBERFLAG;

    public CourierFragment() {
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
        View view = inflater.inflate(R.layout.fragment_courier, container, false);
        linearCourierTask = view.findViewById(R.id.layout_courier_task);
        linearCourierHistory = view.findViewById(R.id.layout_courier_history);

        getData();

        linearCourierTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContext, CourierTaskActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                startActivity(intent);
            }
        });

        linearCourierHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContext, CourierHistoryActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getData()
    {
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            ACTIVITY_TAG = bundle.getString("activity");

            assert ACTIVITY_TAG != null;
            if (!ACTIVITY_TAG.equals("main"))
            {
                PARTYSITEID = bundle.getString("partySiteId");
                CUSTNAME = bundle.getString("username");

                Log.d(CourierFragment.class.getSimpleName(), "custname : " + CUSTNAME);
                Log.d(CourierFragment.class.getSimpleName(), "partyId : " + PARTYSITEID);
//                getUserByCustname(CUSTNAME);
//                countData(CUSTNAME);
//                getParentInfo(PARTYSITEID);
            }
        }
    }
}