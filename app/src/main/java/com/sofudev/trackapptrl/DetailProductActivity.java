package com.sofudev.trackapptrl;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sofudev.trackapptrl.Custom.OnBadgeCounter;
import com.sofudev.trackapptrl.Fragment.DetailFrameFragment;

import es.dmoral.toasty.Toasty;

public class DetailProductActivity extends AppCompatActivity implements OnBadgeCounter {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        getData();

        DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", "1");
        bundle.putString("product_id", String.valueOf(id));
        detailFrameFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.detailproduct_fragment_container, detailFrameFragment);
        fragmentTransaction.commit();
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
    }

    @Override
    public void countWishlist(int counter) {

    }

    @Override
    public void countCartlist(int counter) {

    }
}
