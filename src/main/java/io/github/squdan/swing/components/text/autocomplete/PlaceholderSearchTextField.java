package io.github.squdan.swing.components.text.autocomplete;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatSearchWithHistoryIcon;
import io.github.squdan.swing.components.ComponentItem;
import io.github.squdan.swing.components.text.PlaceholderTextField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class PlaceholderSearchTextField<T extends ComponentItem<K>, K> extends PlaceholderTextField {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = 6388663249826231852L;

    // Configuration
    private static final String DEFAULT_SEARCH_TOOL_TIP_DESC = "Buscar";
    private static final String DEFAULT_NO_VALUES_DESC = "No existen actualmente";

    // Data
    private SelectedItemListener<T, K> searchListener;

    public PlaceholderSearchTextField(final List<T> values) {
        this(null, values);
    }

    public PlaceholderSearchTextField(final String placeholder, final List<T> values) {
        super(placeholder, null);

        // Show search button
        this.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, getSearchButton(null, null, values));

        // Show clear button (if text field is not empty)
        this.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        // Listener - when item selected, save it
        searchListener = new SelectedItemListener<T, K>(values);
        this.getDocument().addDocumentListener(searchListener);
    }

    public T getSelectedItem() {
        return searchListener.getSelectedItem();
    }

    private JButton getSearchButton(final String toolTipDesc, final String noValuesDesc,
                                    final List<? extends ComponentItem<K>> values) {
        final JButton result = new JButton(new FlatSearchWithHistoryIcon(true));
        result.setToolTipText(StringUtils.isBlank(toolTipDesc) ? DEFAULT_SEARCH_TOOL_TIP_DESC : toolTipDesc);
        result.addActionListener(new SearchButtonActionListener(this, values, noValuesDesc));
        return result;
    }

    @AllArgsConstructor
    private class SearchButtonActionListener implements ActionListener {

        // Source component
        private final JTextComponent textComponent;

        // Source values
        private final List<? extends ComponentItem<K>> values;

        // Configuration
        private final String noValuesDesc;

        public void actionPerformed(final ActionEvent event) {
            final JPopupMenu popupMenu = new JPopupMenu();

            if (CollectionUtils.isNotEmpty(values)) {
                // Generate a item for each value to detect when one is selected to update text
                // field
                values.stream().forEach(p -> {
                    // Adds search item to the popup
                    final JMenuItem searchItem = new JMenuItem(p.toTextField());
                    popupMenu.add(searchItem);

                    // Adds event listener to update text field when selected
                    searchItem.addActionListener(selectItemEvent -> {
                        textComponent.setText(selectItemEvent.getActionCommand());
                    });
                });
            } else {
                // Description to show when no values recovered
                popupMenu.add(StringUtils.isBlank(noValuesDesc) ? DEFAULT_NO_VALUES_DESC : noValuesDesc);
            }

            popupMenu.show(textComponent, 0, textComponent.getHeight());
        }
    }

    @Getter
    private static class SelectedItemListener<T extends ComponentItem<K>, K> implements DocumentListener {

        // Data
        private final List<T> values;
        private T selectedItem = null;

        public SelectedItemListener(final List<T> values) {
            this.values = values;
        }

        @Override
        public void changedUpdate(final DocumentEvent e) {
            final String filter = getDocumentEventValue(e);
            searchSelectedValue(filter);
        }

        @Override
        public void removeUpdate(final DocumentEvent e) {
            final String filter = getDocumentEventValue(e);
            searchSelectedValue(filter);
        }

        @Override
        public void insertUpdate(final DocumentEvent e) {
            final String filter = getDocumentEventValue(e);
            searchSelectedValue(filter);
        }

        private String getDocumentEventValue(final DocumentEvent event) {
            String result = null;

            try {
                result = event.getDocument().getText(0, event.getDocument().getLength());
            } catch (final BadLocationException e) {
                log.error("Error al recuperar la información de un filtro. Error: ", e);
            }

            return result;
        }

        private void searchSelectedValue(final String textValue) {

            // This will reset selected value
            if (StringUtils.isBlank(textValue)) {
                this.selectedItem = null;
            }

            // Search item
            else if (CollectionUtils.isNotEmpty(this.values)) {
                final Optional<T> maySelectedItem = this.values.stream().filter(v -> v.toTextField().equals(textValue))
                        .findFirst();

                if (maySelectedItem.isPresent()) {
                    this.selectedItem = maySelectedItem.get();
                } else {
                    this.selectedItem = null;
                }
            }
        }
    }

}