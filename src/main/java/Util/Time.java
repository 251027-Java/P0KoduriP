package Util;

public class Time {
    public static String FormatTime(int days, int hours, int minutes, int seconds){
        String info = "";
        if (days > 0) info += days + " Days, ";
        if (hours > 0) info += hours + " Hours, ";
        if (minutes > 0) info += minutes + " Minutes, ";
        if (seconds > 0) info += seconds + " Seconds, ";
        return info.substring(0, Math.max(0, info.length()-2));
    }
    public static String FormatTime(long seconds){
        return FormatTime(GetDays(seconds), GetHours(seconds), GetMinutes(seconds), GetSeconds(seconds));
    }

    public static int GetDays(long seconds){
        return (int) seconds / 86400;
    }
    public static int GetTotalHours(long seconds){
        return (int) seconds / 3600;
    }
    public static float GetTotalDecimalHours(long seconds){
        return seconds / 3600f;
    }
    public static int GetHours(long seconds){
        int s = (int) (seconds % 86400);
        return s/3600;
    }
    public static int GetMinutes(long seconds){
        int s = (int) (seconds % 86400);
        return s%3600/60;
    }
    public static int GetSeconds(long seconds){
        int s = (int) (seconds % 86400);
        return s%60;
    }
}
