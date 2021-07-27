package com.dbkt.currencyconverter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class backgroundActivity  extends AsyncTask<String,Void,String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("Bhakt","onPreExecute: ran");
    }

    @Override
    protected String doInBackground(String... urls) {
        Log.d("Bhakt","doInBackground: ran");
        StringBuilder result= new StringBuilder();
        URL url;
        HttpURLConnection conn;
        try{
            url = new URL(urls[0]);
            conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while(data!=-1){
                result.append((char) data);
                data = reader.read();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        super.onPostExecute(s);
        Log.d("Bhakt","onPostExecute ran");
        Log.d("Bhakt",s);
    }
}
