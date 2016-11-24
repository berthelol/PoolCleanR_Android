package com.bigot.alexandre.poolcleanr_android;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Bacs_state extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_bac_level, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Etat des bacs");

        //Get the state of the different bacs
        getLevelBac();

        // Refresh values
        Button levelBac_btn = (Button) getView().findViewById(R.id.level_bac_btn);
        levelBac_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), "Test Test", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void getLevelBac() {

        // Handle Progress Bar
        final ProgressBar moinsProgress = (ProgressBar) getView().findViewById(R.id.level_ph_bacmoins);
        final ProgressBar plusProgress = (ProgressBar) getView().findViewById(R.id.level_ph_bacplus);
        final ProgressBar chloreProgress = (ProgressBar) getView().findViewById(R.id.level_chlore_bac);

        // Percentages
        final TextView phMoins_txt = (TextView) getView().findViewById(R.id.percent_moins);
        final TextView phPlus_txt = (TextView) getView().findViewById(R.id.percent_plus);
        final TextView chlore_txt = (TextView) getView().findViewById(R.id.percent_chl);
        final TextView date_txt = (TextView) getView().findViewById(R.id.bac_date);

        Test_connectivity task_ph = new Test_connectivity();
        Test_connectivity task_chlore = new Test_connectivity();

        // Relative to pH Moins, pH Plus and date
        task_ph.execute("http://loicberthelot.freeboxos.fr/device/pH/");
        try {
            //get when the request is finished
            String response = task_ph.get();
            Log.i("Content",response);

            // pH moins and pH plus
            JSONObject json = new JSONObject(response);
            String date = json.getString("time_of_mesure");
            json = json.getJSONObject("bac");
            JSONObject json_moins = json.getJSONObject("bacplus");
            String bacMoins = json_moins.getString("remplissage");
            JSONObject json_plus = json.getJSONObject("bacmoins");
            String bacPlus = json_plus.getString("remplissage");

            phMoins_txt.setText(bacMoins.toString() + "%");
            phPlus_txt.setText(bacPlus.toString() + "%");
            date_txt.setText("relevé fait le : " + date.toString());

            moinsProgress.setProgress(Integer.parseInt(bacMoins));
            plusProgress.setProgress(Integer.parseInt(bacPlus));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Relative to the bac of chlore
        task_chlore.execute("http://loicberthelot.freeboxos.fr/device/chlore/");
        try {
            //get when the request is finished
            String response = task_chlore.get();
            Log.i("Content",response);

            JSONObject json = new JSONObject(response);
            json = json.getJSONObject("bac");
            String bacChlore = json.getString("remplissage");

            chlore_txt.setText(bacChlore.toString() + "%");
            chloreProgress.setProgress(Integer.parseInt(bacChlore));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}