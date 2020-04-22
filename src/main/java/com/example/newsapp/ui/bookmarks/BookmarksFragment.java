package com.example.newsapp.ui.bookmarks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;

import java.util.ArrayList;

import static android.widget.GridLayout.HORIZONTAL;
import static android.widget.GridLayout.VERTICAL;

public class BookmarksFragment extends Fragment {

    RecyclerView recyclerView;
    NewsAdapter newsAdapterInit;
    ArrayList<String> items;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;
        items = new ArrayList<>();
        items.add("First");
        items.add("Second");
        items.add("Third");
        items.add("Four");
        items.add("Five");
//        items.add("Six");
        if (items.size() == 0) {
            root = inflater.inflate(R.layout.empty_bookmark, container, false);
            textView = root.findViewById(R.id.text_no_bookmark);
            textView.setText(R.string.no_bookmark);
        } else {
            root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
            DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
            itemDecor.setOrientation(VERTICAL);
//            recyclerView = root.findViewById(R.id.recyclerViewBookmark);
//            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//            recyclerView.addItemDecoration(itemDecor);
//            newsAdapterInit = new NewsAdapter_Init(getActivity(), items, "Bookmark");
//            recyclerView.setAdapter(newsAdapterInit);
        }
        return root;
    }
}
