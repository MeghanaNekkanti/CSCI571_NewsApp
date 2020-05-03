package com.example.newsapp.ui.headlines;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;

import org.json.JSONArray;
import java.util.Arrays;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class TabFragment extends Fragment {

    int position;
    View root;
    private TextView textView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    List<String> sections = Arrays.asList("world", "business", "politics", "sport", "technology", "science");

    public TabFragment(int position) {
        this.position = position;
        Log.d("TAG", "TabFragment: " + position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_tab, container, false);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh_items);
        textView = root.findViewById(R.id.fetching);
        progressBar = root.findViewById(R.id.progressBarTab);
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Log.d("TAG", "onCreateView: position " + position + " " + sections.get(position));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TAG", "onRefresh: "+position);
                displaySectionNews(sections.get(position), root, getActivity());
            }
        });
        displaySectionNews(sections.get(position), root, getActivity());
        return root;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "onViewCreated: " + position);
    }

    private void displaySectionNews(final String section, final View view, final FragmentActivity activity) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        String url = "http://10.0.2.2:5000/guardiansection?section=" + section;
        Log.d("TAG", "displaySectionNews: " + section);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d("TAG", "onResponse: " + response);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(activity, HORIZONTAL);
                        itemDecor.setOrientation(VERTICAL);
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTab);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        NewsAdapter newsAdapter = new NewsAdapter(activity, response, "TAB");
                        recyclerView.addItemDecoration(itemDecor);
                        recyclerView.setAdapter(newsAdapter);
//                        newsAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                Log.e("Volley", "Error" + error);
            }
        });
        requestQueue.add(request);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
