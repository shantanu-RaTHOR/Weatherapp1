package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static Button bt = null;

    EditText cityEditor;
    TextView tem;
    TextView pressure;
    TextView rain;
    TextView humidity;
    TextView visibility;
    TextView mint;
    TextView maxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con();

    }

     void  hit(View  v)
    {
        new jsontask().execute("P");
    }
    private void con()
    {
        if(tem==null)
        {
            tem=(TextView)findViewById(R.id.temp);
        }

        if(humidity==null)
        {
            humidity=(TextView)findViewById(R.id.Humidity);
        }
        if(visibility==null)
        {
            visibility=(TextView)findViewById(R.id.Visibility);
        }
        if(pressure==null)
        {
            pressure=(TextView)findViewById(R.id.pressure);
        }
        if(mint==null)
        {
            mint=(TextView)findViewById(R.id.mint);
        }
        if(maxt==null)
        {
            maxt=(TextView)findViewById(R.id.maxt);
        }
        if(cityEditor == null)
        {
            cityEditor = (EditText)findViewById(R.id.cityeditor);
        }



    }
    JSONObject root;
    JSONObject main;
    int  visi;
    int temp;
    int pres;
    int hum;
    int mintt;
    int maxtt;

    public  void jsonfun(String s)
    {
        try {
            root=new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            main=root.getJSONObject("main");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            temp=main.getInt("temp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        temp=temp-273;
        String sz=Integer.toString(temp);
        tem.setText(sz+ "°C");

        try {
            pres=main.getInt("pressure");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String pp=Integer.toString(pres);
        pp="Pressure-"+pp+"mbar";
        pressure.setText(pp);
        try {
            hum=main.getInt("humidity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String hmm=Integer.toString(hum);
        hmm="Humidity-"+hmm+"%";
        humidity.setText(hmm);
        try {
            mintt=main.getInt("temp_min");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mintt=mintt-273;
        String mnn=Integer.toString(mintt);
        mnn="Min Temp-"+mnn+"°C";
        mint.setText(mnn);
        try {
            maxtt=main.getInt("temp_max");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        maxtt=maxtt-273;
        String mxx=Integer.toString(maxtt);
        mxx="Max Temp-"+mxx+"°C";
        maxt.setText(mxx);
        try {
            visi=root.getInt("visibility");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        visi=visi/1000;
        visibility.setText("Visibility-"+visi+"Km");


    }


    class jsontask extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings)
        {

            HttpURLConnection httpConn = null;
            InputStreamReader isReader = null;
            BufferedReader bufReader = null;
            StringBuffer readTextBuf = new StringBuffer();
            String u=cityEditor.getText().toString();
            u="https://api.openweathermap.org/data/2.5/weather?q="+u+"&appid=614ee922d27621a23edcc1eb42d1dc8f";
            try
            {

                URL url=new URL(u);
                httpConn = (HttpURLConnection)url.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.setConnectTimeout(10000);
                httpConn.setReadTimeout(10000);
                InputStream inputStream = httpConn.getInputStream();
                isReader = new InputStreamReader(inputStream);
                bufReader = new BufferedReader(isReader);
                String line = bufReader.readLine();
                while(line != null)
                {
                    readTextBuf.append(line);
                    line = bufReader.readLine();
                }
                String s=readTextBuf.toString();
                return (s);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                try {
                    if (bufReader != null) {
                        bufReader.close();
                        bufReader = null;
                    }

                    if (isReader != null) {
                        isReader.close();
                        isReader = null;
                    }

                    if (httpConn != null) {
                        httpConn.disconnect();
                        httpConn = null;
                    }
                }catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String st)
        {
            super.onPostExecute(st);
            jsonfun(st);


        }
    }



}
