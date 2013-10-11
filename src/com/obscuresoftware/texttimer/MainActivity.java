package com.obscuresoftware.texttimer;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.provider.ContactsContract.Contacts;  
import android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TimePicker;

public class MainActivity extends Activity 
{

	private EditText mEdit, cEdit;
	private PendingIntent pendingIntent;
	public static ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
	private static String[] senderInfo = new String[10000];
	private static String[] msg;
	private static final int CONTACT_PICKER_RESULT = 1001; 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cEdit = (EditText)findViewById(R.id.editText1);
		mEdit = (EditText)findViewById(R.id.editText2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendBtnClick(View v)
	{
		Log.d("Test", cEdit.getText().toString());
		
		int textsToSend = intentArray.size();
		DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.MONTH, datePicker.getMonth());
	    calendar.set(Calendar.YEAR, datePicker.getYear());
	    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
	    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
	    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
	    calendar.set(Calendar.SECOND, 0);
	    senderInfo[textsToSend] = cEdit.getText().toString();
	    Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
	    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, textsToSend, myIntent,0);
	    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
	    intentArray.add(pendingIntent);
	    
	    Context context = getApplicationContext();
	    CharSequence text = "Timer Set!";
	    int duration = Toast.LENGTH_SHORT;
	    Toast toast = Toast.makeText(context, text, duration);
	    toast.show();
	}
	
	public void whoBtnClick(View v)
	{
		//Log.d("woah", "WOAH");
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
	}
	
	public static String getContact(int i)
	{
		return senderInfo[i];
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{  
	    if (resultCode == RESULT_OK) 
	    {  
	        switch (requestCode) 
	        {  
	        case CONTACT_PICKER_RESULT:  
	            Cursor cursor = null;  
	            String phonenum = "";  
	            try 
	            {  
	                Uri result = data.getData();  
	                Log.v("debug", "Got a contact result: " + result.toString());
	                String id = result.getLastPathSegment();  
	                cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[] { id }, null);  
	                int phoneIdx = cursor.getColumnIndex(Phone.DATA);  
	                
	                if (cursor.moveToFirst()) 
	                {  
	                    phonenum = cursor.getString(phoneIdx);  
	                    Log.v("debug", "Phone Number: " + phonenum);  
	                } 
	                else 
	                {  
	                    Log.w("debug", "No results");  
	                }  
	            } 
	            catch (Exception e) 
	            {  
	                Log.e("debug", "Failed to get phone number", e);  
	            } 
	            finally 
	            {  
	                if (cursor != null) 
	                {  
	                    cursor.close();  
	                }  
	                EditText phoneEntry = (EditText) findViewById(R.id.editText1);  
	                phoneEntry.setText(phonenum); 
	                if (phonenum.length() == 0) 
	                {  
	                    Toast.makeText(this, "No Phone Number Found For Contact.",  
	                            Toast.LENGTH_LONG).show();  
	                }  
	            }  
	            break;  
	        }  
	    }
	    else 
	    {  
	        Log.w("debug", "Warning: activity result not ok");  
	    }  
	}   

	
	
	
}
