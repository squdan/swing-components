package io.github.squdan.swing.components.calendar;

import io.github.squdan.swing.components.calendar.cell.CalendarDayTime;
import io.github.squdan.swing.components.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CalendarUtils {

    // Configuration
    private static final String DATE_SEPARATOR = "/";

    public static String getTimeFormatted(final int hour, final int minutes) {
        return DateTimeUtils.TimeUtils.getTimeFormatted(hour, minutes);
    }

    public static CalendarDayTime getCalendarDayTimeFrom(final Instant date) {
        final ZonedDateTime dateZoned = DateTimeUtils.InstantUtils.getZonedDateTimeAtMadridZone(date);
        return CalendarDayTime.builder().hour(dateZoned.getHour()).minutes(dateZoned.getMinute()).build();
    }

    public static String getDateFormatted(final int year, final int month, final int day) {
        return day + DATE_SEPARATOR + month + DATE_SEPARATOR + year;
    }

    public static Instant getInstantUtcFrom(final int year, final int month, final int day) {
        return LocalDateTime.of(year, month, day, 1, 0).atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Instant getInstantUtcFrom(final int year, final int month, final int day, final int hour,
                                            final int minutes) {
        return LocalDateTime.of(year, month, day, hour, minutes).atZone(ZoneId.systemDefault()).toInstant();
    }

}
