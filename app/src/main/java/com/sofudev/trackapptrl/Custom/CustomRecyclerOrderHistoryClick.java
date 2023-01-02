package com.sofudev.trackapptrl.Custom;

import android.view.View;

/**
 * Created by sholeh on 1/3/2019.
 */

public interface CustomRecyclerOrderHistoryClick {
    void onItemClick(View view, int pos, String id, String status, String info, String paymentType);
}
