package com.example.max.myshake;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by max on 24.06.16.
 */
public class SensorDataProcessor {

    FFT fft;
    int sumPlots = 64;
    ArrayList<SensorData> sensorDataset;
    ArrayList<SensorData> referenceDataset;

    double acceptable_error_low = 200.0d;
    double acceptable_error_high = 400.0d;

    ReferenceData earthquakeData = new ReferenceData();

    double[] x1 = new double[sumPlots];
    double[] y1 = new double[sumPlots];

    // reference array
    double[] x2 = new double[sumPlots];
    double[] y2 = new double[sumPlots];

    public SensorDataProcessor(){
        sensorDataset = new ArrayList<>(sumPlots);
        referenceDataset = new ArrayList<>(sumPlots);
        referenceDataset = earthquakeData.getData();

        fft = new FFT(sumPlots);

        x2 = new double[sumPlots];
        y2 = new double[sumPlots];

        for (int i = 0; i < sumPlots; ++i) {
            SensorData d1 = this.referenceDataset.get(i);
            x2[i] = (double) calculateMaginitude(d1);

        }
        Arrays.fill(y2, 0.0d);
        fft.fft(x2, y2);
    }

    public void clearData(){
        sensorDataset.clear();
        sensorDataset = new ArrayList<>(sumPlots);
    }

    public void addData(SensorData d){
        sensorDataset.add(d);
        // keep memory in mind
        if (sensorDataset.size() > sumPlots+1){
            sensorDataset.remove(0);
        }
    }

    public float calculateMaginitude(SensorData d){
        double x = (double)d.getX();
        double y = (double)d.getY();
        double z = (double)d.getZ();
        return (float)Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
    }

    public boolean calculate(){
        if(sensorDataset.size() > sumPlots) {

            x1 = new double[sumPlots];
            y1 = new double[sumPlots];
            //calc the magnitude of the sensor data
            for (int i = 0; i < sumPlots; ++i) {
                SensorData d1 = this.sensorDataset.get(i);
                x1[i] = (double) calculateMaginitude(d1);
            }
            Arrays.fill(y1, 0.0d);
            //do the fft
            fft.fft(x1, y1);

            return detectSimmilarity(x1, x2);
        }

        return false;
    }

    public boolean detectSimmilarity(double[] x1, double[] x2){

        int sum_sq = 0;
        double mse;
        int len = x1.length;

        for (int i = 0; i < len; ++i)
        {
            double p1 = x1[i];
            double p2 = x2[i];
            double err = p2 - p1;
            //System.out.println("Each: " + err);
            sum_sq += (err * err);
        }
        mse = (double)sum_sq / (len);

        //System.out.println(mse);

        if (mse > acceptable_error_low && mse < acceptable_error_high){
            return true;
        } else {
            return false;
        }
    }
}
