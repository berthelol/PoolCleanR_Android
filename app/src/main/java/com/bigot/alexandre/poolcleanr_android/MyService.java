package com.bigot.alexandre.poolcleanr_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        try {
            Connectivity connectbacchlore = new Connectivity();
            Connectivity connectbacph = new Connectivity();
            // Relative to pH and date
            connectbacph.execute("http://loicberthelot.freeboxos.fr/device/pH/","GET");
            try {
                //get when the request is finished
                String response = connectbacph.get();
                Log.i("Content",response);

                JSONObject json = new JSONObject(response);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
