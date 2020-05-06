package com.example.newsapp.ui.details;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;
import com.example.newsapp.model.NewsClass;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    CardView cV;
    String article_id, image_url, webUrl = "";
    NewsClass newsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue queue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_details);

        Toolbar myChildToolbar =
                findViewById(R.id.detail_toolbar);
        setSupportActionBar(myChildToolbar);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        cV = findViewById(R.id.detail_card);
        cV.setVisibility(View.GONE);

        Intent intent = getIntent();

        newsClass = (NewsClass) intent.getSerializableExtra("newsClass");
        article_id = newsClass.getId();
        image_url = newsClass.getImageUrl();
        Log.d("TAG", "onCreate: Detail " + article_id);
        getDetails(queue, this, ab);
    }

    private void getDetails(RequestQueue queue, final DetailsActivity detailsActivity, final ActionBar ab) {
        final TextView fetch;
        final ProgressBar progressBar;

        fetch = findViewById(R.id.fetching);
        progressBar = findViewById(R.id.progressBar);

        fetch.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://api-dot-news-app-android-nodejs.ue.r.appspot.com/guardiandetails?id=" + article_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                JSONObject json = null;
                TextView title, section, date, description;
                final ImageView imageView = findViewById(R.id.detail_image);

                Log.d("TAG", "onResponse: DETAIL" + response);
                title = findViewById(R.id.detail_title);
                section = findViewById(R.id.detail_section);
                date = findViewById(R.id.detail_date);
                description = findViewById(R.id.detail_description);
                Picasso.with(detailsActivity).load(image_url).resize(4000, 2400).into(imageView);

                try {
                    json = new JSONObject(response.toString());
                    JSONObject jsonObject0 = response.getJSONObject("blocks");
                    JSONArray ja = jsonObject0.getJSONArray("body");
                    String desc = "";
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jsonObject = ja.getJSONObject(i);
                        desc = desc.concat(jsonObject.getString("bodyHtml"));
                        desc += "<br/>";
                        Log.d("TAG", "onResponse: DESC" + desc);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        description.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        description.setText(Html.fromHtml(desc));
                    }

                    title.setText(json.getString("webTitle"));
                    section.setText(json.getString("sectionName"));
                    String date_converted = NewsClass.convertDate(json.getString("webPublicationDate"), "DETAILS");
                    date.setText(date_converted);
                    ab.setTitle(json.getString("webTitle"));
                    webUrl = json.getString("webUrl");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                fetch.setVisibility(View.GONE);
                cV.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                fetch.setVisibility(View.GONE);
                Log.e("Volley", "Error" + error);
            }
        });

        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_toolbar_menu, menu);
        MenuItem bookmark = menu.findItem(R.id.action_bookmark);
        Boolean exists = NewsClass.checkBookmark(newsClass, this);
        if (exists) {
            bookmark.setIcon(R.drawable.baseline_bookmark_black_24dp_2x);
        } else {
            bookmark.setIcon(R.drawable.baseline_bookmark_border_black_24dp_2x);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        this.finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("TAG", "onOptionsItemSelected: " + id + " " + android.R.id.home);
        switch (item.getItemId()) {
//            case 16908332:
//                Log.d("TAG", "onOptionsItemSelected: ");
//                this.finish();
//                return true;
            case R.id.action_bookmark:
                NewsClass.handleStorage(newsClass, this);
                Boolean exists = NewsClass.checkBookmark(newsClass, this);
                if (exists) {
                    item.setIcon(R.drawable.baseline_bookmark_black_24dp_2x);
                } else {
                    item.setIcon(R.drawable.baseline_bookmark_border_black_24dp_2x);
                }
                return true;
            case R.id.action_twitter:
                String url = "https://twitter.com/intent/tweet?text=Check out this Link:&url=https://www.theguardian.com/" + article_id + "&hashtags=CSCI571NewsSearch";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewFullArticle(View view) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(webUrl));
        startActivity(i);
    }
}
