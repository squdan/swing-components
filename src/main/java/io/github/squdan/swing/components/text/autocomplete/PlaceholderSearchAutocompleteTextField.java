package io.github.squdan.swing.components.text.autocomplete;

import io.github.squdan.swing.components.ComponentItem;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class PlaceholderSearchAutocompleteTextField<T extends ComponentItem<K>, K>
        extends PlaceholderSearchTextField<T, K> {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = 6175112299757187074L;

    public PlaceholderSearchAutocompleteTextField(final List<T> values) {
        this(null, values);
    }

    public PlaceholderSearchAutocompleteTextField(final String placeholder, final List<T> values) {
        super(placeholder, values);

        // Adds auto-complete function to this class
        PlaceholderAutocompleteTextField.fromTextComponent(this, values);
    }
}