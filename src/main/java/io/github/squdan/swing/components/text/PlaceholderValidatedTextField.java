package io.github.squdan.swing.components.text;

import io.github.squdan.swing.components.configuration.SwingComponents;
import lombok.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.io.Serial;
import java.util.Objects;

/**
 * {@link PlaceholderTextField} implementation that also offers validation and restriction feature.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlaceholderValidatedTextField extends PlaceholderTextField {

    @Serial
    private static final long serialVersionUID = 2669443211829990656L;

    // Configuration
    private final static String ALLOWED_NIE_CHARACTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

    /**
     * Constructor without placeholder, just apply restrictions and validations.
     *
     * @param restrictions: to apply to value.
     */
    public PlaceholderValidatedTextField(final TextFieldRestrictions restrictions) {
        this(restrictions, null, null);
    }

    /**
     * Constructor with placeholder and validations over text value.
     * <p>
     * While value is empty, placeholder will be showed.
     *
     * @param placeholder: text to show when no value.
     */
    public PlaceholderValidatedTextField(final TextFieldRestrictions restrictions, final String placeholder) {
        this(restrictions, placeholder, null);
    }

    /**
     * Constructor with placeholder and validations over text value.
     * <p>
     * While value is empty, placeholder will be showed.
     *
     * @param placeholder: text to show when no value.
     * @param value:       default value.
     */
    public PlaceholderValidatedTextField(final TextFieldRestrictions restrictions, final String placeholder, final String value) {
        super(placeholder, value);

        // Set validator
        final TextFieldInputValidator validator = new TextFieldInputValidator(this, restrictions);
        ((AbstractDocument) this.getDocument()).setDocumentFilter(validator);

        // Update validation status field
        if (Objects.nonNull(value)) {
            validator.validateFieldValue();
        }
    }

    @Getter
    @Builder
    public static class TextFieldRestrictions {
        private Boolean numeric;
        private Integer minLenght;
        private Integer maxLenght;
        private String regex;
        private String allowedCharactersRegex;

        @Getter
        @AllArgsConstructor
        public enum CommonRestrictions {
            DNI_NIE_NIF(TextFieldRestrictions.builder()
                    .minLenght(9)
                    .maxLenght(9)
                    .regex(TextFieldRestrictions.CommonRegexs.DNI_NIE_NIF_FORMAT.getRegex())
                    .build()),
            EMAIL(TextFieldRestrictions.builder()
                    .minLenght(5)
                    .maxLenght(200)
                    .regex(TextFieldRestrictions.CommonRegexs.EMAIL_FORMAT.getRegex())
                    .build()),
            PHONE_FORMAT(TextFieldRestrictions.builder()
                    .minLenght(9)
                    .maxLenght(9)
                    .allowedCharactersRegex(TextFieldRestrictions.CommonRegexs.INTEGER_ALLOWED_CHARACTERS.getRegex())
                    .regex(TextFieldRestrictions.CommonRegexs.PHONE_FORMAT.getRegex())
                    .build()),
            INTEGER(TextFieldRestrictions.builder()
                    .minLenght(1)
                    .maxLenght(200)
                    .allowedCharactersRegex(TextFieldRestrictions.CommonRegexs.INTEGER_ALLOWED_CHARACTERS.getRegex())
                    .build()),
            DECIMAL(TextFieldRestrictions.builder()
                    .minLenght(1)
                    .maxLenght(200)
                    .allowedCharactersRegex(TextFieldRestrictions.CommonRegexs.DECIMAL_ALLOWED_CHARACTERS.getRegex())
                    .regex(TextFieldRestrictions.CommonRegexs.DECIMAL_FORMAT.getRegex())
                    .build()),
            NOT_EMPTY(TextFieldRestrictions.builder()
                    .minLenght(1)
                    .maxLenght(200)
                    .build());

            private final TextFieldRestrictions restrictions;
        }

        @Getter
        @AllArgsConstructor
        public enum CommonRegexs {
            DNI_NIE_NIF_FORMAT(String.format("[A-HJ-NP-SUVW][0-9]{7}[0-9A-J]|\\d{8}[%s%s]|[X]\\d{7}[%s%s]|[X]\\d{8}[%s%s]|[YZ]\\d{0,7}[%s%s]",
                    ALLOWED_NIE_CHARACTERS.toLowerCase(), ALLOWED_NIE_CHARACTERS,
                    ALLOWED_NIE_CHARACTERS.toLowerCase(), ALLOWED_NIE_CHARACTERS,
                    ALLOWED_NIE_CHARACTERS.toLowerCase(), ALLOWED_NIE_CHARACTERS,
                    ALLOWED_NIE_CHARACTERS.toLowerCase(), ALLOWED_NIE_CHARACTERS)),
            EMAIL_FORMAT("([\\w.+-]+)\\@(\\w+)\\.(\\w+)"),
            PHONE_FORMAT("([\\d{9}]+)"),
            INTEGER_ALLOWED_CHARACTERS("([\\d])"),
            DECIMAL_ALLOWED_CHARACTERS("[\\d,]"),
            DECIMAL_FORMAT("([\\d]+)([\\d,]*)");

            private final String regex;
        }
    }

    @AllArgsConstructor
    private static class TextFieldInputValidator extends DocumentFilter {

        // Data
        private JTextField textField;
        private TextFieldRestrictions restrictions;

        @Override
        public void insertString(final FilterBypass fb, final int offset, final String text, final AttributeSet attr)
                throws BadLocationException {
            if (validateInput(fb.getDocument().getLength(), text)) {
                super.insertString(fb, offset, text, attr);
                validateFieldValue();
            }
        }

        @Override
        public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
                throws BadLocationException {
            if (validateInput(fb.getDocument().getLength(), text)) {
                super.replace(fb, offset, length, text, attrs);
                validateFieldValue();
            }
        }

        @Override
        public void remove(final FilterBypass fb, final int offset, final int length) throws
                BadLocationException {
            super.remove(fb, offset, length);
            validateFieldValue();
        }

        public void validateFieldValue() {
            boolean valid = true;

            if (Objects.nonNull(this.restrictions.getMinLenght()) && this.textField.getText().length() < this.restrictions.getMinLenght()) {
                valid = false;
            }

            if (StringUtils.isNotBlank(this.restrictions.getRegex()) && !this.textField.getText().matches(this.restrictions.getRegex())) {
                valid = false;
            }

            changeFieldStatus(valid);
        }

        private boolean validateInput(final int length, final String text) {
            boolean valid = true;

            if (BooleanUtils.isTrue(this.restrictions.getNumeric()) && !text.matches("\\d+")) {
                valid = false;
            }

            if (Objects.nonNull(this.restrictions.getMaxLenght()) && (length + text.length() > this.restrictions.getMaxLenght())) {
                valid = false;
            }

            if (StringUtils.isNotBlank(this.restrictions.getAllowedCharactersRegex()) && !text.matches(this.restrictions.getAllowedCharactersRegex())) {
                valid = false;
            }

            return valid;
        }

        private void changeFieldStatus(final boolean valid) {
            if (valid) {
                this.textField.setBackground(SwingComponents.getConfiguration().getColorConfiguration().getNeutral());
            } else {
                this.textField.setBackground(SwingComponents.getConfiguration().getColorConfiguration().getDanger());
            }
        }
    }
}