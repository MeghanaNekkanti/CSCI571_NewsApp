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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BookmarkAdapter extends RecyclerView.Adapter {

    private LayoutInflater layoutInflater;
    JSONArray news;
    Context context;

    public BookmarkAdapter(Context context, JSONArray news) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.news = news;
        Log.d("Data length", String.valueOf(news.length()));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = layoutInflater.inflate(R.layout.empty_bookmark, parent, false);
                return new EmptyViewHolder(view);
            case 1:
                view = layoutInflater.inflate(R.layout.bookmark_card, parent, false);
                return new ViewHolder(view);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (news.length() == 0) {
            Log.d("TAG", "onBindViewHolder: Empty");
        } else {
            loadBookmarkData((ViewHolder) holder, position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadBookmarkData(final ViewHolder holder, final int position) {

        final NewsClass newsClass = new NewsClass();
        Log.d("TAG", "loadBookmark: " + news);

        String url = "";
        try {
            JSONObject json = new JSONObject(news.get(position).toString());
            Log.d("TAG", "loadBookmark: " + json);
            url = json.getString("imageUrl");
            if (url.equals(""))
                url = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
            newsClass.setImageUrl(url);
            newsClass.setWebPublicationDate(json.getString("webPublicationDate"));
            newsClass.setId(json.getString("id"));
            newsClass.setSectionId(json.getString("sectionId"));
            newsClass.setWebTitle(json.getString("webTitle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.textSection.setText(newsClass.getSectionId());
        holder.textTitle.setText(newsClass.getWebTitle());
        String time = NewsClass.convertDate(newsClass.getWebPublicationDate(), "BOOKMARK");
        holder.textTime.setText(time);

        Picasso.with(context).load(newsClass.getImageUrl()).resize(0, 500).into(holder.image);

        Boolean exists = NewsClass.checkBookmark(newsClass, context);
        if (exists) {
            holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp);
        }
        // on long click
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NewsClass.showDialog(newsClass, context, BookmarkAdapter.this, news, position);
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
                Boolean check = NewsClass.handleStorage(newsClass, context);
                if (check) {
                    news.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);
                }
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

        TextView textTitle, textSection, textTime;
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


