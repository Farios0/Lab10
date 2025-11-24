package it.unibo.mvc;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import it.unibo.mvc.utilities.ParametersReader;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {
    private static final int MIN = findParameter(0);
    private static final int MAX = findParameter(1);
    private static final int ATTEMPTS = findParameter(2);
    private final DrawNumber model;
    private final List<DrawNumberView> views;
    
    /**
     * @param views
     *            the views to attach
     */
    public DrawNumberApp(final DrawNumberView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }
        this.model = new DrawNumberImpl(MIN, MAX, ATTEMPTS);
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view: views) {
                view.result(result);
            }
        } catch (IllegalArgumentException e) {
            for (final DrawNumberView view: views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0);
    }

    private static int findParameter(int i) {
        try (final ParametersReader reader = new ParametersReader();) {
            if(i < 0 || i > 2) {
                throw new IllegalArgumentException();
            }
            int counter = 0;
            for(var eger : reader){
                if(i == counter) { return eger; }
                counter++;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param args
     *            ignored
     * @throws FileNotFoundException 
     */
    public static void main(final String... args) throws FileNotFoundException {
        new DrawNumberApp(new DrawNumberViewImpl());
    }

}
