package com.example.siddhant.railways_api;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Main5Activity extends AppCompatActivity {
    EditText t1,t2,t3;
    ListView mylist3;
    String api_key="Enter YOUR API KEY HERE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        t1=(EditText)findViewById(R.id.et1);
        t2=(EditText)findViewById(R.id.et2);
        t3=(EditText)findViewById(R.id.et3);
        mylist3=(ListView)findViewById(R.id.mylist3);
    }

    public void findstan(View view) {
        findstn ob=new findstn(this);
        ob.execute();
    }


    class findstn extends AsyncTask<String, String, String> {
        Context ctx;
        findstn(Context ctx){
            this.ctx=ctx;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String s= t1.getText().toString().trim();
                String y= t2.getText().toString().trim();
                String z= t3.getText().toString().trim();

                String url="https://api.railwayapi.com/v2/between/source/"+s+"/dest/"+y+"/date/"+z+"/apikey/"+api_key+"/";
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


            ArrayList<HashMap<String, String>> stationlist=new ArrayList<HashMap<String,String>>();
            try
            {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray item = jsonObj.getJSONArray("trains");
                for (int i = 0; i < item.length(); i++)
                {

                    JSONObject jsonObj1 = (JSONObject) item.get(i);



                    String name = jsonObj1.getString("name");
                    String tt = jsonObj1.getString("travel_time");
                    String deptt = jsonObj1.getString("src_departure_time");
                    String no = jsonObj1.getString("number");
                    HashMap<String, String> trainn = new HashMap<String, String>();
                    trainn.put("name", "train name: "+name);
                    trainn.put("tt", "travel time: "+tt);
                    trainn.put("deptt", "departure time: "+deptt);
                    trainn.put("no", "train no: "+no);
                    stationlist.add(trainn);
                }
                ListAdapter adapter = new SimpleAdapter(
                        ctx, stationlist, R.layout.mylist3,
                        new String[]{"name","tt","deptt","no"},
                        new int[]{R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4}
                );
                mylist3.setAdapter(adapter);

            }
            catch (Exception ex)
            {
                Toast.makeText(ctx,"Error "+ex,Toast.LENGTH_LONG).show();
            }

        }


    }

}
