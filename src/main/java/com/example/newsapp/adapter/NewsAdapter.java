package com.example.newsapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.model.NewsClass;
import com.example.newsapp.ui.details.DetailsActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NewsAdapter extends RecyclerView.Adapter {

    private LayoutInflater layoutInflater;
    JSONArray news;
    Context context;
    String type = "";

    public NewsAdapter(Context context, JSONArray news, String type) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.news = news;
        this.type = type;
        Log.d("Data length", String.valueOf(news.length()));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = layoutInflater.inflate(R.layout.empty_bookmark, parent, false);
                TextView text = view.findViewById(R.id.text_no_bookmark);
                text.setText(R.string.no_search);
                return new EmptyViewHolder(view);
            case 1:
                view = layoutInflater.inflate(R.layout.single_card, parent, false);
                return new ViewHolder(view);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (type) {
            case "HOME":
                loadHomeData((ViewHolder) holder, position);
                break;
            case "TAB":
                loadSectionData((ViewHolder) holder, position);
                break;
            case "SEARCH":
                if (news.length() == 0) {
                    Log.d("TAG", "onBindViewHolder: Empty");
                } else {
                    loadSectionData((ViewHolder) holder, position);
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadSectionData(final ViewHolder holder, final int position) {

        final NewsClass newsClass = new NewsClass();

        String url = "";
        try {
            JSONObject json = new JSONObject(news.get(position).toString());
            Log.d("TAG", "loadSectionData: " + json);
            url = json.getString("imageUrl");
            if (url.equals(""))
                url = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
            newsClass.setImageUrl(url);
            newsClass.setWebPublicationDate(json.getString("webPublicationDate"));
            newsClass.setId(json.getString("id"));
            newsClass.setSectionName(json.getString("sectionName"));
            newsClass.setWebTitle(json.getString("webTitle"));
            newsClass.setWebUrl(json.getString("url"));
            Log.d("TAG", "onBindViewHolder: " + json.getString("sectionName"));
//            newsArray.add(new NewsClass());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.textSection.setText(newsClass.getSectionName());
        holder.textTitle.setText(newsClass.getWebTitle());
        String time;
        if (type.equals("SEARCH"))
            time = NewsClass.convertDate(newsClass.getWebPublicationDate(), type);
        else
            time = NewsClass.convertDate(newsClass.getWebPublicationDate(), "NEWS");
        holder.textTime.setText(time);
        Picasso.with(context).load(newsClass.getImageUrl()).resize(350, 350).into(holder.image);
        Boolean exists = NewsClass.checkBookmark(newsClass, context);
        if (exists) {
            holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp);
        } else {
            holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_border_black_24dp);
        }
        // on long click
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NewsClass.showDialog(newsClass, context, NewsAdapter.this, null, 0);
                return true;
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("newsClass", newsClass);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        // bookmark button on click
        holder.buttonBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<NewsClass> newsArray = new ArrayList<>();
                NewsClass.handleStorage(newsClass, context);
                notifyDataSetChanged();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadHomeData(final ViewHolder holder, int position) {

        final NewsClass newsClass = new NewsClass();

        String url = "";
        try {
            JSONObject json = new JSONObject(news.get(position).toString());
            JSONObject fields = json.getJSONObject("fields");

            if (fields.has("thumbnail"))
                url = fields.getString("thumbnail");
            if (url.equals(""))
                url = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";

            newsClass.setImageUrl(url);
            newsClass.setWebPublicationDate(json.getString("webPublicationDate"));
            newsClass.setId(json.getString("id"));
            newsClass.setSectionName(json.getString("sectionName"));
            newsClass.setWebTitle(json.getString("webTitle"));
            newsClass.setWebUrl(json.getString("webUrl"));
            Log.d("TAG", "onBindViewHolder: " + json.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.textSection.setText(newsClass.getSectionName());
        holder.textTitle.setText(newsClass.getWebTitle());
        String time = NewsClass.convertDate(newsClass.getWebPublicationDate(), "NEWS");
        holder.textTime.setText(time);
        Picasso.with(context).load(newsClass.getImageUrl()).resize(350, 350).into(holder.image);


        Boolean exists = NewsClass.checkBookmark(newsClass, context);
        if (exists) {
            holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp);
        } else {
            holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_border_black_24dp);
        }

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NewsClass.showDialog(newsClass, context, NewsAdapter.this, null, 0);
                return true;
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: home data");
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("newsClass", newsClass);
                context.startActivity(intent);
            }
        });

        // bookmark button on click
        holder.buttonBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsClass.handleStorage(newsClass, context);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (news.length() == 0)
            return 1;
        return news.length();
    }

    @Override
    public int getItemViewType(int position) {

        int b = news.length();
        Log.d("TAG", "getItemViewType: " + b);
        if (b == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textSection, textTime, noBookmark;
        ImageView image;
        View view;
        ImageButton buttonBookmark;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_heading);
            textSection = itemView.findViewById(R.id.text_section);
            textTime = itemView.findViewById(R.id.text_time);
            buttonBookmark = itemView.findViewById(R.id.bookmarkButton);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            view = itemView;
            image = itemView.findViewById(R.id.imageView);
            image.setEnabled(false);
            image.setOnClickListener(null);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        TextView noBookmark;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            noBookmark = itemView.findViewById(R.id.text_no_bookmark);
        }
    }
}
