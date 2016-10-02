package com.fs.lib.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by milton on 18/02/16.
 * // add joda time gradle dependency
 */
public class DateTimeHelper {
    public static String formateDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
       return dateFormat.format(date);
    }
}
