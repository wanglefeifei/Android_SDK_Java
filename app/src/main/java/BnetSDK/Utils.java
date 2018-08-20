package BnetSDK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;


public class Utils {
    private Activity activity;
    private Context context;
    private static String currVer = "";

    /**
     *
     *
     * @param inputstream
     * @param iBufferSize
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(InputStream inputstream, int iBufferSize)
            throws IOException {
        byte[] bArr = new byte[iBufferSize];
        int iIndex = 0;
        int iLength = 0;
        int iTotalLength = 0;
        while ((iLength = inputstream.read(bArr, iIndex, bArr.length - iIndex)) != -1) {
            iIndex += iLength;
            if (iLength != -1) {
                iTotalLength += iLength;
            }
            if (iIndex >= bArr.length) {
                byte[] arrbTemp = new byte[bArr.length];
                System.arraycopy(bArr, 0, arrbTemp, 0, bArr.length);
                bArr = null;
                bArr = new byte[arrbTemp.length + iBufferSize];
                System.arraycopy(arrbTemp, 0, bArr, 0, arrbTemp.length);
                arrbTemp = null;
            }

        }
        byte[] bBack = new byte[iTotalLength];

        System.arraycopy(bArr, 0, bBack, 0, bBack.length);

        bArr = null;

        return bBack;
    }

    /**
     * dip2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     *
     *
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    public static double getDistance(double longitude1, double latitude1,

                                     double longitude2, double latitude2) {

        final double EARTH_RADIUS = 6378137.0;
        double Lat1 = rad(latitude1);

        double Lat2 = rad(latitude2);

        double a = Lat1 - Lat2;

        double b = rad(longitude1) - rad(longitude2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)

                + Math.cos(Lat1) * Math.cos(Lat2)

                * Math.pow(Math.sin(b / 2), 2)));

        s = s * EARTH_RADIUS;

        s = Math.round(s * 10000) / 10000;

        return s;

    }

    private static double rad(double d) {

        return d * Math.PI / 180.0;

    }

    /**
     * Converting InputStream into some character encoding String
     *
     * @param in
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding)
            throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, 1024)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), encoding);
    }

    // ---------------------------------get uid mac

    @SuppressLint("MissingPermission")
    public static String getUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    public static String getMAC(Context context) {
        String macAddress = null;
        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress == null ? "" : macAddress;

    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * get network status，wifi,wap,2g,3g.
     *
     * @param context
     */
    public static int getNetWorkType(Context context) {
        /** no  network */
        int NETWORKTYPE_INVALID = 0;
        /** wap  */
        int NETWORKTYPE_WAP = 1;
        /** 2G  */
        int NETWORKTYPE_2G = 2;
        /** 3G or 4G */
        int NETWORKTYPE_3G = 3;
        /** wifi  */
        int NETWORKTYPE_WIFI = 4;
        int mNetWorkType = 0;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G
                        : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }


    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * format times
     *
     * @param dateTaken
     * @return
     */
    public static String createName(long dateTaken) {
        return DateFormat.format("yyyy-MM-dd kk.mm.ss", dateTaken).toString();
    }



    public static String replaceAll(String src, String fnd, String rep) throws Exception {
        if (src == null || src.equals("")) {
            return "";
        }

        String dst = src;

        int idx = dst.indexOf(fnd);

        while (idx >= 0) {
            dst = dst.substring(0, idx) + rep + dst.substring(idx + fnd.length(), dst.length());
            idx = dst.indexOf(fnd, idx + rep.length());
        }

        return dst;
    }

    /**
     *
     */
    public static String htmlTextEncoder(String src) throws Exception {
        if (src == null || src.equals("")) {
            return "";
        }

        String dst = src;
        dst = replaceAll(dst, "<", "&lt;");
        dst = replaceAll(dst, ">", "&rt;");
        dst = replaceAll(dst, "\"", "&quot;");
        dst = replaceAll(dst, "'", "&#039;");
        dst = replaceAll(dst, " ", "&nbsp;");
        dst = replaceAll(dst, "\r\n", "<br>");
        dst = replaceAll(dst, "\r", "<br>");
        dst = replaceAll(dst, "\n", "<br>");

        return dst;
    }

    /**
     *
     *
     * @param src
     * @return
     */
    public static String text2Html(String src) {

        //			src = src.replaceAll("<",    "&lt;");
        //			src = src.replaceAll( ">",    "&rt;");
        //			src = src.replaceAll( "\"",   "&quot;");
        //			src = src.replaceAll( "'",    "&#039;");
        //			src = src.replaceAll( " ",    "&nbsp;");

        src = "<p style='text-indent:2em;line-height:1.5em;'>" + src;
        src = src.replaceAll("\r\n", "</p> <p style='text-indent:2em;line-height:1.5em;'>");
        src = src.replaceAll("\r", "</p> <p style='text-indent:2em;line-height:1.5em;'>");
        src = src.replaceAll("\n", "</p> <p style='text-indent:2em;line-height:1.5em;'>");

        src = src + "</p>";
        return src;

    }


    /**
     *
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            int color = 0xff313131;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            RectF rectF = new RectF(rect);
            float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            if (bitmap != null && !bitmap.isRecycled() && bitmap != output) {
                bitmap.recycle();
            }

            return output;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }

        return null;
    }


    /**
     * 根据屏幕的分辨率计算出控件在该分辨率下的高度
     *
     * @param context
     * @param picWidth
     * @param picHeight
     * @return
     */
    public static int getScreenPicHeight(Activity context, int picWidth, int picHeight) {

        Display display = context.getWindowManager().getDefaultDisplay();
        int picScreenHeight = 0;
        picScreenHeight = (display.getWidth() * picHeight) / picWidth;
        return picScreenHeight;
    }

    /**
     * 根据屏幕的分辨率计算出控件在该分辨率下的宽度
     *
     * @param context
     * @param picWidth
     * @return
     */
    public static int getScreenPicWidth(Activity context, int picWidth) {

        Display display = context.getWindowManager().getDefaultDisplay();
        int picScreenWidth = 0;
        picScreenWidth = (display.getWidth() * picWidth) / 480;   //根据480*800的标准进行适配
        return picScreenWidth;
    }

    /**
     * 把电话号码替换成带星号的 例如：182****6742 假如不是电话号码的就不进行替换
     *
     * @param phone
     * @return
     */
    public static String replacePhoneWithAsterisk(String phone) {
        String newphone = phone;
        if (isMobileNO(newphone)) {
            newphone = phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return newphone;
    }

    /**
     * double取两位
     *
     * @param a
     * @return
     */
    public static double formatDoubleReturnDouble(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return isDouble(df.format(a));
    }


    /**
     * 判断APP是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
                if (pn.contains(packageName)) {
                    String version = pinfo.get(i).versionName;// 获取版本号
                    int versionNum = Utils.isInteger(version.replace(".", "").trim());
                }
            }
        }
        return pName.contains(packageName);
    }

    // 字符串的非空
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    // 判断网络是否连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                //                LogFileUtils.WriteText("网络是否连接：" + mNetworkInfo.isAvailable());
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    // 验证手机号
    public static boolean isMobileNO(String mobiles) {
        // Pattern pattern = Pattern
        // .compile("^((13[0-9]{1})|159|153|189|182)+\\d{8}$");
        Pattern pattern = Pattern.compile("^(((13[0-9]{1})|(15[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$");
        Matcher m = pattern.matcher(mobiles);
        return m.matches();
    }

    // 验证身份证
    public static boolean doAuthentication(String shenfen) {
        Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher m = pattern.matcher(shenfen);
        return m.matches();
    }

    // 验证只能输入数字和字母
    public static boolean InputFigureLetter(String input) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = pattern.matcher(input);
        return m.matches();
    }

    /**
     * 字符串转日期(yyyy-MM-dd)
     */
    public static Date StrToDate(String str) {
        return StrToDate(str, "yyyy-MM-dd");
    }

    /**
     * 字符串转日期()
     */
    public static Date StrToDate2(String str) {
        return StrToDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 得到当前时间之后的几个小时时间
     *
     * @param differhour
     * @return
     */
    public static String getCurrentHourAfter(int differhour) {
        long currenttime = new Date().getTime();
        Date dat = new Date(currenttime + 1000 * 60 * 60 * differhour);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(dat);
    }

    /**
     * 得到当前时间之前的几个小时时间
     *
     * @param differhour
     * @return
     */
    public static String getCurrentHourBefor(int differhour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        long currenttime = new Date().getTime();
        Date dat = new Date(currenttime - 1000 * 60 * 60 * 2);
        // format.parse(format.format(dat))
        return format.format(dat);
    }

    /**
     * 字符串转日期
     */
    public static Date StrToDate(String str, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转换成Java字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    /**
     * 时间转化
     *
     * @param time
     * @param type
     * @return
     */
    public static String timeToFormatedString(String time, String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(type);
        long lt = new Long(time);
        Date date = new Date(lt);
        String res = dateFormat.format(date);
        return res;
    }

    /**
     * 获取当前时间，格式为 :yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(now);
        return date;
    }

    /**
     * 获取当前时间，格式为 :yyyy-MM-dd
     *
     * @return
     */
    public static Integer getCurrentDate_MM() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        String date = dateFormat.format(now);
        return isInteger(date);
    }

    /**
     * 获取当前时间，格式为 :yyyy-MM-dd
     *
     * @return
     */
    public static Integer getCurrentDate_dd() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(now);
        return isInteger(date);
    }

    /**
     * 获取当前时间，格式为 :yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentDate2() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(now);
        return date;
    }

    /**
     * 获取当前时间，格式为 :yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String getCurrentDate3() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = dateFormat.format(now);
        return date;
    }

    /**
     * 获取明天零点时间，格式为 :yyyy-MM-dd HH:mm：ss
     *
     * @return
     */
    public static String getTomorrowDateAtZeroAM() {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString.substring(0, dateString.length() - 8) + "00:00:00";
    }

    /**
     * string -> int
     *
     * @param s
     * @return
     */
    public static int strToInt(String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;

    }

    /**
     * 比较两个 yyyy-MM-dd 格式的日期字符串时间前后
     *
     * @param date1
     * @param date2
     * @return true:"date1在date2后" , false:"date1在date2前"
     */
    public static boolean dateComparator(String date1, String date2) {
        return dateComparator(date1, date2, "yyyy-MM-dd");
    }

    /**
     * 比较两个 yyyy-MM-dd HH:mm:ss 格式的日期字符串时间前后
     *
     * @param date1
     * @param date2
     * @return true:"date1在date2后" , false:"date1在date2前"
     */
    public static boolean dateComparator2(String date1, String date2) {
        return dateComparator(date1, date2, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 比较两个 yyyy-MM-dd HH:mm:ss 格式的日期字符串时间前后
     *
     * @param date1
     * @param date2
     * @param str
     * @return true date1 小于date2 false date1大于date2
     */
    public static boolean dateComparator(String date1, String date2, String str) {

        //        DateFormat df = new SimpleDateFormat(str);
        //        try {
        //            Date dt1 = df.parse(date1);
        //            Date dt2 = df.parse(date2);
        //            if (dt1.getTime() > dt2.getTime()) {
        //                return false;
        //            } else if (dt1.getTime() < dt2.getTime()) {
        //                return true;
        //            } else {
        //                return true;
        //            }
        //        } catch (Exception exception) {
        //            exception.printStackTrace();
        return false;
        //        }
    }

    /**
     * 比较两个 yyyy-MM-dd HH:mm:ss 格式的日期字符串是否相等
     *
     * @param date1
     * @param date2
     * @return false 不等 true 相等
     */
    public static boolean dateIsequel(String date1, String date2) {
        //        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //        try {
        //            Date dt1 = df.parse(date1);
        //            Date dt2 = df.parse(date2);
        //            if (dt1.getTime() == dt2.getTime()) {
        //                return true;
        //            } else {
        //                return false;
        //            }
        //        } catch (Exception exception) {
        //            exception.printStackTrace();
        return false;
        //        }
    }

    /**
     * 获取两个日期的差 yyyy-MM-dd
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long dateDifference1(String date1, String date2) {
        return dateDifference(date1, date2, "yyyy-MM-dd");
    }

    /**
     * 获取两个日期的差 yyyy-MM-dd HH:mm:ss
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long dateDifference2(String date1, String date2) {
        return dateDifference(date1, date2, "yyyy-MM-dd HH:mm:ss");

    }

    /**
     * 获取两个日期的差
     *
     * @param date1
     * @param date2
     * @param str
     * @return
     */
    public static long dateDifference(String date1, String date2, String str) {
        //        DateFormat df = new SimpleDateFormat(str);
        //        try {
        //            Date dt1 = df.parse(date1);
        //            Date dt2 = df.parse(date2);
        //            long temp = dt2.getTime() - dt1.getTime();
        //            long result = temp / (1000 * 60);
        //            return result;
        //        } catch (Exception exception) {
        //            exception.printStackTrace();
        return 0;
        //        }
    }

    /**
     * 得到两个日期的差
     *
     * @param fDate
     * @param oDate
     * @return 天数
     */
    public static int daysOfTwo(String fDate, String oDate) {
        //        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //        DateFormat df =   new DateFormat("yyyy-MM-dd HH:mm:ss");
        //
        //        Date dt1;
        //        Date dt2;
        //        try {
        //            dt1 = df.parse(fDate);
        //            dt2 = df.parse(oDate);
        //
        //            Calendar aCalendar = Calendar.getInstance();
        //
        //            aCalendar.setTime(dt1);
        //
        //            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        //            int year1 = aCalendar.get(Calendar.YEAR);
        //
        //            aCalendar.setTime(dt2);
        //
        //            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        //            int year2 = aCalendar.get(Calendar.YEAR);
        //
        //            return (day2 - day1) + (year2 - year1) * 365;
        //        } catch (ParseException e) {
        //            e.printStackTrace();
        return 0;
        //    }
    }

    /**
     * 比较两个数的大小
     *
     * @param num1
     * @param num2
     * @return
     */
    public static boolean numComparator(String num1, String num2) {
        int int1;
        int int2;
        try {
            int1 = Integer.parseInt(num1.trim());
            int2 = Integer.parseInt(num2.trim());
            return int1 > int2;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param time 需要获取的日期
     * @return 当前日期是星期几，(从0开始，周日、周一.....)
     */
    public static int getWeekOfDate(String time) {
        //        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //        Date dt;
        //        int week = 0;
        //        try {
        //            dt = df.parse(time);
        //            Calendar cal = Calendar.getInstance();
        //            cal.setTime(dt);
        //            week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        //            if (week < 0)
        //                week = 0;
        //        } catch (ParseException e) {
        //        }
        //        return week;
        return 0;
    }

    /**
     * 判断是否为double类型
     *
     * @param str
     * @return
     */
    public static boolean isDoubleNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) { // 不是数字
            return false;
        }
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) { // 不是数字
            return false;
        }
    }

    /**
     * 关于EditText的判断方法
     *
     * @param editText
     * @param yajin    限额大小
     * @param c
     */
    public static void setPricePoint(final EditText editText, final double yajin, final Context c) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isDoubleNumeric(s.toString())) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                            editText.setText(s);
                            editText.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        editText.setText(s);
                        editText.setSelection(2);
                    }
                    if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            editText.setText(Utils.isInteger(s.subSequence(0, s.length()).toString()) + "");
                            // editText.setSelection(s.length()+1);
                            return;
                        }
                    }
                } else {
                    return;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    editText.setText("0");
                    return;
                }
                if (!isDoubleNumeric(s.toString())) {
                    editText.setText("0");
                    Toast.makeText(c, "请输入正确价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                double strcount = Double.parseDouble(s.toString());
                double count = yajin;
                if (strcount > count) {
                    editText.setText("0");
                    Toast.makeText(c, "超出限额", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 时间秒数转换为时间
     */
    public static String getDatesft(Long dates) {
        // long sstime = dates.toString();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dates);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    /**
     * 时间秒数转换为时间
     */
    public static String getFullDate(Long dates) {
        // long sstime = dates.toString();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dates);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 时间秒数转换为时间
     */
    public static String getDate(Long dates) {
        // long sstime = dates.toString();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dates);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 时间秒数转换为时间
     */
    public static String getDate2(Long dates) {
        // long sstime = dates.toString();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dates);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String checkInt(String num) {
        return (num == null || !Utils.isNumeric(num)) ? "0" : num;
    }

    public static String checkDouble(String num) {
        return (num == null || "NaN".equals(num) || !Utils.isDoubleNumeric(num) || num == "null") ? "0" : num;
    }

    public static String checkStr(String str) {
        return Utils.isEmpty(str) ? "" : str;
    }

    /**
     * 获取前3天时间
     */
    public static String FrontThreeDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3); // 得到前三天
        Date date = calendar.getTime();
        String dates = DateToStrtimeminute(date);
        return dates;
    }

    /**
     * 获取前15天时间
     */
    public static String FroutFifteenFDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -15); // 得到前十五天
        Date date = calendar.getTime();
        String dates = DateToStrtimeminute(date);
        return dates;
    }

    /**
     * 获取前30天时间
     */
    public static String FroutthirtyDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30); // 得到前三十天
        Date date = calendar.getTime();
        String dates = DateToStrtimeminute(date);
        return dates;
    }

    /**
     * 几分钟以后的时间
     *
     * @param after
     * @return
     */
    public static String MinueLaterTime(int after) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, after); // 得到前三十天
        Date date = calendar.getTime();
        String dates = DateToStrtimeminute(date);
        return dates;
    }

    /**
     * 日期转换字符串
     */
    public static String DateToStrtimeminute(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 检查Integer数据
     */
    public static Integer isInteger(Integer num) {
        return isInteger(num + "");
    }

    /**
     * 检查Double数据
     */
    public static Double isDouble(Double num) {
        return isDouble(num + "");
    }

    /**
     * String检查Integer数据
     */
    public static Integer isInteger(String num) {
        return Integer.parseInt(checkInt(num));
    }

    /**
     * String检查Double数据
     */
    public static Double isDouble(String num) {
        return Double.parseDouble(checkDouble(num));
    }

    /**
     * 获取小时和分钟的字符串
     *
     * @param mDate
     * @return
     */
    public static String getShorDate(Date mDate) {
        if (mDate == null) {
            return "00:00";
        }
        String hoursStr = "";
        String minutesStr = "";
        int hours = mDate.getHours();
        int minutes = mDate.getMinutes();
        if (hours < 10) {
            hoursStr = "0" + hours;
        } else {
            hoursStr = "" + hours;
        }
        if (minutes < 10) {
            minutesStr = "0" + minutes;
        } else {
            minutesStr = "" + minutes;
        }
        return hoursStr + ":" + minutesStr;
    }

    /**
     * 两个double相减 返回保留2位小数的字符串
     *
     * @param a
     * @param b
     * @return
     */
    public static String getDoubleMin(double a, double b) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a - b);
    }

    /**
     * 两个double相减 返回保留2位小数的字符串
     *
     * @param x
     * @param y
     * @return
     */
    public static double getAddDouble(double x, double y) {
        BigDecimal add1 = new BigDecimal(Double.toString(x));
        BigDecimal add2 = new BigDecimal(y + "");

        return add1.add(add2).doubleValue();
    }

    /**
     * 得到小数点后两位
     *
     * @param x
     * @return
     */
    public static String parseDecimalDouble2(double x) {
        BigDecimal bg = new BigDecimal(isDouble(x));
        String a = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
        if (a.contains(".0") && a.lastIndexOf("0") == a.length() - 1) {// 该逻辑判断字符串包括.0并且最后一位也为0
            a = a.substring(0, a.indexOf("."));
        }
        return a;
    }

    /**
     * 得到String字符串小数点后两位
     *
     * @param a
     * @return
     */
    public static String parseDecimalString2(String a) {
        if ((Utils.checkStr(a)).contains(".0") && a.lastIndexOf("0") == a.length() - 1) {
            a = a.substring(0, a.indexOf("."));
        }
        return a;
    }

    /**
     * 得到小数点后两位
     *
     * @param str
     * @return
     */
    public static double parseDecimalDouble2(String str) {
        BigDecimal bg = new BigDecimal(isDouble(str));
        return bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 两个double相减 返回保留2位小数的字符串
     *
     * @param a
     * @param b
     * @return
     */
    public static String getDoubleMin(String a, String b) {
        double x = isDouble(a);
        double y = isDouble(b);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(x - y);
    }

    /**
     * 两个double相加 返回保留2位小数的字符串
     *
     * @param a
     * @param b
     * @return
     */
    public static String getDoubleAdd(double a, double b) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a + b);
    }

    /**
     * 两个double相加 返回保留2位小数的字符串
     *
     * @param a
     * @param b
     * @return
     */
    public static String getDoubleAdd(String a, String b) {
        double x = isDouble(a);
        double y = isDouble(b);
        return parseDecimalDouble2(x + y);
    }

    /**
     * double取两位
     *
     * @param a
     * @return
     */
    public static String formatDoubleReturnString(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }


    public static boolean isPad(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        float screenWidth = display.getWidth();
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        return screenInches >= 6.0;
    }

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    //    public static PLString MD5(PLString str) {
    //        MessageDigest md5 = null;
    //        try {
    //            md5 = MessageDigest.getInstance("MD5");
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return "";
    //        }
    //        char[] charArray = str.toCharArray();
    //        byte[] byteArray = new byte[charArray.length];
    //        for (int i = 0; i < charArray.length; i++) {
    //            byteArray[i] = (byte) charArray[i];
    //        }
    //        byte[] md5Bytes = md5.digest(byteArray);
    //        StringBuffer hexValue = new StringBuffer();
    //        for (int i = 0; i < md5Bytes.length; i++) {
    //            int val = ((int) md5Bytes[i]) & 0xff;
    //            if (val < 16) {
    //                hexValue.append("0");
    //            }
    //            hexValue.append(Integer.toHexString(val));
    //        }
    //        return hexValue.toString();
    //        //return hexValue.toString().substring(8,24)；   //16位
    //    }


    /**
     * 根据传入的字符串生成MD5码
     *
     * @param srcStr 待校验的字符串
     * @return MD5
     */
    public static String cmxMd5(final String srcStr) {
        //        Assert.notNull(srcStr);
        /**
         * 16进制位数字
         */
        final char hexDigits[] =
                {'A', 'B', '6', '7', 'C', 'D', 'E', '0', '1', '2', '3', '4', '5', '8', '9', 'F'};
        try {
            final byte[] btInput = srcStr.getBytes("UTF-8");

            // 获得MD5摘要算法的 MessageDigest 对象
            final MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            final byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            final int j = md.length;
            final char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                final byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        String newStr = new String(key.getBytes(), "UTF-8");
        return Base64.decode(newStr, Base64.DEFAULT);
    }


    /**
     * 公钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public String decryptByPublicKeyGetToken(String data)
            throws Exception {
        String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnkZV7iT+gIAOIgBSpTIIWsDx9kv0uCQ7i5uJGO32XRKKs+QciG8J5PhpmQKR1PiP9wJO+MxyCT/gAE8tVYVhtaQL3RUaHW3HUcAKaleGsTcl52Ha/TWe5sX+CYjPvGRchgmdpCWlPsVwUrzYMZ4c7BDUO7DtWLkY/rbwRualT+QIDAQAB";

        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        String bak = "";

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] bteyData = cipher.doFinal(decryptBASE64(data));
        bak = new String(bteyData, "UTF-8");

        return bak;
    }

    // 对象转为Base64
    public static String objectToBase64Code(Object o) {
        String rtn = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            rtn = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        } catch (Exception e) {
            // : handle exception
            e.printStackTrace();
            rtn = null;
        } finally {
            try {
                baos.close();

                oos.close();
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
        }

        return rtn;
    }

    // Base64转为对象
    @SuppressWarnings("unchecked")
    public static <T> T base64codeToObject(String scr, Type type) {
        byte[] bt = Base64.decode(scr, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(bt);
        ObjectInputStream ois = null;
        T ot = null;
        try {
            ois = new ObjectInputStream(bais);
            ot = (T) ois.readObject();
        } catch (Exception e) {
            // : handle exception
            ot = null;
        } finally {
            try {
                bais.close();
                ois.close();
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }

        }
        return ot;
    }


    /**
     * 获取当前APP的版本号
     *
     * @param mContext
     * @return
     */
    public static String getAppVer(Context mContext) {
        if (currVer != null && currVer.length() > 0) {
            return currVer;
        }
        // 上传用户版本信息
        PackageManager pManager = mContext.getPackageManager();
        //得到包信息
        try {
            PackageInfo pInfo = pManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            currVer = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currVer;
    }

    /**
     * 判断Activity是否活动
     *
     * @param ay
     * @return
     */
    public static boolean ActivityIsActivity(Activity ay) {
        if (ay == null)
            return false;
        if (ay.isFinishing()) {
            return false;
        }
        return !(Build.VERSION.SDK_INT >= 17 && ay.isDestroyed());
    }


    /**
     * 获取虚拟功能键高度
     *
     * @return
     */
    public int getVirtualBarHeigh(Activity activity) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 验证电话号码
     */
    public static boolean vertiPhoneNumber(String number) {
        boolean flag = false;
        // TODO Auto-generated method stub
        // 先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
        //
        // 　　移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、182、183、184、187、188、178(4G)
        //
        // 　　联通：130、131、132、155、156、185、186、176(4G)
        //
        // 　　电信：133、153、180、181、189 、177(4G)；
        // 卫星通信：1349；
        // 虚拟运营商：170
        Pattern p = Pattern
                .compile("^((13[0-9])|(18[0,1,2,3,4,5,6,7,8,9])|(17[0,1,2,3,4,5,6,7,8,9])|(15[^4,\\D]))\\d{8}$");
        Matcher m = p.matcher(number);
        flag = m.matches();
        return flag;
    }

    public static String dateFormatyyyymmddhhddss(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static boolean isServiceRunning(Context context, String ServicePackageName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServicePackageName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //ip addr transfer to int
    public static long ipToLong(String strIp) {
        String[]ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
    }
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }
}
