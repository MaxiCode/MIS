package com.example.max.myshake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by max on 15.05.16.
 */
public class FftDataView extends SensorView {

    FFT fft;
    double[] x = new double[sumPlots];
    double[] y = new double[sumPlots];

    public FftDataView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        fft = new FFT(sumPlots);
    }

    protected void onDraw(Canvas canvas){
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.originY = (this.height - this.padding) - height/2;
        this.xAxEnd = this.width - this.padding;

        super.onDraw(canvas);

        if(sensorDataset.size() > sumPlots) {


            x = new double[sumPlots];
            y = new double[sumPlots];
            //calc the magnitude of the sensor data
            for (int i = 0; i < sumPlots; ++i) {
                SensorData d1 = this.sensorDataset.get(this.sensorDataset.size() - 1 - i);
                x[i] = (double) calculateMaginitude(d1);
            }
            Arrays.fill(y, 0.0d);
            //do the fft

            fft.fft(x, y);
            System.out.println("Start fft");
            double tmp[] = x;
            Arrays.sort(tmp);
            for (int i = 0; i<tmp.length; ++i){
                System.out.println(tmp[i]);
                ++i;
            }

            //calc the magnitude of the resulting real and imaginary parts
            //since we have both parts real and imaginary in the result the plot is mirrored in the middle
            //so it is necessary to cut the result in the middle and go on with only the half
            for (int i = 0; i < sumPlots/2; ++i) {
                y[i] = Math.sqrt(Math.pow(x[i], 2) + Math.pow(y[i], 2));
            }
            //draw the lines
            for (int i = 1; i < (sumPlots/2) - 1; ++i) {
                float l = (float) y[i];
                float r = (float) y[i + 1];
                drawSensorLine(i, l, r, canvas, Color.YELLOW);
            }
        }
    }

    public ArrayList<SensorData> getDataset (){
        return sensorDataset;
    }


    protected void drawSensorLine(int i, float val1, float val2, Canvas canvas, int color) {
        float posX1 = (i * (this.width / (sumPlots/2)));
        float posX2 = ((i + 1) * (this.width/(sumPlots/2)));

        float posY1 = this.height - (height/100)*val1;
        float posY2 = this.height - (height/100)*val2;

        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth(2.0f);
        canvas.drawLine(posX1, posY1, posX2, posY2, p);
    }
}