package com.sofudev.trackapptrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sofudev.trackapptrl.Fragment.NewFrameFragment;

public class FrameProductActivity extends AppCompatActivity {

    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_product);

        imgBack = findViewById(R.id.frame_product_btnback);

        backPage();
        setFragment();
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

    private void setFragment()
    {
        NewFrameFragment newFrameFragment = new NewFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("activity", "dashboard");
        newFrameFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_product_framelayout, newFrameFragment, "detail");
        fragmentTransaction.commit();
    }
}