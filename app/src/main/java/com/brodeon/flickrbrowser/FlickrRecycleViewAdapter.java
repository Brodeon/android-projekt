package com.brodeon.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class FlickrRecycleViewAdapter extends RecyclerView.Adapter<FlickrRecycleViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecycleViewAdapt";
    private List<Photo> photosList;
    private Context context;

    public FlickrRecycleViewAdapter(List<Photo> photosList, Context context) {
        this.photosList = photosList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called by the layout manager when needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        //Called by the layout manager when it wants a new data in an existing raw

        if (photosList == null || photosList.size() == 0) {
            holder.thumbnail.setImageResource(R.drawable.placeholder_second);
            holder.title.setText("No photos match new search.\n\nUse the search icon to search for photos");
        } else {
            Photo photoItem = photosList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + "---->" + position);
            Picasso.with(context).load(photoItem.getImage())
                    .error(R.drawable.placeholder_second)
                    .placeholder(R.drawable.placeholder_second)
                    .into(holder.thumbnail);
            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((photosList != null) && (photosList.size() != 0) ? photosList.size() : 1);
    }

    void loadNewData(List<Photo> newPhotos) {
        photosList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((photosList != null) && (photosList.size() != 0) ? photosList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = itemView.findViewById(R.id.image_small);
            this.title = itemView.findViewById(R.id.title);
        }
    }
}
