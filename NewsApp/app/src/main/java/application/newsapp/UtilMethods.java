package application.newsapp;

import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class UtilMethods {

    public static String formatDateToString(String dateString) {
        String finalStringDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = dateFormat.parse(dateString);

            SimpleDateFormat stringFormat = new SimpleDateFormat("dd'.'MM'.'YYYY");
            finalStringDate = stringFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalStringDate;
    }

}
