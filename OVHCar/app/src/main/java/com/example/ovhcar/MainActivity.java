package com.example.ovhcar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovhcar.control.Control;
import com.example.ovhcar.programmingForKids.ProgrammingForKids;
import com.example.ovhcar.settings.SettingsActivity;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    android.app.AlertDialog dialog;
    RelativeLayout StartEngine, gas, on, self_drive, flash, locks, hazards_btn, honk_btn;
    TextView engine_stat, autonomous_status,interior,data;
    ImageView settings;

    TextView check_honk, check_locks, check_hazards, check_flash;
    ImageView ic_iconfinder_honk,programming_for_kifds;
    String hazards = "on";
    String honk = "on";
    String lock = "on";
    String power_on = "on";
    String flashs = "on";
    String self_driving = "on";
    String accerelation = "on";


    String direction;

    public static String BLUETOOTH_ADDRESS = "00:21:13:00:10:9A"; //R-toy (Robotics Toy)

    BluetoothAdapter adapter;

    //public static String BLUETOOTH_ADDRESS = "2C:CC:15:85:D9:32";
    String address = null;
    private ProgressDialog progress;
    public static BluetoothAdapter myBluetooth = null;
    public static BluetoothSocket btSocket = null;
    public boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_activity);


        changeStatusBarColor();


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /*actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        // setting the action bar title color
        actionBar.setTitle(Html.fromHtml("<font color='#000000'><b>OVH Car</font>")); */

        //changing the back arrow color
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.bluewish), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        //receive the address of the bluetooth device
        Intent newint = getIntent();

        address = newint.getStringExtra(MainActivity.BLUETOOTH_ADDRESS);

        adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.enable();


        try {
            createBond();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (btSocket == null) {
            new ConnectBT().execute();
        } else {


        }


        settings = findViewById(R.id.settings);
        programming_for_kifds = findViewById(R.id.programming_for_kifds);
        StartEngine = findViewById(R.id.StartEngine);
        self_drive = findViewById(R.id.self_drive);
        gas = findViewById(R.id.gas);
        on = findViewById(R.id.on);
        interior = findViewById(R.id.interior);
        data = findViewById(R.id.data);

        interior.setOnClickListener(_interior);
        data.setOnClickListener(_data);
        

        engine_stat = findViewById(R.id.engine_stat);
        autonomous_status = findViewById(R.id.autonomous_stat);
        

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        programming_for_kifds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( MainActivity.this,ProgrammingForKids.class);
                startActivity(intent);
            }
        });


        StartEngine.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                final String gateState = "4";

                String clutch_down_swit_1 = "B";
                String clutch_down_swit_2 = "C";


                StartEngine.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ripple_startengine));
                engine_stat.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.status_checked_styling));


                if (btSocket != null) {


                    try {

                        btSocket.getOutputStream().write((clutch_down_swit_1).getBytes());
                        btSocket.getOutputStream().write((clutch_down_swit_2).getBytes());


                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                //do something

                                try {
                                    btSocket.getOutputStream().write((gateState).getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 5500);//time in milisecond


                        // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();

                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {

                }


                return true;
            }
        });


        StartEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String gateState = "3";
                String clutch_up_swt_1 = "A";
                String clutch_up_swt_2 = "D";

                StartEngine.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ripple_stopengine));
                engine_stat.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.status_style));


                if (btSocket != null) {


                    try {


                        btSocket.getOutputStream().write((clutch_up_swt_1).getBytes());
                        btSocket.getOutputStream().write((clutch_up_swt_2).getBytes());
                        btSocket.getOutputStream().write((gateState).getBytes());


                        // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();

                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }


                } else {

                }


            }
        });


        on.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {


                if (power_on.equals("on"))
                {
                    String gateState = "2";
                    on.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ripple_startengine));
                    power_on = "off";

                    if (btSocket != null) {


                        try {
                            btSocket.getOutputStream().write((gateState).getBytes());
                            // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();

                            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    } else {

                    }


                } else
                {


                    String gateState = "1";
                    power_on = "on";
                    on.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.shapes_styling));
                    StartEngine.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ripple_stopengine));


                    if (btSocket != null) {


                        try {
                            btSocket.getOutputStream().write((gateState).getBytes());
                            // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();

                            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    } else {

                    }

                }


            }
        });


        gas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start


                        String swt1 = "N";
                        String swt2 = "R";

                        gas.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ripple_startengine));

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

                        String swt1_off = "L";
                        String swt2_off = "W";
                        gas.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.shapes_styling));

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


        self_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (self_driving.equals("on")) {
                    String gateState = "2";
                    self_driving = "off";
                    self_drive.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.autonomous_styling));
                    autonomous_status.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.autonomous_checked_styling));


                    if (btSocket != null) {


                        try {

                            btSocket.getOutputStream().write((gateState).getBytes());
                            String clutch_up_swt_1 = "A";
                            String clutch_up_swt_2 = "D";


                            String swt1_acceleration = "N";
                            String swt2_acceleration = "R";


                            String swt1_off = "L";


                            btSocket.getOutputStream().write((clutch_up_swt_1).getBytes());
                            btSocket.getOutputStream().write((clutch_up_swt_2).getBytes());

                            try {
                                //set time in mili
                                Thread.sleep(2500);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            btSocket.getOutputStream().write((swt1_acceleration).getBytes());
                            btSocket.getOutputStream().write((swt2_acceleration).getBytes());


                            try {
                                //set time in mili
                                Thread.sleep(1500);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            btSocket.getOutputStream().write((swt1_off).getBytes());

                            try {
                                //set time in mili
                                Thread.sleep(1500);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            btSocket.getOutputStream().write((swt1_acceleration).getBytes());
                            btSocket.getOutputStream().write((swt2_acceleration).getBytes());

                            try {
                                //set time in mili
                                Thread.sleep(1500);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            btSocket.getOutputStream().write((swt1_off).getBytes());

                            try {
                                //set time in mili
                                Thread.sleep(1500);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            btSocket.getOutputStream().write((swt1_acceleration).getBytes());
                            btSocket.getOutputStream().write((swt2_acceleration).getBytes());


                            // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();

                            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    } else {

                    }


                } else {


                    String gateState = "1";
                    String clutch_down_swit_1 = "B";
                    String clutch_down_swit_2 = "C";

                    String swt1_acceleration_up = "L";
                    String swt2_acceleration_up = "W";

                    self_driving = "on";
                    self_drive.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.shape));
                    autonomous_status.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.status_style));


                    if (btSocket != null) {


                        try {


                            btSocket.getOutputStream().write((swt1_acceleration_up).getBytes());
                            btSocket.getOutputStream().write((swt2_acceleration_up).getBytes());


                            try {
                                //set time in mili
                                Thread.sleep(2500);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            btSocket.getOutputStream().write((clutch_down_swit_1).getBytes());
                            btSocket.getOutputStream().write((clutch_down_swit_2).getBytes());


                            btSocket.getOutputStream().write((gateState).getBytes());
                            // Toast.makeText(getApplicationContext(), "Gate Closing", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();

                            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    } else {

                    }

                }


            }
        });


    }
    View.OnClickListener _interior = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Control.class);
            startActivity(intent);
        }
    };

    View.OnClickListener _data= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {

            //BluetoothDemo.this.AppKeytextview.setText("Connecting.., Please wait!!!");AppKeytextview.setTextColor(Color.rgb(155,155,155));
            progress = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
            progress.setMessage("Please wait");
            progress.setCancelable(false);
            progress.show();


        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {


                if (btSocket == null || !isBtConnected) {


                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device

                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(MainActivity.BLUETOOTH_ADDRESS);//connects to the device's address and checks if it's available


                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                    try {
                        btSocket.connect();


                    } catch (IOException econnect) {
                        ConnectSuccess = false;


                    }


                    progress.dismiss();
                    //ledControl.this.msg("Socket conected");
                    //Toast.makeText(getApplicationContext(),"TEST",Toast.LENGTH_LONG).show();
                } else {


                    progress.dismiss();

                    //ledControl.this.msg("Soccet NOT conected");
                }
            } catch (IOException e) {
                //progress.dismiss();
                ConnectSuccess = false;//if the try failed, you can check the exception here
                //msg("Soccet NOT conected");

                MainActivity.this.msg(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            progress.dismiss();
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                //msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                // BluetoothDemo.this.AppKeytextview.setText("Reconnect..");AppKeytextview.setTextColor(Color.GREEN);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Please turn on Car computer box.")
                        .setTitle("Unable to connect")
                        .setCancelable(false)
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        })
                        .setPositiveButton("Try again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        new ConnectBT().execute();


                                    }
                                }

                        );

                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#B89A5E"));
                alert.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B89A5E"));

                // finish();
            } else {
                //msg("Connected.");
                // BluetoothDemo.this.AppKeytextview.setText("Connected.");AppKeytextview.setTextColor(Color.rgb(0,255,0));
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                isBtConnected = true;
            }
            //progress.dismiss();
        }
    }

    public static final void msg(String s) {
        //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        // AppKeytextview.setText(s);AppKeytextview.setTextColor(Color.rgb(0,255,0));
    }

    public boolean createBond()
            throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(BLUETOOTH_ADDRESS);
        return returnValue.booleanValue();
    }


}
