package io.github.squdan.swing.components.panel.phone;

import io.github.squdan.swing.components.text.PlaceholderValidatedTextField;
import io.github.squdan.swing.components.util.ViewUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Phone input representation using {@link JPanel}.
 * <p>
 * This implementation will show a country selector and an input field for the number.
 */
public class PhoneWithCountryPanel extends JPanel {

    // Data
    private final PhoneCountry defaultCountry;

    // Components
    private final JComboBox<PhoneCountry> countryComboBox;
    private final JTextField phoneTextField;

    /**
     * Constructor without placeholder.
     *
     * @param countries:      available countries.
     * @param defaultCountry: default selected country.
     */
    public PhoneWithCountryPanel(final PhoneCountry[] countries, final PhoneCountry defaultCountry) {
        this(countries, defaultCountry, null, null);
    }

    /**
     * Constructor with placeholder.
     *
     * @param countries:      available countries.
     * @param defaultCountry: default selected country.
     * @param placeholder:    text to show when no value at phone input field.
     */
    public PhoneWithCountryPanel(final PhoneCountry[] countries, final PhoneCountry defaultCountry, final String placeholder) {
        this(countries, defaultCountry, placeholder, null);
    }

    /**
     * Constructor with placeholder and default value.
     *
     * @param countries:      available countries.
     * @param defaultCountry: default selected country.
     * @param placeholder:    text to show when no value at phone input field.
     * @param phone:          default phone value.
     */
    public PhoneWithCountryPanel(final PhoneCountry[] countries, final PhoneCountry defaultCountry, final String placeholder, final PhoneNumber phone) {
        super(new GridLayout(0, 1));

        // Data
        this.defaultCountry = defaultCountry;

        // Common configuration
        this.countryComboBox = new JComboBox<>(countries);

        if (Objects.nonNull(phone)) {
            // Fill PhoneTextField fields
            this.phoneTextField = new PlaceholderValidatedTextField(
                    PlaceholderValidatedTextField.TextFieldRestrictions.CommonRestrictions.PHONE_FORMAT.getRestrictions(),
                    placeholder, String.valueOf(phone.getNumber()));
            this.countryComboBox.setSelectedItem(phone.getCountry());
        } else {
            // Fill PhoneTextField fields
            this.phoneTextField = new PlaceholderValidatedTextField(
                    PlaceholderValidatedTextField.TextFieldRestrictions.CommonRestrictions.PHONE_FORMAT.getRestrictions(),
                    placeholder);
            this.countryComboBox.setSelectedItem(defaultCountry);
        }

        this.add(ViewUtils.generateHorizontalPanelSmallBig(this.countryComboBox, this.phoneTextField));
    }

    /**
     * Generates a String with "+"{country-prefix}{phone-number}
     *
     * @return String: "+"{country-prefix}{phone-number}.
     */
    public String getFullPhoneWithCountry() {
        String result = StringUtils.EMPTY;

        // Phone parts
        final Integer prefix = getSelectedCountryPrefix();
        final String phone = getPhone();

        if (Objects.nonNull(prefix) && StringUtils.isNotBlank(phone)) {
            result = "+" + prefix + phone;
        }

        return result;
    }

    /**
     * Return phone number.
     *
     * @return phone number.
     */
    public String getPhone() {
        return this.phoneTextField.getText();
    }

    public void setPhone(final PhoneNumber phone) {
        if (Objects.nonNull(phone)) {
            this.countryComboBox.setSelectedItem(phone.getCountry());
            this.phoneTextField.setText(String.valueOf(phone.getNumber()));
        } else {
            this.countryComboBox.setSelectedItem(defaultCountry);
            this.phoneTextField.setText(null);
        }
    }

    /**
     * Return country prefix.
     *
     * @return country prefix.
     */
    public Integer getSelectedCountryPrefix() {
        Integer result = null;

        final PhoneCountry selectedCuntry = (PhoneCountry) this.countryComboBox
                .getSelectedItem();

        if (Objects.nonNull(selectedCuntry)) {
            result = selectedCuntry.getPrefix();
        }

        return result;
    }

}
