package solutions;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by campbell on 2015/12/07.
 */
public abstract class BaseSolutions {
    protected static final String INPUT_BASE = "solutions/inputs/";

    private long startTime, endTime;
    private int day;
    private boolean shouldSkip;

    public BaseSolutions(int day) {
        this(day, false);
    }

    public BaseSolutions(int day, boolean shouldSkip) {
        this.day = day;
        this.shouldSkip = shouldSkip;
    }

    public void run() {
        startTime = System.currentTimeMillis();
        printHeader(day);

        if (!shouldSkip) {
            handleSolution();
        } else {
            System.out.println("Skipped day " + day);
        }

        printFooter();
    }

    protected String getFilenameFor(String filename) {
        return INPUT_BASE + filename + ".txt";
    }

    protected String getFilenameForDay(int day) {
        return getFilenameFor(String.format("day%d", day));
    }

    protected void printHeader(int day) {
        System.out.println("Day " + day + " Answers:");
    }

    protected void printFooter() {
        endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + getTimeString(startTime, endTime));
        System.out.println("-+------------------------------------------+-");
    }

    private String getTimeString(long startTime, long endTime) {
        long duration = endTime - startTime;

        StringBuilder sb = new StringBuilder();
        if (duration > 60000) {
            sb.append(Math.round(duration / 60000)).append("m ");
        }
        if (duration > 1000) {
            sb.append(Math.round(duration / 1000)).append("s ");
        }
        sb.append(duration % 1000).append("ms");

        return sb.toString();
    }

    protected abstract void handleSolution();
}
