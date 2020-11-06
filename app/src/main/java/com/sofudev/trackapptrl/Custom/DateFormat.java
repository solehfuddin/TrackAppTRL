package com.sofudev.trackapptrl.Custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    public String indo(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date newdate = sdf.parse(data);
            sdf = new SimpleDateFormat("dd-MM-yyyy");

            data = sdf.format(newdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }

    public String Indotime(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date newdate = sdf.parse(data);
            sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            data = sdf.format(newdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }

    public String ValueDbDate(int dayOfMonth, int month, int year) {
        month = (month + 1);
        String day;

        if (dayOfMonth < 10)
        {
            day = "0" + dayOfMonth;
        }
        else
        {
            day = String.valueOf(dayOfMonth);
        }

        return year + "-" + month + "-" + day;
    }

    public String ValueUserDate(int dayOfMonth, int month, int year) {
        month = (month + 1);
        String mon;
        String day;

        if (month == 1)
        {
            mon = "JAN";
        }
        else if (month == 2)
        {
            mon = "FEB";
        }
        else if (month == 3)
        {
            mon = "MAR";
        }
        else if (month == 4)
        {
            mon = "APR";
        }
        else if (month == 5)
        {
            mon = "MAY";
        }
        else if (month == 6)
        {
            mon = "JUN";
        }
        else if (month == 7)
        {
            mon = "JUL";
        }
        else if (month == 8)
        {
            mon = "AUG";
        }
        else if (month == 9)
        {
            mon = "SEP";
        }
        else if (month == 10)
        {
            mon = "OCT";
        }
        else if (month == 11)
        {
            mon = "NOV";
        }
        else
        {
            mon = "DEC";
        }

        if (dayOfMonth < 10)
        {
            day = "0" + dayOfMonth;
        }
        else
        {
            day = String.valueOf(dayOfMonth);
        }

        return day + " " + mon + " " + year;
    }
}
