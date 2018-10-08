package com.brodeon.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

class RecycleItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecycleItemClickListene";

    interface OnRecycleClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private final OnRecycleClickListener listener;
    private final GestureDetectorCompat gestureDetector;

    public RecycleItemClickListener(Context context, final RecyclerView recyclerView, final OnRecycleClickListener listener) {
        this.listener = listener;
        gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && listener != null) {
                    Log.d(TAG, "onSingleTapUp: calling listener.onItemClick");
                    listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && listener != null) {
                    Log.d(TAG, "onLongPress: calling listener.onItemLongClick");
                    listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if (gestureDetector != null) {
            boolean result = gestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned: " + result);
            return result;
        } else {
            Log.d(TAG, "onInterceptTouchEvent: returned: false");
            return false;
        }
    }
}
