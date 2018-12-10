package com.example.siddhant.railways_api;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] rclass={"1AC","2AC","3AC","Sleeper","CC"};
    Calendar calendar;
    String cl0="GN";
    String cl1="1A";
    int d,m,y;
    EditText t1,t2,t3,t4;
    ListView mylist2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar=Calendar.getInstance();
        d=calendar.get(Calendar.DAY_OF_MONTH);
        m=calendar.get(Calendar.MONTH);
        y=calendar.get(Calendar.YEAR);
        t1=(EditText)findViewById(R.id.e1);
        t2=(EditText)findViewById(R.id.e2);
        t3=(EditText)findViewById(R.id.e3);
        t4=(EditText)findViewById(R.id.e4);
        mylist2=(ListView)findViewById(R.id.mylist2);
        Spinner spin = (Spinner) findViewById(R.id.rail_dd);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,rclass);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }

    public void btn_click1(View view) {
        DatePickerDialog.OnDateSetListener myDateListener=  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                m=m+1;
                t4.setText(d+"-"+m+"-"+y);
            }
        };
        DatePickerDialog ob = new DatePickerDialog(this, myDateListener,y,m,d);
        ob.show();
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.gkcl:
                if (checked){
                    cl0="GN";
                    break;
                }
            case R.id.tkcl:
                if (checked){
                    cl0="TQ";
                    break;
                }
        }
    }

    public void show_t(View view) {
        getfare0 ob= new getfare0(this);
        ob.execute();
    }
    class getfare0 extends AsyncTask<String, String, String> {
        Context ctx;
        getfare0(Context ctx){
            this.ctx=ctx;
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                String s=t1.getText().toString().trim();
                String s1=t2.getText().toString().trim();
                String s2=t3.getText().toString().trim();
                String s3=t4.getText().toString().trim();
                String s5=cl0;
                String s6=cl1;
                String url="https://api.railwayapi.com/v2/check-seat/train/"+s+"/source/"+s1+"/dest/"+s2+"/date/"+s3+"/pref/"+s6+"/quota/"+s5+"/apikey/jhjnk6iqik/";
                URL myurl=new URL(url);
                HttpsURLConnection con=(HttpsURLConnection) myurl.openConnection();
                con.connect();
                InputStream stream=con.getInputStream();
                InputStreamReader isr=new InputStreamReader(stream);
                BufferedReader bd=new BufferedReader(isr);
                StringBuffer st=new StringBuffer();
                String line="";
                while ((line =bd.readLine())!=null){
                    st.append(line+"\n");
                }
                return new String(st);
            }catch(Exception ex){
                Toast.makeText(getApplicationContext(),"Error"+ex,Toast.LENGTH_LONG).show();
            }
            return null;

        }
        @Override
        protected void onPostExecute(String result){
            ArrayList<HashMap<String,String>> seatavl= new ArrayList<>();
            try{
                JSONObject jsonObj=new JSONObject(result);
                JSONArray item= jsonObj.getJSONArray("availability");
                for (int i = 0; i < item.length(); i++) {
                    JSONObject jsonObj1=(JSONObject) item.get(i);
                    String dat0=jsonObj1.getString("date");
                    String status0=jsonObj1.getString("status");
                    HashMap<String, String> avl = new HashMap<>();
                    avl.put("dat0","Date: "+dat0);
                    avl.put("status0","Status: "+status0);
                    seatavl.add(avl);
                }
                ListAdapter adapter=new SimpleAdapter(ctx,seatavl,R.layout.mylist2,new String[]{"dat0","status0"},new int[]{R.id.tv1,R.id.tv2});
                mylist2.setAdapter(adapter);
            }catch (Exception ex) {
                Toast.makeText(ctx,"Error "+ex,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (rclass[position]=="1AC")
            cl1="1A";
        else if (rclass[position]=="2AC")
            cl1="2A";
        else if (rclass[position]=="3AC")
            cl1="3A";
        else if (rclass[position]=="CC")
            cl1="CC";
        else if (rclass[position]=="Sleeper")
            cl1="SL";
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    }
