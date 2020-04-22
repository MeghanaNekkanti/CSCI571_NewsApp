package com.example.newsapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (type == "Bookmark") {
            view = layoutInflater.inflate(R.layout.bookmark_card, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.single_card, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (type == "HOME")
            loadHomeData(holder, position);
        else if (type == "TAB")
            loadSectionData(holder, position);
    }

    private void loadSectionData(ViewHolder holder, int position) {

        String title = "";
        String url = "";
        try {
            JSONObject json = new JSONObject(news.get(position).toString());
            Log.d("TAG", "onBindViewHolder: " + json.getString("sectionId"));
            holder.textSection.setText(json.getString("sectionId"));
            title = json.getString("webTitle");
            holder.textTitle.setText(title);
            url = json.getString("imageUrl");
            if (url.equals(""))
                url = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.with(context).load(url).resize(350, 350).into(holder.image);

        final String finalTitle = title;
        final String finalUrl = url;
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                TextView text = dialog.findViewById(R.id.dialogTitle);
                text.setText(finalTitle);
                ImageView imageView = dialog.findViewById(R.id.dialogImage);
                Picasso.with(context).load(finalUrl).resize(1000, 600).into(imageView);
                dialog.show();
                return true;
            }
        });
    }

    private void loadHomeData(ViewHolder holder, int position) {

        String title = "";
        String url = "";
        try {
            JSONObject json = new JSONObject(news.get(position).toString());
            Log.d("TAG", "onBindViewHolder: " + json.getString("sectionName"));
            holder.textSection.setText(json.getString("sectionName"));
            title = json.getString("webTitle");
            holder.textTitle.setText(title);
            JSONObject fields = json.getJSONObject("fields");
            if (fields.has("thumbnail"))
                url = fields.getString("thumbnail");
            if (url.equals(""))
                url = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";

            Log.d("TAG", "onBindViewHolder: " + url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.with(context).load(url).resize(350, 350).into(holder.image);

        final String finalTitle = title;
        final String finalUrl = url;
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.setContentView(R.layout.custom_dialog);
                TextView text = dialog.findViewById(R.id.dialogTitle);
                text.setText(finalTitle);
                ImageView imageView = dialog.findViewById(R.id.dialogImage);
                Picasso.with(context).load(finalUrl).resize(1000, 600).into(imageView);
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textSection, textTime;
        ImageView image;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_heading);
            textSection = itemView.findViewById(R.id.text_section);
            textTime = itemView.findViewById(R.id.text_time);
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
}
