package tongji.edu.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: Mar 31, 2013 3:06:46 PM
 */
public class DateUtil {        
        static SimpleDateFormat df_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        static SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
        static SimpleDateFormat df_time = new SimpleDateFormat("HH:mm:ss");
        
        public static String longToStr(String time) {
        	
        	long _time = (long)Double.parseDouble(time);
        	
        	Calendar cal = Calendar.getInstance();
        	cal.setTimeInMillis(_time);
        	
        	return df_datetime.format(cal.getTime());
        }
        
        public static Date strToDate(String str) {
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");        
        	Date date = null;                  
        	                    
        	try {   
        	    date = format.parse(str);  // Thu Jan 18 00:00:00 CST 2007   
        	} catch (Exception e) {   
        	    e.printStackTrace();   
        	} 
        	
        	return date;
        }
        
        public static String getNextDay(String str) {
        	Date date = strToDate(str.split(" ")[0]);
        	
        	Calendar cal = Calendar.getInstance(); 
        	cal.setTime(date); 
            cal.add(cal.DATE, 1); 
            
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
            return sdf.format(cal.getTime());
        }
        
        /**
         * 
                 * 此方法描述的是：得到当前时间的datetime
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:33:36
         */
        public static String getDatetime() {
                return  df_datetime.format(new Date());
        }
        
        /**
         * 
                 * 此方法描述的是：以特定形式展示当前datetime
             * @param pattern
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:34:48
         */
        public static String getDatetimeWithPattern(String pattern) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.format(new Date());
        }
        
        /**
         * 
                 * 此方法描述的是：得到当前日期
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:35:32
         */
        public static String getDate() {
                return df_date.format(new Date());
        }
        
        /**
         * 
                 * 此方法描述的是：以特定形式返回当前日期
             * @param pattern
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:35:44
         */
        public static String getDateWithPattern(String pattern) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.format(new Date());
        }
        
        /**
         * 
                 * 此方法描述的是：得到当前时间
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:36:03
         */
        public static String getTime() {
                return df_date.format(new Date());
        }
        
        /**
         * 
                 * 此方法描述的是：以特定形式返回当前时间
             * @param pattern
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:36:21
         */
        public static String getTimeWithPattern(String pattern) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.format(new Date());
        }
        
        /**
         * 
                 * 此方法描述的是：以特定形式返回前后几天的时间
             * @param pattern
             * @param day   +1  表示明天 -1 表示昨天
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:36:51
         */
        public static String getDateWithDayInterval(String pattern, int day) {
                Calendar c =  Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat sf = new SimpleDateFormat(pattern);
                return sf.format(c.getTime());
        }
        
        public static int getDayOfWeek() {
                return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        }
        
        public static int getTimeOfDay() {
                return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        }
        
        public static int getHour(Date d) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                return cal.get(Calendar.HOUR_OF_DAY);
        }
        
        public static int getMinute(Date d) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                return cal.get(Calendar.MINUTE);
        }
        
        public static int getSecond(Date d) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                return cal.get(Calendar.SECOND);
        }
        
        /**
         * 
                 * 此方法描述的是：timestamp convert to string
             * @param t
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:38:20
         */
        public static String timestampToString(Timestamp t) {
                return df_datetime.format(t);
        }
        
        /**
         * 
                 * 此方法描述的是：string convert to timestamp
             * @param str
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:38:35
         */
        public static Timestamp stringToTimestamp(String str) {
                return Timestamp.valueOf(str);
        }
        

        /**
         * 
                 * 此方法描述的是：将timestamp转换为特定形式的时间
             * @param t
             * @param seconds
             * @param pattern
             * @return
             * @author: dmnrei@gmail.com
             * @version: 2013-3-31 下午6:38:52
         */
        public static String timestampToString(Timestamp t, int seconds, String pattern) {
                SimpleDateFormat df = new SimpleDateFormat(pattern);
                Date d = new Date(t.getTime() + seconds * 1000);
                return df.format(d);
        }
}