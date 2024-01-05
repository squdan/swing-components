package io.github.squdan.swing.components.text.autocomplete;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.squdan.swing.components.ComponentItem;
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

@Data
@EqualsAndHashCode(callSuper = false)
public class PlaceholderAutocompleteTextField extends PlaceholderTextField {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = 2669443211939990656L;

    public PlaceholderAutocompleteTextField(final List<? extends ComponentItem<?>> values) {
        this(null, values);
    }

    public PlaceholderAutocompleteTextField(final String placeholder, final List<? extends ComponentItem<?>> values) {
        super(placeholder, null);

        // Adds auto-complete function to this class
        fromTextComponent(this, values);
    }

    public static void fromTextComponent(final JTextComponent textComponent,
                                         final List<? extends ComponentItem<?>> values) {
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
                if (item instanceof ComponentItem) {
                    result = ((ComponentItem<?>) item).toTextField();
                } else {
                    result = item.toString();
                }
            }

            return result;
        }

    }
}