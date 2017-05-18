package cn.wang.img.selector.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
public class DateUtils {

    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_DAY = "yyyy-MM-dd";

    public static String format(long time, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(new Date(time));
    }

    /**
     * 将time转换成为 格式化的时间字符串yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String formatDateTime(long time) {
        return format(time, FORMAT_DATE_TIME);
    }

    /**
     * @param date
     * @param format
     * @return -1表示转换失败
     */
    public static long parseDateTime(String date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将准确的时间值转变成对应的日时间
     *
     * @param time
     * @return
     */
    public static long datetime2DayTime(long time) {
        return parseDateTime(format(time, FORMAT_DATE_DAY), FORMAT_DATE_DAY);
    }

}
