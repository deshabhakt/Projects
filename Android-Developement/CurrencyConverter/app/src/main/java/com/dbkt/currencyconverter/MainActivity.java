package com.dbkt.currencyconverter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dbkt.currencyconverter.data.MyDbHandler;
import com.dbkt.currencyconverter.model.DbRow;
import com.dbkt.currencyconverter.params.Params;
import com.javapapers.android.csvfileread.app.CSVFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomSpinner.OnSpinnerEventsListener, AdapterView.OnItemSelectedListener {

    private Spinner sp_country_1, sp_country_2;
    View view;
    private EditText editText_input_1, editText_input_2;
    private ImageView country_1_image, country_2_image;
    public Double country_1_rate,country_2_rate;
    List<String> countrynames;

    public MyDbHandler db;

    boolean flag = true;        // for switching between input 1 and input 2

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_input_1 = findViewById(R.id.country_1_input);
        editText_input_2 = findViewById(R.id.country_2_input);

        Button bt_reset = findViewById(R.id.bt_reset);
        Button bt_swap = findViewById(R.id.bt_swap);

        country_1_image = findViewById(R.id.country_1_image);
        country_2_image = findViewById(R.id.country_2_image);


        sp_country_1 = findViewById(R.id.spinner_country_1);
        sp_country_2 = findViewById(R.id.spinner_country_2);

        sp_country_1.setOnItemSelectedListener(this);
        sp_country_2.setOnItemSelectedListener(this);




        bt_reset.setOnClickListener(this::onClick_reset);
        bt_swap.setOnClickListener(this::onClick_swap);

        db = new MyDbHandler(this);
        int currentDay = Integer.parseInt(Calendar.getInstance().getTime().toString().split(" ")[2]);
        int last_mod_date = db.get_day_of_first_entry();

        int day = (int)(Math.ceil(last_mod_date+7)%30);
        if(db.isEmpty()){
            makeDB();

            String modified_timestamp = "0000-00-0"+currentDay;
            db.update_rate_base_timeStamp("AFN",0,null,modified_timestamp);

            Log.d("sql",modified_timestamp);

            Toast.makeText(this, "I am here in if "+day+"  "+last_mod_date+"  "+currentDay, Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("sql",""+day+"  "+last_mod_date);
            if ( day== currentDay) {
                Toast.makeText(this, "I am here in else if 1", Toast.LENGTH_SHORT).show();
                Log.d("sql", "I am here in else if "+day+"  "+last_mod_date+"  "+currentDay);

                callAPI();
                String modified_timestamp = "0000-00-0"+currentDay;
                Log.d("sql",modified_timestamp);
                db.update_rate_base_timeStamp("AFN",0,null,modified_timestamp);
            }
            else{
                Log.d("sql",""+day+"  "+last_mod_date+"  "+currentDay);
                Toast.makeText(this, "I am here in else else "+day+"  "+last_mod_date+"  "+currentDay, Toast.LENGTH_SHORT).show();
            }
        }

        countrynames = db.getColumn(Params.KEY_LONG_NAME);

//        countryAdapter adapter1 = new countryAdapter(this,countrynames);
//        sp_country_1.setAdapter(adapter1);
//
//        countryAdapter adapter2 = new countryAdapter(this,countrynames);
//        sp_country_2.setAdapter(adapter2);
        sp_country_1.setPopupBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_popdown_window));
        sp_country_2.setPopupBackgroundDrawable(getResources().getDrawable(R.drawable.bg_spinner_popdown_window));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,countrynames);
        sp_country_1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,countrynames);
        sp_country_2.setAdapter(adapter2);


        editText_input_1.setOnClickListener(v -> {
            editText_input_1.setInputType(InputType.TYPE_CLASS_NUMBER);
            flag = true;
        });

        editText_input_2.setOnClickListener(v -> {
            editText_input_2.setInputType(InputType.TYPE_CLASS_NUMBER);
            flag = false;
        });

        editText_input_1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (flag)
                    calculate(view);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                flag = true;
            }
        });

        editText_input_2.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!flag)
                    calculate(view);
            }

            @Override
            public void afterTextChanged(Editable s) {
                flag = false;
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] st;
        if(parent.getId()==R.id.spinner_country_1){
            String countryname_1 = parent.getItemAtPosition(position).toString();
            try {
                // db.getFlagLocationAndRate returns flag location and rate as a string separated by "_"
                st = db.getFlagLocationAndRate(countryname_1).split("#");
                country_1_rate = Double.parseDouble(st[1]);
                country_1_image.setImageDrawable(makeDrawableFromImagePath(st[0]));

            }catch (Exception e){
                st = db.getFlagLocationAndRate(countryname_1).split("#");
                country_1_rate = Double.parseDouble(st[1]);
                country_1_image.setImageDrawable(makeDrawableFromImagePath("@drawable/default_flag"));
            }
        }

        if(parent.getId()==R.id.spinner_country_2){
            String countryname_2 = parent.getItemAtPosition(position).toString();
            try {
                st = db.getFlagLocationAndRate(countryname_2).split("#");
                country_2_rate = Double.parseDouble(st[1]);
                country_2_image.setImageDrawable(makeDrawableFromImagePath(st[0]));

            }catch (Exception e){
                st = db.getFlagLocationAndRate(countryname_2).split("#");
                country_2_rate = Double.parseDouble(st[1]);
                country_2_image.setImageDrawable(makeDrawableFromImagePath("@drawable/default_flag"));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    // For the top bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar_menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.info_button:

                startActivity(new Intent(MainActivity.this,info_page.class));
                break;

            case R.id.exit_button:

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setInverseBackgroundForced(true);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit ?");
                builder.setPositiveButton("Yes. Exit now!", (dialogInterface, i) -> finish());
                builder.setNegativeButton("Not now", (dialogInterface, i) -> dialogInterface.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    // my functions
    private void makeDB(){
        InputStream inputStream;
        CSVFile csvFile;
        List<String[]> country_list_from_csv;
        try {
            inputStream = getResources().openRawResource(R.raw.data);
        }
        catch (Exception e){
            inputStream = getResources().openRawResource(R.raw.data_prev);
        }

        csvFile = new CSVFile(inputStream);
        country_list_from_csv = csvFile.read();
        int str_size;
        for (int i = 0; i < country_list_from_csv.size(); i++) {

            String[] row_data = Arrays.toString((String[]) country_list_from_csv.get(i)).split(",");

            DbRow row = new DbRow();
            row.setShort_name(row_data[0].substring(1).trim());
            row.setLong_name(row_data[1].trim());

            row.setRates((double) Float.parseFloat(row_data[2]));
            // Firs remove "]" from end;
            String loc = row_data[3].split("]")[0].trim();
            str_size = loc.length();

            // now the loc is in """@drawable/indian_flag""" this format we are to remove the quotes i.e. @drawable/indian_flag
            row.setFlag_location(loc.substring(3, str_size - 3));
            db.addRow(row);
        }
    }

    private void callAPI() {
        Toast.makeText(this, "Calling API", Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://data.fixer.io/api/latest?access_key=7674aa6295b130c2117005c582a3f278&format=1";

        JsonObjectRequest json_obj_request = new JsonObjectRequest(Request.Method.GET, url,
                null, this::onResponse, error -> {//donoting
             });

        queue.add(json_obj_request);
    }

    //  calculating currency conversion here
    @SuppressLint("SetTextI18n")
    public void calculate(View view) {

        double multiplier;

        String input1_str = editText_input_1.getText().toString();
        String input2_str = editText_input_2.getText().toString();


        double input_number,converted_Val;

        if(input1_str.length()==0 && input2_str.length()==0){
            Toast.makeText(MainActivity.this,"Both Fields Empty",Toast.LENGTH_SHORT).show();
        }
        else if(input1_str.length()>0 && flag){
            input_number = Float.parseFloat(input1_str);
             multiplier = this.country_2_rate/this.country_1_rate;
            converted_Val = multiplier*input_number;

            editText_input_2.setText(""+round(converted_Val));

        }
        else if(flag){
            editText_input_2.setText("");
            editText_input_1.setText("");
        }
        else if(input2_str.length() > 0) {

            input_number = Float.parseFloat(input2_str);
            multiplier = this.country_1_rate/this.country_2_rate;
            converted_Val = multiplier*input_number;


            editText_input_1.setText(""+round(converted_Val));
        }
        else {
            editText_input_2.setHint("Enter a Number");

            editText_input_1.setText("");
            editText_input_1.setHint("Enter a Number");
        }
    }


    // function for swap button
    private void onClick_swap(View v) {

        int sp_1_pos = sp_country_1.getSelectedItemPosition();
        int sp_2_pos = sp_country_2.getSelectedItemPosition();
        sp_country_1.setSelection(sp_2_pos);
        sp_country_2.setSelection(sp_1_pos);

        flag = true;
    }

    // function for reset button
    private void onClick_reset(View v) {
        if (editText_input_1.length() != 0 || editText_input_2.length() != 0) {
            editText_input_1.setText("");
            editText_input_2.setText("");
        } else {
            Toast.makeText(this, "Both Fields Empty", Toast.LENGTH_SHORT).show();
        }
        editText_input_1.setHint("Enter a Number");
        editText_input_2.setHint("Enter a Number");
    }

    public Drawable makeDrawableFromImagePath(String S){

        int imageResource = getResources().getIdentifier(S, null, getPackageName());
        @SuppressLint("UseCompatLoadingForDrawables") Drawable res = getResources().getDrawable(imageResource);
        return res;
    }

    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    // function which is called after getting response from api
    private void onResponse(JSONObject response) {
        try {

            String base = response.getString("base");
            String date = response.getString("date");
            JSONObject json_received = response.getJSONObject("rates");
            String json = json_received.toString();


            int len = json.length();
            json = json.substring(1, len - 1);

            String[] json_arr = json.split(",");

            for (String s : json_arr) {
                String[] mod_s = s.split(":");
                String key = mod_s[0].substring(1,4);
                double value = (double) Float.parseFloat(mod_s[1]);
                Log.d("call-API",key+" "+value);
                db.update_rate_base_timeStamp(key,value,base,date);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        if(spinner==sp_country_1) {
            sp_country_1.setBackground(getResources().getDrawable(R.drawable.bg_spinner_country_drop_down));
        }
        if(spinner==sp_country_2){
            sp_country_2.setBackground(getResources().getDrawable(R.drawable.bg_spinner_country_drop_down));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        if(spinner==sp_country_1) {
//            searchView_country_1.setVisibility(View.INVISIBLE);
            sp_country_1.setBackground(getResources().getDrawable(R.drawable.bg_spinner_country));
        }
        if(spinner==sp_country_2){
//            searchView_country_2.setVisibility(View.INVISIBLE);
            sp_country_2.setBackground(getResources().getDrawable(R.drawable.bg_spinner_country));
        }
    }
}