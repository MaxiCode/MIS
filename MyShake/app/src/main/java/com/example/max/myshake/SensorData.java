package com.example.max.myshake;

/**
 * Created by max on 17.06.16.
 */
public class SensorData {

    private float x = 0.0f;
    private float y = 0.0f;
    private float z = 0.0f;

    public SensorData(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getZ(){
        return this.z;
    }

}
