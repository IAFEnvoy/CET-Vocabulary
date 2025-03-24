package com.iafenvoy.cet.util;

public class TimeUtil {
    public static String getDate(int date) {
        if (date < 60) {
            return date + "s";
        } else if (date > 60 && date < 3600) {
            int m = date / 60;
            int s = date % 60;
            return m + "m" + s + "s";
        } else {
            int h = date / 3600;
            int m = (date % 3600) / 60;
            int s = (date % 3600) % 60;
            return h + "h" + m + "m" + s + "s";
        }
    }
}
