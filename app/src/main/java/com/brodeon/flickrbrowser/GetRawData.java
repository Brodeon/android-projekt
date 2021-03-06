package com.brodeon.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Enum zawierający status pobrania
 */
enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALLIZED, FAILD_OR_EMPTY, OK}

/**
 * Odpowiada za pobranie zdjęć z internetu na podstawie podanego linka do Flickr API
 */
class GetRawData extends AsyncTask <String, Void, String> {
    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;
    private final onDownloadComplete mCallback;

    interface onDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(onDownloadComplete callback) {
        mCallback = callback;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter = " + s);
        if (mCallback != null) {
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALLIZED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was: " + response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IOException reading data " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Needs Permition" + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILD_OR_EMPTY;
        return null;
    }

    void runInSameThread(String s) {
        Log.d(TAG, "runInSameThread: starts");
        if (mCallback != null) {
            String result = doInBackground(s);
            mCallback.onDownloadComplete(result, mDownloadStatus);
        }

        Log.d(TAG, "runInSameThread: ends");
    }
}














