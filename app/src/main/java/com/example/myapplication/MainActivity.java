package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button1);
        TextView textView = findViewById(R.id.text1);
        button.setOnClickListener(view -> textView.setText(thermal()));
    }
    public String thermal() {
        String temp, type;
        String string="";
        for (int i = 0; i < 29; i++) {
            temp = thermalTemp(i);
            if (!temp.contains("0.0")) {
                type = thermalType(i);
                if (type != null) {
                    string = string +"ThermalValues "+type+" : "+temp+"\n";
                }
            }
        }
        return string;
    }

    public String thermalTemp(int i) {
        Process process;
        BufferedReader reader;
        String line;
        String t = null;
        float temp = 0;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone" + i + "/temp");
            process.waitFor();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
            if (line != null) {
                temp = Float.parseFloat(line);
            }
            reader.close();
            process.destroy();
            if (!((int) temp == 0)) {
                if ((int) temp > 10000) {
                    t = Float.toString(temp / 1000);
                } else if ((int) temp > 1000) {
                    t = Float.toString(temp / 100);
                } else if ((int) temp > 100) {
                    t = Float.toString(temp / 10);
                }
            } else
                t = "0.0";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public String thermalType(int i) {
        Process process;
        BufferedReader reader;
        String line, type = null;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone" + i + "/type");
            process.waitFor();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
            if (line != null) {
                type = line;
            }
            reader.close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }
}
