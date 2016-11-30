package com.bigot.alexandre.poolcleanr_android;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Historique extends Fragment {
    protected LineChart mChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Historique");

        this.createchart(R.id.graph_pH);
        this.createchart(R.id.graph_chlore);
        this.createchart(R.id.graph_temp);
    }

    public void createchart(int id)
    {
        mChart = (LineChart) getView().findViewById(id);
        //mChart.setOnChartGestureListener(this);
       // mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        //mv.setChartView(mChart); // For bounds control
        //mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
       /* LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);*/

       // XAxis xAxis = mChart.getXAxis();
       // xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line




       /* YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);*/

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(id);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

         // dont forget to refresh the drawing
        // mChart.invalidate();
    }
    private void setData(int id) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        String label;
        switch (id)
        {
            case R.id.graph_pH:
                values= this.askForHistory(id);
                label="pH";
                break;
            case R.id.graph_chlore:
                values= this.askForHistory(id);
                label="chlore";
                break;
            case R.id.graph_temp:
                values= this.askForHistory(id);
                label="temperature";
                break;
            default:
                values= this.askForHistory(id);
                label="unknowned";
        }

        if(values ==null)
        {
            return;
        }
        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, label);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                //set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }
    public ArrayList<Entry> askForHistory(int id)
    {
        //String url =   "http://loicberthelot.freeboxos.fr/device/";
        String url =   MainActivity.path_server + "device/";
        switch (id)
        {
            case R.id.graph_pH:
                url = url +"pH/historique";
                break;
            case R.id.graph_chlore:
                url = url +"chlore/historique";
                break;
            case R.id.graph_temp:
                url = url +"temperature/historique";
                break;
            default:
                url = url +"pH/historique";
        }
        Log.i("Request to:",url);
        ArrayList<Entry> values = new ArrayList<Entry>();
        Connectivity task = new Connectivity();
        task.execute(url,"GET");
        try {
            //get when the request is finished
            String response = task.get();
            JSONArray json = new JSONArray(response);
            Log.i("Length",String.valueOf(json.length()));
            for(int i=0;i<json.length();i++)
            {
                Log.i("data",String.valueOf(json.getJSONObject(i).getInt("mesure")));
                values.add(new Entry(i,json.getJSONObject(i).getInt("mesure")));
            }


            //String mesure = json.getJSONObject(0).getString("mesure");
           // String date =json.getJSONObject(0).getString("time_of_mesure");

           // mesure_output.setText(mesure.toString());
           // date_output.setText(date.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(values.isEmpty())
        {
            return null;
        }
        return values;
    }

    public void sync()
    {
        Log.i("Sync","clicked!!");
    }


}