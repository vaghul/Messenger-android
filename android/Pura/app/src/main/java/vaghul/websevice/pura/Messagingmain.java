package vaghul.websevice.pura;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LogWriter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class Messagingmain extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public Context context;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    static String username="";
    static String phonenum="";
    static String toname="";
    static String tophone="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagingmain);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        context=getApplicationContext();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment= null;
        switch(position)
        {
            case 0:
                addconvo();
                fragment= new frame1();
                break;
            case 1:
                adduser();
                fragment= new frame2();
                break;
            case 2:
                fragment= new frame3();
                break;
            case 3:
                fragment= new frame4();
                break;
        }
        // update the main content by replacing fragments
        if(fragment!=null)
        {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container,fragment).commit();

        }    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = username.toString();
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public void delete(View v)
    {
        this.deleteDatabase("pura");
        Toast.makeText(getApplicationContext(), "database deleted", Toast.LENGTH_SHORT).show();
    }

    static ArrayList<String> listItems = new ArrayList<String>();
    static ArrayList<String> convo = new ArrayList<String>();
    static ArrayList<String> convolist = new ArrayList<String>();


    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    static ArrayAdapter<String> adapter;

    public static class frame1 extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_conversation, container,
                    false);
            TextView noconvo=(TextView)rootView.findViewById(R.id.noconvo);
            if(convo.isEmpty())
                noconvo.setText("No conversation");
            else
            {
                noconvo.setText("");
                adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,convo);

                final ListView list =(ListView)rootView.findViewById(R.id.recieved_convo);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListView lv = (ListView) parent;
                        TextView tv = (TextView) lv.getChildAt(position);
                        String s = tv.getText().toString();
                       // toname=s;
                       // ((Messagingmain)getActivity()).gettophone();
                        username=s;
                        ((Messagingmain)getActivity()).getphone();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        coversationlist fragment = new coversationlist();
                        fragmentTransaction.replace(R.id.container, fragment);
                        fragmentTransaction.commit();


                    }
                });

            }



            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Messagingmain) activity).onSectionAttached(1);
        }
    }

    public static class frame2 extends Fragment {


        @Override
        public View onCreateView( LayoutInflater inflater,  ViewGroup container,
                                  Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_newchat, container,
                    false);


            adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,listItems);

            final ListView list =(ListView)rootView.findViewById(R.id.contact_list);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //ListView listView = (ListView) rootView.findViewById(R.id.contat_list);

            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
            //     .getApplicationContext(),android.R.layout.simple_list_item_1, listItems);

            //listView.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView lv = (ListView) parent;
                    TextView tv = (TextView) lv.getChildAt(position);
                    String s = tv.getText().toString();
                    username=s;
                    ((Messagingmain)getActivity()).getphone();

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    coversationlist fragment = new coversationlist();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();


                }
            });
            return rootView;

        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Messagingmain) activity).onSectionAttached(2);
        }
    }
    public static class frame3 extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_synccontact, container,
                    false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Messagingmain) activity).onSectionAttached(3);
        }
    }
    public static class frame4 extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_account, container,
                    false);



            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Messagingmain) activity).onSectionAttached(4);
        }
    }

    public void save(View v)
    {
        Log.w("save","reached");
        EditText name=(EditText)findViewById(R.id.acc_name);

        EditText status=(EditText)findViewById(R.id.acc_status);

String myphone="";
        SQLiteDatabase db;
        db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        Cursor c=db.rawQuery("select phone from status;",null);
        while(c.moveToNext())
        {
            myphone=c.getString(0);

        }
        c.close();

        loadacc(name.getText().toString(), status.getText().toString(),myphone);

    }
    private void loadacc(final String name, final String status,final String phone) {
        //final Bundle bun=getIntent().getExtras();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://"+getString(R.string.ip)+":3000/pura/accounts");
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("name",name));
                    nameValuePairs.add(new BasicNameValuePair("phone",phone));
                    nameValuePairs.add(new BasicNameValuePair("status",status));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    String message= EntityUtils.toString(response.getEntity());
                    Log.w("responce",message);



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
             Log.w("status","success");
                SQLiteDatabase db;
                db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
                db.execSQL("update status set name=? and status=? where phone=?;",new String[]{name,status,phone});

            }
        }.execute(null, null, null);

    }
    public static class coversationlist extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_conversation_list, container,
                    false);

            ((Messagingmain)getActivity()).getconvo(rootView);

            //adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,convolist);

            //final ListView list =(ListView)rootView.findViewById(R.id.cover_list);
            //list.setAdapter(adapter);
            //adapter.notifyDataSetChanged();


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Messagingmain) activity).onSectionAttached(5);
        }
    }

public void getconvo(View v)
{
    Log.w("getconvo","reached");
    ListView listViewSMS;
    Cursor cursor;

    SMSListAdapter smsListAdapter;
    Context context;

    listViewSMS = (ListView)v.findViewById(R.id.cover_list);

    SQLiteDatabase db;
    db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
    Cursor c=db.rawQuery("select message,sent from conversation where tophone=?;",new String[] { phonenum });
    context = getApplicationContext();

    // Create the Adapter
    smsListAdapter = new SMSListAdapter(context,c);


    // Set The Adapter to ListView
    listViewSMS.setAdapter(smsListAdapter);

    //c.close();

}
    public void adduser() {
        listItems=new ArrayList<String>();
        Log.w("add","reached" );
        SQLiteDatabase db;
        db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        Cursor c=db.rawQuery("select name from userlist;",null);
        while(c.moveToNext())
        {
            listItems.add(c.getString(0));

        }
        c.close();
    }


    public void addconvo() {
        Log.w("convo","reached" );
        convo=new ArrayList<String>();
        String phonetemp="";
        SQLiteDatabase db;
        db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        Cursor c=db.rawQuery("select distinct tophone from conversation;",null);
        while(c.moveToNext())
        {
            phonetemp=c.getString(0);
        }
        if(phonetemp!="") {
            c = db.rawQuery("select name from userlist where phone=? ;", new String[]{phonetemp});
            while (c.moveToNext()) {
                convo.add(c.getString(0));
            }
        }
        c.close();

    }
    public  void gettophone(){
        SQLiteDatabase db;
        db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        Cursor c=db.rawQuery("select phone from userlist where name=?;",new String[] { username });
        while(c.moveToNext())
            tophone=c.getString(0);
    }
    public  void getphone(){
        SQLiteDatabase db;
        db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        Cursor c=db.rawQuery("select phone from userlist where name=?;",new String[] { username });
        while(c.moveToNext())
            phonenum=c.getString(0);
    }
    String message;
    public void send_msg(View v)
    {
       TextView mes= (TextView)findViewById(R.id.msg_type);
       message=mes.getText().toString();
        mes.clearFocus();

        if(!(message.contentEquals("")))
        {
            Toast.makeText(getApplicationContext(), username+phonenum, Toast.LENGTH_LONG).show();
            sendinbackground();

        }


    }




    private void sendinbackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "",myphone="",myname="";
                try {
                    SQLiteDatabase db;
                    db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
                    Cursor c=db.rawQuery("select phone,name from status;",null);
                    while(c.moveToNext())
                    {
                        myphone=c.getString(0);
                        myname=c.getString(1);

                    }
                    c.close();

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://"+getString(R.string.ip)+":3000/pura/send");
                    // Add your data
                    Bundle bun=getIntent().getExtras();
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                    nameValuePairs.add(new BasicNameValuePair("message",message));
                    nameValuePairs.add(new BasicNameValuePair("fromphone",myphone.toString()));
                    nameValuePairs.add(new BasicNameValuePair("tophone",phonenum));
                    nameValuePairs.add(new BasicNameValuePair("name",myname.toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    String message= EntityUtils.toString(response.getEntity());
                    Log.w("responce",message);
                    msg=message;

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

             insertconvo(msg);
            }
        }.execute(null, null, null);

    }
public void insertconvo(String msg){
    if(msg.equals("error"))
        Toast.makeText(getApplicationContext(), "Gcm failed", Toast.LENGTH_LONG).show();
    else
    {
        SQLiteDatabase db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        db.execSQL("insert into conversation(fromphone,tophone,message,sent ) values('"+tophone+"','"+phonenum+"','"+message+"',1);");
    }
}

public void start_sync(View v){
   // Button syncbtn=(Button)findViewById(R.id.sync_start);
   // syncbtn.setVisibility(v.GONE);
startsync();
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
    public void onsync(String output)
    {
        SQLiteDatabase db;
        delete();
Log.w("sync","started");

        db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        try {
            JSONArray array=new JSONArray(output);
            for(int i=0;i<array.length();i++) {
                JSONObject rec = array.getJSONObject(i);
                String id = rec.getString("phone");
                String loc = rec.getString("name");
                db.execSQL("insert into userlist(phone,name) values('"+id+"','"+loc+"');");
            }

        } catch (JSONException e) {
            Log.w("error","in json convertion");

        }
        Toast.makeText(getApplicationContext(), "contact synced", Toast.LENGTH_LONG).show();
     //   Button syncbtn=(Button)findViewById(R.id.sync_start);
      //  syncbtn.setVisibility(View.VISIBLE);

    }


    public void delete()
    {
     SQLiteDatabase  db=openOrCreateDatabase("pura", Context.MODE_PRIVATE,null);
        db.execSQL("DELETE FROM userlist");
    }

}
