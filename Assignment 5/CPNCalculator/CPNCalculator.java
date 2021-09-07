// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 5
 * Name: Todd Wellwood
 * Username: wellwotodd
 * ID: 300529406
 */

import ecs100.*;

import java.util.*;
import java.io.*;
import java.awt.Color;

/**
 * Calculator for Cambridge-Polish Notation expressions
 * (see the description in the assignment page)
 * User can type in an expression (in CPN) and the program
 * will compute and print out the value of the expression.
 * The template provides the method to read an expression and turn it into a tree.
 * You have to write the method to evaluate an expression tree.
 * and also check and report certain kinds of invalid expressions
 */

public class CPNCalculator {

    // Introduce two constants
    final double PI = Math.PI;
    final double E = Math.E;

    /**
     * Setup GUI then run the calculator
     */
    public static void main(String[] args) {
        CPNCalculator calc = new CPNCalculator();
        calc.setupGUI();
        calc.runCalculator();
    }

    /**
     * Setup the gui
     */
    public void setupGUI() {
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    /**
     * Run the calculator:
     * loop forever:  (a REPL - Read Eval Print Loop)
     * - read an expression,
     * - evaluate the expression,
     * - print out the value
     * Invalid expressions could cause errors when reading or evaluating
     * The try-catch prevents these errors from crashing the programe -
     * the error is caught, and a message printed, then the loop continues.
     */
    public void runCalculator() {
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        while(true) {
            UI.println();
            try {
                GTNode<ExpElem> expr = readExpr();
                double value = evaluate(expr);
                UI.println(" -> " + value);
            } catch(Exception e) {
                UI.println("Something went wrong! " + e);
            }
        }
    }

    /**
     * Evaluate an expression and return the value
     * Returns Double.NaN if the expression is invalid in some way.
     * If the node is a number
     * => just return the value of the number
     * or it is a named constant
     * => return the appropriate value
     * or it is an operator node with children
     * => evaluate all the children and then apply the operator.
     */
    public double evaluate(GTNode<ExpElem> expr) {
        if(expr == null) { // if nothing entered
            return Double.NaN;
        }
        ExpElem element = expr.getItem();

        //Return if any type of number
        if(element.operator.equals("#")) return element.value; // if number return number
        if(element.operator.equals("PI")) return PI;// if "PI" return 3.14....
        if(element.operator.equals("E")) return E; // if "E" return math.E

        if(expr.numberOfChildren() > 0) {
            // if user entered in things to actually compute
            if(element.operator.equals("+")) {
                double returningInt = evaluate(expr.getChild(0)); // get first element
                expr.removeChild(0); // remove it
                for(GTNode<ExpElem> currentElement : expr) {
                    returningInt += evaluate(currentElement); // add all the rest to it
                }
                return returningInt; // return
            }
            else if(element.operator.equals("-")) { // Subtraction
                double returningInt = evaluate(expr.getChild(0));
                expr.removeChild(0);
                for(GTNode<ExpElem> currentElement : expr) {
                    returningInt -= evaluate(currentElement);
                }
                return returningInt;
            }
            else if(element.operator.equals("*")) { // Multiplication
                double returningInt = evaluate(expr.getChild(0));
                expr.removeChild(0);
                for(GTNode<ExpElem> currentElement : expr) {
                    returningInt *= evaluate(currentElement);
                }
                return returningInt;
            }

            else if(element.operator.equals("/")) { // Division
                double returningInt = evaluate(expr.getChild(0));
                expr.removeChild(0);
                for(GTNode<ExpElem> currentElement : expr) {
                    //if(evaluate(currentElement) != 0){ // Java handles / 0 error automatically aparntly
                    returningInt /= evaluate(currentElement);
                    //}
                }
                return returningInt;
            }
            else if(element.operator.equals("avg")) { // Average
                double returningInt = evaluate(expr.getChild(0));
                expr.removeChild(0);
                for(GTNode<ExpElem> currentElement : expr) {
                    returningInt += evaluate(currentElement); // add all of them up
                }
                return returningInt / (expr.numberOfChildren() + 1); // +1 due to the first child being removed.
            }
            else if(element.operator.equals("^")) { // Power of
                if(expr.numberOfChildren() == 2) {
                    return Math.pow(evaluate(expr.getChild(0)), evaluate(expr.getChild(1))); // if only two operatands
                }
                else if(expr.numberOfChildren() > 2) { // print if too many
                    UI.println("Too many operands for ^");
                    return Double.NaN;
                }
                else {
                    UI.println("Too few operands for ^"); // print if too few
                    return Double.NaN;
                }
            }
            else if(element.operator.equals("log")) {
                if(expr.numberOfChildren() == 1) { // if only to log of base 10
                    return Math.log10(evaluate(expr.getChild(0)));
                }
                else if(expr.numberOfChildren() == 2) {  //  log to base of other input
                    return Math.log(evaluate(expr.getChild(0))) / Math.log(evaluate(expr.getChild(1)));
                }
                else if(expr.numberOfChildren() < 2) {
                    UI.println("Too many operands for Log");
                    return Double.NaN;
                }
                else {
                    UI.println("Too few operands for Log");
                    return Double.NaN;
                }
            }
            else if(element.operator.equals("ln")) { // Log natural
                if(expr.numberOfChildren() == 1) {
                    return Math.log(evaluate(expr.getChild(0)));  // Standard log
                }
                UI.println("Too many or Two few operands for ln");
                return Double.NaN;
            }
            else if(element.operator.equals("sqrt")) {
                if(expr.numberOfChildren() == 1) { // as long as only one operands
                    return Math.sqrt(evaluate(expr.getChild(0))); // return sqrt
                }
                else {
                    UI.println("To many operands for sqrt");
                }
            }
            else if(element.operator.equals("sin")) {
                if(expr.numberOfChildren() == 1) {
                    return Math.sin(evaluate(expr.getChild(0))); // return sin
                }
                else {
                    UI.println("To many operands for sin");
                }
            }
            else if(element.operator.equals("cos")) {
                if(expr.numberOfChildren() == 1) {
                    return Math.cos(evaluate(expr.getChild(0))); // return cos
                }
                else {
                    UI.println("To many operands for cos");
                }
            }
            else if(element.operator.equals("tan")) {
                if(expr.numberOfChildren() == 1) {
                    return Math.tan(evaluate(expr.getChild(0))); // return tan
                }
                else {
                    UI.println("To many operands for tan");
                }
            }
            else if(element.operator.equals("dist")) {
                if(expr.numberOfChildren() == 4) {// 2D
                    double x1 = evaluate(expr.getChild(0));
                    double y1 = evaluate(expr.getChild(1));

                    double x2 = evaluate(expr.getChild(2));
                    double y2 = evaluate(expr.getChild(3));
                    // for getting euclidan distance
                    double xTotal = Math.abs(x1 - x2);
                    double yTotal = Math.abs(y1 - y2);
                    return Math.sqrt((xTotal) * (xTotal) + (yTotal) * (yTotal)); // taken from my trains program
                }
                else if(expr.numberOfChildren() == 6) { //3D
                    double x1 = evaluate(expr.getChild(0));
                    double y1 = evaluate(expr.getChild(1));
                    double z1 = evaluate(expr.getChild(2));
                    double x2 = evaluate(expr.getChild(3));
                    double y2 = evaluate(expr.getChild(4));
                    double z2 = evaluate(expr.getChild(5));
                    // asked my mathamatics major friend for help with this one (They dont know how to program but explained the math to me)
                    return Math.pow((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2) * 1.0), 0.5);
                }
                else {
                    UI.println("To few or two many operands for distance");
                }
            }
        }

        UI.println(" The operator " + element.operator + " is invalid"); // else invalid operator

        return Double.NaN; // so return special double NAN
    }

    /**
     * Reads an expression from the user and constructs the tree.
     */
    public GTNode<ExpElem> readExpr() {
        String expr = UI.askString("expr:");

        /** CHALLANGE **/
        String copyOfExpr = new String(expr); // make copy for point by refrence avoidance
        Scanner bracketChecker = new Scanner(expr);
        Stack<String> bracketsStack = new Stack<String>(); // stack for checking tags.
        boolean valid = true; // starts valid
        boolean somethingInside = false;
        boolean lastOpening = false;
        while(bracketChecker.hasNext()) { // for all tags
            String token = bracketChecker.next();
            if(token.contains("(") || token.contains(")")) {
                if(token.contains("(")) {
                    bracketsStack.push(token); // add to stack
                    lastOpening = true; // last tag was opening
                }
                else {
                    if(somethingInside = false || bracketsStack.isEmpty() || lastOpening == true) { // if last tag was opening that means empty brackets, or if
                        valid = false;
                    }
                    else {
                        bracketsStack.pop(); // get rid of closing tag
                        somethingInside = false; // reset the something inside
                    }
                }
            }
            else { // if it is not an opening or  closing
                somethingInside = true; // that means something is inside
                lastOpening = false; // the last tag was not an opening tag
            }
        }
        if(!bracketsStack.isEmpty()) { // final check, is anything left inside? If so invalid
            valid = false;
        }
        if(valid == true) { // if valid go ahead
            return readExpr(new Scanner(expr));   // the recursive reading method
        }
        else {
            UI.println("Invalid Brackets"); // otherwise print invalid
        }
        return null; // and return null since was not valid

    }

    /**
     * Recursive helper method.
     * Uses the hasNext(String pattern) method for the Scanner to peek at next token
     */
    public GTNode<ExpElem> readExpr(Scanner sc) {
        if(sc.hasNextDouble()) {                     // next token is a number: return a new node
            return new GTNode<ExpElem>(new ExpElem(sc.nextDouble()));
        }
        else if(sc.hasNext("\\(")) {                 // next token is an opening bracket
            sc.next();                                // read and throw away the opening '('
            ExpElem opElem = new ExpElem(sc.next());  // read the operator
            GTNode<ExpElem> node = new GTNode<ExpElem>(opElem);  // make the node, with the operator in it.
            while(!sc.hasNext("\\)")) {              // loop until the closing ')'
                GTNode<ExpElem> child = readExpr(sc); // read each operand/argument
                node.addChild(child);                 // and add as a child of the node
            }
            sc.next();                                // read and throw away the closing ')'
            return node;
        }
        else {                                        // next token must be a named constant (PI or E)
            // make a token with the name as the "operator"
            return new GTNode<ExpElem>(new ExpElem(sc.next()));
        }
    }

}
