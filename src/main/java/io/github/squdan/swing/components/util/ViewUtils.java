package io.github.squdan.swing.components.util;

import io.github.squdan.swing.components.CustomButtonBorder;
import io.github.squdan.swing.components.SwingComponentsException;
import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.configuration.SwingComponentsConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewUtils {

    // Configuration - Files
    public static String SCREENSHOTS_PATH = "screenshots";

    public static BufferedImage toBufferedImage(final JComponent component) {
        // Generate BufferedImage
        final BufferedImage result = new BufferedImage(component.getWidth(), component.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // Draw component into BufferedImage
        final Graphics g = result.getGraphics();
        component.paint(g);
        g.dispose();

        return result;
    }

    public static String toFileImage(final String componentName, final JComponent component) {
        String result = null;

        try {
            final BufferedImage bufferedImage = toBufferedImage(component);
            final String fileName = DateTimeUtils.LocalDateTimeUtils.toString(LocalDateTime.now()).split("\\.")[0]
                    .replace(":", "-");
            final File imageFile = new File(String.format("%s/%s/%s.png", SCREENSHOTS_PATH, componentName, fileName));
            FileUtils.touch(imageFile);
            ImageIO.write(bufferedImage, "png", imageFile);
            result = imageFile.getAbsolutePath();
        } catch (final IOException e) {
            throw new SwingComponentsException(e, String.format("Error guardando imagen del componente '%s - %s'.",
                    componentName, component.getName()));
        }

        return result;
    }

    /**
     * Gets JLabel instance with title format for received text.
     *
     * @param text: to show as title.
     * @return JLabel with text formatted;
     */
    public static JLabel getDefaultTitleLabel(final String text) {
        JLabel result = new JLabel(text);
        result.setFont(SwingComponents.getConfiguration().getTextConfiguration().getTitleFont());
        return result;
    }

    /**
     * Gets JLabel instance with text format for received text.
     *
     * @param text: to show as text.
     * @return JLabel with text formatted;
     */
    public static JLabel getDefaultTextLabel(final String text) {
        JLabel result = new JLabel(text);
        result.setFont(SwingComponents.getConfiguration().getTextConfiguration().getDefaultText());
        return result;
    }

    /**
     * Gets JButton with default color.
     *
     * @param text: to show in the button.
     * @return JBUtton with text received.
     */
    public static JButton getDefaultColorButton(final String text) {
        final JButton button = new JButton(text);
        ViewUtils.changeButtonColorBackground(button, SwingComponents.getConfiguration().getColorConfiguration().getPrimary());
        button.setForeground(SwingComponents.getConfiguration().getColorConfiguration().getPrimaryText());
        button.setFont(SwingComponents.getConfiguration().getTextConfiguration().getTitleSecondaryFont());
        return button;
    }

    /**
     * This method changes button background coloor.
     *
     * @param button
     * @param color
     */
    public static void changeButtonColorBackground(final JButton button, final Color color) {
        // Configure background color
        button.setBackground(color);

        // Configure border
        button.setBorder(new CustomButtonBorder(color));
    }

    public static JPanel generateVerticalBigPanelMultipleHeaders(final JComponent bodyComponent,
                                                                 final JComponent... headerComponents) {
        final JPanel result = new JPanel(new GridBagLayout());

        // Panel header configuration
        if (Objects.nonNull(headerComponents) && headerComponents.length > 0) {
            final GridBagConstraints headerConstrainst = new GridBagConstraints();
            headerConstrainst.fill = GridBagConstraints.HORIZONTAL;
            headerConstrainst.ipadx = 0; // Margin between components
            headerConstrainst.ipady = 0; // Margin between components
            headerConstrainst.gridwidth = 1; // Cells number taken by component
            headerConstrainst.gridheight = 1; // Cells number taken by component
            headerConstrainst.weightx = 1.0; // To take extra space without use
            headerConstrainst.weighty = 0.0; // To take extra space without use
            headerConstrainst.gridx = 0; // Position X
            headerConstrainst.gridy = 0; // Position Y

            final JPanel headerPanel = new JPanel(new GridLayout(1, 0));
            headerPanel.setBackground(Color.DARK_GRAY);
            Stream.of(headerComponents).forEach(hc -> headerPanel.add(hc));
            result.add(headerPanel, headerConstrainst);
        }

        // Panel body configuration
        if (Objects.nonNull(bodyComponent)) {
            final GridBagConstraints bodyConstrainst = new GridBagConstraints();
            bodyConstrainst.fill = GridBagConstraints.HORIZONTAL;
            bodyConstrainst.ipadx = 40; // Margin between components
            bodyConstrainst.ipady = 2000; // Margin between components
            bodyConstrainst.gridwidth = 1; // Cells number taken by component
            bodyConstrainst.gridheight = 5; // Cells number taken by component
            bodyConstrainst.weightx = 1.0; // To take extra space without use
            bodyConstrainst.weighty = 1.0; // To take extra space without use
            bodyConstrainst.gridx = 0; // Position X
            bodyConstrainst.gridy = 1; // Position Y
            result.add(bodyComponent, bodyConstrainst);
        }

        return result;
    }

    public static JPanel generateVerticalSmallPanelMultipleHeaders(final JComponent bodyComponent,
                                                                   final JComponent... headerComponents) {
        final JPanel result = new JPanel(new GridBagLayout());

        // Panel header configuration
        if (Objects.nonNull(headerComponents) && headerComponents.length > 0) {
            final GridBagConstraints headerConstrainst = new GridBagConstraints();
            headerConstrainst.fill = GridBagConstraints.HORIZONTAL;
            headerConstrainst.ipadx = 0; // Margin between components
            headerConstrainst.ipady = 0; // Margin between components
            headerConstrainst.gridwidth = 1; // Cells number taken by component
            headerConstrainst.gridheight = 1; // Cells number taken by component
            headerConstrainst.weightx = 1.0; // To take extra space without use
            headerConstrainst.weighty = 0.0; // To take extra space without use
            headerConstrainst.gridx = 0; // Position X
            headerConstrainst.gridy = 0; // Position Y

            final JPanel headerPanel = new JPanel(new GridLayout(1, headerComponents.length));
            headerPanel.setBackground(Color.DARK_GRAY);
            Stream.of(headerComponents).forEach(hc -> headerPanel.add(hc));
            result.add(headerPanel, headerConstrainst);
        }

        // Panel body configuration
        if (Objects.nonNull(bodyComponent)) {
            final GridBagConstraints bodyConstrainst = new GridBagConstraints();
            bodyConstrainst.fill = GridBagConstraints.HORIZONTAL;
            bodyConstrainst.ipadx = 40; // Margin between components
            bodyConstrainst.ipady = 50; // Margin between components
            bodyConstrainst.gridwidth = 1; // Cells number taken by component
            bodyConstrainst.gridheight = 5; // Cells number taken by component
            bodyConstrainst.weightx = 1.0; // To take extra space without use
            bodyConstrainst.weighty = 1.0; // To take extra space without use
            bodyConstrainst.gridx = 0; // Position X
            bodyConstrainst.gridy = 1; // Position Y
            result.add(bodyComponent, bodyConstrainst);
        }

        return result;
    }

    public static JPanel generateHorizontalPanelBigSmall(final JComponent infoComponent,
                                                         final JComponent... actionComponents) {
        final JPanel result = new JPanel(new GridBagLayout());

        // Panel info configuration
        final GridBagConstraints bigConstraints = new GridBagConstraints();
        bigConstraints.fill = GridBagConstraints.VERTICAL;
        bigConstraints.ipadx = 240 - (80 * actionComponents.length); // Margin between components
        bigConstraints.ipady = 0; // Margin between components
        bigConstraints.gridwidth = 8; // Cells number taken by component
        bigConstraints.gridheight = 1; // Cells number taken by component
        bigConstraints.weightx = 1.0; // To take extra space without use
        bigConstraints.weighty = 1.0; // To take extra space without use
        bigConstraints.gridx = 0; // Position X
        bigConstraints.gridy = 0; // Position Y
        result.add(infoComponent, bigConstraints);

        // Panel action configuration
        final GridBagConstraints smallConstraints = new GridBagConstraints();
        smallConstraints.fill = GridBagConstraints.VERTICAL;
        smallConstraints.ipadx = 0; // Margin between components
        smallConstraints.ipady = 0; // Margin between components
        smallConstraints.gridwidth = 2; // Cells number taken by component
        smallConstraints.gridheight = 1; // Cells number taken by component
        smallConstraints.weightx = 0.0; // To take extra space without use
        smallConstraints.weighty = 0.0; // To take extra space without use
        smallConstraints.gridx = 8; // Position X
        smallConstraints.gridy = 0; // Position Y

        final JPanel actionsPanel = new JPanel(new GridLayout(1, 0));
        actionsPanel.setBackground(Color.DARK_GRAY);
        Stream.of(actionComponents).forEach(hc -> actionsPanel.add(hc));
        result.add(actionsPanel, smallConstraints);

        return result;
    }

    public static JPanel generateHorizontalPanelSmallBig(final JComponent infoComponent,
                                                         final JComponent actionComponent) {
        final JPanel result = new JPanel(new GridBagLayout());

        // Panel info configuration
        final GridBagConstraints smallConstraints = new GridBagConstraints();
        smallConstraints.fill = GridBagConstraints.VERTICAL;
        smallConstraints.ipadx = 0; // Margin between components
        smallConstraints.ipady = 0; // Margin between components
        smallConstraints.gridwidth = 2; // Cells number taken by component
        smallConstraints.gridheight = 1; // Cells number taken by component
        smallConstraints.weightx = 0.0; // To take extra space without use
        smallConstraints.weighty = 0.0; // To take extra space without use
        smallConstraints.gridx = 0; // Position X
        smallConstraints.gridy = 0; // Position Y
        result.add(infoComponent, smallConstraints);

        // Panel action configuration
        final GridBagConstraints bigConstraints = new GridBagConstraints();
        bigConstraints.fill = GridBagConstraints.VERTICAL;
        bigConstraints.ipadx = 100; // Margin between components
        bigConstraints.ipady = 0; // Margin between components
        bigConstraints.gridwidth = 8; // Cells number taken by component
        bigConstraints.gridheight = 1; // Cells number taken by component
        bigConstraints.weightx = 1.0; // To take extra space without use
        bigConstraints.weighty = 1.0; // To take extra space without use
        bigConstraints.gridx = 2; // Position X
        bigConstraints.gridy = 0; // Position Y
        result.add(actionComponent, bigConstraints);

        return result;
    }
}
