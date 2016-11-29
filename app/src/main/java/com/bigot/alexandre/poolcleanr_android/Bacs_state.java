package com.bigot.alexandre.poolcleanr_android;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
                getLevelBac();
                //Notification notif = new Notification();
                //notif.sendNotification("This is a Test. Thank you");
            }
        });

        // Open the bacs
        Button open_ph = (Button) getView().findViewById(R.id.ph_bac_btn);
        open_ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"pH -", "pH +"};

                final Connectivity task_bacPH = new Connectivity();

                new AlertDialog.Builder(getContext())
                        .setTitle("Bac de pH")
                        .setMessage("Souhaitez-vous ouvrir un bac de pH ?")
                        /*
                        // Radio Button pH plus et pH moins
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast toast = Toast.makeText(getContext(), items[arg1], Toast.LENGTH_LONG);
                                toast.show();
                            }
                        })*/
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                // Demande d'ouverture du bac de pH à la Raspberry
                                // La quantité nécessaire pour un retour à la normale doit être calculé (côté Raspberry ?)
                                int selected = ((AlertDialog)dialog).getListView().getSelectedItemPosition();
                                Toast toast = Toast.makeText(getContext(), "Selectionnné : " + selected, Toast.LENGTH_LONG);
                                toast.show();
                                */
                                JSONObject bacPH_json= new JSONObject();
                                try {
                                    bacPH_json.put("ordername","openbacph");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                task_bacPH.execute("http://loicberthelot.freeboxos.fr/order","POST",bacPH_json.toString());

                                // Toast d'information
                                Toast toast = Toast.makeText(getContext(), "Ouverture d'un bac de pH", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

                //Recalculate the level of the bacs
                getLevelBac();
            }
        });

        Button open_chlore = (Button) getView().findViewById(R.id.chlore_bac_btn);
        open_chlore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Connectivity task_bacChlore = new Connectivity();

                new AlertDialog.Builder(getContext())
                        .setTitle("Bac de Chlore")
                        .setMessage("Souhaitez-vous ouvrir le bac de chlore ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Demande d'ouverture du bac de Chlore
                                JSONObject bacChlore_json= new JSONObject();
                                try {
                                    bacChlore_json.put("ordername","openbacchlore");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                task_bacChlore.execute("http://loicberthelot.freeboxos.fr/order","POST",bacChlore_json.toString());

                                // Toast d'information
                                Toast toast = Toast.makeText(getContext(), "Ouverture du bac de chlore", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

                //Recalculate the level of the bacs
                getLevelBac();
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

        Connectivity task_ph = new Connectivity();
        Connectivity task_chlore = new Connectivity();

        // Relative to pH Moins, pH Plus and date
        task_ph.execute("http://loicberthelot.freeboxos.fr/device/pH/","GET");
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
            if(date==null||bacMoins==null||bacPlus==null)
            {
                throw new Exception("message goes here");
            }
            phMoins_txt.setText(bacMoins.toString() + "%");
            phPlus_txt.setText(bacPlus.toString() + "%");
            String dateToDisplay = date.toString();
            dateToDisplay = dateToDisplay.replace('T',' ');
            dateToDisplay = dateToDisplay.replace('Z',' ');
            date_txt.setText("relevé fait le : " + dateToDisplay);

            moinsProgress.setProgress(Integer.parseInt(bacMoins));
            plusProgress.setProgress(Integer.parseInt(bacPlus));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Relative to the bac of chlore
        task_chlore.execute("http://loicberthelot.freeboxos.fr/device/chlore/","GET");
        try {
            //get when the request is finished
            String response = task_chlore.get();
            Log.i("Content",response);

            JSONObject json = new JSONObject(response);
            json = json.getJSONObject("bac");
            String bacChlore = json.getString("remplissage");
            if(bacChlore==null)
            {
                throw new Exception("message goes here");
            }
            chlore_txt.setText(bacChlore.toString() + "%");
            chloreProgress.setProgress(Integer.parseInt(bacChlore));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}