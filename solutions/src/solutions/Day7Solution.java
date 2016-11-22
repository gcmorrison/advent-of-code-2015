package solutions;

import utils.FileReaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by campbell on 2015/12/10.
 */
/*
--- Day 7: Some Assembly Required ---

This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately, little Bobby is a little under the recommended age range, and he needs help assembling the circuit.

Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535). A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal from one source, but can provide its signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.

The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires x and y to an AND gate, and then connect its output to wire z.

For example:

123 -> x means that the signal 123 is provided to wire x.
x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators for these gates.

For example, here is a simple circuit:

123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i
After it is run, these are the signals on the wires:

d: 72
e: 507
f: 492
g: 114
h: 65412
i: 65079
x: 123
y: 456
In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to wire a?

Your puzzle answer was 956.

--- Part Two ---

Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a). What new signal is ultimately provided to wire a?

Your puzzle answer was 40149.
 */
public class Day7Solution extends BaseSolutions {
    HashMap<String, Integer> wireMap;

    public Day7Solution() {
        this(false);
    }

    public Day7Solution(boolean shouldSkip) {
        super(7, shouldSkip);
    }

    @Override
    protected void handleSolution() {
        String[] inputs = FileReaderUtil.readAllLines(getFilenameForDay(7));

        System.out.println("Part 1: Signal to wire a: " + getSignalTo(inputs, "a"));
        System.out.println("Part 2: Signal to wire a: " + getSignalTo(inputs, "a", "14146 -> b", "956 -> b"));
    }

    private int getSignalTo(String[] inputs, String wireName) {
        wireMap = new HashMap<>();
        List<OperationParser> operationsList = new ArrayList<>();

        for (String input : inputs) {
            buildOperationsList(input, operationsList);
        }

        processOperationsList(operationsList, inputs.length);

        return wireMap.containsKey(wireName) ? wireMap.get(wireName) : -1;
    }

    private int getSignalTo(String[] inputs, String wireName, String operationToReplace, String newOperation) {
        wireMap = new HashMap<>();
        List<OperationParser> operationsList = new ArrayList<>();

        for (String input : inputs) {
            if (input.equals(operationToReplace)) {
                input = newOperation;
            }
            buildOperationsList(input, operationsList);
        }

        processOperationsList(operationsList, inputs.length);

        return wireMap.containsKey(wireName) ? wireMap.get(wireName) : -1;
    }

    private void buildOperationsList(String input, List<OperationParser> operationsList) {
        OperationParser operationParser = new OperationParser(input);

        if (operationParser.inputWire1 != null && operationParser.inputWire1.matches("\\d+")) {
            wireMap.put(operationParser.inputWire1, Integer.valueOf(operationParser.inputWire1));
        }
        if (operationParser.inputWire2 != null && operationParser.inputWire2.matches("\\d+")) {
            wireMap.put(operationParser.inputWire2, Integer.valueOf(operationParser.inputWire2));
        }

        switch (operationParser.op) {
            case ASSIGN_VALUE:
                handleAssignValue(operationParser);
                break;
            default:
                operationsList.add(operationParser);
        }
    }

    private void processOperationsList(List<OperationParser> operationsList, int numberOfInstructions) {
        List<Integer> handledOperations = new ArrayList<>();
        int iterationCount = 0, limit = 100000000;
        while (!operationsList.isEmpty() && iterationCount < limit) {
            iterationCount++;
            handledOperations.clear();

            int index = 0;
            for (OperationParser operation : operationsList) {
                if (handleParsedOperation(operation)) {
                    handledOperations.add(index);
                }
                index++;
            }

            // Remove indexes from back to front
            for (int i = handledOperations.size() - 1; i >= 0; i--) {
                operationsList.remove(handledOperations.get(i).intValue());
            }
        }

        System.out.println(String.format("It took %d iterations to handle %d instructions", iterationCount, numberOfInstructions));
    }

    private boolean handleParsedOperation(OperationParser operation) {
        switch (operation.op) {
            case ASSIGN_VALUE:
                return handleAssignValue(operation);
            case ASSIGN_WIRE:
                return handleAssignWire(operation);
            case AND:
            case OR:
                if (wireMap.containsKey(operation.inputWire1) && wireMap.containsKey(operation.inputWire2)) {
                    wireMap.put(operation.outputWire, calculate(wireMap.get(operation.inputWire1), wireMap.get(operation.inputWire2), operation.op));
                    return true;
                }
                break;
            case LSHIFT:
            case RSHIFT:
            case NOT:
                if (wireMap.containsKey(operation.inputWire1)) {
                    wireMap.put(operation.outputWire, calculate(wireMap.get(operation.inputWire1), operation.shiftValue, operation.op));
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean handleAssignValue(OperationParser operationParser) {
        wireMap.put(operationParser.outputWire, operationParser.outputWireValue);
        return true;
    }

    private boolean handleAssignWire(OperationParser operationParser) {
        if (wireMap.containsKey(operationParser.inputWire1)) {
            wireMap.put(operationParser.outputWire, wireMap.get(operationParser.inputWire1));
            return true;
        }
        return false;
    }

    private Integer calculate(Integer val1, Integer val2, OperationEnum operationEnum) {
        switch(operationEnum) {
            case ASSIGN_VALUE:
            case ASSIGN_WIRE:
                // Shouldn't happen
                break;
            case AND:
                return val1 & val2;
            case OR:
                return val1 | val2;
            case NOT:
                return val1 ^ 65535;
            case LSHIFT:
                return val1 << val2;
            case RSHIFT:
                return val1 >>> val2;
        }
        return -1;
    }

    private enum OperationEnum {
        ASSIGN_VALUE("->"),
        ASSIGN_WIRE("->"),
        AND("AND"),
        OR("OR"),
        NOT("NOT"),
        LSHIFT("LSHIFT"),
        RSHIFT("RSHIFT");

        private String operation;

        OperationEnum(String operation) {
            this.operation = operation;
        }
    }

    private class OperationParser {
        private String inputWire1, inputWire2, outputWire;
        private int shiftValue, outputWireValue;

        private OperationEnum op;

        public OperationParser(String input) {
            parse(input);
        }

        public void parse(String input) {
            String[] assignment = input.split(OperationEnum.ASSIGN_VALUE.operation);

            String gateOp = assignment[0].trim();
            String assignee = assignment[1].trim();

            if (gateOp.matches("\\d+")) {
                // This wire has an actual value
                op = OperationEnum.ASSIGN_VALUE;
                outputWire = assignee;
                outputWireValue = Integer.valueOf(gateOp);
                return;
            }

            if (gateOp.matches("[a-z]+")) {
                // This wire has the same value as another wire
                op = OperationEnum.ASSIGN_WIRE;
                outputWire = assignee;
                inputWire1 = gateOp;
                return;
            }

            for (OperationEnum operation : OperationEnum.values()) {
                if (operation == OperationEnum.ASSIGN_VALUE || operation == OperationEnum.ASSIGN_WIRE) {
                    // Skip these
                    continue;
                }

                if (gateOp.contains(operation.operation)) {
                    op = operation;
                    parseOperation(gateOp, assignee, operation);
                }
            }
        }

        private void parseOperation(String gateOp, String assignee, OperationEnum operation) {
            switch (operation) {

                case ASSIGN_VALUE:
                case ASSIGN_WIRE:
                    // Ignore
                    break;
                case AND:
                case OR:
                    parseAndOrOperations(gateOp, assignee, operation);
                    break;
                case NOT:
                    parseNotOperation(gateOp, assignee, operation);
                    break;
                case LSHIFT:
                case RSHIFT:
                    parseShiftOperations(gateOp, assignee, operation);
                    break;
            }
        }

        private void parseAndOrOperations(String gateOp, String assignee, OperationEnum operation) {
            String[] wires = gateOp.split(operation.operation);

            op = operation;
            inputWire1 = wires[0].trim();
            inputWire2 = wires[1].trim();
            outputWire = assignee;
        }

        private void parseNotOperation(String gateOp, String assignee, OperationEnum operation) {
            op = operation;
            inputWire1 = gateOp.replace(operation.operation, "").trim();
            outputWire = assignee;
        }

        private void parseShiftOperations(String gateOp, String assignee, OperationEnum operation) {
            String[] wires = gateOp.split(operation.operation);

            op = operation;
            inputWire1 = wires[0].trim();
            shiftValue = Integer.valueOf(wires[1].trim());
            outputWire = assignee;
        }
    }
}
