package solutions;

import utils.FileReaderUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by campbell on 2015/12/08.
 */
/*
--- Day 4: The Ideal Stocking Stuffer ---

Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically forward-thinking little girls and boys.

To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.

For example:

If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
Your puzzle answer was 254575.

--- Part Two ---

Now find one that starts with six zeroes.

Your puzzle answer was 1038736.
 */
public class Day4Solution extends BaseSolutions {
    public Day4Solution() {
        this(false);
    }

    public Day4Solution(boolean shouldSkip) {
        super(4, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String input = FileReaderUtil.readAllLines(getFilenameForDay(4))[0];
        doForInput(input, 5);
        doForInput(input, 6);
    }

    private void doForInput(String input, int leadingZeroes) {
        String expectedPrefix = buildZeroesPrefix(leadingZeroes);
        int lowestNumber = findLowestNumberForInput(input, expectedPrefix);
        System.out.println(String.format("Lowest number for '%s' to create hash with %d leading zeroes: %d", input, leadingZeroes, lowestNumber));
    }

    private String buildZeroesPrefix(int leadingZeroes) {
        StringBuilder sb = new StringBuilder(leadingZeroes);
        for (int i = 0; i < leadingZeroes; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

    private int findLowestNumberForInput(String input, String expectedPrefix) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0;
        }

        String appendedInput;
        byte[] inputInBytes;
        String hash = "";
        int numberSuffix = 0;
        while (!hash.startsWith(expectedPrefix)) {
            numberSuffix++;
            appendedInput = input + String.valueOf(numberSuffix);
            try {
                inputInBytes = appendedInput.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                break;
            }
            hash = byteArrayToHexString(md.digest(inputInBytes));
        }

        return numberSuffix;
    }

    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length);

        for (byte val : bytes) {
            sb.append(String.format("%02x", val));
        }
        return sb.toString();
    }
}
