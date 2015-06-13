package vaghul.websevice.pura;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(doesDatabaseExist(this,"pura")) {
            //setContentView(R.layout.activity_messagingmain);
       Intent ne=new Intent(this,Messagingmain.class);
            startActivity(ne);
        }else
        setContentView(R.layout.activity_main);


    }

    private static boolean doesDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void show(View v)
    {
        EditText phone_val =(EditText)findViewById(R.id.phone);
        phone_val.setEnabled(false);
        createotp(phone_val.getText().toString());
    }


    void createotpfinish(String output) {
        if(output=="true") {
            Toast.makeText(getApplicationContext(), "Otp sent", Toast.LENGTH_SHORT).show();


            EditText otp_val = (EditText) findViewById(R.id.otp_val);
            Button resend = (Button) findViewById(R.id.resend);
            Button next = (Button) findViewById(R.id.next);
            Button send = (Button) findViewById(R.id.send_otp);

            send.setVisibility(View.GONE);
            otp_val.setVisibility(View.VISIBLE);
            resend.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(getApplicationContext(), "connection failure", Toast.LENGTH_SHORT).show();
    }


    void checkotpfinish(String output) {
        Toast.makeText(getApplicationContext(),output, Toast.LENGTH_SHORT).show();



        if(output.equals("true")) {
            EditText phone_val =(EditText)findViewById(R.id.phone);
            Toast.makeText(getApplicationContext(), "Otp verified", Toast.LENGTH_SHORT).show();
            Bundle bun=new Bundle();
            bun.putString("phone",phone_val.getText().toString());
            Intent nextscreen=new Intent(this,details.class);
            nextscreen.putExtras(bun);
            startActivity(nextscreen);
            finish();
         }
        else if(output.equals("false"))
            Toast.makeText(getApplicationContext(), "invalid otp", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "connection failure", Toast.LENGTH_SHORT).show();
    }

    public void resend_otp(View v)
    {

        Toast.makeText(getApplicationContext(),"Otp resend",Toast.LENGTH_SHORT).show();
    }

    public void next(View v)
    {
        EditText phone_val =(EditText)findViewById(R.id.phone);
        EditText otp=(EditText)findViewById(R.id.otp_val);
        checkotp(otp.getText().toString(),phone_val.getText().toString());
    }

    public void createotp(final String phone)
    {

        new AsyncTask<Void, Void, String>() {

            @Override

            protected String doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://"+getString(R.string.ip)+":3000/pura/otp");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("phone",phone));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    String resmessage=EntityUtils.toString(response.getEntity());
                    Log.w("responce",resmessage);

                }  catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.w("error",e.toString());
                    return "false";
                }
                return "true";


            }

            @Override
            protected void onPostExecute(String msg) {
            createotpfinish(msg);
            }
        }.execute(null, null, null);


    }


    public void checkotp(final String otp,final String phone)
    {

        new AsyncTask<Void, Void, String>() {
            String resmessage;
            @Override

            protected String doInBackground(Void... params) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://"+getString(R.string.ip)+":3000/pura/checkOtp");
                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("otp",otp));
                    nameValuePairs.add(new BasicNameValuePair("phone",phone));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    resmessage=EntityUtils.toString(response.getEntity());
                    Log.w("response on otp",resmessage);

                }  catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.w("error",e.toString());
                    return "error";
                }
                return resmessage;



            }

            @Override
            protected void onPostExecute(String msg) {
                checkotpfinish(msg);
            }
        }.execute(null, null, null);


    }


}

