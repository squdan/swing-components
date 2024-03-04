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
    private final DefaultListModel<T> listModel = new DefaultListModel<>();
    private final JList<T> jListElements;

    @Getter
    private final List<T> listElemements;

    public ListPanel(final JLabel header) {
        this(header, null, null);
    }

    public ListPanel(final List<T> values) {
        this(null, values, null);
    }

    public ListPanel(final JLabel header, final List<T> values) {
        this(header, values, null);
    }

    public ListPanel(final JLabel header, final List<T> values, final ListActionService<T, K> managementService) {
        super(new GridLayout(0, 1));

        // Initializations
        this.jListElements = new JList<>(listModel);
        this.listElemements = values;

        // Configure elements list
        this.jListElements.setCellRenderer(new ListItemTextFieldCellRenderer());
        this.jListElements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (CollectionUtils.isNotEmpty(this.listElemements)) {
            listModel.addAll(this.listElemements);
        }

        if (Objects.nonNull(managementService)) {
            this.jListElements.addListSelectionListener(new SelectedElementListener(managementService));
        } else {
            this.jListElements.setSelectionModel(new ListModelNoSelection());
        }

        // Configure panel
        final JPanel elementsPanel = new JPanel(new GridLayout(0, 1));
        elementsPanel.add(new JScrollPane(jListElements));

        // Adds header to the panel
        if (Objects.nonNull(header)) {
            // Panel fore header / body
            final JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

            final JPanel headerAlignPanel = new JPanel(new GridLayout(0, 1));
            headerAlignPanel.add(header);
            resultPanel.add(headerAlignPanel);

            resultPanel.add(elementsPanel);
            this.add(resultPanel);
        } else {
            final JPanel resultPanel = new JPanel(new GridLayout(0, 1));
            resultPanel.add(elementsPanel);
            this.add(resultPanel);
        }
    }

    public void addValues(final List<T> values) {
        this.listModel.addAll(values);
    }

    public void setValues(final List<T> values) {
        this.listModel.clear();
        this.listModel.addAll(values);
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
            final T elementSelected = jListElements.getSelectedValue();
            if (Objects.nonNull(elementSelected)) {
                this.managementService.action(elementSelected);
            }
        }
    }
}
