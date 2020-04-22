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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class TabFragment extends Fragment {

    int position;
    View root;

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
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "onViewCreated: " + position);
        switch (position) {
            case 0:
                displaySectionNews("world", view, getActivity());
                break;
            case 1:
                displaySectionNews("business", view, getActivity());
                break;
            case 2:
                displaySectionNews("politics", view, getActivity());
                break;
            case 3:
                displaySectionNews("sport", view, getActivity());
                break;
            case 4:
                displaySectionNews("technology", view, getActivity());
                break;
            case 5:
                displaySectionNews("science", view, getActivity());
                break;

        }
    }

    private void displaySectionNews(final String section, final View view, final FragmentActivity activity) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        final ProgressBar progressBar = view.findViewById(R.id.progressBarTab);
        final TextView textView = view.findViewById(R.id.fetching);
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = "http://10.0.2.2:5000/guardiansection?section=" + section;
        Log.d("TAG", "displaySectionNews: " + section);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d("TAG", "onResponse: " + response);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
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
    }
}
