package io.github.squdan.swing.components.text.composed;

import io.github.squdan.swing.components.util.ViewUtils;
import io.github.squdan.swing.components.text.PlaceholderTextField;

import javax.swing.*;
import java.util.Objects;

public class PhoneWithCountrySelectorPanelGenerator {

    // Components
    private JComboBox<PhoneCountry> countryComboBox;
    private JTextField phoneTextField;

    public PhoneWithCountrySelectorPanelGenerator(final PhoneCountry[] countries, final PhoneCountry defaultCountry) {
        this(countries, defaultCountry, null, null);
    }

    public PhoneWithCountrySelectorPanelGenerator(final PhoneCountry[] countries, final PhoneCountry defaultCountry, final String placeholder) {
        this(countries, defaultCountry, placeholder, null);
    }

    public PhoneWithCountrySelectorPanelGenerator(final PhoneCountry[] countries, final PhoneCountry defaultCountry, final String placeholder, final PhoneNumber phone) {
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
    }

    public JPanel generatePanel() {
        return ViewUtils.generateHorizontalPanelSmallBig(this.countryComboBox, this.phoneTextField);
    }

    public String getFullPhoneWithCountry() {
        return "+" + getSelectedCountryPrefix() + getPhone();
    }

    public String getPhone() {
        return this.phoneTextField.getText();
    }

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
