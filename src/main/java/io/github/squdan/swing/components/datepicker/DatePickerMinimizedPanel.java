package io.github.squdan.swing.components.datepicker;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import io.github.squdan.swing.components.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * Datepicker representation using {@link JPanel}.
 * <p>
 * This implementation will show a non-editable text field with selected date and a button to open the calendar
 * to choose a new one.
 */
public class DatePickerMinimizedPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = -9022651370346477492L;

    // Components
    private final DatePicker datePicker;

    public DatePickerMinimizedPanel() {
        this(null, null);
    }

    public DatePickerMinimizedPanel(final JLabel title) {
        this(title, null);
    }

    public DatePickerMinimizedPanel(final Instant selectedDate) {
        this(null, selectedDate);
    }

    public DatePickerMinimizedPanel(final JLabel title, final Instant selectedDate) {
        super(new GridLayout(0, 1));

        // Instance datepicker
        final DatePickerSettings dateSettings = new DatePickerSettings(new Locale("es"));
        datePicker = new DatePicker(dateSettings);

        // Configure datepicker
        dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        dateSettings.setAllowKeyboardEditing(false);

        // Configure calendar size
        dateSettings.setSizeDatePanelMinimumHeight((int) (dateSettings.getSizeDatePanelMinimumHeight() * 1.6));
        dateSettings.setSizeDatePanelMinimumWidth((int) (dateSettings.getSizeDatePanelMinimumWidth() * 1.6));

        // Calendar image button
        final ImageIcon buttonImage = getButtonImage();

        if (Objects.nonNull(buttonImage)) {
            final JButton datePickerButton = datePicker.getComponentToggleCalendarButton();
            datePickerButton.setText(StringUtils.EMPTY);
            datePickerButton.setIcon(buttonImage);
        }

        // Set selected day into the calendar
        if (Objects.nonNull(selectedDate)) {
            final LocalDate selectedLocalDate = DateTimeUtils.InstantUtils.getLocalDate(selectedDate);
            datePicker.setDate(selectedLocalDate);
        } else {
            datePicker.setDateToToday();
        }

        // Configure panel with header if received
        if (Objects.nonNull(title)) {
            this.add(title);
        }

        this.add(datePicker);

    }

    public LocalDate getSelectedDate() {
        return datePicker.getDate();
    }

    private ImageIcon getButtonImage() {
        final URL dateImageURL = this.getClass().getResource("/images/datepicker_button.png");
        final Image dateExampleImage = Toolkit.getDefaultToolkit().getImage(dateImageURL);
        return new ImageIcon(dateExampleImage);
    }
}
