package com.sofudev.trackapptrl;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class ZoomImageActivity extends AppCompatActivity {
    UniversalFontTextView txtDescription;
    ImageViewZoom zoom;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        UniversalFontComponents.init(getApplicationContext());

        zoom = findViewById(R.id.zoom_image);
        btnBack = findViewById(R.id.zoom_btnBack);
        txtDescription = findViewById(R.id.zoom_txtDescription);

        getData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            zoom.setVisibility(View.VISIBLE);
            zoom.setEnabled(true);
            zoom.setImageBitmap(bitmap);
            zoom.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            zoom.setEnabled(false);
            zoom.setImageResource(R.drawable.progress_zoom);
        }
    };

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            String imgUrl = bundle.getString("image_url");
            String description = bundle.getString("frame_description");

            Picasso.with(getApplicationContext())
                    .load(imgUrl)
                    .placeholder(R.drawable.progress_zoom)
                    .error(R.drawable.pic_holder)
//                    .fit()
                    .into(target);
//                    .into(zoom, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            zoom.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });

            txtDescription.setText(description);
        }
    }
}