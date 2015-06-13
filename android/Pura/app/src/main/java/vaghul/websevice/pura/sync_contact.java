package vaghul.websevice.pura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class sync_contact extends ActionBarActivity {

    GoogleCloudMessaging gcm;
    String regid;
    String SENDER_ID = "ID";  //Sender id for GCM
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_contact);
        context = getApplicationContext();

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.w("reg",regid);

            if (regid.isEmpty())
                 registerInBackground();

                createdb();
                startsync();

                Toast.makeText(getApplicationContext(), "Contact Synced", Toast.LENGTH_SHORT).show();
                Intent ne = new Intent(this, Messagingmain.class);
                startActivity(ne);
                finish();

            } else
                finish();

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sync_contact, menu);
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

    JSONArray array;

    public void startsync() {
        array=new JSONArray();
        // Perform action on click

        int i=0;
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            try {
                JSONObject contact=new JSONObject();

                contact.put("name", name);
                phoneNumber=phoneNumber.replaceAll("\\s","");
                if(phoneNumber.length()>10)
                  phoneNumber= phoneNumber.substring(phoneNumber.length()-10);
                contact.put("phone", phoneNumber);
                i++;

                array.put(contact);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        phones.close();
        getRegId();

    }
    SQLiteDatabase db;

    public void createdb()
    {
        db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        db.execSQL("create table if not exists userlist(phone varchar ,name varchar);");
        db.execSQL("create table if not exists conversation(fromphone varchar,tophone varchar,message varchar,sent int );");
        db.execSQL("create table if not exists status(phone varchar ,name varchar,status varchar);");
        Log.i("database","create success");

    }
    public void onsync(String output)
    {
        try {
            JSONArray array=new JSONArray(output);
           for(int i=0;i<array.length();i++) {
               JSONObject rec = array.getJSONObject(i);
               String id = rec.getString("phone");
               String loc = rec.getString("name");
             db.execSQL("insert into userlist(phone,name) values('"+id+"','"+loc+"');");
           }
            Bundle bundle=getIntent().getExtras();
            db.execSQL("insert into status(phone,name,status) values('"+bundle.getString("phone").toString()+"','"+bundle.getString("name").toString()+"','i am using Pura');");

        } catch (JSONException e) {
            Log.w("error","in json convertion");

        }

    }
    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            String resmessage;
            @Override
            protected String doInBackground(Void... params) {
                JSONObject jsonObject = new JSONObject();

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://"+getString(R.string.ip)+":3000/pura/sync");

                try {
                    // Add your data
                    Bundle bun=getIntent().getExtras();
                    jsonObject.accumulate("contact", array.toString());
                    jsonObject.accumulate("regid",regid);
                    jsonObject.accumulate("phone",bun.getString("phone"));
                    jsonObject.accumulate("name",bun.getString("name"));
                    StringEntity se = new StringEntity(jsonObject.toString());
                    httppost.setEntity(se);
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-type", "application/json");

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    resmessage= EntityUtils.toString(response.getEntity());
                    Log.w("responce",resmessage);

                }  catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.w("error",e.toString());
                    return "false";
                }
                return resmessage;

            }

            @Override
            protected void onPostExecute(String msg) {
                onsync(msg);

            }
        }.execute(null, null, null);
    }



    private boolean checkPlayServices() // to check if device support gcm
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                Log.i("GCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("GCM", "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("GCM", "App version changed.");
            return "";
        }
        return registrationId;

    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("GCM", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                Log.w("reached","doinbackground");
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://"+getString(R.string.ip)+":3000/pura/reg");
                        // Add your data
                        Bundle bun=getIntent().getExtras();
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                        nameValuePairs.add(new BasicNameValuePair("regid",regid));
                        nameValuePairs.add(new BasicNameValuePair("phone",bun.getString("phone")));
                        nameValuePairs.add(new BasicNameValuePair("name",bun.getString("name")));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        // Execute HTTP Post Request
                        HttpResponse response = httpclient.execute(httppost);
                        String message= EntityUtils.toString(response.getEntity());
                        Log.w("responce",message);


                    storeRegistrationId(context, regid);
                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                regid=msg;
                Log.w("msg",msg);

            }
        }.execute(null, null, null);

    }
}
