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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.MainActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<String> items;
    View root;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    protected LocationManager locationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("TAG", "onCreateView: " + "loc");
        if (savedInstanceState == null)
            root.findViewById(R.id.weather_card).setVisibility(View.GONE);
        requestLocationPermission();
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
            Log.d("TAG", "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Log.d("TAG", "onResume: " + addresses);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
//            String countryName = addresses.get(0).getAddressLine(2);
            Log.d("TAG", "onResume: " + cityName + " " + stateName);

            TextView state = root.findViewById(R.id.text_state);
            TextView city = root.findViewById(R.id.text_city);
            state.setText(stateName);
            city.setText(cityName);
            final TextView temperature = root.findViewById(R.id.text_temp);
            final TextView summary = root.findViewById(R.id.text_summary);
            final ImageView back = root.findViewById(R.id.background);

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String api_key = "b4458e11d6e5b81dd5441fa4250e9879";
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + api_key;

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Display the first 500 characters of the response string.
                            try {
                                JSONObject jsonObject0 = response.getJSONObject("main");
                                JSONArray ja = response.getJSONArray("weather");
                                JSONObject jsonObject = ja.getJSONObject(0);
                                String main = jsonObject.getString("main");
                                Integer temp = Math.round(Float.parseFloat(jsonObject0.getString("temp")));
                                temperature.setText(temp + " \u2103");
                                summary.setText(main);
                                switch (main){
                                    case "Clouds":
                                        back.setBackground(getResources().getDrawable(R.drawable.cloudy_weather));
                                        break;
                                    case "Clear":
                                        back.setBackground(getResources().getDrawable(R.drawable.clear_weather));
                                        break;
                                    case "Snow":
                                        back.setBackground(getResources().getDrawable(R.drawable.snowy_weather));
                                        break;
                                    case "Rain":
                                    case "Drizzle":
                                        back.setBackground(getResources().getDrawable(R.drawable.rainy_weather));
                                        break;
                                    case "Thunderstorm":
                                        back.setBackground(getResources().getDrawable(R.drawable.thunder_weather));
                                        break;
                                    default:
                                        back.setBackground(getResources().getDrawable(R.drawable.sunny_weather));
                                        break;
                                }

                                Log.d("TAG", "onResponse: " + response);
                                Log.d("TAG", "onResponse: " + main + temp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            textView.setText("Response is: " + response.substring(0, 500));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley", "Error");

                }
            });

            queue.add(jsonRequest);
            DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), HORIZONTAL);
            itemDecor.setOrientation(VERTICAL);
//        Log.d("TAG", "onCreateView: " + "loc2");
            items = new ArrayList<>();
            items.add("First");
            items.add("Second");
            items.add("Third");
            items.add("Four");
            items.add("Five");
            items.add("Six");
            recyclerView = root.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            newsAdapter = new NewsAdapter(getActivity(), items, "Home");
//        Log.d("TAG", "onCreateView: " + "loc");
            recyclerView.addItemDecoration(itemDecor);
            recyclerView.setAdapter(newsAdapter);

            root.findViewById(R.id.weather_card).setVisibility(View.VISIBLE);
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }
//
//    public void setMyView(Double latitude, Double longitude, MainActivity mainActivity, View view) {
////        MainActivity mainActivity = (MainActivity) getActivity();
////        Double longitude = mainActivity.getLongitude();
////        Double latitude = mainActivity.getLatitude();
//        Log.d("TAG", "onResume: " + longitude + latitude + " " + context);
//        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            Log.d("TAG", "onResume: " + addresses);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String cityName = addresses.get(0).getLocality();
//        String stateName = addresses.get(0).getAdminArea();
////            String countryName = addresses.get(0).getAddressLine(2);
//        Log.d("TAG", "onResume: " + cityName + " " + stateName);
//
//        Log.d("TAG", "onResume: ");
//
//        DividerItemDecoration itemDecor = new DividerItemDecoration(mainActivity, HORIZONTAL);
//        itemDecor.setOrientation(VERTICAL);
////        Log.d("TAG", "onCreateView: " + "loc2");
//        items = new ArrayList<>();
//        items.add("First");
//        items.add("Second");
//        items.add("Third");
//        items.add("Four");
//        items.add("Five");
//        items.add("Six");
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
//        newsAdapter = new NewsAdapter(mainActivity, items, "Home");
////        Log.d("TAG", "onCreateView: " + "loc");
//        recyclerView.addItemDecoration(itemDecor);
//        recyclerView.setAdapter(newsAdapter);
//        TextView textView = view.findViewById(R.id.text_state);
//        textView.setText("STATE");
//        view.findViewById(R.id.weather_card).setVisibility(View.VISIBLE);
//
//    }
}
