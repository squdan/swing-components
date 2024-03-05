package io.github.squdan.swing.components.panel.datepicker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatePickerConfiguration {

    /**
     * [Optional] Label to show as title.
     */
    private JLabel title;

    /**
     * [Optional] Default selected date, if no received LocalDate.now() will be used.
     */
    private Instant selectedDate;

}
