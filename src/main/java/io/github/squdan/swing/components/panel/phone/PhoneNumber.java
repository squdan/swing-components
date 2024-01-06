package io.github.squdan.swing.components.panel.phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Model that contains phone information.
 * <p>
 * This model is used at {@link PhoneWithCountryPanel}.
 */
@Data
@Builder
@AllArgsConstructor
public class PhoneNumber {

    private PhoneCountry country;
    private Long number;

    @Override
    public String toString() {
        return "+" + country.getPrefix() + number;
    }
}
