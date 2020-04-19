package com.example.newsapp.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class TabFragment extends Fragment {

    int position;
    private TextView textView;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<String> items;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        items.add("First");
        items.add("Second");
        items.add("Third");
        items.add("Four");
        items.add("Five");
        items.add("Six");
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tab, container, false);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        itemDecor.setOrientation(VERTICAL);
        recyclerView = root.findViewById(R.id.recyclerViewTab);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(itemDecor);
        newsAdapter = new NewsAdapter(getActivity(), items, "Headlines");
        recyclerView.setAdapter(newsAdapter);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        textView = view.findViewById(R.id.textView);
//
//        textView.setText("Fragment " + (position + 1));

    }
}
