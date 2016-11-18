package com.bigot.alexandre.poolcleanr_android;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by loic on 18/11/2016.
 */

public class Test_connectivity extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;
        //https://developer.android.com/reference/java/net/HttpURLConnection.html
        //post or get
        HttpURLConnection urlconnection =null;

        try
        {
            url = new URL(urls[0]);
            urlconnection =(HttpURLConnection) url.openConnection();

            InputStream in = urlconnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();
            while(data!=-1)
            {
                char current = (char) data;
                result +=current;

                data = reader.read();
            }
            return result;

        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

}
