package io.github.squdan.swing.components.panel.splashscreen;

import io.github.squdan.swing.components.util.ViewUtils;
import lombok.NoArgsConstructor;
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
     * Returns builder to configure splash-screen.
     *
     * @return builder.
     */
    public static SplashscreenBuilder builder() {
        return new SplashscreenBuilder();
    }

    /**
     * Starts splash-screen.
     */
    public void start() {
        if (Objects.nonNull(SPLASH_SCREEN_INSTANCE)) {
            EventQueue.invokeLater(() -> SPLASH_SCREEN_INSTANCE.execute());
        }
    }

    /**
     * Closes manually the splash-screen if still open.
     */
    public void close() {
        if (Objects.nonNull(SPLASH_SCREEN_INSTANCE)) {
            SPLASH_SCREEN_INSTANCE.cancel(true);
        }
    }

    /**
     * Configures the splash-screen to show.
     *
     * @param title        to show.
     * @param message      to show.
     * @param image        to show.
     * @param progressTime time to increase 1% (milliseconds).
     */
    protected Splashscreen(final String title, final String message, final Image image, final Integer progressTime) {
        SPLASH_SCREEN_INSTANCE = new BackgroundWorker(title, message, image, progressTime);
    }

    @NoArgsConstructor
    public static class SplashscreenBuilder {

        // Data
        private String title = null;
        private String message = null;
        private Image image = null;
        private Integer timeDuration = null;

        public SplashscreenBuilder title(final String source) {
            this.title = source;
            return this;
        }

        public SplashscreenBuilder message(final String source) {
            this.message = source;
            return this;
        }

        public SplashscreenBuilder image(final String source) {
            this.image = getSplashImage(source);
            return this;
        }

        public SplashscreenBuilder image(final Image source) {
            this.image = source;
            return this;
        }

        public SplashscreenBuilder timeInSecconds(final int time) {
            this.timeDuration = (int) Math.ceil((double) time * 1000 / ((double) 100));
            return this;
        }

        public SplashscreenBuilder timeInMillis(final int time) {
            this.timeDuration = (int) Math.ceil((double) time / ((double) 100));
            return this;
        }

        public Splashscreen build() {
            return new Splashscreen(title, message, image, timeDuration);
        }
    }

    private static class BackgroundWorker extends SwingWorker<Void, Void> {

        // Configuration
        private static final String EVENT_PROGRESS_NAME = "progress";

        // Data
        private final Integer progressTime;
        private JProgressBar progressBar;
        private JDialog dialog;
        private boolean running = true;

        public BackgroundWorker(final String title, final String message, final Image image, final Integer progressTime) {
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
                        progressBar = new JProgressBar();
                        final JPanel progressBarPanel = new JPanel();
                        progressBarPanel.add(progressBar);
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
                    progressBar.setValue(getProgress());
                }
            });
        }

        @Override
        protected void done() {
            if (Objects.nonNull(dialog)) {
                dialog.dispose();
                running = false;
            }
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (Objects.nonNull(progressTime)) {
                for (int index = 0; index < 100; index++) {
                    setProgress(index);
                    Thread.sleep(progressTime);
                }
            } else {
                while(running) {
                    setProgress((getProgress() + 1) % 100);
                    Thread.sleep(5);
                }
            }

            return null;
        }
    }

    protected static Image getSplashImage(final String imagePath) {
        Image result = null;

        if (StringUtils.isNotBlank(imagePath)) {
            final URL dateImageURL = Splashscreen.class.getResource(imagePath);

            if (Objects.nonNull(dateImageURL)) {
                result = Toolkit.getDefaultToolkit().getImage(dateImageURL);
            }
        }

        return result;
    }
}
