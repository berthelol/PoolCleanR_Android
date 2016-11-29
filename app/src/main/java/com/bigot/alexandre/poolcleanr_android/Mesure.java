package com.bigot.alexandre.poolcleanr_android;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class Mesure extends Fragment {

    public final CharSequence toast_info_ph = "Le pH mesure l'acidité d'un liquide. Il est compris entre 0 et 14. L'eau d'une piscine a idéalement un pH compris entre 7 et 7,5.";
    public final CharSequence toast_info_chlore = "Le taux de chlore d'une piscine doit être compris entre 1,2 et 1,5 mg/Litre d'eau.";
    public final CharSequence toast_info_temp = "Vous êtes libres de déterminer la température optimale de votre piscine.";

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

        // Get the value for each unity
        getMesure();

        // ask for a measure/get
        Button mesure_btn =(Button) getView().findViewById(R.id.mesure_btn);
        mesure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMesure();
            }
        });

        // Toast of information for pH
        ImageView info_ph = (ImageView) getView().findViewById(R.id.icon_info_ph);
        info_ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), toast_info_ph, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Toast of information for the chlore
        ImageView info_chlore = (ImageView) getView().findViewById(R.id.icon_info_chlore);
        info_chlore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), toast_info_chlore, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        // Toast of information for the temperature
        ImageView info_temp = (ImageView) getView().findViewById(R.id.icon_info_temp);
        info_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), toast_info_temp, Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    public void getMesure() {

        final TextView ph_output =(TextView) getView().findViewById(R.id.output_ph);
        final TextView chlore_output =(TextView) getView().findViewById(R.id.output_chlore);
        final TextView temp_output = (TextView) getView().findViewById(R.id.output_temp);
        final TextView date_output = (TextView) getView().findViewById(R.id.mesure_date);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        Connectivity task_ph = new Connectivity();
        Connectivity task_chlore = new Connectivity();
        Connectivity task_temp = new Connectivity();

        // Relative to pH and date
        task_ph.execute("http://loicberthelot.freeboxos.fr/device/pH/","GET");
        try {
            //get when the request is finished
            String response = task_ph.get();
            Log.i("Content",response);

            JSONObject json = new JSONObject(response);
            String ph_mesure = df.format(json.getDouble("mesure"));
            String date = json.getString("time_of_mesure");

            ph_output.setText(ph_mesure.toString());
            String dateToDisplay = date.toString();
            dateToDisplay = dateToDisplay.replace('T',' ');
            dateToDisplay = dateToDisplay.replace('Z',' ');
            date_output.setText("relevé fait le : " + dateToDisplay);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Relative to Chlore
        task_chlore.execute("http://loicberthelot.freeboxos.fr/device/Chlore/","GET");
        try {
            //get when the request is finished
            String response = task_chlore.get();
            Log.i("Content",response);

            JSONObject json = new JSONObject(response);
            String chlore_mesure = df.format(json.getDouble("mesure"));

            chlore_output.setText(chlore_mesure.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Relative to temperature
        task_temp.execute("http://loicberthelot.freeboxos.fr/device/Temperature/","GET");
        try {
            //get when the request is finished
            String response = task_temp.get();
            Log.i("Content",response);

            JSONObject json = new JSONObject(response);
            String temp_mesure = df.format(json.getDouble("mesure"));

            temp_output.setText(temp_mesure.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}