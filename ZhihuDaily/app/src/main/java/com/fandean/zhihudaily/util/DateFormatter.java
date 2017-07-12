package com.fandean.zhihudaily.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fan on 17-6-12.
 */

public class DateFormatter {
    public String ZhihuDailyDateFormat(long date) {
        String sDate;
        Date d = new Date(date + 24*60*60*1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        sDate = format.format(d);

        return sDate;
    }

    public String DoubanDateFormat(long date){
        String sDate;
        Date d = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        sDate = format.format(d);

        return sDate;
    }
}
