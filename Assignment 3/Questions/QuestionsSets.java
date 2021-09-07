// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 3
 * Name:Todd Wellwood
 * Username: wellwotodd
 * ID:300529406
 */

//DO NOT CHANGE THESE IMPORTS OR ADD ANY FURTHER IMPORTS.
import ecs100.UI;   
import java.util.*;

/**
 * QuestionsSets: 
 * Automarked questions about Sets
 */

public class QuestionsSets {

    /**
     * setDifference returns the Things that are in one set
     *  but not in another set.
     * It is passed two Set of Thing.
     * It should return a new Set of Thing that contains every
     * Thing that is in the first set but not in the second set.
     * For example
     *    setDifference({a,b,c,d,e}, {b,d,f,g})
     *  should return the set
     *    {a,c,e}
     * If either set is null, then setDifference should treat the set
     *  as if it were the empty set.
     * setDifference</tt> should not modify either set.
     */
    public Set<Thing> setDifference(Set<Thing> set1, Set<Thing> set2){
       
        Set <Thing> returningSet = new HashSet <Thing>();
        if(set1 == null){return returningSet;} //treat as thou an empty set. (I emailed so this better not fail lol);
        else if(set2 == null){return set1;} // return set 1 if previous line didnt trigger
   
        returningSet.addAll(set1);
        returningSet.addAll(set2);
        returningSet.removeAll(set2); //add both remove all from set two (Hashsets remove duplicates)
        return returningSet;   // You should remove this line - it is just to make the template compile!
    }

    /**
     * inTwoOf returns the Things that are in exactly two of the sets it is given.
     * It is passed a List of Sets of Things.
     * It should return a new Set of Things that contains every
     *  Thing that is in exactly two of the sets
     * For example
     *     inTwoOf([{a,b,c}, {b,c,d}, {c,d}])
     *   should return the set
     *     {b,d}
     * If the List is null or any of the values in the list are null,
     *  then inTwoOf should return null
     * inTwoOf should not modify the list or any of the sets in the list.
     */
    public Set<Thing> inTwoOf(List<Set<Thing>> sets){
        Map <Thing,Integer> map = new HashMap <Thing,Integer>();
        Set <Thing> returningSet = new HashSet<Thing>();
        if(sets == null){
            return null;
        }        
        for(Set <Thing> currentSet : sets){
            if(currentSet == null){return null;}
            for (Thing currentThing : currentSet){
                if(currentThing == null){return null;}
                if(map.containsKey(currentThing)){map.put(currentThing,map.get(currentThing)+1);} // increment how many times it is seen
                else{map.put(currentThing,1);} // else add it
            }
        }
        for(Map.Entry <Thing,Integer> currentEntry: map.entrySet()){
            if(currentEntry.getValue() == 2){ // if only two times
                returningSet.add(currentEntry.getKey()); // add it to returning
            }
        }

        return returningSet;   // You should remove this line - it is just to make the template compile!
    }


    // Part (c)
    // Modify the Storm class so that the testStorm() method works correctly
    // (testStorm puts Storm objects into a HashMap<Storm>)


    //===========================================================
    // For your testing.

    public static void main(String[] args){
        new QuestionsSets().setupGUI();
    }

    public void setupGUI(){
        UI.addButton("setDifference", this::testSetDifference);
        UI.addButton("inTwoOf", this::testInTwoOf);
        UI.addButton("test Storm",        this::testStorm);

        UI.addButton("quit", UI::quit);
        UI.setDivider(1.0);
    }

    /** Test the setDifference method */
    public void testSetDifference(){
        UI.clearText();
        Set<Thing> set1 = askSetThing("Letters for first set");
        Set<Thing> set2 = askSetThing("Letters for secnd set");
        UI.println("setDifference(set1,set2): " + setDifference(set1, set2));
        UI.println("setDifference(null,set2): " + setDifference(null, set2));
        UI.println("setDifference(set1,null): " + setDifference(set1, null));
    }

    /** Test the inTwoOf method */
    public void testInTwoOf(){
        UI.clearText();
        List<Set<Thing>> list = askListSetThing("Sets of letters");
        List<Set<Thing>> listNull = new ArrayList<Set<Thing>>();
        listNull.add(list.get(0));
        listNull.add(null);
        UI.println("inTwoOf(list): " + inTwoOf(list));
        UI.println("inTwoOf(null): " + inTwoOf(null));
        UI.println("inTwoOf(list-with-null-element): " + inTwoOf(listNull));
    }

    /** Test the modifications to the Storm class */
    public void testStorm(){
        UI.clearText();
        Set<Storm> mySet = new HashSet<Storm>();
        mySet.add(new Storm("Charley", 2004, 1, "August/14", "North-Carolina"));
        mySet.add(new Storm("Charles", 2004, 1, "August/14", "North-Carolina"));
        mySet.add(new Storm("Charley", 2005, 1, "August/14", "North-Carolina"));
        mySet.add(new Storm("Charley", 2004, 2, "August/14", "North-Carolina"));
        mySet.add(new Storm("Charley", 2004, 1, "August/15", "North-Carolina"));
        mySet.add(new Storm("Charley", 2004, 1, "August/14", "South-Carolina"));

        Storm storm1 = new Storm("Charley", 2004, 1, "August/14", "North-Carolina");
        if (mySet.contains(storm1)){UI.println("PASS: Found storm1");}
        else                       {UI.println("FAIL: NOT found storm1");}

        Storm storm2 = new Storm("Charley", 2004, 4, "August/13", "Florida");
        if (mySet.contains(storm2)){UI.println("PASS: Found storm2");}
        else                       {UI.println("FAIL: NOT found storm2");}

        Storm storm3 = new Storm("Charley", 1986, 1, "August/17", "North-Carolina");
        if (mySet.contains(storm3)){UI.println("FAIL: FOUND storm3");}
        else                       {UI.println("PASS: Not found storm3");}

        if (mySet.size()<3)        {UI.println("FAIL: Too few Storms added to set");}
        else if (mySet.size()>3)   {UI.println("FAIL: Too many Storms added to set");}
        else                       {UI.println("PASS: Correct number of Storms added to set");}

        UI.println("Done");
    }

    /**
     * Asks user for a string of letters, and constructs
     * a Set of Things, with each letter in the string as the name of a Thing
     */
    public Set<Thing> askSetThing(String prompt){
        String str = UI.askString(prompt);
        Set<Thing> set = new HashSet<Thing>();
        for (int i=0; i<str.length(); i++){
            String nm = str.substring(i, i+1);
            if (!nm.equals(" ")) {set.add(new Thing(nm));}
        }
        return Collections.unmodifiableSet(set);
    }

    /**
     * Asks user for a token, and constructs
     * a Set of Strings consisting of each letter in the token 
     */
    public List<Set<Thing>> askListSetThing(String prompt){
        String str = UI.askString(prompt+" in form  abcde bdgh agde");
        List <Set<Thing>> list = new ArrayList<Set<Thing>>();
        String[] tokens = str.trim().split(" ");
        for(String token : tokens){
            Set<Thing> set = new HashSet<Thing>();
            for (int i=0; i<token.length(); i++){
                String v = token.substring(i, i+1);
                if (!v.equals(" ")) {set.add(new Thing(v));}
            }
            list.add(Collections.unmodifiableSet(set));
        }
        return Collections.unmodifiableList(list);
    }

    //================================================
    // DO NOT CHANGE ANYTHING BELOW THIS LINE!
    // IF YOU CHANGE THIS TO MAKE YOUR CODE COMPILE,
    // THE AUTOMATED MARKING WILL FAIL AND GIVE YOU ZERO

    public void checkCompile(){

        Set<Thing> set = setDifference(new HashSet<Thing>(), new HashSet<Thing>());
        set = inTwoOf(new ArrayList<Set<Thing>>());
    }

}
