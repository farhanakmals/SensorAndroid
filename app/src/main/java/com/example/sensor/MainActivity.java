package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import java.security.PrivilegedAction;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private Sensor mSensorProximity, mSensorLight;

    private TextView mTextSensorLight, mTextSensorProximity;

    private ScrollView mScrollViewBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // untuk menampung list apa saja yang ada di device
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor. TYPE_ALL);

        // pindah string biar tau sensornya apa saja
        StringBuilder sensorText = new StringBuilder();

        // di loop karena sensorlist nya berupa list
        for (Sensor currentSensor : sensorList) {
            sensorText.append(currentSensor.getName()).append(System.getProperty("line.separator"));
        }

        TextView sensorTextView = findViewById(R.id.sensor_list);
        sensorTextView.setText(sensorText);

        mTextSensorLight = findViewById(R.id.lable_light);
        mTextSensorProximity = findViewById(R.id.lable_proximity);

        mSensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        String sensor_error = "no sensor";
        if (mSensorLight == null) {
            mTextSensorLight.setText(sensor_error);
        }
        if (mSensorProximity == null) {
            mTextSensorProximity.setText(sensor_error);
        }
    }

    @Override
    //register sensor disini
    protected void onStart() {
        super.onStart();
        if (mSensorProximity != null) {
            sensorManager.registerListener(this, mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorLight != null) {
            sensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    // unregister disini
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    // jika ada perubahan data, dapat mengetahiu disini
    public void onSensorChanged(SensorEvent event) {
        // dapatkan sensor type nya terlebih dulu
        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];
        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(String.format("Light sensor : %1$.2f", currentValue));
                changeBackgroundolor(currentValue);
                break;
            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(String.format("Proximity sensor : %1$.2f", currentValue));
                break;
            default:
        }
    }

    private void changeBackgroundolor(float currentValue) {
        ConstraintLayout layout = findViewById(R.id.layout_constraint);
        if (currentValue > 20000 && currentValue < 30000) {
            layout.setBackgroundColor(Color.RED);
        } else if (currentValue > 0 && currentValue <20000) {
            layout.setBackgroundColor(Color.BLUE);
        } else if (currentValue > 20000 && currentValue < 40000) {
            layout.setBackgroundResource(R.drawable.img);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}