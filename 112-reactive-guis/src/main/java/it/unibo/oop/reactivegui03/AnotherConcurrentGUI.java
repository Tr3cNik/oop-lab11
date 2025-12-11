package it.unibo.oop.reactivegui03;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unibo.oop.JFrameUtil;

/**
 * Third experiment with reactive gui.
 */
public final class AnotherConcurrentGUI extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnotherConcurrentGUI.class);
    private static final int THREAD_TOTAL_TIME = 10_000;
    private final JLabel display = new JLabel();

    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    private final transient Agent agent = new Agent();

    /**
     * Builds a new CGUI.
     */
    public AnotherConcurrentGUI() { } // NOPMD

    /**
     * Set the view of the GUI.
     */
    public void setGUI() {
        JFrameUtil.dimensionJFrame(this);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);

        /*
         * Create the counter agent and start it. This is actually not so good:
         * thread management should be left to
         * java.util.concurrent.ExecutorService
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(THREAD_TOTAL_TIME);
                    SwingUtilities.invokeAndWait(AnotherConcurrentGUI.this::stoppedCounting);
                } catch (InterruptedException | InvocationTargetException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }).start();
        new Thread(agent).start();
        /*
         * Register a listener that start counting upwards
         */
        up.addActionListener(e -> agent.upCounter());
        /*
         * Register a listener that start counting downwards
         */
        down.addActionListener(e -> agent.downCounter());
        /*
         * Register a listener that stops it
         */
        stop.addActionListener(e -> {
            this.stoppedCounting();
        });
    }

    private void stoppedCounting() {
        agent.stopCounting();
            up.setEnabled(false);
            down.setEnabled(false);
            stop.setEnabled(false);
    }

    /*
     * The counter agent is implemented as a nested class. This makes it
     * invisible outside and encapsulated.
     */
    private final class Agent implements Runnable {
        /*
         * Stop is volatile to ensure visibility. Look at:
         *
         * http://archive.is/9PU5N - Sections 17.3 and 17.4
         *
         * For more details on how to use volatile:
         *
         * http://archive.is/4lsKW
         *
         */
        private volatile boolean stop;
        private boolean upper = true;
        private int counter;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    // The EDT doesn't access `counter` anymore, it doesn't need to be volatile
                    final var nextText = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.display.setText(nextText));
                    if (upper) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }

        /**
         * External command to stop counting.
         */
        public void stopCounting() {
            this.stop = true;
        }

        private void upCounter() {
            this.upper = true;
        }

        private void downCounter() {
            this.upper = false;
        }
    }
}
