# Calculator-application-on-JAVA
# Java Scientific Calculator Application

## Overview
This project is a GUI-based Scientific Calculator developed in Java using Object-Oriented Programming principles and Java Swing. It performs both basic and advanced mathematical operations while maintaining a calculation history using file handling.

The project demonstrates core programming concepts such as abstraction, inheritance, polymorphism, exception handling, and data structures.

## Features

### Basic Operations
- Addition
- Subtraction
- Multiplication
- Division

### Scientific Functions
- Square (x²)
- Square Root (√)
- Logarithm (log)
- Constant (PI)

### Utility Features
- Delete (DEL)
- Clear (C)
- Calculation history display
- History saved to file (`calculator_history.txt`)

## Technologies Used

- Java
- Java Swing (GUI)
- AWT (UI components)
- File I/O (BufferedReader, FileWriter)
- Data Structures:
  - LinkedList (history)
  - ArrayList (factor calculation)

## Object-Oriented Concepts Implemented

- **Abstraction**
  - Abstract class `AbstractCalculator`

- **Inheritance**
  - `ScientificCalculator` extends `AbstractCalculator`

- **Polymorphism**
  - Method overloading (`add(int, int)` and `add(double, double)`)

- **Encapsulation**
  - Controlled access to calculator data and history

- **Exception Handling**
  - Handles division by zero, invalid inputs, and mathematical errors

## Project Structure

```text
src/
└── CalculatorApp.java
