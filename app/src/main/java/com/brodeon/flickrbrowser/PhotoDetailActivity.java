package com.brodeon.flickrbrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if (photo != null) {
            Resources resources = getResources();

            getSupportActionBar().setTitle(photo.getTitle());

            TextView photoTitle = findViewById(R.id.photo_title);
            String title = resources.getString(R.string.photo_title_text, photo.getTitle());
            photoTitle.setText(title);

            TextView photoTags = findViewById(R.id.photo_tags);
            String tags = resources.getString(R.string.photo_tag_text, photo.getTags());
            photoTags.setText(tags);

            TextView photoAuthor = findViewById(R.id.photo_author);
            String author = resources.getString(R.string.photo_author_text, photo.getAuthor());
            photoAuthor.setText(author);

            ImageView photoImage = findViewById(R.id.photoImage);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.placeholder_second)
                    .placeholder(R.drawable.placeholder_second)
                    .into(photoImage);
        }
    }

}
