package com.obscuresoftware.texttimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.DatePicker;
import android.net.Uri;
import android.content.ContentValues;
import android.app.Activity;
                           

public class MyAlarmService extends Service 
{
     
  private NotificationManager mManager;
 
  
   @Override
   public IBinder onBind(Intent arg0)
   {
      // TODO Auto-generated method stub
       return null;
   }

   @Override
   public void onCreate() 
   {
      // TODO Auto-generated method stub  
      super.onCreate();
   }

  @SuppressWarnings("static-access")
  @Override
  public void onStart(Intent intent, int startId)
  {
      super.onStart(intent, startId);
      SmsManager sms = SmsManager.getDefault();
      mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
      Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
      Notification notification = new Notification(R.drawable.ic_launcher,"This is a test message!", System.currentTimeMillis());
      intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
      PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
      notification.flags |= Notification.FLAG_AUTO_CANCEL;
      
      //Testing messaging
      sms.sendTextMessage(MainActivity.getContact(startId-1), null, MainActivity.getMessage(startId-1), null, null);
      ContentValues values = new ContentValues(); 
      values.put("address", MainActivity.getContact(startId-1));          
      values.put("body", MainActivity.getMessage(startId-1)); 
      getContentResolver().insert(Uri.parse("content://sms/sent"), values);
      //messaging test end
      notification.setLatestEventInfo(this.getApplicationContext(), "Timer Text", "Text Sent To " + MainActivity.getContact(startId-1), pendingNotificationIntent);
      Log.d(MainActivity.getContact(startId-1), MainActivity.getMessage(startId-1));      
      
      mManager.notify(0, notification);
      
      Log.d("tag", String.valueOf(MainActivity.intentArray.size()));
   }

   @Override
   public void onDestroy() 
   {
       // TODO Auto-generated method stub
       super.onDestroy();
   }

}