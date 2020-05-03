package com.example.newsapp.ui.trending;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrendingFragment extends Fragment {

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList<Entry> lineEntries;

    private String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    private String value;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_trending, container, false);
        setValue("CoronaVirus");
        Log.d("TAG", "onCreateView: trending");
        final EditText keyword = root.findViewById(R.id.searchText);
        TextView tv = root.findViewById(R.id.textView);
        keyword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    setValue(keyword.getText().toString());
                    getEntries(root);
                    return true;
                }
                return false;
            }
        });
        getEntries(root);
        return root;
    }

    private void getEntries(final View root) {

        lineEntries = new ArrayList<Entry>();
        String key = getValue();
        if (key == null) {
            setValue("CoronaVirus");
        } else if (key.equals("")) {
            setValue("CoronaVirus");
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.d("TAG", "onCreateView: " + getValue());

        String url = "http://10.0.2.2:5000/latesttrends?keyword=" + getValue();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray responseJSONArray = response.getJSONArray("value");
                            for (int i = 0; i < responseJSONArray.length(); i++) {
                                int y = responseJSONArray.getInt(i);
                                lineEntries.add(new Entry(i, y));
                            }
                            Log.d("TAG", "onResponse: " + responseJSONArray);
                            int color = Color.parseColor("#906200EE");
                            lineDataSet = new LineDataSet(lineEntries, "Trending Chart for " + getValue());
                            lineData = new LineData(lineDataSet);
                            lineChart = root.findViewById(R.id.lineChart);
                            lineChart.setData(lineData);

                            lineDataSet.setColor(color);
                            lineDataSet.setValueTextColor(color);
                            lineDataSet.setValueTextSize(10f);
                            lineDataSet.setDrawCircleHole(false);
                            lineDataSet.setCircleColor(color);
                            lineDataSet.setLineWidth(1f);

                            lineChart.getAxisLeft().setDrawGridLines(false);
                            lineChart.getXAxis().setDrawGridLines(false);
                            lineChart.getAxisRight().setDrawGridLines(false);
                            lineChart.getAxisLeft().setDrawAxisLine(false);

                            Legend legend = lineChart.getLegend();
                            legend.setTextSize(15f);
                            legend.setFormSize(15f);

                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();
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
