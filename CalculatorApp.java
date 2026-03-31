import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;


abstract class AbstractCalculator {    //ABSTRACTION + Inheritance

    public abstract double calculate(double num1, double num2, String operation) throws ArithmeticException, IllegalArgumentException;
    public String getInfo() {
        return "Scientific Calculator Engine v2.0";
    }
}


class ScientificCalculator extends AbstractCalculator {      //Inheritance
    private LinkedList<String> history;
    private double lastResult;
    public ScientificCalculator() {
        this.history = new LinkedList<>();
        this.lastResult = 0.0;
        String[] basicOps = {"+", "-", "*", "/"};
        System.out.println("1D Array Demo: Basic ops initialized: " + String.join(", ", basicOps));
    }
    public ScientificCalculator(double initialValue) {
        this();
        this.lastResult = initialValue;
        System.out.println("Constructor Overloaded: Initial value set to " + initialValue);
    }


    public double add(int a, int b) {         //polymorphism
        return (double) a + b;
    }
    public double add(double a, double b) {        //polymorphism
        return a + b;
    }

    @Override
    public double calculate(double num1, double num2, String operation) throws ArithmeticException, IllegalArgumentException {
        double result; //Exception Handling (Logic)

        String logEntry = String.format("%.2f %s %.2f = ", num1, operation, num2);

        switch (operation) {
            case "+":
                result = add(num1, num2);
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    throw new ArithmeticException("Division by zero is not allowed."); //Exception Handling (Logic)
                }
                result = num1 / num2;
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }

        this.lastResult = result;
        history.add(logEntry + String.format("%.2f", result));
        return result;
    }


    public LinkedList<String> getHistory() {
        return history;
    }

    public double getLastResult() {
        return lastResult;
    }
    public String arrayListDemo(int n) {
        ArrayList<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                factors.add(i);
            }
        }
        return "Factors of " + n + ": " + factors.toString();
    }
    public String twoDArrayDemo(String constantName) {

        String[][] constants = {
                {"PI", "3.1415926535"}
        };

        for (int i = 0; i < constants.length; i++) {
            if (constants[i][0].equalsIgnoreCase(constantName)) {
                return constants[i][1];
            }
        }
        return "Constant not found.";
    }
}

class HistoryLogger {
    private static final String FILENAME = "calculator_history.txt";
    public void writeHistory(LinkedList<String> history) {                   // File writing
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILENAME))) {
            for (String entry : history) {
                pw.println(entry);
            }
            System.out.println("History saved to " + FILENAME);
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }
    public LinkedList<String> readHistory() {                      // File Reading
        LinkedList<String> history = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                history.add(line);
            }
            System.out.println("History loaded from " + FILENAME);
        } catch (FileNotFoundException e) {

            System.out.println("History file not found. Starting new history.");
        } catch (IOException e) {
            System.err.println("Error reading history: " + e.getMessage());
        }
        return history;
    }
}

public class CalculatorApp implements ActionListener {
    private final Color BG_MAIN = new Color(200, 230, 200);
    private final Color BG_DISPLAY = new Color(245, 255, 245);
    private final Color BTN_DIGIT = new Color(170, 210, 170);
    private final Color BTN_OPERATOR = new Color(0, 150, 136);
    private final Color BTN_UTILITY = new Color(130, 170, 130);
    private final Color TEXT_COLOR = new Color(30, 30, 30);
    private final JFrame frame;
    private final JTextField display;
    private final ScientificCalculator calculator;
    private final HistoryLogger logger;

    private String currentInput = "0";
    private String fullDisplay = "0";
    private String operation = "";
    private double firstOperand = 0;
    private boolean startNewNumber = true;
    private boolean errorState = false;

    private final String[] buttonLabels = {
            "C", "0", ".", "DEL",
            "7", "8", "9", "/",
            "4", "5", "6", "X",
            "1", "2", "3", "-",
            "^2", "sqrt", "log", "+",
            "PI", "Hist", "%", "="
    };

    public CalculatorApp() {

        this.calculator = new ScientificCalculator(0.0);
        this.logger = new HistoryLogger();


        this.calculator.getHistory().addAll(logger.readHistory());

        frame = new JFrame(" Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(BG_MAIN);
        frame.setLayout(new BorderLayout(10, 10));

        display = new JTextField(fullDisplay);
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.BOLD, 32));
        display.setHorizontalAlignment(SwingConstants.RIGHT);

        display.setBackground(BG_DISPLAY);
        display.setForeground(TEXT_COLOR);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        frame.add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();

        buttonPanel.setBackground(BG_MAIN);
        buttonPanel.setLayout(new GridLayout(6, 4, 15, 15));

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.addActionListener(this);

            if (label.matches("[0-9]|\\.")) {
                button.setBackground(BTN_DIGIT);
                button.setForeground(TEXT_COLOR);
            } else if (label.matches("[X/\\-+]")) {
                button.setBackground(BTN_OPERATOR);
                button.setForeground(Color.WHITE);
            } else if (label.equals("=")) {
                button.setBackground(BTN_OPERATOR.darker());
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(BTN_UTILITY);
                button.setForeground(Color.WHITE);
            }

            button.setBorderPainted(false);
            button.setFocusPainted(false);

            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(420, 600);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        errorState = false;

        switch (command) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case ".":
                handleInput(command);
                break;
            case "+":
            case "-":
            case "/":
                handleOperation(command);
                break;
            case "X":
                handleOperation("*");
                break;
            case "=":
                handleEquals();
                break;
            case "C":
                handleClear();
                break;
            case "DEL":
                handleDelete();
                break;
            case "PI":
                handleConstant("PI");
                break;
            case "sqrt":
            case "^2":
            case "log":
                handleScientificOp(command);
                break;
            case "Hist":
                showHistory();
                break;
            case "(":
            case ")":
                break;
        }

        display.setText(fullDisplay.isEmpty() ? "0" : fullDisplay);
    }

    private void handleInput(String input) {
        if (input.equals(".")) {
            if (currentInput.contains(".")) {
                return;
            }
            if (currentInput.isEmpty() || startNewNumber) {
                currentInput = "0.";
                startNewNumber = false;
            } else {
                currentInput += ".";
            }
        } else {
            if (startNewNumber) {
                currentInput = input.equals("0") ? "0" : input;
                startNewNumber = false;
            } else {
                if (currentInput.equals("0") && input.equals("0")) return;
                if (currentInput.equals("0") && !input.equals("0") && !currentInput.contains(".")) {
                    currentInput = input;
                } else {
                    currentInput += input;
                }
            }
        }

        if (operation.isEmpty() || startNewNumber) {
            fullDisplay = currentInput;
        } else {
            String opSymbol = operation.equals("*") ? "X" : operation;
            String firstOpString = formatDouble(firstOperand);
            fullDisplay = firstOpString + " " + opSymbol + " " + currentInput;
        }
    }

    private void handleOperation(String newOperation) {
        if (!operation.isEmpty() && !startNewNumber) {
            handleEquals();
            if (errorState) return;
        }

        try {
            if (currentInput.equals("0") && operation.isEmpty() && firstOperand != 0 && startNewNumber) {

            } else {
                firstOperand = Double.parseDouble(currentInput);
            }

            operation = newOperation;
            startNewNumber = true;
            currentInput = "0";

            String opSymbol = newOperation.equals("*") ? "X" : newOperation;
            String firstOpString = formatDouble(firstOperand);
            fullDisplay = firstOpString + " " + opSymbol + " ";

        } catch (NumberFormatException ex) {
            display.setText("Error: Invalid number");
            handleClear();
        }
    }

    private void handleEquals() {
        if (operation.isEmpty()) return;

        try {
            double secondOperand = startNewNumber ? firstOperand : Double.parseDouble(currentInput);

            double result = calculator.calculate(firstOperand, secondOperand, operation);

            currentInput = String.valueOf(result);
            firstOperand = result;
            operation = "";
            startNewNumber = true;

            fullDisplay = formatDouble(result);

            logger.writeHistory(calculator.getHistory());

            if (secondOperand % 1 == 0) {
                System.out.println(calculator.arrayListDemo((int)secondOperand));
            }


        } catch (ArithmeticException | IllegalArgumentException ex) {   // Exception handling by catching 2 types of exception
            display.setText("Error: " + ex.getMessage());
            fullDisplay = "Error";
            errorState = true;
            handleClear();
        }
    }

    private void handleClear() {
        currentInput = "0";
        fullDisplay = "0";
        operation = "";
        firstOperand = 0;
        startNewNumber = true;
        errorState = false;
    }

    private void handleDelete() {
        if (startNewNumber || currentInput.equals("0")) return;

        if (currentInput.length() > 1) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        } else {
            currentInput = "0";
            startNewNumber = true;
        }

        if (operation.isEmpty()) {
            fullDisplay = currentInput;
        } else {
            String opSymbol = operation.equals("*") ? "X" : operation;
            String firstOpString = formatDouble(firstOperand);
            fullDisplay = firstOpString + " " + opSymbol + " " + currentInput;
        }
    }

    private void handleConstant(String constant) {
        String value = calculator.twoDArrayDemo(constant);
        if (!value.equals("Constant not found.")) {
            currentInput = value;
            fullDisplay = currentInput;
            startNewNumber = true;
            operation = "";
        }
    }

    private void handleScientificOp(String op) {
        if (currentInput.equals("0") && !op.equals("sqrt") && !op.equals("log")) return;

        try {
            double operand = Double.parseDouble(currentInput);
            double result = 0;
            String logEntry = "";

            if (op.equals("sqrt")) {
                if (operand < 0) throw new ArithmeticException("Cannot sqrt negative number."); //Exception Handling (Logic)
                result = Math.sqrt(operand);
                logEntry = String.format("sqrt(%.2f) = %.2f", operand, result);
            } else if (op.equals("log")) {
                if (operand <= 0) throw new ArithmeticException("Cannot log non-positive number."); //Exception Handling (Logic)
                result = Math.log(operand);
                logEntry = String.format("log(%.2f) = %.2f", operand, result);
            } else if (op.equals("^2")) {
                result = operand * operand;
                logEntry = String.format("sqr(%.2f) = %.2f", operand, result);
            }

            currentInput = String.valueOf(result);
            fullDisplay = formatDouble(result);
            calculator.getHistory().add(logEntry);
            logger.writeHistory(calculator.getHistory());
            startNewNumber = true;
            operation = "";

        } catch (ArithmeticException ex) {
            display.setText("Error: " + ex.getMessage());
            fullDisplay = "Error";
            errorState = true;
            handleClear();
        } catch (NumberFormatException ex) {
            display.setText("Error: Invalid input");
            fullDisplay = "Error";
            errorState = true;
            handleClear();
        }
    }

    private void showHistory() {
        StringBuilder sb = new StringBuilder();
        for (String entry : calculator.getHistory()) {
            sb.append(entry).append("\n");
        }

        JOptionPane.showMessageDialog(frame,
                sb.length() > 0 ? sb.toString() : "No history recorded yet.",
                "Calculation History",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatDouble(double value) {
        String s = String.valueOf(value);
        if (s.endsWith(".0")) {
            return s.substring(0, s.length() - 2);
        }
        return s;
    }

    public void start() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalculatorApp().start();
        });
    }
}
