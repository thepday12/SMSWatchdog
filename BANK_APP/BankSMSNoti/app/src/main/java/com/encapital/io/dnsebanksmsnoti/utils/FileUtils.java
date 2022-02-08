package com.encapital.io.dnsebanksmsnoti.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    private static final String EXCEPTION_FILE_NAME ="EXCEPTION";
    private static final String EXCEPTION_SEND_REQUEST ="EXCEPTION_SEND_REQUEST";
    private static final String FAIL_SEND_REQUEST ="FAIL_SEND_REQUEST";

    private static void writeErrorLogFile(String name, String message)
    {
        String fileName =  name+ CommonUtils.getCurrentDate()+".txt";

        File myDir = new File(Config.EXCEPTION_DIR);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File(myDir, fileName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try
        {
            writer = new FileWriter(file,true);
            writer.write ( CommonUtils.getCurrentDate("yyyy-mm-dd hh:mm:ss ")+"-------------------------------------\n");
            writer.write (message);
            writer.close();

        }
        catch (IOException e)
        {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException ignored) {
                }

        }

    }

    public  static  void writeErrSendDeposit(String message){
        writeErrorLogFile(EXCEPTION_SEND_REQUEST,message);
    }

    public  static  void writeFailSendDeposit(String message){
        writeErrorLogFile(FAIL_SEND_REQUEST,message);
    }
}
