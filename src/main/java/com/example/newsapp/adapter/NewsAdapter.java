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

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;
    Context context;
    String type = "";

    public NewsAdapter(Context context, List<String> data, String type) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.type = type;
        Log.d("Data length", String.valueOf(data.size()));
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

        final String heading = data.get(position);
        holder.textTitle.setText(heading);

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.setContentView(R.layout.custom_dialog);
                TextView text = dialog.findViewById(R.id.dialogTitle);
                text.setText(heading);
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        ImageView image;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_heading);
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
