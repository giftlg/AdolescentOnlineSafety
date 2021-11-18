package com.example.adolescentonlinesafety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBootReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(LoggedInActivity.TAG, "boot completed");

            // start the PowerButton Service
            context.startService(new Intent(context, PowerButtonService.class));
        }

}
