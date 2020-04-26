package com.example.newsapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.google.gson.Gson;
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
        if (type.equals("BOOKMARK")) {
            view = layoutInflater.inflate(R.layout.bookmark_card, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.single_card, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (type.equals("HOME"))
            loadHomeData(holder, position);
        else if (type.equals("TAB") || type.equals("BOOKMARK"))
            loadSectionData(holder, position);
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
            newsClass.setSectionId(json.getString("sectionId"));
            newsClass.setWebTitle(json.getString("webTitle"));
            Log.d("TAG", "onBindViewHolder: " + json.getString("sectionId"));
//            newsArray.add(new NewsClass());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.textSection.setText(newsClass.getSectionId());
        holder.textTitle.setText(newsClass.getWebTitle());
        String time = NewsClass.convertDate(newsClass.getWebPublicationDate());
        holder.textTime.setText(time);
        Picasso.with(context).load(newsClass.getImageUrl()).resize(350, 350).into(holder.image);

        String key = newsClass.getId();
        SharedPreferences s_href;
        s_href = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        String response = s_href.getString(key, "");
        if (!response.equals("")) {
            holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp);
        }
        // on long click
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                TextView text = dialog.findViewById(R.id.dialogTitle);
                text.setText(newsClass.getWebTitle());
                ImageView imageView = dialog.findViewById(R.id.dialogImage);
                Picasso.with(context).load(newsClass.getImageUrl()).resize(1000, 600).into(imageView);
                dialog.show();
                return true;
            }
        });


        // bookmark button on click
        holder.buttonBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<NewsClass> newsArray = new ArrayList<>();
                Boolean check = NewsClass.handleStorage(newsClass, context, holder.buttonBookmark);
                if (check && type.equals("BOOKMARK")) {
                    holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_border_black_24dp);
                    news.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);
                }
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
            newsClass.setSectionId(json.getString("sectionName"));
            newsClass.setWebTitle(json.getString("webTitle"));

            Log.d("TAG", "onBindViewHolder: " + json.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.textSection.setText(newsClass.getSectionId());
        holder.textTitle.setText(newsClass.getWebTitle());
        String time = NewsClass.convertDate(newsClass.getWebPublicationDate());
        holder.textTime.setText(time);
        holder.textTime.setText(time);
        Picasso.with(context).load(newsClass.getImageUrl()).resize(350, 350).into(holder.image);


        String key = newsClass.getId();
        SharedPreferences s_href;
        s_href = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        String response = s_href.getString(key, "");
        if (!response.equals("")) {
            holder.buttonBookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp);
        }


        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.setContentView(R.layout.custom_dialog);
                TextView text = dialog.findViewById(R.id.dialogTitle);
                text.setText(newsClass.getWebTitle());
                ImageView imageView = dialog.findViewById(R.id.dialogImage);
                Picasso.with(context).load(newsClass.getImageUrl()).resize(1000, 600).into(imageView);
                dialog.show();
                return true;
            }
        });
        // bookmark button on click
        holder.buttonBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsClass.handleStorage(newsClass, context, holder.buttonBookmark);
                notifyDataSetChanged();

//                String response = s_href.getString(key, "");
//                ArrayList<NewsClass> newsArray = gson.fromJson(response,
//                        new TypeToken<List<NewsClass>>() {
//                        }.getType());
//                Log.d("TAG", "onClick: bookmark" + newsArray);
//                editor = s_href.edit();
//                editor.remove(key).apply();
//                if (newsArray == null) {
//                    ArrayList<NewsClass> newsArray_ = new ArrayList<>();
//                    newsArray_.add(newsClass);
//                    json = gson.toJson(newsArray_);
//
//                } else {
//                    newsArray.add(newsClass);
//                    json = gson.toJson(newsArray);
//                }
//
//                editor.putString(key, json);
//                editor.commit();
//                String response1 =s_href.getString(key , "");
//                ArrayList<NewsClass> lstArrayList = gson.fromJson(response1,
//                        new TypeToken<List<NewsClass>>(){}.getType());
//                Log.d("TAG", "onClick: bookmark "+lstArrayList);
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
}
