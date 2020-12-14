package org.ualr.mobileapps.sneakercalendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.US);

        return format.format(date);
    }
}
