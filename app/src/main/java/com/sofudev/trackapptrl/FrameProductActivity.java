package com.sofudev.trackapptrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Fragment.NewFrameFragment;

public class FrameProductActivity extends AppCompatActivity {

    UniversalFontTextView txtTitle;
    ImageView imgBack;

    String ACTIVITY_TAG, TAG_NAME, TAG_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        UniversalFontComponents.init(this);
        setContentView(R.layout.activity_frame_product);

        imgBack = findViewById(R.id.frame_product_btnback);
        txtTitle = findViewById(R.id.frame_product_txtTitle);

        getData();
        backPage();
    }

    private void backPage()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            ACTIVITY_TAG = bundle.getString("activity");
            TAG_ID       = bundle.getString("tag_id");
            TAG_NAME     = bundle.getString("tag_name");

            Log.d(FrameProductActivity.class.getSimpleName(), "TAG Product : " + ACTIVITY_TAG);
            Log.d(FrameProductActivity.class.getSimpleName(), "TAG Name : " + TAG_NAME);
            Log.d(FrameProductActivity.class.getSimpleName(), "TAG Id : " + TAG_ID);

            txtTitle.setText(TAG_NAME);

            setFragment();
        }
    }

    private void setFragment()
    {
        NewFrameFragment newFrameFragment = new NewFrameFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("activity", "dashboard");
        bundle.putString("activity", ACTIVITY_TAG);
        bundle.putString("access", "dashboard");
        bundle.putString("tag_name", TAG_NAME);
        bundle.putString("tag_id", TAG_ID);
        newFrameFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_product_framelayout, newFrameFragment, "detail");
        fragmentTransaction.commit();
    }
}