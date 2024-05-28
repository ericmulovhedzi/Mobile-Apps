package com.example.ovhcar.programmingForKids;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ovhcar.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProgrammingForKids extends AppCompatActivity {

    Button btnCreateClass,btnCar,btnAnimals,btnFruits;
    LinearLayout class_list;
    TextView class_name,class_close;
    String _className;
    ListView list, selected_list,getters;
    ArrayList<String> _class_list;
    ArrayList<String> _parameters_list;
    ArrayList<String> _getters_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programming_for_kids);


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



        btnCreateClass = findViewById(R.id.create_class);
        class_list = findViewById(R.id.class_list);
        btnCar = findViewById(R.id.car);
        btnAnimals = findViewById(R.id.animals);
        btnFruits = findViewById(R.id.fruits);
        class_name = findViewById(R.id.class_name);
        class_close = findViewById(R.id.class_close);
        list = findViewById(R.id.list);
        getters = findViewById(R.id.getters);
        selected_list = findViewById(R.id.selected_list);

        _class_list = new ArrayList<>();
        _parameters_list = new ArrayList<>();
        _getters_list = new ArrayList<>();
        getClassList();



        class_list.setVisibility(View.GONE);
        list.setVisibility(View.GONE);

        btnCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createClassClicked();
            }
        });
        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _className = "Car";

                carBtnPressed();

            }
        });
        btnAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                _className = "Animals";
                AnimalBtnPressed();

            }
        });
        btnFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                _className = "Fruits";
                FruitBtnPressed();

            }
        });



    }
    private void createClassClicked(){
        class_list.setVisibility(View.VISIBLE);
        class_name.setText(getResources().getString(R.string.start_class));
        class_close.setText(getResources().getString(R.string.close_class));

    }
    private void carBtnPressed(){
        list.setVisibility(View.VISIBLE);
        btnAnimals.setVisibility(View.GONE);
        btnFruits.setVisibility(View.GONE);
        btnCar.setVisibility(View.VISIBLE);

        class_name.setText("public Class " + _className + " {");



    }
    private void FruitBtnPressed(){
        list.setVisibility(View.VISIBLE);
        btnCar.setVisibility(View.GONE);
        btnAnimals.setVisibility(View.GONE);
        btnFruits.setVisibility(View.VISIBLE);

        class_name.setText("public Class " + _className + " {");



    }
    private void AnimalBtnPressed(){
        list.setVisibility(View.VISIBLE);
        btnCar.setVisibility(View.GONE);
        btnAnimals.setVisibility(View.VISIBLE);
        btnFruits.setVisibility(View.GONE);

        class_name.setText("public Class " + _className + " {");

    }

    void getClassList()
    {
        _class_list.add("Name");
        _class_list.add("Color");
        _class_list.add("Make");
        _class_list.add("Model");
        _class_list.add("Gender");



        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, _class_list);
        // Set The Adapter
        list.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                String selected_list=_class_list.get(position);
                String nameToAdd = "private String " + selected_list + ";";


                if (_parameters_list.contains(nameToAdd)) {
                    Toast.makeText(getApplicationContext(),"Exists",Toast.LENGTH_LONG).show();
                } else {
                    _parameters_list.add(nameToAdd);

                    _getters_list.add( "public String get"+selected_list+"() { " +
                            "\n" +
                            "    return "+selected_list+"; " +
                            "\n" +
                            "} " +
                            "\n\n" +
                            "public void set"+selected_list+"(String "+selected_list+") { " +
                            "\n" +
                            "    this."+selected_list + "=" + selected_list+";" +
                            "\n" +
                            "} ");
                }





                paremeterData();
                getterSetters();
                //Toast.makeText(getApplicationContext(), "Movie Selected : "+selected_list,   Toast.LENGTH_LONG).show();
            }
        });
    }

    void paremeterData(){


        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, _parameters_list);
        // Set The Adapter
        selected_list.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        selected_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                //String selected_list=_class_list.get(position);

                //parameters_list.add("private String" + selected_list + ";");
                //Toast.makeText(getApplicationContext(), "Movie Selected : "+selected_list,   Toast.LENGTH_LONG).show();
            }
        });


    }
    void getterSetters(){


        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, _getters_list);
        // Set The Adapter
        getters.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        getters.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                //String selected_list=_class_list.get(position);





               // _getters_list.add("private String" + selected_list + ";");
                //Toast.makeText(getApplicationContext(), "Movie Selected : "+selected_list,   Toast.LENGTH_LONG).show();
            }
        });
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }
}