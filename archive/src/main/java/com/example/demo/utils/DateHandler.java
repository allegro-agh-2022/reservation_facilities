package com.example.demo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateHandler {
    public static Date handleDate(String date){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date result;
        try{
            result = df.parse(date);
        }
        catch(Exception e){
            result = null;
        }
        return result;
    }

    public static String getDate(int offset){
        LocalDate dateObj = LocalDate.now();
        dateObj = dateObj.plusDays(offset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = dateObj.format(formatter);
        return date;
    }
}
