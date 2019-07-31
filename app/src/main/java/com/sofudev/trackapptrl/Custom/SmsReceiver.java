package com.sofudev.trackapptrl.Custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by sholeh on 12/06/2018.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener smsListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();

        try {
            if (data != null)
            {
                //Ambil data dari PDU SMS
                Object[] pduData = (Object[]) data.get("pdus");

                for (int i = 0; i < pduData.length; i++)
                {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pduData[i]);

                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String msg    = smsMessage.getDisplayMessageBody();

                    if (sender.contentEquals("083122668937") || sender.contentEquals("+6283122668937"))
                    {
                        smsListener.ReceiveMsg(msg);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getNewMessage(SmsListener listener)
    {
        smsListener = listener;
    }
}
