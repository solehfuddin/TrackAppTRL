package com.sofudev.trackapptrl.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sofudev.trackapptrl.Form.ShippingInfoActivity;
import com.sofudev.trackapptrl.R;

public class Step2Fragment extends Fragment {

    public Step2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step2, container, false);
        Button buttonNext = view.findViewById(R.id.button_next_fragment_step_2);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShippingInfoActivity.goToStepUlasan();
                Step3Fragment step3Fragment = new Step3Fragment();
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, R.anim.slide_in_from_left, R.anim.slide_out_from_left)
                        .replace(R.id.frame_layout, step3Fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}