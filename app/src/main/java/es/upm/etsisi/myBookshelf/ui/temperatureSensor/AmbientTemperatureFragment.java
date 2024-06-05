package es.upm.etsisi.myBookshelf.ui.temperatureSensor;

import androidx.lifecycle.ViewModelProvider;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import es.upm.etsisi.myBookshelf.Firebase.Firebase_Utils;
import es.upm.etsisi.myBookshelf.R;
import es.upm.etsisi.myBookshelf.databinding.FragmentAmbientTemperatureBinding;

public class AmbientTemperatureFragment extends Fragment implements SensorEventListener  {

    //private MainViewModel mViewModel;
    private SensorManager sensorManager;
    private Sensor temperature;
    private FragmentAmbientTemperatureBinding binding;

    private String TEMPERATURE_PATH = "temperature";

    private View view;

public static AmbientTemperatureFragment newInstance() {
    return new AmbientTemperatureFragment();}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) Objects.requireNonNull(getContext()).getSystemService(Context.SENSOR_SERVICE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAmbientTemperatureBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        //binding.currentTemperature.setText(String.format(Locale.getDefault(), "%.2f", 100.0));
        view = inflater.inflate(R.layout.fragment_ambient_temperature, container, false);

        // Generate a random temperature value
        //float randomTemperature = new Random().nextFloat() * 100;
        //TextView temperatureView = (TextView) view.findViewById(R.id.current_temperature);
        //temperatureView.setText(String.format(Locale.getDefault(), "%.2f", randomTemperature));

        // Save the random temperature to Firebase
       //saveTemperatureToFirebase(randomTemperature);

        // Update the graph
        //updateTemperatureGraph();

        return view;
    }

        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do something here if sensor accuracy changes.
        }

        @Override
        public final void onSensorChanged(SensorEvent event) {
            float currentTemperature = event.values[0];
            TextView temperatureView = (TextView) view.findViewById(R.id.current_temperature);
            temperatureView.setText(String.format(Locale.getDefault(), "%.2f", currentTemperature));

            // Save the current temperature to Firebase
            saveTemperatureToFirebase(currentTemperature);

            // Update the graph
            updateTemperatureGraph();
        }

        @Override
        public void onResume() {
            // Register a listener for the sensor.
            super.onResume();
            sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        }

    @Override
    public void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
    }

private void saveTemperatureToFirebase(float temperature) {
    // Get a reference to the Firebase database
    DatabaseReference userTemperatureHistory = Firebase_Utils.getRootFirebase().child(TEMPERATURE_PATH);

    // Save the current temperature to the database
    userTemperatureHistory.push().setValue(temperature);
}

private void updateTemperatureGraph() {
    // Get a reference to the Firebase database
    DatabaseReference userTemperatureHistory = Firebase_Utils.getRootFirebase().child(TEMPERATURE_PATH);

    // Retrieve the temperature history
    userTemperatureHistory.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                // Get a reference to the Firebase database
                DatabaseReference userTemperatureHistory = Firebase_Utils.getRootFirebase().child(TEMPERATURE_PATH);

                // Retrieve the temperature history
                userTemperatureHistory.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Create a new series for the graph
                        List<Entry> entries = new ArrayList<>();
                        float lastX = 0.0f;

                        // Add each temperature to the series
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            float temperature = snapshot.getValue(Float.class);
                            entries.add(new Entry(lastX++, temperature));
                        }

                        // Create a dataset
                        LineDataSet dataSet = new LineDataSet(entries, "Temperature");
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    });
}
}
