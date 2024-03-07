package com.example.pacepal;

/*A class representing the accelerometer functionality,
responsible for recording steps. */

// Import necessary Android hardware and context libraries
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

// Define a class that will handle step counting, implementing SensorEventListener for receiving sensor updates
public class StepCounterService implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private final float[] gravity = new float[3]; // Gravity components, now as an instance variable
    private int stepCount = 0; // Counts the number of steps detected

    // Constructor
    public StepCounterService(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    // Called when there is a new sensor event, i.e., new accelerometer data
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Ensure we're reacting to accelerometer updates
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Extract the x, y, and z values from the sensor event
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // With raw data extracted, move to preprocessing
            preprocessData(x, y, z);
        }
    }

    // Placeholder method for handling accuracy changes - not used here but required by interface
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Preprocess data to filter out noise and prepare for feature extraction
    private void preprocessData(float x, float y, float z) {
        final float alpha = 0.8f; // Smoothing constant for low-pass filter

        // Apply low-pass filter to isolate gravity component
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;

        // Subtract gravity from raw acceleration to get linear acceleration
        float linearX = x - gravity[0];
        float linearY = y - gravity[1];
        float linearZ = z - gravity[2];

        // Processed data ready for feature extraction
        extractFeatures(linearX, linearY, linearZ);
    }

    // Extract relevant features from the preprocessed data for step detection
    private void extractFeatures(float linearX, float linearY, float linearZ) {
        // Calculate the magnitude of acceleration vector
        double magnitude = Math.sqrt(linearX * linearX + linearY * linearY + linearZ * linearZ);

        // Send magnitude for step classification
        classifyStep(magnitude);
    }

    // Classify whether the given magnitude of acceleration indicates a step
    private void classifyStep(double magnitude) {
        // Define a threshold for detecting a step. Adjust based on experimentation
        final double STEP_THRESHOLD = 10.0;

        // If magnitude exceeds threshold, increment step count
        if (magnitude > STEP_THRESHOLD) {
            stepCount++;
            // Here, you could update the UI or perform other actions with the step count
        }
    }

    // Ensure to unregister the listener when it's no longer needed to conserve battery
    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }
}