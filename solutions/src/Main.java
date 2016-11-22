import solutions.*;

/**
 * Created by campbell on 2015/12/07.
 */
public class Main {
    public static void runTest(BaseSolutions solution) {
        solution.run();
    }

    public static void main(String[] args) {
        runTest(new Day1Solution());
        runTest(new Day2Solution());
        runTest(new Day3Solution());
        runTest(new Day4Solution());
        runTest(new Day5Solution());
        runTest(new Day6Solution());
        runTest(new Day7Solution());
        runTest(new Day8Solution());
        runTest(new Day9Solution());
        runTest(new Day10Solution());
    }
}
