package io.github.squdan.swing.components.panel.list;

import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.panel.list.action.ListActionService;
import io.github.squdan.swing.components.panel.list.cell.ListItemTextFieldCellRenderer;
import io.github.squdan.swing.components.panel.list.cell.ListModelNoSelection;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.Serial;
import java.util.List;
import java.util.Objects;

/**
 * List items representation using {@link JPanel}.
 * <p>
 * This implementation will show a container with a list of elements that will offer an on-click action if it's
 * implemented at received {@link ListActionService} implementation.
 */
public class ListPanel<T extends SwingComponentsItem<K>, K> extends JPanel {

    @Serial
    private static final long serialVersionUID = 5291140686384373317L;

    // Data
    @Getter
    private final List<T> selectedValues;
    private final JList<T> availableElementsList;

    public ListPanel(final List<T> values) {
        this(null, values, null);
    }

    public ListPanel(final JLabel header, final List<T> values) {
        this(header, values, null);
    }

    public ListPanel(final JLabel header, final List<T> values, final ListActionService<T, K> managementService) {
        super(new GridLayout(0, 1));

        // Initializations
        final DefaultListModel<T> availableElementsListModel = new DefaultListModel<>();
        this.availableElementsList = new JList<>(availableElementsListModel);
        this.selectedValues = values;

        // Configure elements list
        this.availableElementsList.setCellRenderer(new ListItemTextFieldCellRenderer());
        this.availableElementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (CollectionUtils.isNotEmpty(this.selectedValues)) {
            availableElementsListModel.addAll(this.selectedValues);
        }

        if (Objects.nonNull(managementService)) {
            this.availableElementsList.addListSelectionListener(new SelectedElementListener(managementService));
        } else {
            this.availableElementsList.setSelectionModel(new ListModelNoSelection());
        }

        // Configure panel
        JPanel tagsPanel = new JPanel(new GridLayout(0, 1));
        tagsPanel.add(new JScrollPane(availableElementsList));

        // Adds header to the panel
        if (Objects.nonNull(header)) {
            // Panel fore header / body
            final JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

            final JPanel headerAlignPanel = new JPanel(new GridLayout(0, 1));
            headerAlignPanel.add(header);
            resultPanel.add(headerAlignPanel);

            resultPanel.add(tagsPanel);
            this.add(resultPanel);
        } else {
            final JPanel resultPanel = new JPanel(new GridLayout(0, 1));
            resultPanel.add(tagsPanel);
            this.add(resultPanel);
        }
    }

    private class SelectedElementListener implements ListSelectionListener {

        // Service
        private final ListActionService<T, K> managementService;

        public SelectedElementListener(final ListActionService<T, K> managementService) {
            this.managementService = managementService;
        }

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            // Draw popup
            final T elementSelected = availableElementsList.getSelectedValue();
            if (Objects.nonNull(elementSelected)) {
                this.managementService.action(elementSelected);
            }
        }
    }
}
