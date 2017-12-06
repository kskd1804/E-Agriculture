package com.example.lekha.stickpost;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;

import static com.example.lekha.stickpost.MainActivity.url;


public class Register extends AppCompatActivity {
    private Button reg;
    private EditText lid,nm,no,rpw,eid,rpw1;
    private static final String REGISTER_URL=url+"register.php";
    private Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        reg = (Button) findViewById(R.id.reg);
        lid = (EditText) findViewById(R.id.lid);
        rpw = (EditText) findViewById(R.id.rpw);
        eid = (EditText) findViewById(R.id.eid);
        rpw1 = (EditText) findViewById(R.id.rpw1);
        nm=(EditText) findViewById(R.id.nm);
        no = (EditText) findViewById(R.id.pn);
        in = new Intent(Register.this, Login.class);

        reg.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {


                        String logid = lid.getText().toString().trim().toLowerCase();
                        String name = nm.getText().toString().trim().toLowerCase();
                        String password =rpw.getText().toString().trim().toLowerCase();
                        String email = eid.getText().toString().trim().toLowerCase();
                        String ph = no.getText().toString();
                        String password1 =rpw1.getText().toString().trim().toLowerCase();
                        if(!(password1.equals(password)))
                           Toast.makeText(getBaseContext(),"password does not match",Toast.LENGTH_LONG).show();
                        else
                        register(logid,name,password,email,ph);


                    }
                }
        );
    }
    private void register(String logid, String name, String password, String email,String ph) {
        String urlSuffix = "?logid="+logid+"&name="+name+"&password="+password+"&email="+email+"&ph="+ph;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(!(s.equals("registered successfully")))
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                else
                    startActivity(in);

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                   // if(g(con))
                   //int r= con.getResponseCode();
                    //if (r != HttpsURLConnection.HTTP_OK)
                        // Toast.makeText(getApplicationContext(),"check intranet connection",Toast.LENGTH_SHORT).show();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Error creating HTTP connection",Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }



    }


