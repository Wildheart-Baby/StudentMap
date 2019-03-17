package com.example.v8181191.studentmap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Wildheart on 16/03/2019.
 */

/**
 * This code was found at https://www.youtube.com/redirect?v=pke6sMxOsuw&event=video_description&redir_token=-0UcSnhrTy4tCyrF1X4vrziV_kZ8MTU1Mjg2NjUyNEAxNTUyNzgwMTI0&q=https%3A%2F%2Fdrive.google.com%2Fdrive%2Ffolders%2F1K_bhMwjwb31izBjjEP9vMvxVtfTcF9mp%3Fusp%3Dsharing
 * by youtuber Sasasushiq
 * https://www.youtube.com/watch?v=pke6sMxOsuw
 */

public class SmsReciever extends BroadcastReceiver{         //sets the class up to extend the broadcast reciever class

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";       //sets a string with the broadcast message type
    private static final String TAG = "SmsBroadcastReceiver";                                   //sets up a tag name
    String msg, phoneNo, theMessage;

    @Override
    public void onReceive(Context context, Intent intent) {                                     //the on recieve function
        Log.i(TAG, "Intent Received: " +intent.getAction());                               //an info log message
        if (intent.getAction()==SMS_RECEIVED)                                                   //checks to see if the intent recieved is a text message
        {
            //retrieves a map of extended data from the intent
            Bundle dataBundle = intent.getExtras();                                             //gets the data from the intent bundle
            if (dataBundle!=null)                                                               //if the bundle isn't null
            {
                //creating PDU(Protocol Data Unit) object which is a protocol for transferring message
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];                      //a message string array the size of the mypu object

                for (int i = 0; i<mypdu.length; i++)                                            //a for statement that runs for the number of messages in the bundle
                {
                    message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);                    //puts the content of the mypdu object into the message string array
                    msg = message[i].getMessageBody();                                          //sets the message body to the msg string
                    phoneNo = message[i].getOriginatingAddress();                               //sets the originating address to the phone number string
                }
                Toast.makeText(context, "Message: " +msg +"\nNumber: " +phoneNo, Toast.LENGTH_LONG).show();     //shows a toast message with the messge contents
            }
        }
    }
}
