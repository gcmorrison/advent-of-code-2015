package solutions;

import utils.FileReaderUtil;

import java.util.HashMap;

/**
 * Created by campbell on 2015/12/08.
 */
/*
--- Day 3: Perfectly Spherical Houses in a Vacuum ---

Santa is delivering presents to an infinite two-dimensional grid of houses.

He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.

However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?

For example:

> delivers presents to 2 houses: one at the starting location, and one to the east.
^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
Your puzzle answer was 2565.

--- Part Two ---

The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.

Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.

This year, how many houses receive at least one present?

For example:

^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
Your puzzle answer was 2639.
 */
public class Day3Solution extends BaseSolutions {
    public Day3Solution() {
        this(false);
    }

    public Day3Solution(boolean shouldSkip) {
        super(3, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String instructions = FileReaderUtil.readAllLines(getFilenameForDay(3))[0];

        handleSoloSanta(instructions);
        handleDuoSantas(instructions);
    }

    private void handleSoloSanta(String instructions) {
        HashMap<Coordinate, Integer> presentsMap = new HashMap<>();

        int ns = 0, ew = 0;
        deliverPresent(ns, ew, presentsMap); // First house's delivery

        for (char nsew : instructions.toCharArray()) {
            switch (nsew) {
                case '^':
                    ns++;
                    break;
                case 'v':
                    ns--;
                    break;
                case '>':
                    ew++;
                    break;
                case '<':
                    ew--;
                    break;
            }
            deliverPresent(ns, ew, presentsMap);
        }

        int atLeastOne = countHousesWithAtLeast(1, presentsMap);
        System.out.println("Number of houses with at least one present while Santa was Solo: " + atLeastOne);
    }

    private void handleDuoSantas(String instructions) {
        HashMap<Coordinate, Integer> presentsMap = new HashMap<>();

        int santaNs = 0, santaEw = 0, roboNs = 0, roboEw = 0;
        boolean instructionForSanta = true; // Because santa first

        deliverPresent(santaNs, santaEw, presentsMap);
        deliverPresent(roboNs, roboEw, presentsMap);

        int ns = 0, ew = 0;
        for (char nsew : instructions.toCharArray()) {
            switch (nsew) {
                case '^':
                    ns = instructionForSanta ? ++santaNs : ++roboNs;
                    ew = instructionForSanta ? santaEw : roboEw;
                    break;
                case 'v':
                    ns = instructionForSanta ? --santaNs : --roboNs;
                    ew = instructionForSanta ? santaEw : roboEw;
                    break;
                case '>':
                    ew = instructionForSanta ? ++santaEw : ++roboEw;
                    ns = instructionForSanta ? santaNs : roboNs;
                    break;
                case '<':
                    ew = instructionForSanta ? --santaEw : --roboEw;
                    ns = instructionForSanta ? santaNs : roboNs;
                    break;
            }

            deliverPresent(ns, ew, presentsMap);
            instructionForSanta = !instructionForSanta;
        }

        int atLeastOne = countHousesWithAtLeast(1, presentsMap);
        System.out.println("Number of houses with at least one present with Santa and Robo-Santa: " + atLeastOne);
    }

    private int countHousesWithAtLeast(int minPresentCount, HashMap<Coordinate, Integer> presentsMap) {
        int count = 0;
        for (int presentCount : presentsMap.values()) {
            count = count + (presentCount >= minPresentCount ? 1 : 0);
        }
        return count;
    }

    private void deliverPresent(int ns, int ew, HashMap<Coordinate, Integer> presentsMap) {
        Coordinate coordinate = new Coordinate(ns, ew);
        if (!presentsMap.containsKey(coordinate)) {
            presentsMap.put(coordinate, 0);
        }

        presentsMap.put(coordinate, presentsMap.get(coordinate) + 1);
    }

    private class Coordinate {
        final int ns; // N/S coordinate
        final int ew; // E/W coordinate

        public Coordinate(int ns, int ew) {
            this.ns = ns;
            this.ew = ew;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (ew != that.ew) return false;
            return ns == that.ns;

        }

        @Override
        public int hashCode() {
            int result = ew;
            result = 31 * result + ns;
            return result;
        }
    }
}
