package io.github.squdan.swing.components.text.autocomplete;

import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.text.PlaceholderTextField;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * {@link PlaceholderTextField} implementation that also offers autocomplete and searching feature.
 * <p>
 * - Value will be auto-completed while user is writing.
 * <p>
 * A button will be showed and user could use it to select some available value.
 */
@EqualsAndHashCode(callSuper = false)
public class PlaceholderSearchAutocompleteTextField<T extends SwingComponentsItem<K>, K>
        extends PlaceholderSearchTextField<T, K> {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = 6175112299757187074L;

    /**
     * Constructor without placeholder, this one just offers autocomplete and search feature.
     *
     * @param values: available values to show with search button and to autocomplete user input.
     */
    public PlaceholderSearchAutocompleteTextField(final List<T> values) {
        this(null, values);
    }

    /**
     * Constructor with placeholder and autocomplete and search feature.
     * <p>
     * While value is empty, placeholder will be showed.
     *
     * @param placeholder: text to show when no value.
     * @param values:      available values to show with search button and to autocomplete user input.
     */
    public PlaceholderSearchAutocompleteTextField(final String placeholder, final List<T> values) {
        super(placeholder, values);

        // Adds auto-complete function to this class
        PlaceholderAutocompleteTextField.fromTextComponent(this, values);
    }
}