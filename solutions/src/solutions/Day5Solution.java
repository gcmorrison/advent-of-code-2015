package solutions;

import utils.FileReaderUtil;

/**
 * Created by campbell on 2015/12/10.
 */
/*
--- Day 5: Doesn't He Have Intern-Elves For This? ---

Santa needs help figuring out which strings in his text file are naughty or nice.

A nice string is one with all of the following properties:

It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
For example:

ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...), a double letter (...dd...), and none of the disallowed substrings.
aaa is nice because it has at least three vowels and a double letter, even though the letters used by different rules overlap.
jchzalrnumimnmhp is naughty because it has no double letter.
haegwjzuvuyypxyu is naughty because it contains the string xy.
dvszwmarrgswjxmb is naughty because it contains only one vowel.
How many strings are nice?

Your puzzle answer was 255.

--- Part Two ---

Realizing the error of his ways, Santa has switched to a better model of determining whether a string is naughty or nice. None of the old rules apply, as they are all clearly ridiculous.

Now, a nice string is one with all of the following properties:

It contains a pair of any two letters that appears at least twice in the string without overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
It contains at least one letter which repeats with exactly one letter between them, like xyx, abcdefeghi (efe), or even aaa.
For example:

qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with exactly one letter between them (zxz).
xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between, even though the letters used by each rule overlap.
uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that appears twice.
How many strings are nice under these new rules?

Your puzzle answer was 55.
 */
public class Day5Solution extends BaseSolutions {
    public Day5Solution() {
        this(false);
    }

    public Day5Solution(boolean shouldSkip) {
        super(5, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String[] inputs = FileReaderUtil.readAllLines(getFilenameForDay(5));

        System.out.println("Number of strings that are nice according to old rules: " + getNiceCount_OldRules(inputs));
        System.out.println("Number of strings that are nice according to new rules: " + getNiceCount_NewRules(inputs));
    }

    private int getNiceCount_OldRules(String[] inputs) {
        int niceCount = 0;
        for (String input : inputs) {
            if (hasThreeVowels(input) && hasRepeatingLetter(input) && hasOnlyAllowedStrings(input)) {
                niceCount++;
            }
        }
        return niceCount;
    }

    private boolean hasThreeVowels(String input) {
        return input.matches("(.*[aeiou].*){3,}");
    }

    private boolean hasRepeatingLetter(String input) {
        boolean hasRepeating = false;

        int i = 0;
        while (!hasRepeating && i < input.length() - 1) {
            hasRepeating = input.charAt(i) == input.charAt(i + 1);
            i++;
        }

        return hasRepeating;
    }

    private boolean hasOnlyAllowedStrings(String input) {
        return !(input.contains("ab") || input.contains("cd") || input.contains("pq") || input.contains("xy"));
    }

    private int getNiceCount_NewRules(String[] inputs) {
        int niceCount = 0;

        for (String input : inputs) {
            if (hasPairOfRepeatingCharsWithNoOverlap(input) && hasOneRepeatingAfterOneBreak(input)) {
                niceCount++;
            }
        }

        return niceCount;
    }

    private boolean hasPairOfRepeatingCharsWithNoOverlap(String input) {
        boolean hasRepeating = false;

        char c1, c2;
        for (int i = 0; i < input.length() - 1; i++) {
            c1 = input.charAt(i);
            c2 = input.charAt(i + 1);

            if (input.lastIndexOf(String.format("%s%s", c1, c2)) - input.indexOf(String.format("%s%s", c1, c2)) > 1) {
                hasRepeating = true;
            }
        }
        return hasRepeating;
    }

    private boolean hasOneRepeatingAfterOneBreak(String input) {
        boolean hasRepeating = false;

        int i = 0;
        while (!hasRepeating && i < input.length() - 2) {
            hasRepeating = input.charAt(i) == input.charAt(i + 2);
            i++;
        }

        return hasRepeating;
    }
}
