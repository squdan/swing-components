package io.github.squdan.swing.components.panel.calendar;

import io.github.squdan.swing.components.panel.calendar.cell.CalendarDayTime;
import io.github.squdan.swing.components.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Calendar utilities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CalendarUtils {

    // Configuration
    private static final String DATE_SEPARATOR = "/";

    /**
     * Return String hour:minutes from received parameters.
     *
     * @param hour    to add to String representation.
     * @param minutes to add to String representation.
     * @return String hour:minutes
     */
    public static String getTimeFormatted(final int hour, final int minutes) {
        return DateTimeUtils.TimeUtils.getTimeFormatted(hour, minutes);
    }

    /**
     * Return {@link CalendarDayTime} from received instant.
     *
     * @param date to obtain time from.
     * @return {@link CalendarDayTime} from received instant.
     */
    public static CalendarDayTime getCalendarDayTimeFrom(final Instant date) {
        final ZonedDateTime dateZoned = DateTimeUtils.InstantUtils.getZonedDateTimeAtMadridZone(date);
        return CalendarDayTime.builder().hour(dateZoned.getHour()).minutes(dateZoned.getMinute()).build();
    }

    /**
     * Return String day/month/year from received parameters.
     *
     * @param year  to add to String representation.
     * @param month to add to String representation.
     * @param day   to add to String representation.
     * @return String day/month/year.
     */
    public static String getDateFormatted(final int year, final int month, final int day) {
        return day + DATE_SEPARATOR + month + DATE_SEPARATOR + year;
    }

    /**
     * Parses received date information to Instant.
     *
     * @param year  to use in the conversion.
     * @param month to use in the conversion.
     * @param day   to use in the conversion.
     * @return Instant from received information.
     */
    public static Instant getInstantUtcFrom(final int year, final int month, final int day) {
        return LocalDateTime.of(year, month, day, 1, 0).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * Parses received date information to Instant.
     *
     * @param year    to use in the conversion.
     * @param month   to use in the conversion.
     * @param day     to use in the conversion.
     * @param hour    to use in the conversion.
     * @param minutes to use in the conversion.
     * @return Instant from received information.
     */
    public static Instant getInstantUtcFrom(final int year, final int month, final int day, final int hour,
                                            final int minutes) {
        return LocalDateTime.of(year, month, day, hour, minutes).atZone(ZoneId.systemDefault()).toInstant();
    }

}
