package io.github.squdan.swing.components.panel.phone;

import io.github.squdan.swing.components.text.PlaceholderTextField;
import io.github.squdan.swing.components.util.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

// TODO: check if this component can be changed to extedns from JPanel instead of using generatePanel() method.

/**
 * Phone input representation using {@link JPanel}.
 * <p>
 * This implementation will show a country selector and an input field for the number.
 */
public class PhoneWithCountryPanel extends JPanel {

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

        // Common configuration
        this.countryComboBox = new JComboBox<>(countries);

        if (Objects.nonNull(phone)) {
            // Fill PhoneTextField fields
            this.phoneTextField = new PlaceholderTextField(placeholder, phone.getNumber().toString());
            this.countryComboBox.setSelectedItem(phone.getCountry());
        } else {
            // Fill PhoneTextField fields
            this.phoneTextField = new PlaceholderTextField(placeholder, null);
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
        return "+" + getSelectedCountryPrefix() + getPhone();
    }

    /**
     * Return phone number.
     *
     * @return phone number.
     */
    public String getPhone() {
        return this.phoneTextField.getText();
    }

    /**
     * Return country prefix.
     *
     * @return country prefix.
     */
    public int getSelectedCountryPrefix() {
        int result = 0;
        final PhoneCountry selectedCuntry = (PhoneCountry) this.countryComboBox
                .getSelectedItem();

        if (Objects.nonNull(selectedCuntry)) {
            result = selectedCuntry.getPrefix();
        }

        return result;
    }

}
