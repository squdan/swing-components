package io.github.squdan.swing.components.datepicker;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;
import io.github.squdan.swing.components.util.DateTimeUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * Datepicker representation using {@link JPanel}.
 * <p>
 * This implementation will show a calendar where user can select a day which will be returned with method getSelectedDate().
 */
public class DatePickerPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -4745317742413051176L;

    // Components
    private final CalendarPanel calendarPanel;

    public DatePickerPanel() {
        this(null, null);
    }

    public DatePickerPanel(final JLabel title) {
        this(title, null);
    }

    public DatePickerPanel(final Instant selectedDate) {
        this(null, selectedDate);
    }

    public DatePickerPanel(final JLabel title, final Instant selectedDate) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Instance calendar
        final DatePickerSettings dateSettings = new DatePickerSettings(new Locale("es"));
        calendarPanel = new CalendarPanel(dateSettings);

        // Configure datepicker
        dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);

        // Configure calendar size
        dateSettings.setSizeDatePanelMinimumHeight((int) (dateSettings.getSizeDatePanelMinimumHeight() * 1.3));
        dateSettings.setSizeDatePanelMinimumWidth((int) (dateSettings.getSizeDatePanelMinimumWidth() * 1.3));

        if (Objects.nonNull(selectedDate)) {
            final LocalDate selectedLocalDate = DateTimeUtils.InstantUtils.getLocalDate(selectedDate);
            calendarPanel.setSelectedDate(selectedLocalDate);
        } else {
            calendarPanel.setSelectedDate(LocalDate.now());
        }

        // Configure panel with header if received
        if (Objects.nonNull(title)) {
            final JPanel titlePanel = new JPanel(new GridLayout(0, 1));
            titlePanel.add(title);
            this.add(titlePanel);
        } else {
        }

        this.add(calendarPanel);

    }

    public LocalDate getSelectedDate() {
        return calendarPanel.getSelectedDate();
    }
}
