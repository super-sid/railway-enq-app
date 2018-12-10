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

public class Main4Activity extends AppCompatActivity {
    EditText t1;
    ListView mylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        t1 = (EditText) findViewById(R.id.et1);
        mylist = (ListView) findViewById(R.id.mylist);
    }
    public void find_route(View view) {
        findroute ob=new findroute(this);
        ob.execute();
    }
    class findroute extends AsyncTask<String, String, String> {
        Context ctx;
        findroute(Context ctx){
            this.ctx=ctx;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String s= t1.getText().toString().trim();
                String url="https://api.railwayapi.com/v2/route/train/"+s+"/apikey/jhjnk6iqik/";
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
                JSONArray item = jsonObj.getJSONArray("route");
                for (int i = 0; i < item.length(); i++)
                {

                    JSONObject jsonObj1 = (JSONObject) item.get(i);

                    JSONObject jsonObj2 = (JSONObject) jsonObj1.get("station");

                    String name = jsonObj2.getString("name");
                    HashMap<String, String> trainn = new HashMap<String, String>();
                    trainn.put("name", name);
                    stationlist.add(trainn);
                }
                ListAdapter adapter = new SimpleAdapter(
                        ctx, stationlist, R.layout.mylist,
                        new String[]{"name"},
                        new int[]{R.id.tv,}
                );
                mylist.setAdapter(adapter);

            }
            catch (Exception ex)
            {
                Toast.makeText(ctx,"Error "+ex,Toast.LENGTH_LONG).show();
            }

        }


    }
}
