package io.github.squdan.swing.components.panel.splashscreen;

import io.github.squdan.swing.components.util.ViewUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

/**
 * Shows a splash-screen during configured time or until is closed manually.
 */
public class Splashscreen {

    // Data
    private BackgroundWorker SPLASH_SCREEN_INSTANCE;

    /**
     * Configures the splash-screen to show.
     *
     * @param title        to show.
     * @param message      to show.
     * @param imagePath    to show.
     * @param progressTime time to increase 1% (milliseconds).
     */
    public Splashscreen(final String title, final String message, final String imagePath, final int progressTime) {
        EventQueue.invokeLater(() -> {
            SPLASH_SCREEN_INSTANCE = new BackgroundWorker(title, message, getSplashImage(imagePath), progressTime);
            SPLASH_SCREEN_INSTANCE.execute();
        });
    }

    /**
     * Configures the splash-screen to show.
     *
     * @param title        to show.
     * @param message      to show.
     * @param image        to show.
     * @param progressTime time to increase 1% (milliseconds).
     */
    public Splashscreen(final String title, final String message, final Image image, final int progressTime) {
        EventQueue.invokeLater(() -> {
            SPLASH_SCREEN_INSTANCE = new BackgroundWorker(title, message, image, progressTime);
            SPLASH_SCREEN_INSTANCE.execute();
        });
    }

    /**
     * Closes manually the splash-screen if still open.
     */
    public void closeIfStillOpen() {
        if (Objects.nonNull(SPLASH_SCREEN_INSTANCE)) {
            SPLASH_SCREEN_INSTANCE.cancel(true);
        }
    }

    private Image getSplashImage(final String imagePath) {
        Image result = null;

        if (StringUtils.isNotBlank(imagePath)) {
            final URL dateImageURL = this.getClass().getResource(imagePath);

            if (Objects.nonNull(dateImageURL)) {
                result = Toolkit.getDefaultToolkit().getImage(dateImageURL);
            }
        }

        return result;
    }

    private static class BackgroundWorker extends SwingWorker<Void, Void> {

        // Configuration
        private static final String EVENT_PROGRESS_NAME = "progress";

        // Data
        private final int progressTime;
        private JProgressBar pb;
        private JDialog dialog;

        public BackgroundWorker(final String title, final String message, final Image image, final int progressTime) {
            this.progressTime = progressTime;

            addPropertyChangeListener(evt -> {
                if (EVENT_PROGRESS_NAME.equalsIgnoreCase(evt.getPropertyName())) {

                    // Configure popup
                    if (Objects.isNull(dialog)) {
                        final JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                        // Adds message to the popup
                        final JLabel messageLabel = ViewUtils.getDefaultTitleLabel(message);
                        final JPanel labelPanel = new JPanel();
                        labelPanel.add(messageLabel);
                        panel.add(labelPanel);

                        // Adds image to the popup
                        if (Objects.nonNull(image)) {
                            final JPanel imagePanel = new JPanel();
                            imagePanel.add(new JLabel(new ImageIcon(image)));
                            panel.add(imagePanel);
                        }

                        // Adds progress bar to the popup
                        pb = new JProgressBar();
                        final JPanel progressBarPanel = new JPanel();
                        progressBarPanel.add(pb);
                        panel.add(progressBarPanel);

                        // Popup configuration
                        dialog = new JDialog();
                        dialog.setTitle(title);
                        dialog.setLayout(new GridBagLayout());
                        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        dialog.add(panel);
                        dialog.pack();
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                    }

                    // Actualiza el progreso
                    pb.setValue(getProgress());
                }
            });
        }

        @Override
        protected void done() {
            if (Objects.nonNull(dialog)) {
                dialog.dispose();
            }
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (int index = 0; index < 100; index++) {
                setProgress(index);
                Thread.sleep(progressTime);
            }
            return null;
        }
    }
}
