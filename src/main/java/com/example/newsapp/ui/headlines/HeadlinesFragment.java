package com.example.newsapp.ui.headlines;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;

import com.example.newsapp.R;
import com.google.android.material.tabs.TabLayout;

public class HeadlinesFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_headlines, container, false);

        tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("World"));
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        tabLayout.addTab(tabLayout.newTab().setText("Politics"));
        tabLayout.addTab(tabLayout.newTab().setText("Sports"));
        tabLayout.addTab(tabLayout.newTab().setText("Technology"));
        tabLayout.addTab(tabLayout.newTab().setText("Science"));

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TabFragment(0)).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new TabFragment(position)).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("TAG", "onTabReselected: " + tab.getPosition());
            }
        });

        return root;
    }
}
