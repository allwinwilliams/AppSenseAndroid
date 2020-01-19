package com.example.appsense;

public class SensorValueProcessor {
    public static float[] process(float[] values){
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float[] processed_values = {x, y, z};
        return processed_values;
    }
}
