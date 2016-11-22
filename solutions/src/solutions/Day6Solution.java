package solutions;

import utils.FileReaderUtil;

/**
 * Created by campbell on 2015/12/10.
 */
/*
--- Day 6: Probably a Fire Hazard ---

Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to deploy one million lights in a 1000x1000 grid.

Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the ideal lighting configuration.

Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square. The lights all start turned off.

To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you in order.

For example:

turn on 0,0 through 999,999 would turn on (or leave on) every light.
toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
After following the instructions, how many lights are lit?

Your puzzle answer was 543903.

--- Part Two ---

You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.

The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.

The phrase turn on actually means that you should increase the brightness of those lights by 1.

The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.

The phrase toggle actually means that you should increase the brightness of those lights by 2.

What is the total brightness of all lights combined after following Santa's instructions?

For example:

turn on 0,0 through 0,0 would increase the total brightness by 1.
toggle 0,0 through 999,999 would increase the total brightness by 2000000.
Your puzzle answer was 14687245.
 */
public class Day6Solution extends BaseSolutions {
    public Day6Solution() {
        this(false);
    }

    public Day6Solution(boolean shouldSkip) {
        super(6, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String[] inputs = FileReaderUtil.readAllLines(getFilenameForDay(6));

        System.out.println("Part 1: Number of lights lit: " + getLightsLitForDimension(inputs, 1000, 1000));
        System.out.println("Part 2: Light brightness: " + getLightsBrightnessForDimension(inputs, 1000, 1000));
    }

    private int getLightsLitForDimension(String[] inputs, int gridWidth, int gridLength) {
        boolean[][] grid = new boolean[gridWidth][gridLength];

        Instruction instruction = new Instruction();
        for (String input : inputs) {
            instruction.parse(input);
            applyLitInstruction(grid, instruction);
        }

        return countLightsOn(grid, gridWidth, gridLength);
    }

    private void applyLitInstruction(boolean[][] grid, Instruction instruction) {
        for (int w = instruction.widthFrom; w <= instruction.widthTo; w++) {
            for (int l = instruction.lengthFrom; l <= instruction.lengthTo; l++) {
                boolean currentVal = grid[w][l];
                switch (instruction.action) {
                    case ON:
                        currentVal = true;
                        break;
                    case OFF:
                        currentVal = false;
                        break;
                    case TOGGLE:
                        currentVal = !currentVal;
                        break;
                }
                grid[w][l] = currentVal;
            }
        }
    }

    private int countLightsOn(boolean[][] grid, int gridWidth, int gridLength) {
        int lit = 0;
        for (int w = 0; w < gridWidth; w++) {
            for (int l = 0; l < gridLength; l++) {
                lit = lit + (grid[w][l] ? 1 : 0);
            }
        }
        return lit;
    }

    private int getLightsBrightnessForDimension(String[] inputs, int gridWidth, int gridLength) {
        byte[][] grid = new byte[gridWidth][gridLength];

        Instruction instruction = new Instruction();
        for (String input : inputs) {
            instruction.parse(input);
            applyBrightnessInstruction(grid, instruction);
        }

        return countLightBrightness(grid, gridWidth, gridLength);
    }

    private void applyBrightnessInstruction(byte[][] grid, Instruction instruction) {
        for (int w = instruction.widthFrom; w <= instruction.widthTo; w++) {
            for (int l = instruction.lengthFrom; l <= instruction.lengthTo; l++) {
                byte currentVal = grid[w][l];
                switch (instruction.action) {
                    case ON:
                        currentVal++;
                        break;
                    case OFF:
                        currentVal = (byte)(currentVal == 0 ? currentVal : currentVal - 1);
                        break;
                    case TOGGLE:
                        currentVal += 2;
                        break;
                }
                grid[w][l] = currentVal;
            }
        }
    }

    private int countLightBrightness(byte[][] grid, int gridWidth, int gridLength) {
        int brightness = 0;
        for (int w = 0; w < gridWidth; w++) {
            for (int l = 0; l < gridLength; l++) {
                brightness = brightness + grid[w][l];
            }
        }
        return brightness;
    }

    private enum ActionEnum {
        ON("turn on"),
        OFF("turn off"),
        TOGGLE("toggle");

        private String actionString;

        ActionEnum(String actionString) {
            this.actionString = actionString;
        }
    }

    private class Instruction {
        int widthFrom, widthTo, lengthFrom, lengthTo;
        ActionEnum action;

        public void parse(String input) {
            resetValues();

            for (ActionEnum action : ActionEnum.values()) {
                if (input.startsWith(action.actionString)) {
                    this.action = action;
                    input = input.replace(action.actionString, "").trim();
                    break;
                }
            }

            input = input.replace("through", ",");
            String[] coordinates = input.split(",");
            widthFrom = Integer.valueOf(coordinates[0].trim());
            lengthFrom = Integer.valueOf(coordinates[1].trim());
            widthTo = Integer.valueOf(coordinates[2].trim());
            lengthTo = Integer.valueOf(coordinates[3].trim());
        }

        private void resetValues() {
            widthFrom = 0;
            widthTo = 0;
            lengthFrom = 0;
            lengthTo = 0;
            action = null;
        }
    }
}
