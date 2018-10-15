package com.brodeon.flickrbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements GetFlickrJsonData.OnDataAvailable, RecycleItemClickListener.OnRecycleClickListener{
    /**
     * Tag MainActivity
     */
    private static final String TAG = "MainActivity";

    /**
     * String odpowiedzialny za wyjęcie Taga z SharedPreferences
     */
    static final String FLICKR_QUERY = "FLICKR_QUERY";

    /**
     * String odpowiedzialny za przypisanie danych podczas otwierania nowej aktywności
     */
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    /**
     * Adapter do RecycleView
     */
    private FlickrRecycleViewAdapter flickrRecycleViewAdapter;

    /**
     * Pole odpowiedzialny za zapisanie SwipeRefreshLayout który implementuje zachowanie odświeżania strony
     */
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Lista przechowująca listę zdjęć
     */
    List<Photo> photos = null;

    @Override
    /**
     * Otwiera się gdy aktywność jest pierwszy raz włączana. Odpowiada za znalezienie layoutu RecycleView, przypisanie do niego adaptera
     * oraz LayoutManagera. Odpowiada również za przypisanie do swipeRefreshLayout odpowiedniego layoutu i stworzenie setOnRefreshListener
     * do swipeRefreshLayout. Listener swipeRefreshLayout odpowiada za pobranie zdjęć z internetu
     */
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);

        RecyclerView recyclerView = findViewById(R.id.recycleView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        recyclerView.addOnItemTouchListener(new RecycleItemClickListener(this, recyclerView, this));

        flickrRecycleViewAdapter = new FlickrRecycleViewAdapter(new ArrayList<Photo>(), this);
        recyclerView.setAdapter(flickrRecycleViewAdapter);

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadData();
            }
        });

        Log.d(TAG, "onCreate: ends");
    }

    /**
     * Gdy aktywność jest ponownie uruchamiana ma pobrać
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        downloadData();
        Log.d(TAG, "onResume: ends");
    }

    /**
     * Inflatuje menu do aktywności
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Decyduje który z elementów menu(search bądź refresh) ma zostać użyty
     * @param item item do wybrania
     * @return true jeśli został wybrany element z menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_refresh:
                downloadData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Callback wywoływany wtedy gdy pobrało dane z internetu
     * @param data pobrane zdjęcia z internetu
     * @param status enum, zawierający status pobrania
     */
    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK) {
            flickrRecycleViewAdapter.loadNewData(data);
            this.photos = data;
        } else {
            Log.e(TAG, "onDownloadComplete: faild with status " + status);
        }

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
//        Toast.makeText(this, "Normal tap at position " + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback wywołany gdy zostało wykryte długie naciśniecie na elemencie RecycleView
     * @param view naciśnięty element RecycleView
     * @param position pozycja naciśniętego elementu RecycleView w liście zdjęć(List<Photo> photos)
     */
    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
//        Toast.makeText(this, "Long tap at position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, flickrRecycleViewAdapter.getPhoto(position));
        startActivity(intent);
    }

    /**
     * Metoda odpowiedzialna za zlecenie pobrania danych do GetFlickrJsonData
     */
    private void downloadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferences.getString(FLICKR_QUERY, "");

        if (queryResult.length() > 0) {
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData("https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true, this);
            getFlickrJsonData.execute(queryResult);
        }
    }


}
