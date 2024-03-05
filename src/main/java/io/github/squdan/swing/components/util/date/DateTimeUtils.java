package io.github.squdan.swing.components.util.date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    // Constants
    public static final ZoneId EUROPE_MADRID_ZONE_ID = ZoneId.of("Europe/Madrid");

    // Formatter
    public final static DateTimeFormatter LOCAL_DATE_FORMATTER_DEFAULT = DateTimeFormatter.ISO_DATE;
    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER_DEFAULT = DateTimeFormatter.ISO_DATE_TIME;

    public static void setTimezone(final ZoneId zoneId) {
        if (Objects.isNull(zoneId)) {
            log.info("Configurando timezone 'UTC'.");
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        } else {
            log.info("Configurando timezone '{}'.", zoneId);
            TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
        }
    }

    public static void setTimezone(final TimeZone timezone) {
        if (Objects.isNull(timezone)) {
            log.info("Configurando timezone 'UTC'.");
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        } else {
            log.info("Configurando timezone '{}'.", timezone);
            TimeZone.setDefault(timezone);
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringSourceMethods {

        /**
         * Returns received String as Instant, must follow the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null or empty String is received then this method will return null.
         *
         * @param date (java.lang.String) to parse to Date.
         * @return (java.time.Instant) Instant.
         */
        public static Instant toInstant(final String date) {
            Instant result = null;

            if (StringUtils.isNotBlank(date)) {
                LocalDateTime localDateTime = toLocalDateTime(date);

                if (Objects.nonNull(localDateTime)) {
                    result = localDateTime.atZone(TimeZone.getDefault().toZoneId()).toInstant();
                }
            }

            return result;
        }

        /**
         * Returns received String as LocalDate, must follow the next format
         * DateTimeUtils.LOCAL_DATE_FORMATTER_DEFAULT
         * <p>
         * If null or empty String is received then this method will return null.
         *
         * @param date (java.lang.String) to parse to Date.
         * @return (java.time.LocalDate) Date.
         */
        public static LocalDate toLocalDate(final String date) {
            LocalDate result = null;

            if (StringUtils.isNotBlank(date)) {
                result = LocalDate.parse(date, LOCAL_DATE_FORMATTER_DEFAULT);
            }

            return result;
        }

        /**
         * Returns received String as LocalDateTime, must follow the next format
         * DateTimeUtils.LOCAL_DATE_TIME_FORMATTER_DEFAULT
         * <p>
         * If null or empty String is received then this method will return null.
         *
         * @param date (java.lang.String) to parse to Date.
         * @return (java.time.LocalDateTime) Date.
         */
        public static LocalDateTime toLocalDateTime(final String date) {
            LocalDateTime result = null;

            if (StringUtils.isNotBlank(date)) {
                result = LocalDateTime.parse(date, LOCAL_DATE_TIME_FORMATTER_DEFAULT);
            }

            return result;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TimeSourceMethods {
        // Configuration
        private static final String TIME_SEPARATOR = ":";
        private static final String TIME_CERO = "0";
        private static final String TIME_CERO_DOUBLE = "00";

        public static String getTimeFormatted(final int hour, final int minutes) {
            final String hourFormatted = (hour < 10) ? (TIME_CERO + hour) : String.valueOf(hour);
            final String minutesFormatted = (minutes == 0) ? TIME_CERO_DOUBLE : String.valueOf(minutes);
            return hourFormatted + TIME_SEPARATOR + minutesFormatted;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LocalDateSourceMethods {

        /**
         * Returns received LocalDate as UTC Instant.
         *
         * @param source: received LocalDate to parse to instant.
         * @return Instant generated from LocalDate in UTC.
         */
        public static Instant toInstant(final LocalDate source) {
            return source.atStartOfDay(TimeZone.getDefault().toZoneId()).toInstant();
        }

        /**
         * Returns received LocalDate as String with the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null is received then this method will return null.
         *
         * @param date (java.util.LocalDate) to parse to String.
         * @return (String) Date.
         */
        public static String toString(final LocalDate date) {
            String result = null;

            if (Objects.nonNull(date)) {
                try {
                    result = date.format(LOCAL_DATE_FORMATTER_DEFAULT);
                } catch (final DateTimeException e) {
                    log.warn("Error formateando fecha como String '{}'. Error: ", date, e);
                }
            }

            return result;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LocalDateTimeSourceMethods {
        /**
         * Returns received LocalDateTime as String with the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null is received then this method will return null.
         *
         * @param date (java.util.LocalDateTime) to parse to String.
         * @return (String) Date.
         */
        public static String toString(final LocalDateTime date) {
            String result = null;

            if (Objects.nonNull(date)) {
                try {
                    result = date.format(LOCAL_DATE_TIME_FORMATTER_DEFAULT);
                } catch (final DateTimeException e) {
                    log.warn("Error formateando fecha como String '{}'. Error: ", date, e);
                }
            }

            return result;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class InstantSourceMethods {

        /**
         * Return {@link DayTime} from received instant.
         *
         * @param date to obtain time from.
         * @return {@link DayTime} from received instant.
         */
        public static DayTime toDayTime(final Instant date, final ZoneId zone) {
            final ZonedDateTime dateZoned = date.atZone(zone);
            return DayTime.builder().hour(dateZoned.getHour()).minutes(dateZoned.getMinute()).build();
        }

        /**
         * Returns received Instant as String with the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null is received then this method will return null.
         *
         * @param date   (java.util.Instant) to parse to String.
         * @param zoneId (java.time.ZoneId) to select desired zone, if null UTC will be
         *               used.
         * @return (String) Date.
         */
        public static String toString(final Instant date, final ZoneId zoneId) {
            String result = null;

            if (Objects.nonNull(date)) {
                LocalDateTime localDateTime;

                if (Objects.nonNull(zoneId)) {
                    localDateTime = LocalDateTime.ofInstant(date, zoneId);
                } else {
                    localDateTime = LocalDateTime.ofInstant(date, TimeZone.getDefault().toZoneId());
                }

                result = LocalDateTimeSourceMethods.toString(localDateTime);
            }

            return result;
        }
    }
}
