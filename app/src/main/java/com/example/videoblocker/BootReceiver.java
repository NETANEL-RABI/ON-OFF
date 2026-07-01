package com.example.videoblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // האות של עליית המערכת התקבל.
            // מערכת אנדרואיד תפעיל אוטומטית את שירות הנגישות בהדלקה אם המשתמש אישר אותו.
        }
    }
}
