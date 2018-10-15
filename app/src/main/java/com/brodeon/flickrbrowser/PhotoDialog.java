package com.brodeon.flickrbrowser;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PhotoDialog extends DialogFragment {

    private SubsamplingScaleImageView photoView;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private Target target;
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_dialog_layout, container, false);
        photoView = view.findViewById(R.id.photo_view);
        photoView.setDoubleTapZoomDuration(400);
        photoView.setMinimumDpi(30);
        Bundle bundle = getArguments();
        Photo photo = (Photo) bundle.getSerializable(PHOTO_TRANSFER);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                photoView.setImage(ImageSource.bitmap(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getActivity()).load(photo.getLink())
                .error(R.drawable.placeholder_second)
                .placeholder(R.drawable.placeholder_second)
                .into(target);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

}
