package com.sofudev.trackapptrl.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;
public class CustomHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "imgSlider";

    private String imageUrls;

    public CustomHomeFragment() {
        // Required empty public constructor
    }

    public static CustomHomeFragment newInstance(String params) {
        CustomHomeFragment fragment = new CustomHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            imageUrls = getArguments().getString(ARG_PARAM1);
        }
        View view = inflater.inflate(R.layout.fragment_custom_home, container, false);
        ImageView img = view.findViewById(R.id.fragment_customhome_imageView);

        Picasso.with(getActivity())
                .load(imageUrls)
                .placeholder(R.drawable.bumperbanner)
                .fit()
                .into(img);
        return view;
    }
}