package com.example.ovhcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import java.io.IOException;

public class Settings extends AppCompatActivity {




    private SpeedometerGauge speedometer;
    RelativeLayout acceleration;
    BluetoothSocket btSocket = null;
    RelativeLayout clutch,brakes;


    //Range range,range2,range3 ;
    //HalfGauge halfGauge;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        changeStatusBarColor();



        btSocket = MainActivity.btSocket;


       // Customize SpeedometerGauge
        speedometer = (SpeedometerGauge)findViewById(R.id.halfGauge);
        acceleration = findViewById(R.id.accelerator);
        clutch = findViewById(R.id.clutch);
        brakes = findViewById(R.id.brakes);

        speedometer.setMaxSpeed(50);

        speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });
        speedometer.setMaxSpeed(50);
        speedometer.setMajorTickStep(5);
        speedometer.setMinorTicks(4);
        speedometer.addColoredRange(0, 20, Color.GREEN);
        speedometer.addColoredRange(20, 40, Color.YELLOW);
        speedometer.addColoredRange(40, 50, Color.RED);
        //speedometer.setSpeed(33, 1000, 300);











        acceleration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start


                        speedometer.setSpeed(50, 1000, 300);



                        String swt1 ="N";
                        String swt2  ="R";




                        if (btSocket != null) {


                            try {
                                btSocket.getOutputStream().write((swt1).getBytes());
                                btSocket.getOutputStream().write((swt2).getBytes());
                                // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();

                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        } else {

                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        // End

                        speedometer.setSpeed(0, 1000, 300);

                        String swt1_off  ="L";
                        String swt2_off  ="W";

                        if (btSocket != null) {


                            try {
                                btSocket.getOutputStream().write((swt1_off).getBytes());
                                btSocket.getOutputStream().write((swt2_off).getBytes());
                                // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();

                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        } else {

                        }




                        break;
                }
                return false;
            }
        });





        clutch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start






                        String clutch_down_swit_1 ="B";
                        String clutch_down_swit_2  ="C";




                        if (btSocket != null) {


                            try {
                                btSocket.getOutputStream().write((clutch_down_swit_1).getBytes());
                                btSocket.getOutputStream().write((clutch_down_swit_2).getBytes());

                            } catch (IOException e) {
                                e.printStackTrace();



                            }
                        } else {

                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        // End


                        String clutch_up_swt_1  ="A";
                        String clutch_up_swt_2  ="D";

                        if (btSocket != null) {


                            try {
                                btSocket.getOutputStream().write((clutch_up_swt_1).getBytes());
                                btSocket.getOutputStream().write((clutch_up_swt_2).getBytes());
                                // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();

                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        } else {

                        }




                        break;
                }
                return false;
            }
        });






        brakes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start






                        String brake_down_swit_1 ="F";
                        String brake_down_swit_2  ="G";




                        if (btSocket != null) {


                            try {
                                btSocket.getOutputStream().write((brake_down_swit_1).getBytes());
                                btSocket.getOutputStream().write((brake_down_swit_2).getBytes());

                            } catch (IOException e) {
                                e.printStackTrace();



                            }
                        } else {

                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        // End


                        String brake_up_swt_1  ="E";
                        String brake_up_swt_2  ="H";

                        if (btSocket != null) {


                            try {
                                btSocket.getOutputStream().write((brake_up_swt_1).getBytes());
                                btSocket.getOutputStream().write((brake_up_swt_2).getBytes());
                                // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();

                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        } else {

                        }




                        break;
                }
                return false;
            }
        });






        //halfGauge = findViewById(R.id.halfGauge);


        //halfGauge.addRange(range);
        //halfGauge.addRange(range2);
        //halfGauge.addRange(range3);



        //range.setColor(Color.parseColor("#00b20b"));
        //range.setFrom(0.0);
        //range.setTo(50.0);



        //range2.setColor(Color.parseColor("#E3E500"));
        //range2.setFrom(50.0);
        //range2.setTo(100.0);



        //range3.setColor(Color.parseColor("#ce0000"));
        //range3.setFrom(100.0);
        //range3.setTo(150.0);


        //add color ranges to gauge
        //halfGauge.addRange(range);
        //halfGauge.addRange(range2);
        //halfGauge.addRange(range3);






    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }
}