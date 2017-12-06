package com.example.lekha.stickpost;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.lekha.stickpost.MainActivity.url;
import static  com.example.lekha.stickpost.Login.li;

public class settings extends AppCompatActivity {

    EditText p,s,r;
    private static final String seturl=url+"register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        p=(EditText)findViewById(R.id.pp);
        s=(EditText)findViewById(R.id.sp);
        r=(EditText)findViewById(R.id.rp);
    }
    public void set(View v)
    {
        String pp=p.getText().toString();
        String sp=s.getText().toString();
        String rp=r.getText().toString();

        if(!(sp.equals(rp)))
            Toast.makeText(this,"New passwords are not matching",Toast.LENGTH_LONG);
        update(li,pp,sp);
    }
    public void update(String lid,String pp,String sp)
    {
        String urlSuffix = "?lid="+lid+"&pp="+pp+"&sp="+sp;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(settings.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(!(s.equals("registered successfully")))
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
               // else
                    //startActivity(in);

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(seturl+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

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
