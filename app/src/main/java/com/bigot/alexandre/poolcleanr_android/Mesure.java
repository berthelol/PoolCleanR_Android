package com.bigot.alexandre.poolcleanr_android;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class Mesure extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_measures, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mesures Piscine");

        final TextView ph_mesure_output =(TextView) getView().findViewById(R.id.ph_measure_output);
        final TextView ph_date_output = (TextView) getView().findViewById(R.id.ph_date_output);
        Button pH_button=(Button) getView().findViewById(R.id.ph_button);
        pH_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test_connectivity task = new Test_connectivity();
                task.execute("http://loicberthelot.freeboxos.fr/device/pH/");
                try {
                    //get when the request is finished
                    String response = task.get();
                    Log.i("Content",response);

                    JSONArray json = new JSONArray(response);
                    String mesure = json.getJSONObject(0).getString("mesure");
                    String date =json.getJSONObject(0).getString("time_of_mesure");

                    ph_mesure_output.setText(mesure.toString());
                    ph_date_output.setText(date.toString());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final TextView chlore_mesure_output =(TextView) getView().findViewById(R.id.chlore_measure_output);
        final TextView chlore_date_output = (TextView) getView().findViewById(R.id.chlore_date_output);
        Button chlore_button=(Button) getView().findViewById(R.id.chlore_button);
        chlore_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test_connectivity task = new Test_connectivity();
                task.execute("http://loicberthelot.freeboxos.fr/device/Chlore/");
                try {
                    //get when the request is finished
                    String response = task.get();
                    Log.i("Content",response);

                    JSONArray json = new JSONArray(response);
                    String mesure = json.getJSONObject(0).getString("mesure");
                    String date =json.getJSONObject(0).getString("time_of_mesure");

                    chlore_mesure_output.setText(mesure.toString());
                    chlore_date_output.setText(date.toString());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}