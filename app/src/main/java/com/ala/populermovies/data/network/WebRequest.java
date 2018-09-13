package com.ala.populermovies.data.network;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Scanner;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class WebRequest extends AsyncTask<URL, Void, String> {
    private NetworkCompleteListener mListener;
    private URL url;

    public WebRequest(URL url, NetworkCompleteListener mListener) {
        this.mListener = mListener;
        this.url = url;
        this.execute(url);
    }

    @Override
    protected String doInBackground(URL... strings) {
        if (strings.length == 0) {
            return null;
        }
        if (!isOnline())
            return null;
        URL url = strings[0];
        try {
            return getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //check if there is an internet connection
    private boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress add = new InetSocketAddress("8.8.8.8", 53);
            sock.connect(add, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (mListener != null) {
            if (s != null)
                mListener.onSuccess(s);
            else
                mListener.onError(new Throwable("COULDN'T CONNECT TO THE INTERNET"));
        }

    }

    public interface NetworkCompleteListener {
        void onSuccess(String response);

        void onError(Throwable throwable);
    }
}
