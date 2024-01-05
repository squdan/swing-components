package io.github.squdan.swing.components.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    // Constants
    public static final ZoneId EUROPE_MADRID_ZONE_ID = ZoneId.of("Europe/Berlin");

    // Formatter
    public final static String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER_DEFAULT = DateTimeFormatter
            .ofPattern(DATE_TIME_FORMAT_DEFAULT);

    // Dependencies
    public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StringDateUtils {

        // Supported date formats
        public final static String DATE_TIME_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SS";
        public final static String DATE_TIME_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss.S";
        public final static String DATE_TIME_FORMAT_3 = "yyyy-MM-dd'T'HH:mm:ss";
        protected final static List<String> EXTRA_SUPORTED_FORMATS = List.of(DATE_TIME_FORMAT_1, DATE_TIME_FORMAT_2,
                DATE_TIME_FORMAT_3);

        // Formatters
        protected final static List<DateTimeFormatter> FORMATTERS = new ArrayList<>();

        static {
            // Adds default formatter
            FORMATTERS.add(LOCAL_DATE_TIME_FORMATTER_DEFAULT);

            // Adds extra formatters
            if (!CollectionUtils.isEmpty(EXTRA_SUPORTED_FORMATS)) {
                for (String format : EXTRA_SUPORTED_FORMATS) {
                    FORMATTERS.add(DateTimeFormatter.ofPattern(format));
                }
            }
        }

        /**
         * Returns received String as Date, must follow the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null or empty String is received then this method will return null.
         *
         * @param date (java.lang.String) to parse to Date.
         * @return (java.util.Date) Date.
         */
        public static Date toDate(final String date) {
            Date result = null;

            Instant instant = toInstantUtc(date);

            if (Objects.nonNull(instant)) {
                result = Date.from(instant);
            }

            return result;
        }

        /**
         * Returns received String as Instant, must follow the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null or empty String is received then this method will return null.
         *
         * @param date (java.lang.String) to parse to Date.
         * @return (java.time.Instant) Instant.
         */
        public static Instant toInstantUtc(final String date) {
            Instant result = null;

            if (StringUtils.isNotBlank(date)) {
                LocalDateTime localDateTime = toLocalDateTime(date);

                if (Objects.nonNull(localDateTime)) {
                    result = localDateTime.toInstant(ZoneOffset.UTC);
                }
            }

            return result;
        }

        /**
         * Returns received String as LocalDateTime, must follow the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null or empty String is received then this method will return null.
         *
         * @param date (java.lang.String) to parse to Date.
         * @return (java.time.LocalDateTime) Date.
         */
        public static LocalDateTime toLocalDateTime(final String date) {
            LocalDateTime result = null;

            if (StringUtils.isNotBlank(date)) {
                for (DateTimeFormatter formatter : FORMATTERS) {
                    result = toLocalDateTime(formatter, date, result);
                }
            }

            return result;
        }

        private static LocalDateTime toLocalDateTime(final DateTimeFormatter formatter, final String date,
                                                     final LocalDateTime previousResult) {
            LocalDateTime result = null;

            if (Objects.isNull(previousResult)) {
                try {
                    result = LocalDateTime.parse(date, formatter);
                } catch (DateTimeParseException e) {
                    // Do nothing
                }
            } else {
                result = previousResult;
            }

            return result;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TimeUtils {
        // Configuration
        private static final String DATE_SEPARATOR = "/";
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
    public static class DateUtils {
        /**
         * Returns received Date as String with the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null is received then this method will return null.
         *
         * @param date (java.util.Date) to parse to String.
         * @return (String) Date.
         */
        public static String toString(final Date date) {
            String result = null;

            LocalDateTime localDateTime = toLocalDateTimeUtc(date);
            result = LocalDateTimeUtils.toString(localDateTime);

            return result;
        }

        /**
         * Returns received Date with time to 00:00:00.000
         * <p>
         * If null is received then this method will return null.
         *
         * @param date (java.util.Date) without time.
         * @return (Date) Date.
         */
        public static Date dateWithoutTime(final Date date) {

            Date result = null;

            if (Objects.nonNull(date)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                result = calendar.getTime();
            }

            return result;
        }

        /**
         * Returns received Date as LocalDateTime
         * <p>
         * If null is received then this method will return null.
         *
         * @param date (java.util.Date) to parse to LocalDateTime.
         * @return (java.time.LocalDateTime) Instant.
         */
        public static LocalDateTime toLocalDateTimeUtc(final Date date) {
            LocalDateTime result = null;

            if (Objects.nonNull(date)) {
                result = date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
            }

            return result;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LocalDateUtils {

        /**
         * Returns received LocalDate as UTC Instant.
         *
         * @param source: received LocalDate to parse to instant.
         * @return Instant generated from LocalDate in UTC.
         */
        public static Instant toInstantUtc(final LocalDate source) {
            return source.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }

        /**
         * Returns received LocalDate as String with the next format
         * DateTimeUtils.DATE_FORMAT
         * <p>
         * If null is received then this method will return null.
         *
         * @param date   (java.util.LocalDate) to parse to String.
         * @param zoneId (java.time.ZoneId) to select desired zone, if null UTC will be
         *               used.
         * @return (String) Date.
         */
        public static String toString(final LocalDate date, final ZoneId zoneId) {
            String result = null;

            if (Objects.nonNull(date)) {
                Instant instant = LocalDateUtils.toInstantUtc(date);
                result = InstantUtils.toString(instant, zoneId);
            }

            return result;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LocalDateTimeUtils {
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
                } catch (DateTimeException e) {
                    // Do nothing
                }
            }

            return result;
        }

        /**
         * Returns received LocalDateTime as UTC Date.
         * <p>
         * If null is received then this method will return null.
         *
         * @param date (java.util.LocalDateTime) to parse to Date.
         * @return (Date) UTC Date.
         */
        public static Date toDate(final LocalDateTime date) {
            Date result = null;

            if (Objects.nonNull(date)) {
                try {
                    result = Date.from(date.toInstant(ZoneOffset.UTC));
                } catch (DateTimeException e) {
                    // Do nothing
                }
            }

            return result;
        }

        /**
         * Returns received Timestamp as LocalDateTime
         *
         * @param timestamp (long) to parse to LocalDateTime.
         * @return (java.time.LocalDateTime) Date.
         */
        public static LocalDateTime fromTimestampSeconds(final long timestamp) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class InstantUtils {
        /**
         * Parses UTC instant to Madrid zone instant.
         *
         * @param source: received instant date.
         * @return returned instant date.
         */
        public static Instant changeZoneIdToMadrid(final Instant source) {
            return getZonedDateTimeAtMadridZone(source).toInstant();
        }

        /**
         * Parses UTC instant to Madrid zone zoned date time.
         *
         * @param source: received instant date.
         * @return returned zoned date time.
         */
        public static ZonedDateTime getZonedDateTimeAtMadridZone(final Instant source) {
            return source.atZone(EUROPE_MADRID_ZONE_ID);
        }

        /**
         * Parses Instant to LocalDate with ZoneId UTC
         *
         * @param source: received instant to parse.
         * @return returned local date.
         */
        public static LocalDate getLocalDate(final Instant source) {
            return getLocalDate(source, ZoneOffset.UTC);
        }

        /**
         * Parses Instant to LocalDate with specified ZoneId
         *
         * @param source: received instant to parse.
         * @param zoneId: selected ZoneId.
         * @return returned local date time.
         */
        public static LocalDate getLocalDate(final Instant source, final ZoneId zoneId) {
            return LocalDate.ofInstant(source, zoneId);
        }

        /**
         * Parses Instant to LocalDateTime with ZoneId UTC;
         *
         * @param source: received instant to parse.
         * @return returned local date.
         */
        public static LocalDateTime getLocalDateTime(final Instant source) {
            return getLocalDateTime(source, ZoneOffset.UTC);
        }

        /**
         * Parses Instant to LocalDateTime with specified ZoneId;
         *
         * @param source: received instant to parse.
         * @param zoneId: selected ZoneId.
         * @return returned local date time.
         */
        public static LocalDateTime getLocalDateTime(final Instant source, final ZoneId zoneId) {
            return LocalDateTime.ofInstant(source, zoneId);
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
                    localDateTime = getLocalDateTime(date, zoneId);
                } else {
                    localDateTime = getLocalDateTime(date);
                }

                result = LocalDateTimeUtils.toString(localDateTime);
            }

            return result;
        }
    }
}
