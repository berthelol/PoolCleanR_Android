package com.bigot.alexandre.poolcleanr_android;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.id.input;
import static android.transition.Fade.OUT;
import static java.lang.System.in;

/**
 * Created by loic on 18/11/2016.
 */

public class Connectivity extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... urls) {
        String request_type = urls[1];

        String result="";
        URL url;
        //https://developer.android.com/reference/java/net/HttpURLConnection.html
        //post or get
        HttpURLConnection urlconnection =null;


        try
        {
            url = new URL(urls[0]);

            if(request_type=="POST")
            {
                urlconnection =(HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod(request_type);
                urlconnection.setDoOutput(true);
                urlconnection.setChunkedStreamingMode(0);
                urlconnection.setRequestProperty("Content-Type", "application/json");
                urlconnection.setRequestProperty("Accept", "application/json");
                String json = urls[2];

                OutputStreamWriter streamWriter = new OutputStreamWriter(urlconnection.getOutputStream(), "UTF-8");
                streamWriter.write(json.toString());
                streamWriter.flush();
                Log.i("Data sent",json.toString());

                StringBuilder stringBuilder = new StringBuilder();
                if (urlconnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader streamReader = new InputStreamReader(urlconnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String response = null;
                    while ((response = bufferedReader.readLine()) != null) {
                        stringBuilder.append(response + "\n");
                    }
                    bufferedReader.close();

                    Log.d("test", stringBuilder.toString());
                    return stringBuilder.toString();
                } else {
                    Log.e("test", urlconnection.getResponseMessage());
                    return null;
                }
            }
            if(request_type=="GET") {
            urlconnection =(HttpURLConnection) url.openConnection();
            InputStream in = urlconnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;

                data = reader.read();
            }
            return result;
        }
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }

}
