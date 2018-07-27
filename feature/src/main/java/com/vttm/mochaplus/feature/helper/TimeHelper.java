package com.vttm.mochaplus.feature.helper;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeFormatException;

import com.vttm.mochaplus.feature.R;

import org.jivesoftware.smack.packet.ReengMusicPacket;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by thaodv on 7/1/2014.
 */
public class TimeHelper {
    private static final String TAG = TimeHelper.class.getSimpleName();
    //      ngay sinh de mac dinh la ko co j
    public static final long BIRTHDAY_DEFAULT = -1L;
    public static final long BIRTHDAY_DEFAULT_PICKER = 631152000000L;
    public static final long INTERVAL = -30000L;              //-180000L
    //    public static final long INTERVAL_MIN = 300000L;
    public static final long INTERVAL_MIN = 120000L;
    private static final long INTERVAL_MAX = 1800000L;
    private static final long TIME_OUT_LASTON = 5 * 60 * 1000;//(5 phut)
    public static final long ONE_DAY = 86400000L;// nhanh 1 ngay
    private static final long ONE_DAY_ = -86400000L;// cham 1 ngay
    public static final int ONE_HOUR_IN_MILISECOND = 1 * 60 * 60 * 1000;
    public static final long FIVE_MIN_IN_MILISECOND = 1 * 5 * 60 * 1000;
    private static final int TIME_OUT_STRANGER_MUSIC = 10 * 60 * 1000;           // 10 phut
    private static final long TIME_OUT_ACCEPT_STRANGER_MUSIC = 1 * 60 * 1000;    // 1 phuts

    private static final long ONE_MINUTE = 60000;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long SEVEN_DAY = ONE_DAY * 7;
    public static final long ONE_MONTH = ONE_DAY * 30;

//    private static final SimpleDateFormat spfTime = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
//    private static final SimpleDateFormat spfTimeSection = new SimpleDateFormat("HH:mm, dd/MM/yyyy");

    private static final String SDF_IN_DAY = "HH:mm";
    private static final String SDF_IN_YEAR = "dd/MM";
    private static final String SDF_OTH_YEAR = "dd/MM/yyyy";
    private static final String SDF_DURATION = "mm:ss";
    private static final String SDF_EVENT_MSG_OTHER_DAY = "HH:mm, dd/MM/yyyy";
    private static final String SDF_TRANSFER_MONEY_DAY = "HH:mm - dd/MM/yyyy";
    private static final String SDF_BIRTHDAY_STRING = "yyyy-MM-dd";
    private static final String SDF_BIRTHDAY_FACEBOOK = "MM/dd/yyyy";
    private static final String SDF_FULL = "dd/MM/yyyy HH:mm";
    private static final String SDF_KQI = "dd/MM/yyyy HH:mm:ss";
    private static final String SDF_TIME_LOCATION = "yyyy-MM-dd HH:mm:ss";


    public static String getDateOfMessage(long time) {
        SimpleDateFormat othYear = new SimpleDateFormat(SDF_OTH_YEAR);
        return othYear.format(time);
    }

    public static String getHourOfMessage(long time) {
        SimpleDateFormat inDay = new SimpleDateFormat(SDF_IN_DAY);
        return inDay.format(time);
    }

//    public static String formatTimeLocked(long time, Resources res) {
//        int timeLock = (int) (time / 1000);
//        int hourLock = timeLock / 3600;
//        int minuteLock = (timeLock / 60) - (hourLock * 60);
//        int secondLock = timeLock - (hourLock * 3600) - (minuteLock * 60);
//        if (hourLock == 0) {
//            if (minuteLock == 0) {
//                if (secondLock == 0)
//                    secondLock = 1;
//                return res.getString(R.string.login_locked) + " " + secondLock + " s";
//            } else {
//                return res.getString(R.string.login_locked) + " " + minuteLock + ":" + secondLock + " s";
//            }
//        } else {
//            return res.getString(R.string.login_locked) + " " + hourLock + ":" + minuteLock + " m";
//        }
//    }
//
//    public static String formatSeparatorTimeOfMessage(long time, Resources res) {
//        Calendar calCurrent = Calendar.getInstance();
//        Calendar calMessage = Calendar.getInstance();
//        calMessage.setTimeInMillis(time);
//        int currentDay = calCurrent.get(Calendar.DAY_OF_YEAR);
//        int messageDay = calMessage.get(Calendar.DAY_OF_YEAR);
//        if (currentDay == messageDay) {
//            return res.getString(R.string.today) + " " + formatTimeInDay(time);
//        } else if ((currentDay - messageDay) == 1) {
//            return res.getString(R.string.yesterday) + " " + formatTimeInDay(time);
//        } else {
//            return formatTimeSection(time);
//        }
//    }

//    public static String formatCommonTime(long mTime, long currentTime, Resources res) {
//        SimpleDateFormat inDay = new SimpleDateFormat(SDF_IN_DAY);
//        SimpleDateFormat inYear = new SimpleDateFormat(SDF_IN_YEAR);
//        SimpleDateFormat othYear = new SimpleDateFormat(SDF_OTH_YEAR);
//        Calendar currentCal = Calendar.getInstance();
//        currentCal.setTimeInMillis(currentTime);
//        Calendar lastSeenCal = Calendar.getInstance();
//        lastSeenCal.setTimeInMillis(mTime);
//        int currentDay = currentCal.get(Calendar.DAY_OF_YEAR);
//        int lastSeenDay = lastSeenCal.get(Calendar.DAY_OF_YEAR);
//        int currentMonth = currentCal.get(Calendar.MONTH);
//        int lastSeenMonth = lastSeenCal.get(Calendar.MONTH);
//        int currentYear = currentCal.get(Calendar.YEAR);
//        int lastSeenYear = lastSeenCal.get(Calendar.YEAR);
//
//        if (lastSeenYear == currentYear) {
//            if (lastSeenMonth == currentMonth) {
//                if (lastSeenDay == currentDay) {
//                    return inDay.format(mTime);
//                } else {
//                    int dayOfYear = currentDay - lastSeenDay;
//                    if (dayOfYear == 1) {// Hom qua
//                        return res.getString(R.string.yesterday);
//                    } else {
//                        return inYear.format(mTime);
//                    }
//                }
//            } else {
//                return inYear.format(mTime);
//            }
//        } else {
//            return othYear.format(mTime);
//        }
//    }

    public static String formatTimeLogKQI(long mTime) {
        SimpleDateFormat spf = new SimpleDateFormat(SDF_KQI);
        return spf.format(mTime);
    }
    public static String formatTimeLocation(long mTime) {
        SimpleDateFormat spf = new SimpleDateFormat(SDF_TIME_LOCATION);
        return spf.format(mTime);
    }


    public static String formatTimeSection(long mTime) {
        SimpleDateFormat spf = new SimpleDateFormat(SDF_FULL);
        return spf.format(mTime);
    }

    public static String formatTimeInDay(long time) {
        SimpleDateFormat inDay = new SimpleDateFormat(SDF_IN_DAY);
        return inDay.format(time);
    }

//    public static String formatTimeLastSeen(long lastSeenTime, Resources res, long timeSeen) {
//        SimpleDateFormat inDay = new SimpleDateFormat(SDF_IN_DAY);
//        SimpleDateFormat inYear = new SimpleDateFormat(SDF_IN_YEAR);
//        SimpleDateFormat othYear = new SimpleDateFormat(SDF_OTH_YEAR);
//        Calendar calCurrent = Calendar.getInstance();
//        Calendar calLastSeen = Calendar.getInstance();
//        calLastSeen.setTimeInMillis(lastSeenTime);
//        if (timeSeen < INTERVAL) {
//            return othYear.format(lastSeenTime);
//        } else if (timeSeen <= INTERVAL_MIN) {
//            return res.getString(R.string.offline_one_minute);
//        } else if (timeSeen <= INTERVAL_MAX) {
//            int minuteSeen = (int) (timeSeen / 60000L);
//            if (minuteSeen == 1) {
//                return String.format(res.getString(R.string.offline_minute), String.valueOf(minuteSeen));
//            }
//            return String.format(res.getString(R.string.offline_minutes), String.valueOf(minuteSeen));
//        } else {
//            int currentDay = calCurrent.get(Calendar.DAY_OF_YEAR);
//            int lastSeenDay = calLastSeen.get(Calendar.DAY_OF_YEAR);
//            if (currentDay == lastSeenDay) {
//                return inDay.format(lastSeenTime);
//            } else if ((currentDay - lastSeenDay) == 1) {
//                return res.getString(R.string.yesterday).toLowerCase() + " " + inDay.format(lastSeenTime);
//            } else {
//                int currentYear = calCurrent.get(Calendar.YEAR);
//                int lastSeenYear = calLastSeen.get(Calendar.YEAR);
//                if (lastSeenYear == currentYear) {
//                    return inYear.format(lastSeenTime);
//                } else {
//                    return othYear.format(lastSeenTime);
//                }
//            }
//        }
//    }

    public static String formatTimeFakeLastSeen() {
        SimpleDateFormat inYear = new SimpleDateFormat(SDF_IN_YEAR);
        return inYear.format(System.currentTimeMillis());
    }

    public static String formatTimeBirthday(long time) {
        if (time == BIRTHDAY_DEFAULT)
            return "";
        SimpleDateFormat spfBirthday = new SimpleDateFormat(SDF_OTH_YEAR);
        return spfBirthday.format(new Date(time));
    }

    public static String convertBirthDayFacebookToString(String facebookBirthDay) {
        if (TextUtils.isEmpty(facebookBirthDay)) {
            return "";
        }
        SimpleDateFormat spfBirthdayFacebook = new SimpleDateFormat(SDF_BIRTHDAY_FACEBOOK);
        try {
            Date date = spfBirthdayFacebook.parse(facebookBirthDay);
            return formatTimeBirthday(date.getTime());
        } catch (ParseException e) {
            Log.e(TAG, "ParseException", e);
            return "";
        } catch (NumberFormatException e) {
            Log.e(TAG, "NumberFormatException", e);
            return "";
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException", e);
            return "";
        } catch (Exception ex) {
            Log.e(TAG, "Exception", ex);
            return "";
        }
    }

    public static long convertBirthdayToTime(String birthDay) {
        //        spfBirthday.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (TextUtils.isEmpty(birthDay))
            return BIRTHDAY_DEFAULT;
        SimpleDateFormat spfBirthday = new SimpleDateFormat(SDF_OTH_YEAR);
        try {
            Date date = spfBirthday.parse(birthDay);
            return date.getTime();
        } catch (ParseException e) {
            Log.e(TAG, "ParseException", e);
            return BIRTHDAY_DEFAULT;
        } catch (NumberFormatException e) {
            Log.e(TAG, "NumberFormatException", e);
            return BIRTHDAY_DEFAULT;
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException", e);
            return BIRTHDAY_DEFAULT;
        } catch (Exception ex) {
            Log.e(TAG, "Exception", ex);
            return BIRTHDAY_DEFAULT;
        }
    }

    public static String formatTimeEventMessage(long time) {
        SimpleDateFormat inDay = new SimpleDateFormat(SDF_IN_DAY);
        SimpleDateFormat eventMsgOtherDay = new SimpleDateFormat(SDF_EVENT_MSG_OTHER_DAY);
        Calendar currentCal = Calendar.getInstance();
        int currentDay = currentCal.get(Calendar.DAY_OF_YEAR);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTimeInMillis(time);
        int timeDay = timeCal.get(Calendar.DAY_OF_YEAR);
        if (currentDay == timeDay) {
            return inDay.format(time);
        } else {
            return eventMsgOtherDay.format(time);
        }
    }

    public static String formatTimeTransferMoney(long time) {
        SimpleDateFormat transferMoneyDay = new SimpleDateFormat(SDF_TRANSFER_MONEY_DAY);
        return transferMoneyDay.format(time);
    }

    /**
     * kiem tra thoi gian trong ngay khong
     *
     * @param time
     * @return
     */
    public static boolean checkTimeInDay(long time) {
        Calendar currentCal = Calendar.getInstance();
        int currentDay = currentCal.get(Calendar.DAY_OF_YEAR);
        //        currentCal.setTimeInMillis(System.currentTimeMillis());
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTimeInMillis(time);
        int timeDay = timeCal.get(Calendar.DAY_OF_YEAR);
        return currentDay == timeDay;
    }

    public static boolean checkSameDay(long time1, long time2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        Calendar call2 = Calendar.getInstance();
        call2.setTimeInMillis(time2);
        int day2 = call2.get(Calendar.DAY_OF_YEAR);
        return day1 == day2;
    }

    public static boolean checkTimeOutForDurationTime(long time, long durationTime) {
        long currentTime = getCurrentTime();
        if (currentTime >= time && currentTime - time >= durationTime) {            // nhanh
            return true;
        } else // cham
            return time >= currentTime && time - currentTime >= durationTime;
    }

    public static boolean checkTimeOutStrangerMusic(long time) {
        if (time < 0) {// gia tri mac dinh
            return false;
        }
        long currentTime = getCurrentTime();
        return currentTime > time && currentTime - time > TIME_OUT_STRANGER_MUSIC;
    }

    /**
     * kiem tra thoi gian hien tai da qua mot khoang thoi gian chua
     *
     * @param checkingTime
     * @return
     */
    public static boolean isPassedARangeTime(long checkingTime, long pastDuration) {
        long now = System.currentTimeMillis();
        long diffTime = (int) (now - checkingTime);
        return diffTime > pastDuration;
    }

    public static boolean isPassedARangeTime(long oldTime, long newTime, long pastDuration) {
        long diffTime = (newTime - oldTime);
        Log.i(TAG, "diffTime = " + diffTime + " oldTime = " + oldTime + " - pastDuration = " + pastDuration);
        return diffTime > pastDuration;
    }

    public static boolean checkTimeOutLastOn(long lastOn) {
        long currentTime = System.currentTimeMillis();
        return currentTime > lastOn && currentTime - lastOn > TIME_OUT_LASTON;
    }

    public static long getCurrentTime() {
        Calendar currentCal = Calendar.getInstance();
        return currentCal.getTimeInMillis();
    }

    public static long getSVDurationLastSeen(long currtentTimeSv) {
        // server test cc time !=0
        if (currtentTimeSv == 0 || currtentTimeSv == -1)
            return -1;
        Calendar currentCal = Calendar.getInstance();
        long duration = currentCal.getTimeInMillis() - currtentTimeSv;
        if (duration > ONE_DAY_ && duration < ONE_DAY) {
            return duration;
        } else {
            return 0;
        }
    }

    public static boolean checkTimeShowSuggestMusic(long lastTime, int timeOutMinute) {
        long currentTime = getCurrentTime();
        // tinh so phut tu lan cuoi cung nghe toi hien tai
        int temp = (int) (((currentTime - lastTime) / 1000) / 60);
        return temp > 0 && temp >= timeOutMinute;
    }

    public static String getDuraionMediaFile(long duration) {
        SimpleDateFormat spfDuration = new SimpleDateFormat(SDF_DURATION);
        String time;
        if (duration > 60 * 60) {
            int minute = (int) (duration / 60);
            int second = (int) (duration % 60);
            time = minute + ":" + second;
        } else {
            time = spfDuration.format(new Date(duration * 1000L));
        }
        return time;
    }
//
//    public static int getTimeOutInviteMusic(ReengMusicPacket reengMusicPacket) {
//        long timeSend = reengMusicPacket.getTimeSend();
//        long timeReceive = reengMusicPacket.getTimeReceive();
//        Log.d(TAG, "getTimeOutInviteMusic:-timeSend: " + timeSend + " ,timeReceive: " + timeReceive);
//        if (timeSend <= 0 || timeReceive <= 0) {
//            return CountDownInviteManager.COUNT_DOWN_DURATION;
//        } else {
//            long duration = timeReceive - timeSend;
//            if (duration <= 0) {
//                return CountDownInviteManager.COUNT_DOWN_DURATION;
//            } else if (duration > CountDownInviteManager.COUNT_DOWN_DURATION) {// qua thoi gian timeout
//                return 0;
//            } else {// chua qua thi lay 120s - khoang lech de set time out
//                return (int) (CountDownInviteManager.COUNT_DOWN_DURATION - duration);
//            }
//        }
//    }
//
//    public static long getTimeOutIncomingCall(long timeSend, long timeReceive) {
//        if (timeSend <= 0 || timeReceive <= 0) {
//            return CallConstant.CALL_TIME_OUT;
//        } else {
//            long duration = timeReceive - timeSend;
//            if (duration <= 0) {
//                return CallConstant.CALL_TIME_OUT;
//            } else if (duration > CallConstant.CALL_TIME_OUT) {// qua thoi gian timeout
//                return 0;
//            } else {// chua qua thi lay CALL_TIME_OUT - khoang lech de set time out
//                return (int) (CallConstant.CALL_TIME_OUT - duration);
//            }
//        }
//    }

    public static boolean checkTimeOutAcceptStrangerMusic(ReengMusicPacket reengMusicPacket) {
        long timeSend = reengMusicPacket.getTimeSend();
        long timeReceive = reengMusicPacket.getTimeReceive();
        long duration = timeReceive - timeSend;
        // qua thoi gian timeout
        // chua qua thi tra ve true
        return duration > TIME_OUT_ACCEPT_STRANGER_MUSIC;
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        if (currentDuration <= 0 || totalDuration <= 0) return 0;
        long percentage = (currentDuration * 100) / totalDuration;
        return (int) percentage;
    }

    public static int getOldFromBirthday(long birthday) {
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTimeInMillis(birthday);
        int timeYear = timeCal.get(Calendar.YEAR);
        timeCal.setTimeInMillis(System.currentTimeMillis());
        int yearNow = timeCal.get(Calendar.YEAR);
        int deltaYear = yearNow - timeYear;
        if (deltaYear <= 0) {
            return 0;
        } else {
            return deltaYear;
        }
    }

    public static int getYearOfBirth(long birthday) {
        try {
            Calendar timeCal = Calendar.getInstance();
            timeCal.setTimeInMillis(birthday);
            int timeYear = timeCal.get(Calendar.YEAR);
            return timeYear;
        } catch (TimeFormatException e) {
            Log.e(TAG, "TimeFormatException", e);
            return 0;
        }
    }

    /**
     * kieu time 1 phut truoc
     *
     * @param context
     * @param
     * @return
     */

//    public static String caculateTimeFeed(Context context, long timeStamp, long deltaTimeServer) {
//        SimpleDateFormat othYear = new SimpleDateFormat(SDF_OTH_YEAR);
//        SimpleDateFormat sdfDate = new SimpleDateFormat(SDF_IN_YEAR);
//        long timeFeedReCal = deltaTimeServer + timeStamp;
//        Calendar currentCal = Calendar.getInstance();
//        currentCal.setTimeInMillis(System.currentTimeMillis());
//        Calendar timeFeed = Calendar.getInstance();
//        timeFeed.setTimeInMillis(timeFeedReCal);
//        int currentYear = currentCal.get(Calendar.YEAR);
//        int timeFeedYear = timeFeed.get(Calendar.YEAR);
//        if (timeFeedYear == currentYear) {
//            long detal = System.currentTimeMillis() - timeFeedReCal;
//            if (detal > 0) {
//                if (detal < SEVEN_DAY) {
//                    if (detal > ONE_DAY) {
//                        int num = (int) (detal / ONE_DAY);
//                        if (num == 1) {
//                            return context.getString(R.string.yesterday);
//                        } else {
//                            //return num + " d";
//                            return num + " " + context.getString(R.string.feed_time_day);
//                        }
//                    } else if (detal > ONE_HOUR) {
//                        int num = (int) (detal / ONE_HOUR);
//                        //return num + " h";
//                        if (num == 1) { //TODO thay hour ago => h
//                            return num + " " + context.getString(R.string.feed_time_hour);
//                        } else {
//                            return num + " " + context.getString(R.string.feed_time_hours);
//                        }
//                    } else if (detal > ONE_MINUTE) {
//                        int num = (int) (detal / ONE_MINUTE);
//                        //return num + " m";
//                        if (num == 1) { //TODO thay minute ago => m
//                            return num + " " + context.getString(R.string.feed_time_minute);
//                        } else {
//                            return num + " " + context.getString(R.string.feed_time_minutes);
//                        }
//                    } else {
//                        return context.getString(R.string.onmedia_time_just_now);
//                    }
//                } else {
//                    return sdfDate.format(timeFeedReCal);
//                }
//            } else {
//                return context.getString(R.string.onmedia_time_just_now);
//            }
//        } else {
//            return othYear.format(timeFeedReCal);
//        }
//    }

    public static String formatTimeCall(Resources res, long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return String.format("%1$tH:%1$tM", calendar);
    }

//    public static String formatTimeCallDetail(Resources res, long time) {
//        Calendar calCurrent = Calendar.getInstance();
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(time);
//        int currentYear = calCurrent.get(Calendar.YEAR);
//        int calYear = cal.get(Calendar.YEAR);
//        int currentDay = calCurrent.get(Calendar.DAY_OF_YEAR);
//        int calDay = cal.get(Calendar.DAY_OF_YEAR);
//        if (currentYear == calYear && currentDay == calDay) {
//            return res.getString(R.string.today);
//        } else if (currentYear == calYear && (currentDay - calDay) == 1) {
//            return res.getString(R.string.yesterday);
//        } else {
//            return String.format("%1$td/%1$tm/%1$tY", cal);
//        }
//    }

    public static long convertBirthdayStringToLong(String birthdayString) {
        SimpleDateFormat spfBirthdayString = new SimpleDateFormat(SDF_BIRTHDAY_STRING);
        try {
            Date d = spfBirthdayString.parse(birthdayString);
            return d.getTime();
        } catch (ParseException e) {
            Log.e(TAG, "Exception", e);
        }
        return BIRTHDAY_DEFAULT;
    }

    public static String getStringBirthday(String birthdayString) {
        long timeMili = convertBirthdayStringToLong(birthdayString);
        return formatTimeBirthday(timeMili);
    }

    public static String formatTimeBirthdayString(long birthday) {
        if (birthday == BIRTHDAY_DEFAULT)
            return "";
        SimpleDateFormat spfBirthdayString = new SimpleDateFormat(SDF_BIRTHDAY_STRING);
        return spfBirthdayString.format(new Date(birthday));
    }

//    public static String msToClockTime(long milliseconds) {
//        int seconds = (int) (milliseconds / Constants.ONMEDIA.ONE_SECOND);
//        int minutes = seconds / 60;
//        seconds %= 60;
//        StringBuilder sb = new StringBuilder();
//        sb.append(twoDigit(minutes)).append(':');
//        sb.append(twoDigit(seconds));
//        return sb.toString();
//    }

    private static String twoDigit(int d) {
        NumberFormat formatter = new DecimalFormat("#00");
        return formatter.format(d);
    }
}