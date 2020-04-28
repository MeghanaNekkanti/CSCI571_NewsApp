package com.example.newsapp.ui.bookmarks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.BookmarkAdapter;
import com.example.newsapp.adapter.NewsAdapter;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.widget.GridLayout.HORIZONTAL;
import static android.widget.GridLayout.VERTICAL;

public class BookmarksFragment extends Fragment {

    RecyclerView recyclerView;
    BookmarkAdapter newsAdapter;
    ArrayList<String> items;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;

        SharedPreferences shref;
        SharedPreferences.Editor editor;
        shref = getContext().getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = shref.getAll();
        JSONArray news = new JSONArray();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                JSONObject object = new JSONObject(entry.getValue().toString());
                news.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("map values", String.valueOf(news));
        }
//        if (news.length() == 0) {
//            root = inflater.inflate(R.layout.empty_bookmark, container, false);
//            textView = root.findViewById(R.id.text_no_bookmark);
//            textView.setText(R.string.no_bookmark);
//        } else {
        root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        itemDecor.setOrientation(VERTICAL);
        recyclerView = root.findViewById(R.id.recyclerViewBookmark);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                Log.d("TAG", "getSpanSize: " + newsAdapter.getItemViewType(position));
                switch (newsAdapter.getItemViewType(position)) {
                    case 0:
                        return 2;
                    case 1:
                        return 1;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(itemDecor);
        newsAdapter = new BookmarkAdapter(getActivity(), news);
        recyclerView.setAdapter(newsAdapter);
        return root;
    }
}
