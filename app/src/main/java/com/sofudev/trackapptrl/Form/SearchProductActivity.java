package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_recent_view;
import com.sofudev.trackapptrl.Adapter.Adapter_search_product;
import com.sofudev.trackapptrl.Adapter.Adapter_searchnow;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_recent_view;
import com.sofudev.trackapptrl.Data.Data_search_product;
import com.sofudev.trackapptrl.Data.Data_searchnow;
import com.sofudev.trackapptrl.DetailProductActivity;
import com.sofudev.trackapptrl.LocalDb.Db.RecentSearchHelper;
import com.sofudev.trackapptrl.LocalDb.Db.RecentViewHelper;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import viethoa.com.snackbar.BottomSnackBarMessage;

public class SearchProductActivity extends AppCompatActivity implements View.OnClickListener {

    Config config = new Config();
    String URLSEARCH = config.Ip_address + config.dashboard_search_product;
    String URLSEARCHBYTITLE = config.Ip_address + config.dashboard_search_producttitle;

    LinearLayout linearBefore, linearAfter, linearRecentSearch;
    RippleView btnBack, btnDeleteText;
    BootstrapEditText txtSearch;
    RecyclerView recyRecentSearch, recyRecentView, recyOutputSearch;
    UniversalFontTextView deleteRecentSearch, deleteRecentView;

    RecentSearchHelper recentSearchHelper;
    RecentViewHelper recentViewHelper;
    BottomSnackBarMessage bottomSnackBar;

    Adapter_search_product adapter_search_product;
    Adapter_recent_view adapter_recent_view;
    Adapter_searchnow adapter_searchnow;
    List<Data_search_product> dataSearchList = new ArrayList<>();
    List<Data_recent_view> dataRecentViews = new ArrayList<>();
    List<Data_searchnow> dataSearchnows = new ArrayList<>();

    private int id1, id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        linearBefore = findViewById(R.id.search_product_containerBefore);
        linearAfter  = findViewById(R.id.search_product_containerAfter);
        linearRecentSearch = findViewById(R.id.search_product_layout_recentSearch);
        btnBack =  findViewById(R.id.search_product_ripplebtnback);
        btnDeleteText = findViewById(R.id.search_product_ripplebtndelete);
        txtSearch = findViewById(R.id.search_product_txtsearch);
        recyRecentSearch = findViewById(R.id.search_product_recyRecentSearch);
        recyRecentView   = findViewById(R.id.search_product_recyRecentView);
        recyOutputSearch = findViewById(R.id.search_product_recyOutputSearch);
        deleteRecentSearch = findViewById(R.id.search_product_deleteSearch);
        deleteRecentView = findViewById(R.id.search_product_deleteView);
        bottomSnackBar = new BottomSnackBarMessage(this);

        btnBack.setOnClickListener(this);
        btnDeleteText.setOnClickListener(this);
        deleteRecentSearch.setOnClickListener(this);
        deleteRecentView.setOnClickListener(this);

        LinearLayoutManager layoutManagerSearch = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyRecentSearch.setLayoutManager(layoutManagerSearch);

        LinearLayoutManager layoutManagerSearchNow = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyOutputSearch.setLayoutManager(layoutManagerSearchNow);

        RecyclerView.LayoutManager verticalgridLayout = new GridLayoutManager(this, 4);
        recyRecentView.setLayoutManager(verticalgridLayout);
        recyRecentView.addItemDecoration(new EqualSpacingItemDecoration(16));
        recyRecentView.setItemAnimator(new DefaultItemAnimator());

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                {
                    linearAfter.setVisibility(View.VISIBLE);
                    linearBefore.setVisibility(View.GONE);
                    btnDeleteText.setVisibility(View.VISIBLE);

                    getSearchNow(txtSearch.getText().toString());
                }
                else
                {
                    linearBefore.setVisibility(View.VISIBLE);
                    linearAfter.setVisibility(View.GONE);
                    btnDeleteText.setVisibility(View.GONE);

                    getRecentSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String title = txtSearch.getText().toString();
                    int check = recentSearchHelper.getIsDuplicate(title);

                    searchByTitle(title);

                    if (id1 > 0)
                    {
                        Intent intent = new Intent(SearchProductActivity.this, DetailProductActivity.class);
                        intent.putExtra("id", id1);
                        startActivity(intent);

                        if (check > 0)
                        {
                            Data_search_product item = new Data_search_product();
                            item.setSearchId(id1);
                            item.setSearchTitle(title);

                            insertRecentSearch(item);
                        }

                        id1 = 0;
                    }
                    else if (id2 > 0) {
                        Intent intent = new Intent(SearchProductActivity.this, DetailProductActivity.class);
                        intent.putExtra("id", id2);
                        startActivity(intent);

                        if (check > 0)
                        {
                            Data_search_product item = new Data_search_product();
                            item.setSearchId(id2);
                            item.setSearchTitle(title);

                            insertRecentSearch(item);
                        }

                        id2 = 0;
//                        int id = recentSearchHelper.countRecentSearch() + 1;
                    }
                    else
                    {
                        bottomSnackBar.showWarningMessage("Product Not found");
                    }

                    return true;
                }
                return false;
            }
        });

        recentSearchHelper = RecentSearchHelper.getINSTANCE(this);
        recentSearchHelper.open();

        recentViewHelper = RecentViewHelper.getINSTANCE(this);
        recentViewHelper.open();

        getRecentSearch();
        getRecentView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecentView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.search_product_ripplebtnback:
                finish();
                break;

            case R.id.search_product_ripplebtndelete:
                txtSearch.getText().clear();
                id1 = 0;
                id2 = 0;

                linearRecentSearch.setVisibility(View.VISIBLE);
                recyRecentSearch.setVisibility(View.VISIBLE);

                break;
            case R.id.search_product_deleteSearch:
                int output = recentSearchHelper.truncAllRecentSearch();

                if (output > 0)
                {
                    linearRecentSearch.setVisibility(View.VISIBLE);
                    recyRecentSearch.setVisibility(View.VISIBLE);

                    dataSearchList.clear();
                    adapter_search_product.notifyDataSetChanged();

                    bottomSnackBar.showErrorMessage("All recent search has been deleted");
                }

                break;

            case R.id.search_product_deleteView:
                int output1 = recentViewHelper.truncAllRecentView();

                if (output1 > 0)
                {
                    dataRecentViews.clear();
                    adapter_recent_view.notifyDataSetChanged();

                    bottomSnackBar.showErrorMessage("All recent view has been deleted");
                }

                break;
        }
    }

    private void searchByTitle(final String key)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSEARCHBYTITLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    id2 = jsonObject.getInt("item_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("keyword", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void insertRecentSearch(Data_search_product item)
    {
        recentSearchHelper.insertRecentSearch(item);
    }

    private void getRecentSearch()
    {
        final int counter = recentSearchHelper.countRecentSearch();

        if (counter > 0)
        {
            dataSearchList = recentSearchHelper.getAllRecentSearch();

            //Log.d("Ini si ", dataSearchList.get(0).getSearchTitle());

            adapter_search_product = new Adapter_search_product(this, dataSearchList, new RecyclerViewOnClickListener() {
                @Override
                public void onItemClick(View view, int pos, String id) {
                    int position = view.getId();

                    switch (position)
                    {
                        case R.id.item_search_cardview:

//                            Toasty.info(getApplicationContext(), "Link to product search", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SearchProductActivity.this, DetailProductActivity.class);
                            intent.putExtra("id", dataSearchList.get(pos).getSearchId());
                            startActivity(intent);

                            break;

                        case R.id.item_search_btndelete:
                            int count = recentSearchHelper.deleteRecentSearch(dataSearchList.get(pos).getSearchId());

                            if (count > 0)
                            {
                                dataSearchList.remove(pos);
                                adapter_search_product.notifyItemRemoved(pos);
                            }

                            break;
                    }
                }
            });

            recyRecentSearch.setAdapter(adapter_search_product);
        }
        else
        {
            linearRecentSearch.setVisibility(View.GONE);
        }
    }

    private void getRecentView()
    {
//        dataRecentViews.add(new Data_recent_view(1, "http://www.timurrayalab.com/dev/uploads/img/thumb/MOE-8248-C4-54_thumb.jpg"));
//        dataRecentViews.add(new Data_recent_view(2, "http://www.timurrayalab.com/dev/uploads/img/thumb/MOE-8219-C5-51_thumb.jpg"));
//        dataRecentViews.add(new Data_recent_view(3, "http://www.timurrayalab.com/dev/uploads/img/thumb/BORELLI-T8807-C1-53_thumb.jpg"));
//        dataRecentViews.add(new Data_recent_view(4, "http://www.timurrayalab.com/dev/uploads/img/thumb/BORELLI-T8808-C1-53_thumb.jpg"));
//        dataRecentViews.add(new Data_recent_view(5, "http://www.timurrayalab.com/dev/uploads/img/thumb/MOE-8248-C4-54_thumb.jpg"));
//        dataRecentViews.add(new Data_recent_view(6, "http://www.timurrayalab.com/dev/uploads/img/thumb/MOE-8219-C5-51_thumb.jpg"));
//        dataRecentViews.add(new Data_recent_view(7, "http://www.timurrayalab.com/dev/uploads/img/thumb/BORELLI-T8807-C1-53_thumb.jpg"));
//        dataRecentViews.add(new Data_recent_view(8, "http://www.timurrayalab.com/dev/uploads/img/thumb/BORELLI-T8808-C1-53_thumb.jpg"));

        int counter = recentViewHelper.countRecentView();

        if (counter > 0)
        {
            dataRecentViews = recentViewHelper.getAllRecentView();

            //Log.d("Ini si ", dataRecentViews.get(0).getImage());

            adapter_recent_view = new Adapter_recent_view(this, dataRecentViews, new RecyclerViewOnClickListener() {
                @Override
                public void onItemClick(View view, int pos, String id) {
                    int position = view.getId();

                    switch (position){
                        case R.id.item_recentview_delete:

                            int count = recentViewHelper.deleteRecentView(dataRecentViews.get(pos).getId());

                            if (count > 0)
                            {
                                dataRecentViews.remove(pos);
                                adapter_recent_view.notifyItemRemoved(pos);
                            }

                            break;

                        case R.id.item_recentview_image:

//                            DetailFrameFragment detailFrameFragment = new DetailFrameFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("product_id", String.valueOf(dataRecentViews.get(pos).getId()));
//                            detailFrameFragment.setArguments(bundle);
//
////                            getActivity().getSupportFragmentManager().beginTransaction()
////                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
////                                    .addToBackStack(null)
////                                    .commit();
//
//                            getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.appbarmain_fragment_container, detailFrameFragment)
//                                    .addToBackStack(null)
//                                    .commit();
//
//                            finish();

                            Intent intent = new Intent(SearchProductActivity.this, DetailProductActivity.class);
                            intent.putExtra("id", dataRecentViews.get(pos).getId());
                            startActivity(intent);

                            break;
                    }
                }
            });

            recyRecentView.setAdapter(adapter_recent_view);
        }
    }

    private void getSearchNow(final String key)
    {
//        dataSearchnows.add(0, new Data_searchnow(0, "MOE-8248-C4-54"));
//        dataSearchnows.add(1, new Data_searchnow(1, "BORELLI-T8808-C2-53"));
//        dataSearchnows.add(2, new Data_searchnow(2, "FUSION-8472-C14"));
//        dataSearchnows.add(3, new Data_searchnow(3, "NC1009 011-BLKSLV"));
//        dataSearchnows.add(4, new Data_searchnow(4, "LNZ-U-ST-9220-COL1-BRN"));

        adapter_searchnow = new Adapter_searchnow(this, dataSearchnows, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                int position = view.getId();

                switch (position){
                    case R.id.item_searchnow_btnselect:
                        txtSearch.setText(dataSearchnows.get(pos).getKeyword());
                        id1 = Integer.valueOf(id);
                        break;

                    case R.id.item_searchnow_layout:
                        String title = dataSearchnows.get(pos).getKeyword();
                        id1 = dataSearchnows.get(pos).getId();

                        Intent intent = new Intent(SearchProductActivity.this, DetailProductActivity.class);
                        intent.putExtra("id", id1);
                        startActivity(intent);

                        int check = recentSearchHelper.getIsDuplicate(title);
//
                        if (check > 0)
                        {
                            Data_search_product item = new Data_search_product();
                            item.setSearchId(id1);
                            item.setSearchTitle(title);

                            insertRecentSearch(item);
                        }

                        txtSearch.setText(title);

                        break;
                }
            }
        });
        dataSearchnows.clear();
        recyOutputSearch.removeAllViews();

        StringRequest request = new StringRequest(Request.Method.POST, URLSEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("item_id");
                        String desc = jsonObject.getString("description");

                        Data_searchnow item = new Data_searchnow();
                        item.setId(id);
                        item.setKeyword(desc);

                        dataSearchnows.add(item);
                    }

                    recyOutputSearch.setAdapter(adapter_searchnow);
                    adapter_searchnow.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("keyword", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    public class EqualSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;
        private int displayMode;

        public static final int HORIZONTAL = 0;
        public static final int VERTICAL = 1;
        public static final int GRID = 2;

        public EqualSpacingItemDecoration(int spacing) {
            this(spacing, -1);
        }

        public EqualSpacingItemDecoration(int spacing, int displayMode) {
            this.spacing = spacing;
            this.displayMode = displayMode;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildViewHolder(view).getAdapterPosition();
            int itemCount = state.getItemCount();
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            setSpacingForDirection(outRect, layoutManager, position, itemCount);
        }

        private void setSpacingForDirection(Rect outRect,
                                            RecyclerView.LayoutManager layoutManager,
                                            int position,
                                            int itemCount) {

            // Resolve display mode automatically
            if (displayMode == -1) {
                displayMode = resolveDisplayMode(layoutManager);
            }

            switch (displayMode) {
                case HORIZONTAL:
                    outRect.left = spacing;
                    outRect.right = position == itemCount - 1 ? spacing : 0;
                    outRect.top = spacing;
                    outRect.bottom = spacing;
                    break;
                case VERTICAL:
                    outRect.left = spacing;
                    outRect.right = spacing;
                    outRect.top = spacing;
                    outRect.bottom = position == itemCount - 1 ? spacing : 0;
                    break;
                case GRID:
                    if (layoutManager instanceof GridLayoutManager) {
                        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                        int cols = gridLayoutManager.getSpanCount();
                        int rows = itemCount / cols;

                        outRect.left = spacing;
                        outRect.right = position % cols == cols - 1 ? spacing : 0;
                        outRect.top = spacing;
                        outRect.bottom = position / cols == rows - 1 ? spacing : 0;
                    }
                    break;
            }
        }

        private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
            if (layoutManager instanceof GridLayoutManager) return GRID;
            if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
            return VERTICAL;
        }
    }
}
