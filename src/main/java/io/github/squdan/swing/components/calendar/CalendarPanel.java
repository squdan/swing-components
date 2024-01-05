package io.github.squdan.swing.components.calendar;

import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;
import io.github.squdan.swing.components.calendar.cell.renderer.CalendarDayCellRenderer;
import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.util.ViewUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Slf4j
public class CalendarPanel extends JPanel {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = 7616964167280354806L;

    // Configuration
    private final int MAX_YEARS_ALLOWED = 20;
    private final String[] DAYS_OF_THE_WEEK = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado",
            "Domingo"};
    private final String[] MONTHS = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    // Components: Labels
    private final JLabel monthLabel = getCalendarTitle("mes");

    // Components: Control buttons
    private final JButton prevButton = new JButton("Anterior");
    private final JButton nextButton = new JButton("Siguiente");
    private final JComboBox<String> changeYearButton = new JComboBox<String>();

    // Panel organization
    private final DefaultTableModel calendarTableModel = new DefaultTableModel(DAYS_OF_THE_WEEK, 6) {
        /** Generated Serial Version UID */
        @Serial
        private static final long serialVersionUID = 938979105889422729L;

        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };
    private final JTable calendarTable = new JTable(calendarTableModel);
    private final JScrollPane calendarTableScrollPane = new JScrollPane(calendarTable);

    // APP state
    private final JFrame window;
    private final CalendarDayCellRenderer cellRenderer;
    private final CalendarDataProviderService service;
    private final CalendarDayActions actions;

    // Panel state
    private int currentYear;
    private int currentMonth;
    private int selectedYear;
    private int selectedMonth;
    private int selectedRow;
    private int selectedColumn;

    public CalendarPanel(final JFrame window, final CalendarDayCellRenderer cellRenderer,
                         final CalendarDataProviderService service, final CalendarDayActions actions) {
        // Panel initialization
        super(new GridLayout(1, 1));

        this.window = window;
        this.cellRenderer = cellRenderer;
        this.service = service;
        this.actions = actions;

        // Initialize calendar state
        setCurrentDate();
        setYearSelector();
        configureCalendarTable(window, cellRenderer);

        // Draw components
        this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(calendarTableScrollPane, prevButton, monthLabel,
                changeYearButton, nextButton));

        // Register received actions - action listeners
        Stream.of(actions.getAvailableActions().getComponents()).map(c -> (JMenuItem) c)
                .forEach(c -> c.addActionListener(new OpenPopupDayActionListener(this)));

        // Draw calendar
        refresh();

        // Register calendar - action listeners
        prevButton.addActionListener(new PrevButtonActionListener());
        nextButton.addActionListener(new NextButtonActionListener());
        changeYearButton.addActionListener(new ChangeYearActionListener());
    }

    public void refresh() {
        // Update row height by windows size
        calendarTable.setRowHeight((int) Math.round(window.getWidth() / 17));

        // If user tries to go further than year limits, block the buttons
        if (selectedMonth == 0 && selectedYear <= currentYear - MAX_YEARS_ALLOWED) {
            this.prevButton.setEnabled(false);
        } else {
            this.prevButton.setEnabled(true);
        }

        if (selectedMonth == 11 && selectedYear >= currentYear + MAX_YEARS_ALLOWED) {
            this.nextButton.setEnabled(false);
        } else {
            this.nextButton.setEnabled(true);
        }

        // Update selected month/year
        monthLabel.setText(MONTHS[selectedMonth]);
        changeYearButton.setSelectedItem(String.valueOf(selectedYear));
        cellRenderer.updateSelectedDate(selectedMonth, selectedYear);

        // Add days from selected month/year into the table
        fillDaysIntoCalendar();
    }

    private JLabel getCalendarTitle(final String text) {
        final JLabel result = new JLabel(text, SwingConstants.CENTER);
        result.setForeground(SwingComponents.getConfiguration().getColorConfiguration().getPrimaryText());
        result.setFont(SwingComponents.getConfiguration().getTextConfiguration().getTitleFont());
        return result;
    }

    private void setCurrentDate() {
        // Set current date
        final GregorianCalendar calendarFromToday = new GregorianCalendar();
        this.currentMonth = calendarFromToday.get(GregorianCalendar.MONTH);
        this.currentYear = calendarFromToday.get(GregorianCalendar.YEAR);

        // Select current date by default to show in the calendar
        this.selectedMonth = currentMonth;
        this.selectedYear = currentYear;
    }

    private void setYearSelector() {
        for (int i = currentYear - MAX_YEARS_ALLOWED; i <= currentYear + MAX_YEARS_ALLOWED; i++) {
            changeYearButton.addItem(String.valueOf(i));
        }
    }

    private void configureCalendarTable(final JFrame window, final TableCellRenderer cellRenderer) {
        // No resize/reorder
        calendarTable.getTableHeader().setResizingAllowed(false);
        calendarTable.getTableHeader().setReorderingAllowed(false);

        // Single cell selection
        calendarTable.setColumnSelectionAllowed(true);
        calendarTable.setRowSelectionAllowed(true);
        calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configure cell renderer
        calendarTable.setDefaultRenderer(calendarTable.getColumnClass(0), cellRenderer);

        // Add table actions
        calendarTable.setComponentPopupMenu(actions.getAvailableActions());
        calendarTable.addMouseListener(new SelectCellMouseListener());

        // Configure table size
        calendarTableScrollPane.setSize((int) Math.round(window.getWidth() / 1.5),
                (int) Math.round((window.getHeight() / 1.5)));
    }

    private void fillDaysIntoCalendar() {
        // Clear table
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                calendarTableModel.setValueAt(null, i, j);
            }
        }

        // Get first day of month and number of days
        final GregorianCalendar calendarFromMonthYear = new GregorianCalendar(selectedYear, selectedMonth, 1);
        final int monthDays = calendarFromMonthYear.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        final int monthFirstDay = calendarFromMonthYear.get(GregorianCalendar.DAY_OF_WEEK);

        // Generate days information to set into the calendar
        int row;
        int column;

        for (int day = 1; day <= monthDays; day++) {
            // Calculates the week of the day
            row = (day + monthFirstDay - 3) / 7;

            // Calculates the column of the day
            column = (day + monthFirstDay - 3) % 7;

            // Special case: day 1 is Sunday, column calculation would be -1
            if (day > 1 && monthFirstDay == 1) {
                row += 1;
            }

            if (column == -1) {
                column = 6;
            }

            // Set default value
            calendarTableModel.setValueAt(new CalendarDayCell(selectedYear, (selectedMonth + 1), day), row, column);

            // Async request to get calendar day cell information
            final int finalDay = day;
            final int finalRow = row;
            final int finalColumn = column;
            final CompletableFuture<CalendarDayCell> calendarDayCellInfo = service.getAsync(selectedYear,
                    (selectedMonth + 1), day, row, column);

            calendarDayCellInfo.whenComplete((info, exception) -> {
                if (Objects.isNull(exception)) {
                    if (!info.getCustomInfo().isEmpty()) {
                        calendarTableModel.setValueAt(info, finalRow, finalColumn);
                    }
                } else {
                    log.error("Error recuperando información para el día '{}/{}/{}'. Error: ", finalDay,
                            (selectedMonth + 1), selectedYear, exception);
                }
            });
        }
    }

    private class PrevButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // If selected month by user is January
            // Change to the previous year to December
            if (selectedMonth == 0) {
                selectedMonth = 11;
                selectedYear -= 1;
            } else {
                // Decrease one month
                selectedMonth -= 1;
                refresh();
            }
        }
    }

    private class NextButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // If selected month by user is December
            // Change to the next year to January
            if (selectedMonth == 11) {
                selectedMonth = 0;
                selectedYear += 1;
            } else {
                // Increase one month
                selectedMonth += 1;
                refresh();
            }
        }
    }

    private class ChangeYearActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (Objects.nonNull(changeYearButton.getSelectedItem()) && selectedYear != Integer.parseInt(changeYearButton.getSelectedItem().toString())) {
                final String selectedYearString = changeYearButton.getSelectedItem().toString();
                selectedYear = Integer.parseInt(selectedYearString);
                refresh();
            }
        }
    }

    private class OpenPopupDayActionListener implements ActionListener {

        // Data
        private final CalendarPanel calendarInstance;

        public OpenPopupDayActionListener(final CalendarPanel calendar) {
            this.calendarInstance = calendar;
        }

        public void actionPerformed(final ActionEvent e) {
            try {
                Object valor = calendarTableModel.getValueAt(selectedRow, selectedColumn);
                actions.manageActionEvents(calendarInstance, e.getSource(), e.getActionCommand(), (CalendarDayCell) valor, selectedRow,
                        selectedColumn);
            } catch (final Exception ex) {
                log.error("Error gestionando evento del calendario. Error: {}", e);
            }
        }
    }

    private class SelectCellMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            selectedRow = calendarTable.rowAtPoint(e.getPoint());
            selectedColumn = calendarTable.columnAtPoint(e.getPoint());
        }
    }
}
