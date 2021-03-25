package services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.sd.carc.Sipdroid;

/**
 * Created by ramsrini on 14/03/18.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsMessages = null;
        try {
            if (bundle != null) {
                // Get the SMS message
                Object[] pdus = (Object[]) bundle.get("pdus");
                smsMessages = pdus != null ? new SmsMessage[pdus.length] : new SmsMessage[0];
                for (int i = 0; i < smsMessages.length; i++) {
                    smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    Sipdroid.getmInstanceActivity().updateOtp(smsMessages[i].getDisplayOriginatingAddress(), smsMessages[i].getMessageBody());

//                    smsDetails += "\r\nMessage: ";
//                    smsDetails += smsm[i].getMessageBody();
//                    smsDetails += "\r\nNumber: ";
//                    smsDetails += smsm[i].getDisplayOriginatingAddress();
//                    smsDetails += "\r\n";

                    //Check here sender is yours
//                    Intent smsIntent = new Intent("otp");
//                    smsIntent.putExtra("message", smsDetails);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
