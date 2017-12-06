package com.example.lekha.stickpost;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kousthubha on 3/22/2017.
 */

public class RequestMessage  extends AsyncTask<String, Void, String> {

    static MainActivity activity;

    public RequestMessage() {
    }

    public RequestMessage(MainActivity mainActivity) {
        RequestMessage message = new RequestMessage();
        message.activity=mainActivity;
        message.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(!s.equals("Unsuccessful!"))
        activity.frndmsg(s);
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader bufferedReader = null;
        try {
            Log.i("StickPost","Entered try block");
            URL url = new URL("http://192.168.43.38/" +
                    "sendmsg.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // if(g(con))
            // Toast.makeText(getApplicationContext(),"check intranet connection",Toast.LENGTH_SHORT).show();
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String result;

            result = bufferedReader.readLine();
            //if(!(result.equals("send is false"))) {
            Log.i("StickPost","Message received: "+result);
            // }
            return result;
            ////return result;
        }catch(Exception e){
            Log.i("StickPost","Entered catch block");
            Log.i("StickPost",e.getMessage());
            e.printStackTrace();
            return "Unsuccessful!";
            // Toast.makeText(getApplicationContext(),"check intranet connection",Toast.LENGTH_SHORT).show();
            //// return null;
        }
    }
}
