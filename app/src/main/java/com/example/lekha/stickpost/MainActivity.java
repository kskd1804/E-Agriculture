package com.example.lekha.stickpost;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.lang.Integer;
import java.util.Timer;
import java.util.TimerTask;

import static  com.example.lekha.stickpost.Login.li;


public class MainActivity extends AppCompatActivity {

    NotificationCompat.Builder nc=new NotificationCompat.Builder(this);



    public static final String url="http://192.168.43.38/" ;

    private static EditText messageET;
    private static ListView messagesContainer;
    private ImageButton sendBtn;
    private static ChatAdapter adapter;
    private static ArrayList<ChatMessage> chatHistory;
    public String servmsg,lid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
        nc.setAutoCancel(true);
        Log.v("StickPost","Entered MainActivity");
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                RequestMessage message = new RequestMessage(MainActivity.this);
            }
        };
        Timer t = new Timer();
        t.schedule(tt,1000,1000);
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);            
        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);

        //  TextView meLabel = (TextView) findViewById(R.id.meLbl);
        //TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        // companionLabel.setText("My Buddy");// Hard Coded
        loadDummyHistory();
    }
    public void sent(View v){

        String messageText = messageET.getText().toString();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(Long.parseLong(li));
        chatMessage.setMessage(messageText);
        chatMessage.setDate(DateFormat.getTimeInstance().format(new Date()));
        chatMessage.setMe(true);

        storemsg(chatMessage.getId(),chatMessage.getMessage(),chatMessage.getDate()); // to store msgs into db

        messageET.setText("");

        displayMessage(chatMessage);

        long[] vibrate={0,100,200,300};
        nc.setSmallIcon(R.mipmap.ic);
        nc.setContentTitle("Stick Post");
        nc.setTicker("new message from stick post");
        nc.setWhen(System.currentTimeMillis());
        nc.setContentText(messageText);
      //  Uri path=Uri.parse("android.resource://com.example.lekha.stickpost/"+R.music.msg.mp3);
      //  nc.setSound(path);
        nc.setVibrate(vibrate);
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent p=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(p);
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1903,nc.build());
    }



    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getTimeInstance().format(new Date()));
        chatHistory.add(msg1);

        adapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }

    public void frndmsg(String s) {
        Log.i("StickPost","Frndmsg function started! "+s);
        String[] seperate=s.split("\\$");
        int len = seperate.length;
        if(len>0){
            ChatMessage[] chatLen = new ChatMessage[len];
            for(int i=0;i<len;i++) {
                chatLen[i] = new ChatMessage();
                Log.i("StickPost","Loop breaker: "+seperate[i]);
                String[] row = seperate[i].split("\\#");
                Log.i("StickPost","Inner breaker: "+Integer.parseInt(row[0])+" "+row[1]+" "+row[2]);
                chatLen[i].setId(Integer.parseInt(row[0]));
                chatLen[i].setDate(row[2]);
                chatLen[i].setMessage(row[1]);
                if(!row[0].equals(li)){
                    chatLen[i].setMe(false);
                }else{
                    chatLen[i].setMe(true);
                }

            }

            chatHistory.clear();
            for (int i=0;i<len;i++)
            {
                chatHistory.add(chatLen[i]);

            }

            adapter = new ChatAdapter(MainActivity.this,chatHistory);
            messagesContainer.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.i("StickPost","Frndmsg ended!");
        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent is = new Intent(MainActivity.this, settings.class);
                startActivity(is);
                break;
            case R.id.active:
                Intent ia = new Intent(MainActivity.this, active.class);
                startActivity(ia);
                break;
            case R.id.grpmem:
                Intent ig = new Intent(MainActivity.this, grpmem.class);
                startActivity(ig);
                break;
            case R.id.logout:
                Toast.makeText(this, "All the messages will be deleted", Toast.LENGTH_LONG).show();
                break;

        }
        return true;

    }

    public  void storemsg(long logid,String msg,String time)
    {
        final String sturl=url+"msg.php?";
        String urlSuffix = null;
        try {
            urlSuffix = URLEncoder.encode("logid","UTF-8")+"="+URLEncoder.encode(String.valueOf(logid),"UTF-8")+"&"+URLEncoder.encode("msg","UTF-8")+"="+URLEncoder.encode(msg,"UTF-8")+"&"+URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        class msg extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
    //          loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
              //  String[] seperate=s.split(" ");
                   //servmsg=seperate[1];
                  //  lid=seperate[0];
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                //frndmsg();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(sturl+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    // if(g(con))
                    // Toast.makeText(getApplicationContext(),"check intranet connection",Toast.LENGTH_SHORT).show();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    //Toast.makeText(getApplicationContext(),"check intranet connection",Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }

        msg ru = new msg();
        ru.execute(urlSuffix);
    }

}
