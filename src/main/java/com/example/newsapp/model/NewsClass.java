package com.example.newsapp.model;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.BookmarkAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class NewsClass {
    String webTitle;
    String id;
    String imageUrl;
    String sectionId;

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    String webPublicationDate;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertDate(String webPublicationDate, String type) {
        String timeText = "";
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Instant date = Instant.parse(webPublicationDate);
//        LocalDateTime date = LocalDateTime.parse(webPublicationDate, inputFormatter);
        ZoneId zoneId = ZoneId.of("America/Los_Angeles");
        ZonedDateTime laZone = date.atZone(zoneId);

        if (type == "BOOKMARK") {
            Log.d("TAG", "convertDate: " + laZone + "   " + DateTimeFormatter.ofPattern("dd MMM").format(laZone));
            return DateTimeFormatter.ofPattern("dd MMM").format(laZone);
        } else {
            LocalDateTime ldt = LocalDateTime.now();
            ZonedDateTime current = ldt.atZone(zoneId);
            Duration diff = Duration.between(laZone.toInstant(), current.toInstant());
//        String output = diff.toString();
            int minutes = (int) ((diff.getSeconds() % (60 * 60)) / 60);
            int seconds = (int) (diff.getSeconds() % 60);
            long hours = diff.toHours();
//        Log.d("TAG", "convertDate: " + diff + " " + hours + " " + minutes + " " + seconds);
            if (hours >= 1)
                timeText = hours + "h ago";
            else if (minutes >= 1 && hours < 1)
                timeText = minutes + "m ago";
            else if (minutes < 1)
                timeText = seconds + "s ago";
            return timeText;
        }
    }

    public static Boolean handleStorage(NewsClass newsClass, Context context) {

        String json = null;
        String key = newsClass.getId();
        SharedPreferences s_href;
        SharedPreferences.Editor editor;
        s_href = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String response = s_href.getString(key, "");
        editor = s_href.edit();
        json = gson.toJson(newsClass);
        if (response.equals("")) {
//            buttonBookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp);
            editor.putString(key, json);
            editor.apply();
            return false;
        } else {
//            buttonBookmark.setImageResource(R.drawable.baseline_bookmark_border_black_24dp);
            editor.remove(key).apply();
            return true;
        }
//        Log.d("TAG", "onClick: " + key + " " + s_href.getString(key, ""));
    }

    public static Boolean checkBookmark(NewsClass newsClass, Context context) {
        String key = newsClass.getId();
        SharedPreferences s_href;
        s_href = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        final String response = s_href.getString(key, "");
        Log.d("TAG", "checkBookmark: " + response);
        return !response.equals("");
    }

    public static void showDialog(final NewsClass newsClass, final Context context, final RecyclerView.Adapter adapter, final JSONArray news, final int pos) {
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.custom_dialog);
        TextView text = dialog.findViewById(R.id.dialogTitle);
        text.setText(newsClass.getWebTitle());
        ImageView imageView = dialog.findViewById(R.id.dialogImage);
        final ImageButton bookmark = dialog.findViewById(R.id.dialogBookmark);
        Picasso.with(context).load(newsClass.getImageUrl()).resize(1000, 600).into(imageView);
        Boolean exists = NewsClass.checkBookmark(newsClass, context);
        if (exists) {
            bookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp_2x);
        } else {
            bookmark.setImageResource(R.drawable.baseline_bookmark_border_black_24dp_2x);
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = handleStorage(newsClass, context);
                if (check) {
                    bookmark.setImageResource(R.drawable.baseline_bookmark_border_black_24dp_2x);
                    Log.d("TAG", "onClick: " + news);
                    if (!(news == null)) {
                        news.remove(pos);
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRemoved(pos);
                        dialog.dismiss();
                    }
//                    news.remove(pos);
//                    adapter.notifyDataSetChanged();
//                    adapter.notifyItemRemoved(pos);
                } else {
                    bookmark.setImageResource(R.drawable.baseline_bookmark_black_24dp_2x);
                }
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

}
