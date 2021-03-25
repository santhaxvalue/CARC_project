package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

/**
 * Created by ramsrini on 14/03/18.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str = "";
        try {
            if (bundle != null) {
                // Get the SMS message
                Object[] pdus = (Object[]) bundle.get("pdus");
                smsm = new SmsMessage[pdus.length];
                for (int i = 0; i < smsm.length; i++) {
                    smsm[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    sms_str += "\r\nMessage: ";
                    sms_str += smsm[i].getMessageBody();
                    sms_str += "\r\nNumber: ";
                    sms_str += smsm[i].getDisplayOriginatingAddress();
                    sms_str += "\r\n";

                    //String Sender = smsm[i].getOriginatingAddress();
                    //Check here sender is yours
                    Intent smsIntent = new Intent("otp");
                    smsIntent.putExtra("message", sms_str);

                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
