package io.github.squdan.swing.components.text.autocomplete;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.text.PlaceholderTextField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.text.JTextComponent;
import java.io.Serial;
import java.util.List;
import java.util.Objects;

/**
 * {@link PlaceholderTextField} implementation that also offers autocomplete feature.
 * <p>
 * Value will be auto-completed while user is writing.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlaceholderAutocompleteTextField extends PlaceholderTextField {

    @Serial
    private static final long serialVersionUID = 2669443211939990656L;

    /**
     * Constructor without placeholder, this one just offers autocomplete feature.
     *
     * @param values: available values to autocomplete user input.
     */
    public PlaceholderAutocompleteTextField(final List<? extends SwingComponentsItem<?>> values) {
        this(null, values);
    }

    /**
     * Constructor with placeholder and autocomplete feature.
     * <p>
     * While value is empty, placeholder will be showed.
     *
     * @param placeholder: text to show when no value.
     * @param values:      available values to autocomplete user input.
     */
    public PlaceholderAutocompleteTextField(final String placeholder, final List<? extends SwingComponentsItem<?>> values) {
        super(placeholder, null);

        // Adds auto-complete function to this class
        fromTextComponent(this, values);
    }

    /**
     * Adds autocomplete feature to received {@link JTextComponent}.
     *
     * @param textComponent: {@link JTextComponent} to add autocomplete feature.
     * @param values:        available values to autocomplete user input.
     */
    public static void fromTextComponent(final JTextComponent textComponent,
                                         final List<? extends SwingComponentsItem<?>> values) {
        // Show clear button (if text field is not empty)
        textComponent.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        // Auto-complete values when user writes
        AutoCompleteDecorator.decorate(textComponent, values, true, new GenericObjectToStringConverter());
    }

    private static class GenericObjectToStringConverter extends ObjectToStringConverter {

        @Override
        public String getPreferredStringForItem(final Object item) {
            String result = StringUtils.EMPTY;

            if (Objects.nonNull(item)) {
                if (item instanceof SwingComponentsItem) {
                    result = ((SwingComponentsItem<?>) item).toTextField();
                } else {
                    result = item.toString();
                }
            }

            return result;
        }

    }
}