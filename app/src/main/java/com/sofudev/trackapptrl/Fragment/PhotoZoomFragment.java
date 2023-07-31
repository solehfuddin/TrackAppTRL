package com.sofudev.trackapptrl.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class PhotoZoomFragment extends Fragment {

    ImageViewZoom imageViewZoom;
    String IMGURL;

    public PhotoZoomFragment() {
        // Required empty public constructor
    }

    private void getData()
    {
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            IMGURL = bundle.getString("imgname");

            assert IMGURL != null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_zoom, container, false);

        imageViewZoom = view.findViewById(R.id.fragment_photo_image);

//        if (!IMGURL.isEmpty())
//        {
//            Picasso.with(getContext()).load(IMGURL).centerCrop().fit().into(imageViewZoom);
//        }
//        else
//        {
//            imageViewZoom.setImageResource(R.drawable.pic_holder);
//        }

        return view;
    }
}