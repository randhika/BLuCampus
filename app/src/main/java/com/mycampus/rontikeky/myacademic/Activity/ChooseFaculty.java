package com.mycampus.rontikeky.myacademic.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.mycampus.rontikeky.myacademic.R;

import java.util.Calendar;

public class ChooseFaculty extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";
    String faculty_key = "faculty";
    String fromDate_key = "fromDate";
    String toDate_key = "toDate";
    String token;
    String faculty;
    String fromDateS;
    String toDateS;

    Button btnFilter;
    LinearLayout from,to;
    EditText fromDate,toDate;
    boolean view;

    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;

    int year_x2,month_x2,day_x2;
    static final int DIALOG_ID2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_faculty);
        Button btnSimpan = (Button)findViewById(R.id.btnSelesai);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        toDate = (EditText)findViewById(R.id.toDate);
        fromDate = (EditText)findViewById(R.id.fromDate);
        from = (LinearLayout)findViewById(R.id.fromLayout);
        to = (LinearLayout)findViewById(R.id.toLayout);
        btnFilter = (Button)findViewById(R.id.filterDate);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.faculty, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");
        faculty = sharedPreferences.getString(faculty_key, "");
        fromDateS = sharedPreferences.getString(fromDate_key,"");
        toDateS = sharedPreferences.getString(toDate_key,"");

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        year_x2 = calendar.get(Calendar.YEAR);
        month_x2 = calendar.get(Calendar.MONTH);
        day_x2 = calendar.get(Calendar.DAY_OF_MONTH);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(fromDate_key);
        editor.remove(toDate_key);
        editor.commit();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view == false){
                    from.setVisibility(View.VISIBLE);
                    to.setVisibility(View.VISIBLE);
                    view = true;
                }else{
                    from.setVisibility(View.GONE);
                    to.setVisibility(View.GONE);
                    view = false;
                }
            }
        });


        if (sharedPreferences.contains(fromDate_key) && sharedPreferences.contains(toDate_key)){
            fromDate.setText(fromDateS.toString());
            toDate.setText(toDateS.toString());
        }else{
            fromDate.setText(year_x+"-"+month_x+"-"+day_x);
            toDate.setText(year_x2+"-"+month_x2+"-"+day_x2);
        }


        showDialogOnClick();
        showDialogOnClick2();



        if (faculty.equalsIgnoreCase("BAAK")){
            Log.d("FILTER 1", "LOAD BAAK");
            spinner.setSelection(adapter.getPosition("BAAK"));
        } else if (faculty.equalsIgnoreCase("FTI")){
            Log.d("FILTER 2","LOAD FTI");
            spinner.setSelection(adapter.getPosition("FTI"));
        } else if (faculty.equalsIgnoreCase("FT")){
            Log.d("FILTER 2", "LOAD FT");
            spinner.setSelection(adapter.getPosition("FT"));
        }else if(faculty.equalsIgnoreCase("FE")){
            Log.d("FILTER 2", "LOAD FE");
            spinner.setSelection(adapter.getPosition("FE"));
        }else if(faculty.equalsIgnoreCase("FIKOM")){
            Log.d("FILTER 2", "LOAD FIKOM");
            spinner.setSelection(adapter.getPosition("FIKOM"));
        }else if(faculty.equalsIgnoreCase("FISIP")){
            Log.d("FILTER 2", "LOAD FISIP");
            spinner.setSelection(adapter.getPosition("FISIP"));
        } else {
            Log.d("FILTER 2", "LOAD BAAK");
            spinner.setSelection(adapter.getPosition("BAAK"));
        }

        spinner.setOnItemSelectedListener(this);

        getSupportActionBar().setTitle("Pilih Fakultas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view == true){
                    sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(fromDate_key, fromDate.getText().toString());
                    editor.putString(toDate_key,toDate.getText().toString());
                    editor.commit();
                }

                Intent intent = new Intent(ChooseFaculty.this,MainFeed.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void showDialogOnClick(){


        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID){
            return new DatePickerDialog(this, dpickerdialog, year_x,month_x,day_x);
        }

        if (id == DIALOG_ID2){
            return new DatePickerDialog(this, dpickerdialog2, year_x,month_x,day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerdialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month+1;
            day_x = dayOfMonth;
            fromDate.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };

    public void showDialogOnClick2(){


        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID2);
            }
        });
    }


    private DatePickerDialog.OnDateSetListener dpickerdialog2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x2 = year;
            month_x2 = month+1;
            day_x2 = dayOfMonth;
            toDate.setText(year_x2+"-"+month_x2+"-"+day_x2);
        }
    };

    @Override
        public boolean onSupportNavigateUp() {
            finish();
            return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(faculty_key, item);
        editor.commit();
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
