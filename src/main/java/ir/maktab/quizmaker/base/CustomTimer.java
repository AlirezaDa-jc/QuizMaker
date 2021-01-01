package ir.maktab.quizmaker.base;

/**
 * @author Alireza.d.a
 */
public class CustomTimer {

    private final long start;
    private final long examId;

    public CustomTimer(long examId) {
        start = System.currentTimeMillis();
        this.examId = examId;
    }

    public long getExamId() {
        return examId;
    }

    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start);
    }
}
