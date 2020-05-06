package com.example.newsapp;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.adapter.NewsAdapter;
import com.example.newsapp.model.NewsClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MainActivity extends AppCompatActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myChildToolbar =
                findViewById(R.id.main_toolbar);
        setSupportActionBar(myChildToolbar);

        handleIntent(getIntent());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_headlines, R.id.navigation_trending, R.id.navigation_bookmarks)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v("App", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.d("TAG", query);
            Intent intent1 = new Intent(this, SearchResultsActivity.class);
            intent1.putExtra("query", query);
            startActivity(intent1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(1150);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("TAG", "onQueryTextSubmit: " + query);
//                Intent intent = new Intent(MainActivity.this, SearchableActivity.class);
//                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TAG", "onQueryTextChange: " + newText);
                if (newText.length() >= 3) {
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String url = "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?q=" + newText;

                    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", "onResponse: " + response);
                                    String[] dataArr = new String[5];
                                    try {
                                        JSONArray array = response.getJSONArray("suggestionGroups").getJSONObject(0).getJSONArray("searchSuggestions");
                                        for (int i = 0; i < array.length(); i++) {
                                            if (i > 4)
                                                break;
                                            dataArr[i] = array.getJSONObject(i).getString("displayText");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
                                    searchAutoComplete.setAdapter(newsAdapter);
                                    newsAdapter.notifyDataSetChanged();

                                    searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                                            String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                                            searchAutoComplete.setText(queryString);
//                                            Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("Volley", "Error" + error);
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<>();
                            params.put("Ocp-Apim-Subscription-Key", "81f39fa946cc4ff18c6db8021bfad75f");
                            return params;
                        }
                    };
                    queue.add(request);
                    return true;
                } else {
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line);
                    searchAutoComplete.setAdapter(newsAdapter);
                    newsAdapter.notifyDataSetChanged();
                    return false;
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Log.d("TAG", "onOptionsItemSelected: ");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }
}



