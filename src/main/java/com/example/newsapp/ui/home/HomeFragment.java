package com.example.newsapp.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class HomeFragment extends Fragment {

    private View root;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private TextView textView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        if (savedInstanceState == null)
            root.findViewById(R.id.weather_card).setVisibility(View.GONE);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh_items);
        textView = root.findViewById(R.id.fetching);
        progressBar = root.findViewById(R.id.progressBar);
        requestLocationPermission();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                updateNewsList(queue, textView, progressBar);
            }
        });
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    private void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) getActivity());
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            updateWeatherCard(location, queue, textView, progressBar);

        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    private void updateNewsList(RequestQueue queue, final TextView textView, final ProgressBar progressBar) {

        String url = "http://10.0.2.2:5000/guardianlatestnews";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", "onResponse: " + response);
                        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), HORIZONTAL);
                        itemDecor.setOrientation(VERTICAL);
                        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), response, "HOME");
                        recyclerView.addItemDecoration(itemDecor);
                        recyclerView.setAdapter(newsAdapter);
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                Log.e("Volley", "Error" + error);
            }
        });

        queue.add(request);
        root.findViewById(R.id.weather_card).setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void updateWeatherCard(Location location, final RequestQueue queue, final TextView textView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAdminArea();
        Log.d("TAG", "onResume: " + cityName + " " + stateName);

        final TextView temperature = root.findViewById(R.id.text_temp);
        final TextView summary = root.findViewById(R.id.text_summary);
        final ImageView back = root.findViewById(R.id.background);
        TextView state = root.findViewById(R.id.text_state);
        TextView city = root.findViewById(R.id.text_city);

        state.setText(stateName);
        city.setText(cityName);

        String api_key = "b4458e11d6e5b81dd5441fa4250e9879";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + api_key;

        final HashMap<String, Integer> backgrounds = new HashMap<String, Integer>() {{
            put("Clouds", R.drawable.cloudy_weather);
            put("Clear", R.drawable.clear_weather);
            put("Snow", R.drawable.snowy_weather);
            put("Rain", R.drawable.rainy_weather);
            put("Drizzle", R.drawable.rainy_weather);
            put("Thunderstorm", R.drawable.thunder_weather);
        }};

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject0 = response.getJSONObject("main");
                            JSONArray ja = response.getJSONArray("weather");
                            JSONObject jsonObject = ja.getJSONObject(0);
                            String main = jsonObject.getString("main");
                            Integer temp = Math.round(Float.parseFloat(jsonObject0.getString("temp")));
                            temperature.setText(temp + " \u2103");
                            summary.setText(main);
                            if (backgrounds.containsKey(main)) {
                                back.setBackground(getResources().getDrawable(backgrounds.get(main)));
                            } else {
                                back.setBackground(getResources().getDrawable(R.drawable.sunny_weather));
                            }
                            updateNewsList(queue, textView, progressBar);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error" + error);
            }
        });
        queue.add(jsonRequest);
    }

}
