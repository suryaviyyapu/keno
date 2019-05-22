package com.bl4k3.keno;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");

    public static DateFormatter instance = new DateFormatter();

    private DateFormatter() {
    }

    public String convertDateToString(Date date) {
        return dateFormat.format(date);
    }

}
