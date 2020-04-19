package com.example.newsapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<String> items;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        items = new ArrayList<>();
        items.add("First");
        items.add("Second");
        items.add("Third");
        items.add("Four");
        items.add("Five");
        items.add("Six");

        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        itemDecor.setOrientation(VERTICAL);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsAdapter = new NewsAdapter(getActivity(), items, "Home");
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(newsAdapter);
//        final TextView textView = root.findViewById(R.id.text_state);
//
//        textView.setText("s");
//
        return root;
    }
}
