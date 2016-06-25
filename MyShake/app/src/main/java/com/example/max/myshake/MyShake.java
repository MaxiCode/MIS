package com.example.max.myshake;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MyShake extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    FftDataView fftDataView;
    SensorDataProcessor processor;
    SeismicState state;
    TextView output;
    Button printBtn;

    ArrayList<SensorData> dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shake);

        // read Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),100000);

        // init Sensor Data processor
        processor = new SensorDataProcessor();
        state = new SeismicState();

        //
        fftDataView = (FftDataView) findViewById(R.id.fftView);
        fftDataView.setBackgroundColor(Color.GRAY);

        output = (TextView) findViewById(R.id.textViewOutput);

        printBtn = (Button) findViewById(R.id.buttonPrint);

        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < dataSet.size(); ++i) {
                    SensorData item = dataSet.get(i);
                    System.out.println("Dataset = X: " + item.getX() + " Y: " + item.getY() + " Z: " + item.getZ());
                }
            }
        });
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            SensorData data = new SensorData(event.values[0], event.values[1], event.values[2]);

            this.fftDataView.addData(data);
            this.fftDataView.invalidate();
            dataSet = this.fftDataView.getDataset();

            this.processor.addData(data);
            boolean currState = this.processor.calculate();

            state.addState(currState);
        }


        if (state.isEarthquake()){
            output.setText("Erdbeben!!");
            output.setBackgroundColor(Color.RED);
        } else {
            output.setText("Kein Erdbeben.");
            output.setBackgroundColor(Color.GREEN);
        }
    }
}