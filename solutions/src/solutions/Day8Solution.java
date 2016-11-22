package solutions;

import utils.FileReaderUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by campbell on 2015/12/14.
 */
public class Day8Solution extends BaseSolutions {
    private int totalLiteralCount, totalMemoryCount;
    private int totalEncodedLiteralCount;

    public Day8Solution() {
        this(false);
    }

    public Day8Solution(boolean shouldSkip) {
        super(8, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String[] inputs = FileReaderUtil.readAllLines(getFilenameForDay(8));

//        String[] inputs = new String[]{
//                "\"\\\\\\x27\"",
//                "\"\\\\ab\"",
//                "\"ab\\\\\"",
//                "\"ab\\\\\\\"\"",
//                "\"\\\"\\\\ab\\\\\\\"\"",
//                "\"\"",
//                "\"abc\"",
//                "\"aaa\\\"aaa\"",
//                "\"\\x27\""
//        };
        parseInputs(inputs);

        System.out.println(String.format("Total size for file (%d - %d): %d", totalLiteralCount, totalMemoryCount, (totalLiteralCount - totalMemoryCount)));
        System.out.println(String.format("Total size for encoded file (%d - %d): %d", totalEncodedLiteralCount, totalLiteralCount, (totalEncodedLiteralCount - totalLiteralCount)));
    }

    private void parseInputs(String[] inputs) {
        totalLiteralCount = 0;
        totalMemoryCount = 0;

        totalEncodedLiteralCount = 0;

        for (String input : inputs) {
            input = input.trim();

            processCharacterCounts(input, false);
            input = getEncodedInput(input);
            processCharacterCounts(input, true);
        }
    }

    private void processCharacterCounts(String input, boolean isEncoded) {
        int currentLiteralCount = input.length();

        if (isEncoded) {
            this.totalEncodedLiteralCount += currentLiteralCount;
        } else {
            int backslashCount = getBackslashCount(input);
            String reducedInput = input.replaceAll("\\\\\\\\", "");

            int quoteCount = getQuoteCount(reducedInput);
            int hexCount = getHexCount(reducedInput);

            int currentMemoryCount = input.length() - backslashCount - quoteCount - (3 * hexCount) - 2;

            totalLiteralCount += currentLiteralCount;
            totalMemoryCount += currentMemoryCount;
        }
    }

    private int getBackslashCount(String input) {
        Pattern backslashPattern = Pattern.compile("\\\\\\\\");
        return countMatches(backslashPattern.matcher(input));
    }

    private int getQuoteCount(String input) {
        Pattern quotePattern = Pattern.compile("\\\\\"");
        return countMatches(quotePattern.matcher(input));
    }

    private int getHexCount(String input) {
        Pattern hexPattern = Pattern.compile("\\\\x[a-f0-9]{2}");
        return countMatches(hexPattern.matcher(input));
    }

    private int countMatches(Matcher matcher) {
        int count = 0;

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    private String getEncodedInput(String input) {
        String encoded = escapeEscapeChars(input);
        encoded = handleBodyQuote(encoded);
        encoded = handleLeadingQuote(encoded);
        encoded = handleTrailingQuote(encoded);

        return encoded;
    }

    private String escapeEscapeChars(String input) {
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == '\\') {
                sb.append("\\");
            }
            sb.append(c);
        }

        return sb.toString();
    }

    private String handleBodyQuote(String input) {
        Pattern quotePattern = Pattern.compile("\\\\\"[^$]");
        Matcher matcher = quotePattern.matcher(input);

        StringBuilder sb = new StringBuilder();
        int lastHead = 0, currentTail;

        while(matcher.find()) {
            currentTail = matcher.start();
            sb.append(input.substring(lastHead, currentTail)).append("\\").append(input.substring(currentTail, currentTail + 2));
            lastHead = currentTail + 2;
        }
        sb.append(input.substring(lastHead));

        return sb.toString();
    }

    private String handleLeadingQuote(String input) {
        return "\"" + input.replaceFirst("^\"", "\\\\\"");
    }

    private String handleTrailingQuote(String input) {
        return input.replaceFirst("\"$", "\\\\\"") + "\"";
    }
}
