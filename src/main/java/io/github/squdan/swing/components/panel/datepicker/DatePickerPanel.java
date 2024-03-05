package io.github.squdan.swing.components.panel.datepicker;

import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Datepicker representation using {@link JPanel}.
 * <p>
 * This implementation will show a calendar where user can select a day which will be returned with method getSelectedDate().
 */
public class DatePickerPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -4745317742413051176L;

    // Data
    private final DatePickerConfiguration configuration;

    // Components
    private final CalendarPanel calendarPanel;

    public DatePickerPanel(final DatePickerConfiguration configuration) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.configuration = configuration;

        // Instance calendar
        final DatePickerSettings dateSettings = new DatePickerSettings(new Locale("es"));
        calendarPanel = new CalendarPanel(dateSettings);

        // Configure datepicker
        dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);

        // Configure calendar size
        dateSettings.setSizeDatePanelMinimumHeight((int) (dateSettings.getSizeDatePanelMinimumHeight() * 1.3));
        dateSettings.setSizeDatePanelMinimumWidth((int) (dateSettings.getSizeDatePanelMinimumWidth() * 1.3));

        if (Objects.nonNull(configuration.getSelectedDate())) {
            final LocalDate selectedLocalDate = LocalDate.ofInstant(configuration.getSelectedDate(), TimeZone.getDefault().toZoneId());
            calendarPanel.setSelectedDate(selectedLocalDate);
        } else {
            calendarPanel.setSelectedDate(LocalDate.now(TimeZone.getDefault().toZoneId()));
        }

        // Configure panel with header if received
        if (Objects.nonNull(configuration.getTitle())) {
            final JPanel titlePanel = new JPanel(new GridLayout(0, 1));
            titlePanel.add(configuration.getTitle());
            this.add(titlePanel);
        }

        this.add(calendarPanel);

    }

    public LocalDate getSelectedDate() {
        return calendarPanel.getSelectedDate();
    }
}
