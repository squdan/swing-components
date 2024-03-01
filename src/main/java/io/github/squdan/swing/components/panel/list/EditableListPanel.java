package io.github.squdan.swing.components.panel.list;

import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.panel.list.action.EditableListActionService;
import io.github.squdan.swing.components.panel.list.cell.ListItemTextFieldCellRenderer;
import io.github.squdan.swing.components.text.autocomplete.PlaceholderSearchAutocompleteTextField;
import io.github.squdan.swing.components.util.ViewUtils;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * List items representation using {@link JPanel}.
 * <p>
 * This implementation will show a container with a list of elements that will offer a create action if it's
 * implemented at received {@link EditableListActionService} implementation.
 * <p>
 * Also, this implementation allows to the user add or remove elements from the selected elements into the list.
 */
public class EditableListPanel<T extends SwingComponentsItem<K>, K> extends JPanel {

    @Serial
    private static final long serialVersionUID = 5291140686381473317L;

    // Service
    private final EditableListActionService<T, K> managementService;

    // Data
    @Getter
    private final List<T> selectedValues;
    private final List<T> availableValues;
    private final DefaultListModel<T> availableElementsListModel;
    private final JList<T> availableElementsList;
    private final PlaceholderSearchAutocompleteTextField<T, K> searchAutocompleteTextField;

    public EditableListPanel(final List<T> values) {
        this(null, null, values, null);
    }

    public EditableListPanel(final JLabel header, final List<T> values) {
        this(header, null, values, null);
    }

    public EditableListPanel(final JLabel header, final EditableListActionService<T, K> managementService,
                             final List<T> values, final List<T> defaultSelectedValues) {
        super();

        // Initializations
        this.managementService = managementService;
        this.availableValues = values;
        this.selectedValues = new ArrayList<>();
        this.availableElementsListModel = new DefaultListModel<>();
        this.availableElementsList = new JList<>(availableElementsListModel);
        this.searchAutocompleteTextField = new PlaceholderSearchAutocompleteTextField<>(values);

        // Configure elements list
        availableElementsList.setCellRenderer(new ListItemTextFieldCellRenderer());
        availableElementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableElementsList.addListSelectionListener(new RemoveSelectedElementListener());

        // If default selected values received, adds them to the list
        if (CollectionUtils.isNotEmpty(defaultSelectedValues)) {
            availableElementsListModel.addAll(defaultSelectedValues);
            this.selectedValues.addAll(defaultSelectedValues);
        }

        // Buttons to [CREATE] or [ADD] elements to the list
        JPanel searchingPanel;
        final JButton addTag = new JButton("Añadir");
        addTag.addActionListener(new AddElementToListActionListener());

        if (Objects.nonNull(managementService)) {
            final JButton newTag = new JButton("Nuevo");
            newTag.addActionListener(new CreateElementToListActionListener());
            searchingPanel = ViewUtils.generateHorizontalPanelBigSmall(searchAutocompleteTextField, newTag, addTag);
        } else {
            searchingPanel = ViewUtils.generateHorizontalPanelBigSmall(searchAutocompleteTextField, addTag);
        }

        // Adds list to the panel so see selected items
        final JPanel tagsPanel = ViewUtils
                .generateVerticalSmallPanelMultipleHeaders(new JScrollPane(availableElementsList), searchingPanel);

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
            this.add(tagsPanel);
        }
    }

    private class RemoveSelectedElementListener implements ListSelectionListener {

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            // Draw popup
            final T elementToRemove = availableElementsList.getSelectedValue();
            if (Objects.nonNull(elementToRemove)) {
                final String removeTagMsg = String.format("¿Está seguro que desea quitar '%s' de la selección?",
                        elementToRemove.toTextField());
                final int result = JOptionPane.showConfirmDialog(null, removeTagMsg, "Quitar elemento",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // Manage response
                if (result == JOptionPane.OK_OPTION) {
                    availableElementsList.clearSelection();
                    availableElementsListModel.removeElement(elementToRemove);
                    selectedValues.remove(elementToRemove);
                }
            }
        }

    }

    private class AddElementToListActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (Objects.nonNull(searchAutocompleteTextField.getSelectedItem())
                    && !availableElementsListModel.contains(searchAutocompleteTextField.getSelectedItem())) {
                availableElementsListModel.addElement(searchAutocompleteTextField.getSelectedItem());
                selectedValues.add(searchAutocompleteTextField.getSelectedItem());
            }
        }

    }

    private class CreateElementToListActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (Objects.nonNull(managementService)) {
                T createdElement = managementService.create();

                if (Objects.nonNull(createdElement)) {
                    availableValues.add(createdElement);
                    availableElementsListModel.addElement(createdElement);
                    selectedValues.add(createdElement);
                    searchAutocompleteTextField.setText(
                            availableElementsListModel.get(availableElementsListModel.size() - 1).toTextField());
                }
            }
        }

    }

}
