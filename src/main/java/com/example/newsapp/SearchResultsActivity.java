package com.example.newsapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.adapter.NewsAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class SearchResultsActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Toolbar myChildToolbar =
                findViewById(R.id.search_toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        final String query = getIntent().getStringExtra("query");
        ab.setTitle("Search Results for " + query);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSearchResults(SearchResultsActivity.this, query);
            }
        });

        getSearchResults(this, query);
        Log.d("TAG", "onCreate: Search" + getIntent().getStringExtra("query"));
    }

    private void getSearchResults(final SearchResultsActivity searchResultsActivity, String query) {

        RequestQueue queue = Volley.newRequestQueue(this);

        final TextView fetch;
        final ProgressBar progressBar;

        fetch = findViewById(R.id.fetching);
        progressBar = findViewById(R.id.progressBarSearch);

        fetch.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://api-dot-news-app-android-nodejs.ue.r.appspot.com/guardiansearch?q=" + query;
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", "onResponse: " + response);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(searchResultsActivity, DividerItemDecoration.VERTICAL);
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
                        recyclerView.setLayoutManager(new LinearLayoutManager(searchResultsActivity));
                        newsAdapter = new NewsAdapter(searchResultsActivity, response, "SEARCH");
                        recyclerView.addItemDecoration(itemDecor);
                        recyclerView.setAdapter(newsAdapter);
                        progressBar.setVisibility(View.GONE);
                        fetch.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                fetch.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                Log.e("Volley", "Error" + error);
            }
        });
        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (newsAdapter != null)
            newsAdapter.notifyDataSetChanged();
    }
}
