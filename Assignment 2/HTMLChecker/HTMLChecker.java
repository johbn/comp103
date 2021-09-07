// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 2
 * Name:Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

//DO NOT CHANGE THESE IMPORTS OR ADD ANY FURTHER IMPORTS.
import ecs100.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Files;

public class HTMLChecker{

    /**
     * Check that a list of opening and closing tags match and nest properly
     * tags is a List of Strings which are the names of the tags, possibly starting with "/".
     * For example  ["html","header", "/header", "body", "div", "/div", "/body", "/html"]
     * Opening tags do not start with "/"; closing tags start with "/".
     * Should use a Stack in the checking process
     */
    public boolean checkTags(List<String> tags) {        
        ArrayList <String> safeToEditList = new ArrayList <String> (); // creates a list that is safe to edit
        for(int i = 0; i < tags.size(); i ++){
            safeToEditList.add(i,tags.get(i)); // populate safe to edit list
        }
        while(!safeToEditList.isEmpty()){ // run for entirety of the list
            String firstTag = safeToEditList.get(0);
            int closingTagLocation = safeToEditList.indexOf("/"+firstTag); // closing tag will be the first tag found that has a slash infront 
            List <String> subListTest = safeToEditList.subList(0,closingTagLocation+1); // create sublist for measuring ammount of tags between opening and closing tags
            
            // if the closing tag doesnt exist or there isnt an even ammount of tags between opening and closing tags then the nesting is wrong/ missing tag
            if(closingTagLocation == -1 || subListTest.size() % 2 != 0){  
                return false; // means the tags are invalid and not html compadible
            }
            else{
                safeToEditList.remove(closingTagLocation); // remove first to stop arrayListShifting
                safeToEditList.remove(0); // remove the tag at the start (opening tag);
            }
        }
        return true;   // if the false return never triggered then the tags are valid
    }
 
    //----------------- TESTING ----------------------
    public void setupGUI(){
        UI.addButton("check", this::testCheckTags);
        UI.addButton("quit", UI::quit);
        UI.setDivider(1.0);
    }

    /**
     * Reads a list of tags from an html file and calls checkTags on the list of tags.
     * There are several html files provided for checking; you may test it on
     * any html file.
     */
    public void testCheckTags(){
        String filename = UIFileChooser.open();
        List<String> tags = readTags(filename);
        if (checkTags(tags)){ UI.println("Tags are balanced:"); }
        else                { UI.println("Tags are NOT balanced:"); }
        UI.println(tags);
    }

    /**
     * Return a list of all the opening and closing html tags in a file.
     * It removes the <, the attributes, and the closing > from the tag
     * Closing tags start with "/"
     * Opening tags do not
     * Uses a Scanner with a "magic" delimiter pattern to pull out the tags.
     * Ignores any text between tags, and any self-closing tags (like <br/>)
     */
    public List<String> readTags(String filename) {
        UI.clearText();
        if (filename==null || !(Files.exists(Path.of(filename)))) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        try {
            Scanner scan = new Scanner(Path.of(filename));
            scan.useDelimiter("(?=[<])|(?<=[>])");
            //            scan.useDelimiter("\\s+|(?=[<>=])|(?<=[>])");
            // Print out all the tokens, one on each line
            while (scan.hasNext()) {
                String token = scan.next();
                if (token.startsWith("<") && !token.endsWith("/>") && !token.startsWith("<!")){
                    tags.add(new Scanner(token.substring(1,token.length()-1)).next());
                }
            }
            scan.close();
            for (String tag : tags){
                UI.println(tag);
            }
            return tags;
        }
        catch (Exception e) { return tags;}
    }

    public static void main(String[] args){
        new HTMLChecker().setupGUI();
    }

    //================================================
    // DO NOT CHANGE ANYTHING BELOW THIS LINE!
    // IF YOU CHANGE THIS TO MAKE YOUR CODE COMPILE,
    // THE AUTOMATED MARKING WILL FAIL AND GIVE YOU ZERO

    /**
     * Does nothing, but compiling with this method ensures that the method
     *  headers have not been changed.
     */
    public void checkCompile(){
        List<String> tags = new ArrayList<String>();
        if (checkTags(tags)){};
    }

}