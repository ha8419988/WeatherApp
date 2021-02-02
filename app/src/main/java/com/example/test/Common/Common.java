package com.example.test.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID = "e63dfb6164dcf33183e1a6ded25a3020";
    public static Location current_location=null;
public static String convertToDate(int dt){
    Date date=new Date(dt*1000L);
    SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm dd EEE MM yyyy");
    String formatted=dateFormat.format(date);
    return formatted;
}
    public static String convertToHour(long dt){
        Date date=new Date(dt*1000L);
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm");
        String formatted=dateFormat.format(date);
        return formatted;

    }
}
