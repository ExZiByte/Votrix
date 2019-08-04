package votrix.Discord.utils;

import java.util.concurrent.TimeUnit;

public class Time {

    public static TimeUnit getTime(String args){

        if(args.toLowerCase().endsWith("s")){
            return TimeUnit.SECONDS;
        } else if(args.toLowerCase().endsWith("m")){
            return TimeUnit.MINUTES;
        } else if(args.toLowerCase().endsWith("h")){
            return TimeUnit.HOURS;
        } else if(args.toLowerCase().endsWith("d")){
            return TimeUnit.DAYS;
        }
        return TimeUnit.SECONDS;
    }
}
