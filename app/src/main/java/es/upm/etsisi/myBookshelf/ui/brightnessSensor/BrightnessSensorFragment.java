package es.upm.etsisi.myBookshelf.ui.brightnessSensor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.upm.etsisi.myBookshelf.Firebase.Firebase_Utils;
import es.upm.etsisi.myBookshelf.R;
import es.upm.etsisi.myBookshelf.databinding.FragmentBrightnessSensorBinding;

public class BrightnessSensorFragment extends Fragment implements SensorEventListener  {

    //private MainViewModel mViewModel;
    private SensorManager sensorManager;
    private Sensor brigthnessSensor;
    private FragmentBrightnessSensorBinding binding;

    private final String BRIGHTNESS_PATH = "brightness";

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) Objects.requireNonNull(getContext()).getSystemService(Context.SENSOR_SERVICE);
        brigthnessSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, brigthnessSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBrightnessSensorBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        view = inflater.inflate(R.layout.fragment_brightness_sensor, container, false);
        init();
        return view;
    }

    float lastX = 0.0f;

    public void init() {
        DatabaseReference userBrightnessHistory = Firebase_Utils.getRootFirebase().child(BRIGHTNESS_PATH);

        userBrightnessHistory.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // Create a new series for the graph
                List<Entry> entries = new ArrayList<>();

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    float brightness = snapshot.getValue(Float.class);
                    entries.add(new Entry(lastX++, brightness));
                }

                // Create a dataset
                LineDataSet dataSet = new LineDataSet(entries, "Brightness");
                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextColor(Color.BLACK);

                // Get a reference to the LineChart
                LineChart chart = (LineChart) view.findViewById(R.id.chart);
                chart.clear();
                // Clear the old data and add the new series
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate(); // refresh the chart
            }
        });

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float currentBrightness = event.values[0];
        TextView brightnessView = (TextView) view.findViewById(R.id.current_brightness);
        brightnessView.setText(String.format(Locale.getDefault(), "%.2f", currentBrightness));
        saveSensorDataToFirebase(currentBrightness);
        updateSensorGraph(currentBrightness);
    }

    @Override
    public void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, brigthnessSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void saveSensorDataToFirebase(float brightness) {
        // Get a reference to the Firebase database
        DatabaseReference userSensorHistory = Firebase_Utils.getRootFirebase().child(BRIGHTNESS_PATH);

        // Save the current brightness to the database
        userSensorHistory.push().setValue(brightness);
    }

    private void updateSensorGraph(float brightness) {
        LineChart chart = (LineChart) view.findViewById(R.id.chart);
        if (chart.getLineData() == null)
            return;

        LineData lineData = chart.getLineData();
        lineData.addEntry(new Entry(lineData.getEntryCount(), brightness), 0);
        chart.clear();
        chart.setData(lineData);
        chart.invalidate();
    }
}
