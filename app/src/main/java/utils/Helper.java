package utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ramsrini on 14/03/17.
 */

public class Helper {

    public static String encodeSingleQuote(String esoAndMsgTxt) {
        String[] splitArrayTxtMsg = esoAndMsgTxt.split("'");
        String slashedTxtmsg = "";
        int lengthOfMsgTxt = splitArrayTxtMsg.length;
        if (lengthOfMsgTxt > 1) {
            for (int eachMessage = 0; eachMessage < lengthOfMsgTxt; eachMessage++) {
                slashedTxtmsg += splitArrayTxtMsg[eachMessage];
                if (eachMessage != lengthOfMsgTxt - 1) {
                    slashedTxtmsg += "\\'";
                }
            }
            return slashedTxtmsg;
        } else {
            return esoAndMsgTxt;
        }
    }

    public static String encodeDoubleQuote(String esoAndMsgTxt) {
        String[] splitArrayTxtMsg = esoAndMsgTxt.split("'\"");
        String slashedTxtmsg = "";
        int lengthOfMsgTxt = splitArrayTxtMsg.length;
        if (lengthOfMsgTxt > 1) {
            for (int eachMessage = 0; eachMessage < lengthOfMsgTxt; eachMessage++) {
                slashedTxtmsg += splitArrayTxtMsg[eachMessage];
                if (eachMessage != lengthOfMsgTxt - 1) {
                    slashedTxtmsg += "\"";
                }
            }
            return slashedTxtmsg;
        } else {
            return esoAndMsgTxt;
        }
    }

    public String encode(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static String getDateDiffInStr(String appClosedTime) {
        String diffInSecStr = "";
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
        String currentTime = dateFormat.format(Calendar.getInstance().getTime());

        try {
            Date currentTimeInDate = dateFormat.parse(currentTime);
            Date appClosedTimeInDate = dateFormat.parse(appClosedTime);
            long differenceInTime = currentTimeInDate.getTime() - appClosedTimeInDate.getTime();
            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(differenceInTime);
            diffInSecStr = String.valueOf(diffInSec);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diffInSecStr;
    }
}
