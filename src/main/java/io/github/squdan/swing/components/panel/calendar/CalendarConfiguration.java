package io.github.squdan.swing.components.panel.calendar;

import io.github.squdan.swing.components.panel.calendar.action.CalendarDayActions;
import io.github.squdan.swing.components.panel.calendar.cell.renderer.CalendarDayCellRenderer;
import lombok.*;

import javax.swing.*;
import java.time.ZoneId;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CalendarConfiguration {

    /**
     * [Required] Current JFrame used in the application.
     * <p>
     * Necessary to calculate calendar size.
     */
    private final JFrame sourceFrame;

    /**
     * [Required] How days content should be represented.
     * <p>
     * Examples:
     * - {@link io.github.squdan.swing.components.panel.calendar.cell.renderer.CalendarDayJLabelCellRenderer}
     * - {@link io.github.squdan.swing.components.panel.calendar.cell.renderer.CalendarDayJListCellRenderer}
     */
    private final CalendarDayCellRenderer cellRenderer;

    /**
     * [Required] Service {@link CalendarDataProviderService} implementation to obtain calendar days content information.
     */
    private final CalendarDataProviderService service;

    /**
     * [Optional] Allowed actions to execute over the days.
     */
    private CalendarDayActions actions;

    /**
     * [Required] ZoneId used to print dates and times.
     */
    private final ZoneId zoneId;

}
