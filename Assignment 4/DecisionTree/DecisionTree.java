// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 4
 * Name: Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

/**
 * Implements a decision tree that asks a user yes/no questions to determine a decision.
 * Eg, asks about properties of an animal to determine the type of animal.
 * 
 * A decision tree is a tree in which all the internal nodes have a question, 
 * The answer to the question determines which way the program will
 *  proceed down the tree.  
 * All the leaf nodes have the decision (the kind of animal in the example tree).
 *
 * The decision tree may be a predermined decision tree, or it can be a "growing"
 * decision tree, where the user can add questions and decisions to the tree whenever
 * the tree gives a wrong answer.
 *
 * In the growing version, when the program guesses wrong, it asks the player
 * for another question that would help it in the future, and adds it (with the
 * correct answers) to the decision tree. 
 *
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.Color;

public class DecisionTree {

    public DTNode theTree;    // root of the decision tree;   
    Set <String> savingSet;
    /**
     * Setup the GUI and make a sample tree
     */
    public static void main(String[] args){
        DecisionTree dt = new DecisionTree();
        dt.setupGUI();
        dt.loadTree("sample-animal-tree.txt");
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Load Tree", ()->{loadTree(UIFileChooser.open("File with a Decision Tree"));});
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Run Tree", this::runTree);
        UI.addButton("Grow Tree", this::growTree);
        UI.addButton("Save Tree", this::saveTree);  // for completion
        UI.addButton("Draw Tree", this::drawTree);  // for challenge
        UI.addButton("Reset", ()->{loadTree("sample-animal-tree.txt");});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.5);
    }

    /**  
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree, and then
     * its "no" subtree.
     * Each node should be indented by how deep it is in the tree.
     * Needs a recursive "helper method" which is passed a node and an indentation string.
     *  (The indentation string will be a string of space characters)
     */
    public void printTree(){
        UI.clearText();
        if(theTree != null){ //if root isnt null
            UI.println(theTree.getText() + "?"); // print root
            recursivePrintTree(theTree,"");   // then call recursive
        }
    }

    public void recursivePrintTree(DTNode currentNode,String spaceCharacters){
        spaceCharacters += "    "; // increment tabing
        if(currentNode.getYes() != null){ // if yes node is not null
            if(currentNode.isAnswer()){ // if it is an answer (both are just null checks ^);
                UI.println(spaceCharacters+"Yes:" +currentNode.getYes().getText()); // print as answer
            }
            else{
                UI.println(spaceCharacters+"Yes:" +currentNode.getYes().getText()+"?"); // print as question
            }
            recursivePrintTree(currentNode.getYes(),spaceCharacters);
        }
        // Same as above but for no
        if(currentNode.getNo() != null){     
            if(currentNode.isAnswer()){
                UI.println(spaceCharacters+"No:" +currentNode.getNo().getText());
            }
            else{
                UI.println(spaceCharacters+"No:" +currentNode.getNo().getText()+"?");
            }
            recursivePrintTree(currentNode.getNo(),spaceCharacters);        
        }
    }

    /**
     * Run the tree by starting at the top (of theTree), and working
     * down the tree until it gets to a leaf node (a node with no children)
     * If the node is a leaf it prints the answer in the node
     * If the node is not a leaf node, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     */
    public void runTree() {
        UI.clearText();
        DTNode currentNode = theTree;
        if(currentNode != null){
            while(currentNode.getYes() != null && currentNode.getNo() != null){ // while it isnt an answer or they havent entered a node
                String answer = UI.askString("Is it true: " + currentNode.getText() + "? (Y/N)");
                if(answer.toUpperCase().contains("Y")){ // if yes
                    currentNode = currentNode.getYes();
                }
                else if(answer.toUpperCase().contains("N")){ // if no
                    currentNode = currentNode.getNo();
                }
                else{
                    UI.println("Please answer yes or no"); // if not use or no tell user to enter yes or no
                }

            }
            UI.println("The answer is: " + currentNode.getText());
        }
    }

    /**
     * Grow the tree by allowing the user to extend the tree.
     * Like runTree, it starts at the top (of theTree), and works its way down the tree
     *  until it finally gets to a leaf node. 
     * If the current node has a question, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     * If the current node is a leaf it prints the decision, and asks if it is right.
     * If it was wrong, it
     *  - asks the user what the decision should have been,
     *  - asks for a question to distinguish the right decision from the wrong one
     *  - changes the text in the node to be the question
     *  - adds two new children (leaf nodes) to the node with the two decisions.
     */
    public void growTree () {
        UI.clearText();
        DTNode currentNode = theTree;
        if(currentNode != null){
            while(currentNode.getYes() != null && currentNode.getNo() != null){ // keeps going till the node does not have children
                String answer = UI.askString("Is it true: " + currentNode.getText() + "? (Y/N)");
                if(answer.toUpperCase().contains("Y")){
                    currentNode = currentNode.getYes(); // update node for loop to keep going but down new path
                }
                else if(answer.toUpperCase().contains("N")){
                    currentNode = currentNode.getNo();
                }
                else{
                    UI.println("Please answer yes or no");
                }

            }
            String answer = ""; // clear the answer so we can reuse variable
            while(!answer.toUpperCase().contains("Y") && ! answer.toUpperCase().contains("N")){ // while havent entered yes or no
                answer = UI.askString("Ok I think I know. Is it a: " + currentNode.getText() + "?");
                if(answer.toUpperCase().contains("Y")){ // break is to exist while loop
                    UI.println("Thanks for playing!");
                    break;
                }
                else if(answer.toUpperCase().contains("N")){
                    answer = UI.askString("Ok what should the answer be?"); // get what it should be
                    UI.println("Ok I cant distingusih a: " + currentNode.getText() + " from a " + answer); // state what cant tell difference between
                    String property = UI.askString("What is something that is true for a " + answer + " and not a " + currentNode.getText() + "?"); // get property
                    currentNode.setChildren(new DTNode(answer),new DTNode(currentNode.getText())); // set children
                    currentNode.setText(property); // then update text ^ needs to happen after else it will clear the info holding child name
                    UI.println("Thankyou I have updated my decision tree!");
                    break;
                }
                else{
                    UI.println("Please answer yes or no"); // if didnt enter yes or no             
                }
            }

        }
    }
    // You will need to define methods for the Completion and Challenge parts.
    public void drawTree(){
        UI.clearGraphics(); // clear the graphics pane
        int rectWidth = 100;
        int rectHeight = 30;
        drawTreeRecursive(theTree,500,20,rectWidth,rectHeight,1.8);
    }

    public void drawTreeRecursive(DTNode currentNode,float x, float y,int rectWidth,int rectHeight,double multipler){
        // This is a mess todd clean up
        UI.drawRect(x, y, rectWidth, rectHeight);
        UI.drawString(currentNode.getText(),x,y+rectHeight/2);
        if(currentNode.getYes() != null){
            UI.drawLine(x + rectWidth/2,y + rectHeight,x+150*multipler + rectWidth/2,y+200);
            drawTreeRecursive(currentNode.getYes(),(int) (x+ 150 * multipler),y+200,rectWidth,rectHeight,1);
        }
        if(currentNode.getNo() != null){         
            UI.drawLine(x + rectWidth/2,y + rectHeight,x-150*multipler + rectWidth/2,y+200);
            drawTreeRecursive(currentNode.getNo(),(int) (x-150*multipler),y+200,rectWidth,rectHeight,1);        
        }
    }

    public void saveTree(){
        File path = new File(UIFileChooser.save()); // get path (techniclaly a file)
        BufferedWriter writer; // new writer
        try{
            writer = new BufferedWriter(new FileWriter(path)); // initalize writer to be writing on the file prechoosen
            saveTreeRecursive(theTree, writer); // run the recursive
            writer.close(); // close the writer (Actually writes to the file)
        }             
        catch(IOException e){UI.println("File Saving failed: " + e);}
    }

    public void saveTreeRecursive(DTNode currentNode,BufferedWriter writer){
        try{
            if(currentNode.isAnswer()){ // if it is an answer
                writer.write("Answer: " + currentNode.getText());
                writer.newLine(); // drop a line
                return; // quit out
            }
            else{
                writer.write("Question: " + currentNode.getText());
                writer.newLine();
                saveTreeRecursive(currentNode.getYes(), writer); // recall yes (yes first for saving to work with reading)
                saveTreeRecursive(currentNode.getNo(), writer);  // recall no
            }
        }
        catch(IOException e){UI.println("File Saving failed: " + e);}
    }

    // Written for you
    /** 
     * Loads a decision tree from a file.
     * Each line starts with either "Question:" or "Answer:" and is followed by the text
     * Calls a recursive method to load the tree and return the root node,
     *  and assigns this node to theTree.
     */
    public void loadTree (String filename) { 
        if (!Files.exists(Path.of(filename))){
            UI.println("No such file: "+filename);
            return;
        }
        try{theTree = loadSubTree(new ArrayDeque<String>(Files.readAllLines(Path.of(filename))));}
        catch(IOException e){UI.println("File reading failed: " + e);}
    }

    /**
     * Loads a tree (or subtree) from a Scanner and returns the root.
     * The first line has the text for the root node of the tree (or subtree)
     * It should make the node, and 
     *   if the first line starts with "Question:", it loads two subtrees (yes, and no)
     *    from the scanner and add them as the  children of the node,
     * Finally, it should return the  node.
     */
    public DTNode loadSubTree(Queue<String> lines){
        Scanner line = new Scanner(lines.poll());
        String type = line.next();
        String text = line.nextLine().trim();
        DTNode node = new DTNode(text);
        if (type.equals("Question:")){
            DTNode yesCh = loadSubTree(lines);
            DTNode noCh = loadSubTree(lines);
            node.setChildren(yesCh, noCh);
        }
        return node;

    }

}
