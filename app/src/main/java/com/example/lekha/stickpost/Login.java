package com.example.lekha.stickpost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

import static com.example.lekha.stickpost.MainActivity.url;

public class Login extends AppCompatActivity {
    private Button lgbt, rhbt;
    private EditText lg, pw;
    public static String li="";
    private static final String LOGIN_URL=url+"login.php";
    private Intent i1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        lgbt = (Button) findViewById(R.id.lgbt);
        rhbt = (Button) findViewById(R.id.rhbt);
        lg = (EditText) findViewById(R.id.lg);
        pw = (EditText) findViewById(R.id.pw);
        i1 = new Intent(Login.this, MainActivity.class);

    }
    public void login(View v) {
        li=lg.getText().toString();
        String pwd=pw.getText().toString();
        String type="login";
       // Toast.makeText(getBaseContext(), "login php background starts ", Toast.LENGTH_SHORT).show();
       // RegisterUserClass w = new RegisterUserClass(this);
       // Toast.makeText(getBaseContext(), "login php background object created ", Toast.LENGTH_SHORT).show();
        //w.execute(type,log,pwd);
      //  Toast.makeText(getBaseContext(), "login php background finish ", Toast.LENGTH_SHORT).show();
        /*if(w.result.equals("success login")) {
            Intent in = new Intent(Login.this, MainActivity.class);
            //startActivity(in);
            Toast.makeText(getBaseContext(), "login !!!!!", Toast.LENGTH_SHORT).show();

        }
        else if(w.result.equals("check login id and password")){
            Toast.makeText(getBaseContext(), "check login id and password", Toast.LENGTH_SHORT).show();

        }
*/
        log(li,pwd);
    }
    public void reg(View v) {
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
    }

    private void log(String logid,String password) {
        //String urlSuffix = "?logid="+logid+"&password="+password;
        class LoginUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this, "Please Wait",null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(!(s.equals("success login")))
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                else  {
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    startActivity(i1);
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("logid",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }

        LoginUser u = new LoginUser();
        u.execute(logid,password);
    }



}



