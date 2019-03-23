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
    Boolean campusMap;
    CampusMap cm;
    Intent switchToMap;
    Bundle extras;

    @Override
    public void onReceive(Context context, Intent intent) {                                     //the on recieve function
        cm = new CampusMap();
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
                    String bn;
                    campusMap = false;
                    //.substring(example.lastIndexOf(" ") + 1)
                    //yourString.substring(yourString.indexOf("no") + 3 , yourString.length());
                    if(msg.contains("building:")){                                              //if a text messge has the word building follwed by a colon,
                        bn = msg.substring(msg.indexOf("building:") + 9, msg.length());         //get the text of the message 9 characters after the first letter in building which should be the building number

                        campusMap = true;                                                       //set the boolean to true
                        sendPath(bn, context);                                                  //run the function passing over the context and the building number
                    }
                }
                if(campusMap==false) {                                                          //if the boolean is false
                    Toast.makeText(context, "Message: " + msg + "\nNumber: " + phoneNo, Toast.LENGTH_LONG).show();     //show a toast message with the messge contents
                }
            }
        }
    }

    private void sendPath(String bn, Context context){
        switchToMap = new Intent(context, CampusMap.class);                             //sets up and intent that switches to the campus map activity
        extras = new Bundle();                                                          //sets up the bundle called extras

        switch (bn){                                                                    //switch case statement taking the building number
            case "1":                                                                   //building number 1
                extras.putDouble("ARG_MARKERLAT", 54.570381);                           //put the latitude of the marker into the bundle
                extras.putDouble("ARG_MARKERLNG", -1.236945);                           //put the longitude of the marker into the the bundle
                switchToMap.putExtras(extras);                                          //add the extras to the intent
                context.startActivity(switchToMap);                                     //use the context to start the activity from a class that isn't itself an activity
                break;                                                                  //end the case
            case "2":
                extras.putDouble("ARG_MARKERLAT", 54.569961);
                extras.putDouble("ARG_MARKERLNG", -1.236089);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "3":
                extras.putDouble("ARG_MARKERLAT", 54.570126);
                extras.putDouble("ARG_MARKERLNG",-1.234845);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "4":
                extras.putDouble("ARG_MARKERLAT", 54.570056);
                extras.putDouble("ARG_MARKERLNG",-1.23382);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "5":
                extras.putDouble("ARG_MARKERLAT", 54.569639);
                extras.putDouble("ARG_MARKERLNG",-1.23419);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "6":
                extras.putDouble("ARG_MARKERLAT", 54.569286);
                extras.putDouble("ARG_MARKERLNG",-1.234944);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "7":
                extras.putDouble("ARG_MARKERLAT", 54.568462);
                extras.putDouble("ARG_MARKERLNG",-1.233892);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "8":
                extras.putDouble("ARG_MARKERLAT", 54.569124);
                extras.putDouble("ARG_MARKERLNG",-1.236141);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "9":
                extras.putDouble("ARG_MARKERLAT", 54.569286);
                extras.putDouble("ARG_MARKERLNG",-1.237451);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "10":
                extras.putDouble("ARG_MARKERLAT", 54.571083);
                extras.putDouble("ARG_MARKERLNG", -1.235672);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "11":
                extras.putDouble("ARG_MARKERLAT", 54.57124);
                extras.putDouble("ARG_MARKERLNG",-1.236662);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "12":
                extras.putDouble("ARG_MARKERLAT", 54.571729);
                extras.putDouble("ARG_MARKERLNG",-1.235879);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "14":
                extras.putDouble("ARG_MARKERLAT", 54.571886);
                extras.putDouble("ARG_MARKERLNG",-1.234682);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "15":
                extras.putDouble("ARG_MARKERLAT", 54.572517);
                extras.putDouble("ARG_MARKERLNG",-1.234878);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "16":
                extras.putDouble("ARG_MARKERLAT", 54.572195);
                extras.putDouble("ARG_MARKERLNG",-1.234094);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "17":
                extras.putDouble("ARG_MARKERLAT", 54.572395);
                extras.putDouble("ARG_MARKERLNG",-1.233284);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "18":
                extras.putDouble("ARG_MARKERLAT", 54.572162);
                extras.putDouble("ARG_MARKERLNG",-1.233581);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "19":
                extras.putDouble("ARG_MARKERLAT", 54.571697);
                extras.putDouble("ARG_MARKERLNG",-1.233249);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "20":
                extras.putDouble("ARG_MARKERLAT", 54.571347);
                extras.putDouble("ARG_MARKERLNG",-1.232328);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "21":
                extras.putDouble("ARG_MARKERLAT", 54.571058);
                extras.putDouble("ARG_MARKERLNG",-1.233947);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "22":
                extras.putDouble("ARG_MARKERLAT", 54.570711);
                extras.putDouble("ARG_MARKERLNG",-1.233218);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "23":
                extras.putDouble("ARG_MARKERLAT", 54.570717);
                extras.putDouble("ARG_MARKERLNG",-1.233619);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
            case "24":
                extras.putDouble("ARG_MARKERLAT", 54.570768);
                extras.putDouble("ARG_MARKERLNG",-1.234372);
                switchToMap.putExtras(extras);
                context.startActivity(switchToMap);
                break;
        }
    }
}
