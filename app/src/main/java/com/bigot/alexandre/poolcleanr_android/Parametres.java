package com.bigot.alexandre.poolcleanr_android;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Parametres extends Fragment {
    private Boolean data_change;
    private EditText firstname;
    private EditText lastname;
    private Button savebutton ;
    private CheckBox heated;
    private String userid;
    private String poolid;

    private NumberPicker numberpicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_parametres, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Parametres");
        numberpicker = (NumberPicker) getView().findViewById(R.id.numberPicker);
        numberpicker.setMaxValue(100);
        numberpicker.setMinValue(0);

        data_change=false;
        //declare all object from view
        firstname = (EditText) getView().findViewById(R.id.firstname);
        lastname = (EditText) getView().findViewById(R.id.lastname);
        heated =(CheckBox)getView().findViewById(R.id.checkBox) ;
        savebutton= (Button) getView().findViewById(R.id.savebutton);

        //add gets
        Connectivity task_user = new Connectivity();
        Connectivity task_pool = new Connectivity();

        // Relative to user data
        task_user.execute("http://loicberthelot.freeboxos.fr/user","GET");
        try {
            //get when the request is finished
            String response = task_user.get();

            JSONObject json = new JSONObject(response);
            String firstname = json.getString("firstname");
            String lastname = json.getString("lastname");
            this.userid = json.getString("_id");


            this.firstname.setText(firstname.toString());
            this.lastname.setText(lastname.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Relative to pool data
        task_pool.execute("http://loicberthelot.freeboxos.fr/user/pool","GET");
        try {
            //get when the request is finished
            String response = task_pool.get();

            JSONObject json = new JSONObject(response);
            String size = json.getString("size");
            Boolean heated= json.getBoolean("heated");
            this.poolid = json.getString("_id");
            numberpicker.setValue(Integer.parseInt(size));
            this.heated.setChecked(heated);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //add all listenners to theses objects
        firstname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        lastname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //change color of the save button to notify the values has changes
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        this.heated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }
        });

        numberpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                savebutton.setBackgroundResource(getResources().getIdentifier("@drawable/myunsavebutton", "drawable", getActivity().getPackageName()));
                data_change=true;
            }
        });

        //add listenner to save button
        final Button save_btn =(Button) getView().findViewById(R.id.savebutton);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_btn.setBackgroundResource(R.drawable.mysavebutton);

                Connectivity task_user = new Connectivity();
                Connectivity task_pool = new Connectivity();

                JSONObject userjson= new JSONObject();
                JSONObject pooljson= new JSONObject();
                try {
                    userjson.put("firstname",firstname.getText().toString());
                    userjson.put("lastname",lastname.getText().toString());
                    userjson.put("id",userid.toString());

                    pooljson.put("size",numberpicker.getValue());
                    pooljson.put("heated",heated.isChecked());
                    pooljson.put("id",poolid.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                task_user.execute("http://loicberthelot.freeboxos.fr/user","POST",userjson.toString());
                task_pool.execute("http://loicberthelot.freeboxos.fr/user/pool","POST",pooljson.toString());

                Toast toast = Toast.makeText(getContext(), "Modifications saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}