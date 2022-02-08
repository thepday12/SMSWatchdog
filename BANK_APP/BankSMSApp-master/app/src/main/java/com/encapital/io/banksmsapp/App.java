package com.encapital.io.banksmsapp;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.Toast;

import com.encapital.io.banksmsapp.utils.CommonUtils;
import com.encapital.io.banksmsapp.utils.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.encapital.io.banksmsapp.utils.CommonUtils.sendSMSError;


public class App extends Application  {

    @Override
    public void onCreate() {
        setDeveloperMode();
        super.onCreate();


        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });
    }

    public void setDeveloperMode(){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
    }

    public void handleUncaughtException (Thread thread, final Throwable e)
    {

            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Application crashed", Toast.LENGTH_LONG).show();
                    sendSMSError(getApplicationContext(), "Application BankSMS crashed  ", new CommonUtils.IOnSendSMSComplete() {
                        @Override
                        public void onSendSMSComplete(Boolean status) {
                        }
                    });
                    extractLogToFile();
                    Looper.loop();
                }
            }.start();

            try
            {
                Thread.sleep(4000); // Let the Toast display before app will get shutdown
                System.exit(1);
            }
            catch (InterruptedException ex)
            {
                // Ignored.
            }

    }

    private void extractLogToFile()
    {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException  e2 ) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        String fileName =  "log_crash_"+ CommonUtils.getCurrentDate()+".txt";

        File myDir = new File(Config.CRASH_DIR);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File(myDir, fileName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try
        {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = "logcat -e time";
            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader (process.getInputStream());

            // write output stream
            writer = new FileWriter(file,true);
            writer.write ("Android version: " +  Build.VERSION.SDK_INT + "\n");
            writer.write ("Device: " + model + "\n");
            writer.write ("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            char[] buffer = new char[10000];
            do
            {
                int n = reader.read (buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write (buffer, 0, n);
            } while (true);
            writer.write ("\n------------------------------------------------------");
            reader.close();
            writer.close();

        }
        catch (IOException e)
        {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                }
        }

    }

}
