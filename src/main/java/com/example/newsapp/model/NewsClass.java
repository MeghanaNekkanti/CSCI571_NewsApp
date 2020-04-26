package com.example.newsapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;

import com.example.newsapp.R;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    public static String convertDate(String webPublicationDate) {
        String timeText = "";
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Instant date = Instant.parse(webPublicationDate);
//        LocalDateTime date = LocalDateTime.parse(webPublicationDate, inputFormatter);
        ZoneId zoneId = ZoneId.of("America/Los_Angeles");
        ZonedDateTime laZone = date.atZone(zoneId);
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime current = ldt.atZone(zoneId);
        Log.d("TAG", "convertDate: " + date + "   " + laZone + " " + current);

        Duration diff = Duration.between(laZone.toInstant(), current.toInstant());
//        String output = diff.toString();
        int minutes = (int) ((diff.getSeconds() % (60 * 60)) / 60);
        int seconds = (int) (diff.getSeconds() % 60);
        long hours = diff.toHours();
        Log.d("TAG", "convertDate: " + diff + " " + hours + " " + minutes + " " + seconds);
        if (hours >= 1)
            timeText = hours + "h ago";
        else if (minutes >= 1 && hours < 1)
            timeText = minutes + "m ago";
        else if (minutes < 1)
            timeText = seconds + "s ago";
        return timeText;
    }

    public static Boolean handleStorage(NewsClass newsClass, Context context, ImageButton buttonBookmark) {

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

}
