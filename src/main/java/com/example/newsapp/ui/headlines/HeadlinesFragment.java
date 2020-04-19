package com.example.newsapp.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;

import com.example.newsapp.R;
import com.example.newsapp.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class HeadlinesFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_headlines, container, false);
        viewPager = root.findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }
}
