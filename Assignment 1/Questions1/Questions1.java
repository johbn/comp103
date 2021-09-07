// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 1
 * Name: Todd Wellwood
 * Username: wellwotodd
 * ID: 
 */

//DO NOT CHANGE THESE IMPORTS OR ADD ANY FURTHER IMPORTS.
import ecs100.UI;   
import java.util.List;
import java.util.ArrayList;

/** Questions1   */
public class Questions1{

    /**
     * union is passed two lists, list1 and list2.
     * It can assume that there are no duplicates in list1 or list2.
     * It should return a new list which contains every Thing that is in
     * list1 or list2 or both, but should not contain any duplicates.
     */
    public List<Thing> union(List<Thing> list1, List<Thing> list2){
        List <Thing> jointList = list1; // set jointList to be list1
        for(int i = 0; i < list2.size(); i++){ // for each element in the second list
            boolean noDuplicate = true; // set to true to start
            for(int j = 0; j < jointList.size(); j++){ // compare against every element currently in the joinlist
                if(jointList.get(j).equals(list2.get(i))){ // if we ever find a duplicate
                    noDuplicate = false; // then set noDuplicate to be false
                }
            }
            if(noDuplicate == true){ // if no duplicate's were found
                jointList.add(list2.get(i)); // then add it to the list
            }
            noDuplicate = true;// reset noDuplicate for next time round
        }
        return(jointList); // return the new list
        // I realise I should have used sets but the videos were not posted at the time of me programming this
    }

    /**
     * everyNth is passed a list of Things, and two integers (start and nth)
     * It returns a new list consisting of every nth thing in the list, begining
     * at item at index start.
     * For example, if myList contains
     *     {a, b, c, d, e, f, g, h}
     *  everyNth(myList, 3, 2)
     *  should return the list
     *     {d, f, h},
     *  since d is the item at index 3, and f and h are every 2nd item after d.
     * If start is not the index of an item in the list, everyNth should return an empty list.
     * If nth is 0 or negative, then everyNth should return a list with only the item at start
     */
    public List<Thing> everyNth(List<Thing> list, int start, int nth){
        List <Thing> finalList = new ArrayList<Thing>(); // initilize arraylist
        if(start > list.size() || start < 0){ // if the index to start isnt inside the array
            return(finalList); // return an empty list
        }
        if(nth > 0){ // check that nth is bigger than 0 / not negative
            for(int i = start; i < list.size(); i += nth){ // get all the values including start then incremented by nth
                finalList.add(list.get(i)); // add to the final array
            } 
            return finalList; //return the final arraylist
        }
        else{ // if the nth was negative or equal 0
            finalList.add(list.get(start)); // then add only the start to the list
            return(finalList); // return that list
        }
    }

    /**
     * Rotates the values in the list n steps to the right
     * by moving each item n steps to the right, and bringing items that
     *  "fall off the end"  to the front of the list.
     * If n is negative, it should rotate the list -n steps to the left
     * For example, if myList contains
     *     {a, b, c, d, e, f, g, h}
     *  rotateRight(myList, 3)
     *  should result in myList being changed to contain
     *     {f, g, h, a, b, c, d, e}
     *
     *  rotateRight(myList, 9)
     *  should result in myList being changed to contain
     *     {h, a, b, c, d, e, f, g}
     *  (8 steps will return the list to its original order)
     *
     *  rotateRight(myList, -2)
     *  should result in myList being changed to contain
     *     {c, d, e, f, g, h, a, b}
     */
    public void rotateRight(List<Thing> list, int n){
        if (n > 0){ // move to the right
            Thing thingStorage; // for storing things
            for(int i = 0; i < n; i++){ // rotate n number of times
                thingStorage = list.get(list.size() - 1); // get the last element
                list.remove(list.size()-1); // remove the final element
                list.add(0,thingStorage); // add the final element at the start of the list
            }
        }
        if (n < 0){ // move to the right
            Thing thingStorage; // for storing things
            for(int i = 0; i > n; i--){ // rotate n number of times
                thingStorage = list.get(0); // get the first element
                list.remove(0); // remove the first element
                list.add(thingStorage); // add the first at the end of the list
            }
        }
    }

    /**
     * Checks whether list2 is a sublist of list1,
     * list2 is a sublist of list1, if you could remove items from the front and/or
     *   end of list1 to make it the same as list2
     * For example,
     *     
     *    hasSublist({a, b, c, d, e, f, g, h}, {c, d, e, f, g}) is true
     *    hasSublist({a, b, c, d, e, f, g, h}, {a, c, e, g}) is not true
     *      - the values in list2 are all in list1, but not in sequence.
     *    hasSublist({a, b, c}, {a, b, c}) is NOT true
     *      - list2 must be smaller than list1
     * An empty list is a sublist of any list (except an empty list).
     */
    public boolean hasSublist(List<Thing> list1, List<Thing> list2){        
        List subListTest;
        if(list1.size() <= list2.size()){ // returns false if list2 is bigger or equal to list 1 (Not possible for sublist to be same size or bigger)
            return false; 
        }
        else if (list2.isEmpty()){ // if an empty sublist
            return true; // then return true as an empty list is a sublist of any list except another empty list
        }
        for(int i = 0; i < list1.size() - list2.size() +2; i++){ // - list2.size to avoid overflow +2 so that the last one is checked as well
            if(i+list2.size() <= list1.size()){ // stops overflow
                subListTest = list1.subList(i,i+list2.size()); // create a sublist thats the same size as list 2 at every point in array
                if(subListTest.equals(list2)){ // compare this sublist with list  2
                    return(true); // if at any point the sublists match then return true as it is a sublist
                }
            }
        }
        return false; // Return false as not a sublist
    }

    //===========================================================
    // For your testing.

    public static void main(String[] args){
        Questions1 q1 = new Questions1();
        q1.setupGUI();
    }

    public void setupGUI(){
        UI.addButton("union", this::testUnion);
        UI.addButton("everyNth", this::testEveryNth);
        UI.addButton("rotateRight", this::testRotateRight);
        UI.addButton("hasSublist", this::testHasSublist);
        UI.addButton("quit", UI::quit);
        UI.setDivider(1.0);
    }

    /** Test the union method */
    public void testUnion(){
        List<Thing> list1 = askThingList();
        UI.println("List1: "+list1);
        List<Thing> list2 = askThingList();
        UI.println("List2: "+list2);
        UI.println("union: " + union(list1, list2));
    }

    /** Test the everyNth method */
    public void testEveryNth(){
        List<Thing> list = askThingList();
        UI.println("List: "+list);
        int start = UI.askInt("Start at:");
        int nth = UI.askInt("nth:");
        UI.println("everyNth: " + everyNth(list, start, nth));
    }

    /** Test the rotateRight method */
    public void testRotateRight(){
        List<Thing> list = askThingList();
        UI.println("List: " + list);
        int steps = UI.askInt("Steps:");
        rotateRight(list, steps);
        UI.println("List: " + list );
    }

    /** Test the hasSublist method */
    public void testHasSublist(){
        List<Thing> list1 = askThingList();
        UI.println("List1: "+list1);
        List<Thing> list2 = askThingList();
        UI.println("List2: "+list2);
        UI.println("hasSublist: " + hasSublist(list1, list2));
    }

    /** Asks user for a string of letters, and constructs
     * a List of Things, with each letter in the string as the name of a Thing
     */
    public List<Thing> askThingList(){
        String str = UI.askString("Enter string of letters:");
        List<Thing> ans = new ArrayList<Thing>();
        for (int i=0; i<str.length(); i++){
            String nm = str.substring(i, i+1);
            if (!nm.equals(" ")) {ans.add(new Thing(nm));}
        }
        return ans;
    }

    //================================================
    // DO NOT CHANGE ANYTHING BELOW THIS LINE!
    // IF YOU CHANGE THIS TO MAKE YOUR CODE COMPILE,
    // THE AUTOMATED MARKING WILL FAIL AND GIVE YOU ZERO

    public void checkCompile(){
        List<Thing> list = new ArrayList<Thing>();
        list = union(list, list);
        list = everyNth(list, 1, 1);
        rotateRight(list, 1);
        if (hasSublist(list, list)){};
    }

}
