package solutions;

import utils.FileReaderUtil;

import java.util.*;

/**
 * Created by campbell on 2015/12/17.
 */
/*
--- Day 9: All in a Single Night ---

Every year, Santa manages to deliver all of his presents in a single night.

This year, however, he has some new locations to visit; his elves have provided him the distances between every pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?

For example, given the following distances:

London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
The possible routes are therefore:

Dublin -> London -> Belfast = 982
London -> Dublin -> Belfast = 605
London -> Belfast -> Dublin = 659
Dublin -> Belfast -> London = 659
Belfast -> Dublin -> London = 605
Belfast -> London -> Dublin = 982
The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.

What is the distance of the shortest route?

Your puzzle answer was 251.

--- Part Two ---

The next year, just to show off, Santa decides to take the route with the longest distance instead.

He can still start and end at any two (different) locations he wants, and he still must visit each location exactly once.

For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.

What is the distance of the longest route?

Your puzzle answer was 898.
 */
public class Day9Solution extends BaseSolutions {

    public Day9Solution() {
        this(false);
    }

    public Day9Solution(boolean shouldSkip) {
        super(9, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String[] inputs = FileReaderUtil.readAllLines(getFilenameForDay(9));

        TravelStructure map = buildTravelMap(inputs);
        map.calculatePathsDistance();

        System.out.println(String.format("Shortest distance to reach all locations: %s; Start: %s; End: %s",
                map.getShortestDistance(), map.getShortestStart().getLocation(), map.getShortestEnd().getLocation()));
        System.out.println(String.format("Longest distance to reach all locations: %s; Start: %s; End: %s",
                map.getLongestDistance(), map.getLongestStart().getLocation(), map.getLongestEnd().getLocation()));
    }

    private TravelStructure buildTravelMap(String[] inputs) {
        TravelStructure map = new TravelStructure();

        for (String route : inputs) {
            map.parseRoute(route);
        }

        return map;
    }

    private class TravelStructure {
        List<TravelMap> allLocations;

        TravelMap currentStart, shortestStart, shortestEnd, longestStart, longestEnd;
        int shortestDistance, longestDistance;

        public TravelStructure() {
            allLocations = new ArrayList<>();
        }

        public void parseRoute(String route) {
            String[] components = parseInput(route);
            addToMap(components[0], components[1], components[2]);
        }

        private String[] parseInput(String route) {
            String[] components = new String[3];

            String[] assignmentSplit = route.split(" = ");
            components[2] = assignmentSplit[1];

            String[] locationSplit = assignmentSplit[0].split(" to ");
            components[0] = locationSplit[0];
            components[1] = locationSplit[1];

            return components;
        }

        private void addToMap(String start, String end, String distance) {
            goToLocation(start).getLinks().put(goToLocation(end), Integer.valueOf(distance));
            goToLocation(end).getLinks().put(goToLocation(start), Integer.valueOf(distance));
        }

        private TravelMap goToLocation(String start) {
            for (TravelMap location : allLocations) {
                if (location.getLocation().equals(start)) {
                    return location;
                }
            }
            // Location doesn't exist yet
            TravelMap newLocation = new TravelMap(start);
            allLocations.add(newLocation);

            return newLocation;
        }

        public void calculatePathsDistance() {
            shortestDistance = Integer.MAX_VALUE;
            longestDistance = 0;

            for (TravelMap startLocation : allLocations) {
                List<TravelMap> visited = new ArrayList<>();
                visited.add(startLocation);

                currentStart = startLocation;
                traverseRoute(0, startLocation, visited);
            }
        }

        private void traverseRoute(int distanceTraveled, TravelMap location, List<TravelMap> visited) {
            if (visited.size() == allLocations.size()) {
                handleEndOfRoute(distanceTraveled, location);
                return;
            }

            for (TravelMap nextLocation : location.getLinks().keySet()) {
                if (visited.contains(nextLocation)) {
                    continue;
                }

                List<TravelMap> visitedLocations = new ArrayList<>(visited);
                visitedLocations.add(nextLocation);

                traverseRoute(distanceTraveled + location.getLinks().get(nextLocation), nextLocation, visitedLocations);
            }
        }

        private void handleEndOfRoute(int distanceTraveled, TravelMap location) {
            if (distanceTraveled < shortestDistance) {
                shortestDistance = distanceTraveled;
                shortestEnd = location;
                shortestStart = currentStart;
            }

            if (distanceTraveled > longestDistance) {
                longestDistance = distanceTraveled;
                longestEnd = location;
                longestStart = currentStart;
            }
        }

        public TravelMap getShortestStart() {
            return shortestStart;
        }

        public TravelMap getShortestEnd() {
            return shortestEnd;
        }

        public TravelMap getLongestStart() {
            return longestStart;
        }

        public TravelMap getLongestEnd() {
            return longestEnd;
        }

        public int getShortestDistance() {
            return shortestDistance;
        }

        public int getLongestDistance() {
            return longestDistance;
        }
    }

    private class TravelMap {
        private String location;
        private Map<TravelMap, Integer> links;

        public TravelMap(String location) {
            this.location = location;
            links = new HashMap<>();
        }

        public String getLocation() {
            return location;
        }

        public Map<TravelMap, Integer> getLinks() {
            return links;
        }

        @Override
        public boolean equals(Object obj) {
            return getLocation().equals(((TravelMap)obj).getLocation());
        }

        @Override
        public String toString() {
            return "TravelMap{" +
                    "location='" + location + '\'' +
                    '}';
        }
    }
}
