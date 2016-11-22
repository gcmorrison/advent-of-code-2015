package solutions;

import utils.FileReaderUtil;

/**
 * Created by campbell on 2015/12/17.
 */
/*
--- Day 10: Elves Look, Elves Say ---

Today, the Elves are playing a game called look-and-say. They take turns making sequences by reading aloud the previous sequence and using that reading as the next sequence. For example, 211 is read as "one two, two ones", which becomes 1221 (1 2, 2 1s).

Look-and-say sequences are generated iteratively, using the previous value as input for the next step. For each step, take the previous value, and replace each run of digits (like 111) with the number of digits (3) followed by the digit itself (1).

For example:

1 becomes 11 (1 copy of digit 1).
11 becomes 21 (2 copies of digit 1).
21 becomes 1211 (one 2 followed by one 1).
1211 becomes 111221 (one 1, one 2, and two 1s).
111221 becomes 312211 (three 1s, two 2s, and one 1).
Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?

Your puzzle answer was 492982.

--- Part Two ---

Neat, right? You might also enjoy hearing John Conway talking about this sequence (that's Conway of Conway's Game of Life fame).

Now, starting again with the digits in your puzzle input, apply this process 50 times. What is the length of the new result?

Your puzzle answer was 6989950.
 */
public class Day10Solution extends BaseSolutions {

    public Day10Solution() {
        this(false);
    }

    public Day10Solution(boolean shouldSkip) {
        super(10, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String input = FileReaderUtil.readAllLines(getFilenameForDay(10))[0];

        for (int i = 0; i < 40; i++) {
            input = lookAndSay(input);
        }
        System.out.println("Length of result after 40 iterations: " + input.length());

        for (int i = 40; i < 50; i++) {
            input = lookAndSay(input);
        }
        System.out.println("Length of result after 50 iterations: " + input.length());

    }

    private String lookAndSay(String input) {
        StringBuilder sb = new StringBuilder();

        int currentIndex = 0;
        while(currentIndex < input.length()) {
            char c = input.charAt(currentIndex);

            int count = 1;
            while(currentIndex + count < input.length() && input.charAt(currentIndex + count) == c) {
                count++;
            }
            sb.append(count);
            sb.append(c);

            currentIndex = currentIndex + count;
        }

        return sb.toString();
    }
}
