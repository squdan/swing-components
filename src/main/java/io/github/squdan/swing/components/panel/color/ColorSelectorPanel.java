package io.github.squdan.swing.components.panel.color;

import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.util.ViewUtils;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * Color selector representation using {@link JPanel}.
 * <p>
 * This implementation will show a button with selected color, if button is clicked, a color selector will be open.
 */
public class ColorSelectorPanel extends JPanel {

    // Components
    private static final Random RANDOM = new Random();
    private final JButton colorButton;

    // Data
    @Getter
    private Color selectedColor;

    /**
     * Constructor without color selected.
     *
     * @param title: text to show as header at color selector.
     */
    public ColorSelectorPanel(final String title) {
        this(title, null);
    }

    /**
     * Constructor with default color selected.
     *
     * @param title: text to show as header at color selector.
     * @param color: default color selected.
     */
    public ColorSelectorPanel(final String title, final Color color) {
        // Set initial color
        if (Objects.nonNull(color)) {
            this.selectedColor = color;
        } else {
            this.selectedColor = SwingComponents.getConfiguration().getColorConfiguration().getPrimary();
        }

        // Configure button
        this.colorButton = new JButton();
        updateButtonColor(this.selectedColor);

        // Add listener to open color selector popup
        colorButton.addActionListener(e -> {
            selectedColor = JColorChooser.showDialog(null, title, selectedColor);

            if (Objects.isNull(selectedColor)) {
                updateButtonColor(SwingComponents.getConfiguration().getColorConfiguration().getNeutral());
            } else {
                updateButtonColor(selectedColor);
            }
        });

        // Add components to the panel
        this.add(this.colorButton);
    }

    public static Color generateRandomColor() {
        final int r = RANDOM.nextInt(256);
        final int g = RANDOM.nextInt(256);
        final int b = RANDOM.nextInt(256);
        return new Color(r, g, b);
    }

    public void setColor(final Color color) {
        this.selectedColor = color;
        updateButtonColor(selectedColor);
    }

    public static String colorToString(final Color color) {
        String result = null;

        if (Objects.nonNull(color)) {
            result = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        }

        return result;
    }

    private void updateButtonColor(final Color color) {
        this.colorButton.setText(colorToString(color));
        ViewUtils.changeButtonColorBackground(this.colorButton, color);
    }
}
