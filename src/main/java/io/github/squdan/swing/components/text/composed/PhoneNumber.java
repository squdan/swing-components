package io.github.squdan.swing.components.text.composed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
