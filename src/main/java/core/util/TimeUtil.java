package core.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneOffset zoneOffSet = ZoneOffset.of("+02:00");

    private TimeUtil() {
    }

    public static String formatTime(long timeInSeconds) {
        return LocalDateTime.ofEpochSecond(timeInSeconds, 0, zoneOffSet).format(formatter);
    }
}
