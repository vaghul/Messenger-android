package vaghul.websevice.pura;



import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SMSListAdapter extends BaseAdapter
{

    private Context mContext;
    Cursor cursor;
    private LayoutInflater myInflater;

    public SMSListAdapter(Context context, Cursor cur)
    {
        super();
        myInflater = LayoutInflater.from(context);
        mContext=context;

        mContext=context;
        cursor=cur;

    }

    public int getCount()
    {
        // return the number of records in cursor
        return cursor.getCount();
    }

    // getView method is called for each item of ListView
    public View getView(int position,  View view, ViewGroup parent)
    {
        cursor.moveToPosition(position);

        // fetch the sender number and sms body from cursor
        String senderNumber=cursor.getString(cursor.getColumnIndex("message"));
        String smsBody=cursor.getString(cursor.getColumnIndex("sent"));

        // inflate the layout for each item of listView
        if (smsBody.equals("0"))
            view = myInflater.inflate(R.layout.row_left, null);
        else
            view = myInflater.inflate(R.layout.row_right, null);

        // move the cursor to required position

        // get the reference of textViews
        TextView textViewConatctNumber=(TextView)view.findViewById(R.id.message);
        TextView textViewSMSBody=(TextView)view.findViewById(R.id.dataAndTime);

        // Set the Sender number and smsBody to respective TextViews
        textViewConatctNumber.setText(senderNumber);
        textViewSMSBody.setText(smsBody);


        return view;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}

