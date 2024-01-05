package io.github.squdan.swing.components.text.composed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PhoneCountry {

    private String country;
    private int prefix;

    public boolean isFromCountry(final String phone) {
        return phone.startsWith("+" + prefix);
    }

}
