package ir.maktab.quizmaker.base;

/**
 * @author Alireza.d.a
 */
public class CustomTimer {

    private final long start;

    /**
     * Initializes a new stopwatch.
     */
    public CustomTimer() {
        start = System.currentTimeMillis();
    }


    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in seconds) since the stopwatch was created
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start);
    }
}
