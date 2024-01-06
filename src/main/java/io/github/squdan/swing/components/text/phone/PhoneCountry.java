package io.github.squdan.swing.components.text.phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Model that contains phone country information.
 * <p>
 * This model is used at {@link PhoneWithCountrySelectorPanelGenerator}.
 */
@Data
@Builder
@AllArgsConstructor
public class PhoneCountry {

    private String country;
    private int prefix;

    public boolean isFromCountry(final String phone) {
        return phone.startsWith("+" + prefix);
    }

    @Override
    public String toString() {
        return String.format("[+%s] %s", this.getPrefix(), this.getCountry());
    }
}
