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
 * QuestionsMaps: 
 * Automarked questions about Maps
 */

public class QuestionsMaps {

    /**
     * lookupAll is passed a Map (from Strings to Things) and a Set of Strings.
     * It should return a new List containing every
     *  Thing that is associated (according to the map) with a string in the set, 
     * If a String in the set is not in the map, lookupAll should not add
     *   a value to the list.
     * If two different Strings are mapped to the same Thing, then
     *  the returned list should contain the Thing twice.
     * For example,
     *   if the map contains the key=value pairs
     *     {"a"=X, "b"=U, "d"=W, "e"=U, "f"=Z}
     *   and the set contains {"a", "b", "c", "e"}
     *   then lookupAllInLists should return the list
     *     [X, U, U],
     * The order of Things in the returned list does not matter.
     * If the map and/or the set are null, lookupAll should return an empty list
     * lookupAll should not modify the map.
     */
    public List<Thing> lookupAll(Map<String,Thing> map, Set<String> set){
        List <Thing> returnList = new ArrayList <Thing>();
        if(map == null || set == null){return returnList;}

        for(String tempString : set){
            if(map.containsKey(tempString)){ // if it exists
                returnList.add(map.get(tempString)); // get it and add it
            }
        }

        return returnList;   // You should remove this line - it is just to make the template compile!
    }

    /**
     * lookupAllInLists is passed a map (from Strings to Lists of Things) and
     *  a Set of Strings.
     * It should return a new List containing every Thing that is
     *  in a List associated with a string in the Set.
     * The returned List should not contain any duplicates.
     * For example,
     *   if the map contains
     *     {"a"=[X, Y, Z], "b"=[U,V], "c"=[V, W, X]}
     *   and the set contains {"a","c"}
     *   then lookupAllInLists should return the list
     *     [X, Y, Z, V, W],
     * If the map and/or the set are null, lookupAllInLists should return an empty list
     * The order of Things in the returned List does not matter.
     * If the map and/or the set are null, lookupAll should return an empty list
     * lookupAllInLists should not modify the map or the set.
     */
    public List<Thing> lookupAllInLists(Map<String,List<Thing>> map, Set<String> set){
        List <Thing> returnList = new ArrayList <Thing>();
        HashSet <Thing> returnHashSet = new HashSet <Thing>();
        if(map == null || set == null){return returnList;}

        for(String tempString : set){
            if(map.containsKey(tempString)){  // if exists
                returnHashSet.addAll(map.get(tempString)); // add all things from that list of things
            }
        }
        returnList.addAll(returnHashSet); // hashset removed duplicates
        return returnList;
    }

    /**
     * transitiveLookup is passed two maps
     *  a map from Strings to Lists of Things
     *  a map from Things to Items)
     *  and a String.
     * It should look up the String in the first map.
     * If there are any associated Things, then it should look up each Thing
     *  in the second map to find associated items.
     * It should return a Set of all the associated Items. 
     * It should look up the String in the first map, and then look up
     * each of the associated Things (if any) in the second map, and return a Set of
     * all the associated Items. 
     * For example,
     *   if map1 contains
     *     {"a"=[X,Y,Z], "b"=[U,V,U], "c"=[V,W,X,Z]}
     *   and map2 contains
     *     {Y=M, U=P, W=Q, Z=N, T=M}
     *   transitiveLookup(map1, map2, "a") should return {M, N}
     *   transitiveLookup(map1, map2, "b") should return {P}
     *   transitiveLookup(map1, map2, "d") should return {}
     * If either of the maps or the string are null, transitiveLookup should return an empty set
     * transitiveLookup should not modify the maps.
     */
    public Set<Item> transitiveLookup(Map<String,List<Thing>> map1, Map<Thing,Item> map2, String string){
        Set <Item> returningSet = new HashSet <Item>();
        if(map1 == null || map2 == null || string == null){return returningSet;}
        if(map1.containsKey(string)){ // if it exists
            List <Thing> thingsToLookup = map1.get(string); // get it as a list
            for(Thing currentThing : thingsToLookup){ // itterate through the list
                if(map2.containsKey(currentThing)){ // if that thing exists in map 2 (as a key)
                    returningSet.add(map2.get(currentThing)); // add the value of the thing
                }
            }
        }
        return returningSet;   // You should remove this line - it is just to make the template compile!
    }

    /**
     * inverseLookup is passed two maps (one from Strings to Things
     * and the other from Things to Items) and an Item.
     * It should return a Set of all the Strings that are keys in the first map
     * associated with Things that are keys in the second map associated with the Item.
     * For example,
     *   if map1 contains
     *     {"a"=X, "b"=U, "c"=V, "d"=U, "e"=Y, "f"=Z}
     *   and map2 contains
     *     {W=M, U=P, X=N, V=Q, Y=N}
     *   inverseLookup(map1, map2, Q) should return [c]
     *   inverseLookup(map1, map2, P) should return [b, d]
     *   inverseLookup(map1, map2, N) should return [a, e]
     * If either of the maps or the Item are null, inverseLookup should return an empty set
     * inverseLookup should not modify the maps.
     */
    public Set<String> inverseLookup(Map<String,Thing> map1, Map<Thing,Item> map2, Item item){
        Set <String> returingSet = new HashSet <String>();
        List <Thing> keySet = new ArrayList <Thing>();
        if(map1 == null || map2 == null || item == null){return returingSet;}

        for(Map.Entry <Thing,Item> currentEntry: map2.entrySet()){ // for everything in map 2
            if(currentEntry.getValue().equals(item)){ // if the value of that thing is the same as the item lookup
                keySet.add(currentEntry.getKey());       // add that key to a set of keys
            }
        }        

        for(Thing currentThing : keySet){ // for all those keys *technically values)
            for(Map.Entry <String,Thing> currentEntry: map1.entrySet()){ // go through map 1
                if(currentThing.equals(currentEntry.getValue())){ // if that thing in keyset matches with value
                    returingSet.add(currentEntry.getKey()); // add its key to returningSet

                }
            }
        }

        return returingSet;   // You should remove this line - it is just to make the template compile!
    }

    /**
     * constructCounts is passed a List of Things which may contain many duplicates.
     * constructCounts should return a Map from Thing to Integer that contains the counts
     * of every distinct Thing in the List.
     * For example,
     *    if the list has [A, B, A, C, A, B, A, D, A]
     *    then constructCounts should return the Map
     *    {A=5, B=2, C=1, D=1}
     * If the List is null, constructCounts should return null
     * constructCounts should not modify the list.
     */
    public Map<Thing, Integer> constructCounts(List<Thing> list){
        Map <Thing,Integer> returningMap = new HashMap <Thing,Integer>();
        if(list == null){return null;}

        for(Thing currentThing : list){ // for all the things
            if(returningMap.containsKey(currentThing)){returningMap.put(currentThing,returningMap.get(currentThing)+1);} // if that thing already exists increase count
            else{returningMap.put(currentThing,1);} // else add it to the map
        }

        return returningMap; // You should remove this line - it is just to make the template compile!
    }

    //===========================================================
    // For your testing.

    private Map<String,Thing> mapST;
    private Map<String,List<Thing>> mapSLT;
    private Map<Thing,Item> mapTI;

    public static void main(String[] args){
        new QuestionsMaps().setupGUI();
    }

    public void setupGUI(){
        UI.addButton("lookupAll",        this::testLookupAll);
        UI.addButton("lookupAllInLists", this::testLookupAllInLists);
        UI.addButton("transitiveLookup", this::testTransitiveLookup);
        UI.addButton("inverseLookup",    this::testInverseLookup);
        UI.addButton("constructCounts",  this::testConstructCounts);

        UI.addButton("Set mapST",
            ()->{mapST = askMapST("Set mapST: String->Thing"); reportMaps();});
        UI.addButton("Set mapSLT",
            ()->{mapSLT= askMapSLT("Set mapSLT: String->List<Thing>"); reportMaps();});
        UI.addButton("Set mapTI",
            ()->{mapTI = askMapTI("Set mapTI: Thing->Item"); reportMaps();});
        UI.addButton("Show maps",        this::reportMaps);

        UI.addButton("quit", UI::quit);
        UI.setDivider(1.0);
        initMapST(); 
        initMapSLT();
        initMapTI();
        reportMaps();
    }

    /** Test the lookupAll method */
    public void testLookupAll(){
        UI.clearText();
        UI.println("mapST is: "+mapST+"\n");
        Set<String> set = askSetString("Letters to look up in mapST");
        UI.println("lookupAll(mapST,set): " + lookupAll(mapST, set));
        UI.println("lookupAll(mapST,null): " + lookupAll(mapST, null));
        UI.println("lookupAll(null,set): " + lookupAll(null, set));

    }

    /** Test the lookupAllInLists method */
    public void testLookupAllInLists(){
        UI.clearText();
        UI.println("mapSLT is: "+mapSLT+"\n");
        Set<String> set = askSetString("Letters to look up in mapSLT");
        UI.println("lookupAllInLists(mapSLT, set): " + lookupAllInLists(mapSLT, set));
        UI.println("lookupAllInLists(mapSLT, null): " + lookupAllInLists(mapSLT, null));
        UI.println("lookupAllInLists(null, set): " + lookupAllInLists(null, set));
    }

    /** Test the transitiveLookup method
     *  Example maps were   a=XYZ b=UVU c=VWXZ  and  Y=M U=P W=Q Z=N T=M
     */
    public void testTransitiveLookup(){
        UI.clearText();
        UI.println("mapSLT is: "+mapSLT);
        UI.println("mapTI  is: "+mapTI+"\n");
        String letter = UI.askToken("Letter to look up in mapSLT and mapTI");
        UI.println("transitiveLookup: " + transitiveLookup(mapSLT, mapTI, letter));
        UI.println("transitiveLookup(null, mapTI,letter): " + transitiveLookup(null, mapTI, letter));
        UI.println("transitiveLookup(mapSLT,null,letter): " + transitiveLookup(mapSLT, null, letter));
        UI.println("transitiveLookup(mapSLT,mapTI,null): " + transitiveLookup(mapSLT, mapTI, null));
    }

    /** Test the inverseLookup method
     *  Example maps were  a=X b=U c=V d=U e=Y f=Z  and  W=M U=P X=N V=Q Y=N
     */
    public void testInverseLookup(){
        UI.clearText();
        UI.println("mapST is: "+mapST);
        UI.println("mapTI is: "+mapTI+"\n");
        Item item = new Item(UI.askToken("Item to look up (backwards) in mapST and mapTI"));
        UI.println("inverseLookup: " + inverseLookup(mapST, mapTI, item));
        UI.println("inverseLookup(null, mapTI,letter): " + inverseLookup(null, mapTI, item));
        UI.println("inverseLookup(mapSLT,null,letter): " + inverseLookup(mapST, null, item));
        UI.println("inverseLookup(mapSLT,mapTI,null):  " + inverseLookup(mapST, mapTI, null));
    }

    /** Test the constructCounts method */
    public void testConstructCounts(){
        UI.clearText();
        List<Thing> list = askListThing("List of things to count: ");
        UI.println("constructCounts: " + constructCounts(list));
        UI.println("constructCounts(null): " + constructCounts(null));
    }

    /**
     * Asks user for a string of letters, and constructs
     * a List of Things, with each letter in the string as the name of a Thing
     */
    public List<Thing> askListThing(String prompt){
        String str = UI.askString(prompt);
        List<Thing> list = new ArrayList<Thing>();
        for (int i=0; i<str.length(); i++){
            String nm = str.substring(i, i+1);
            if (!nm.equals(" ")) {list.add(new Thing(nm));}
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Asks user for a token, and constructs
     * a Set of Strings consisting of each letter in the token 
     */
    public Set<String> askSetString(String prompt){
        String str = UI.askString(prompt);
        Set<String> set = new HashSet<String>();
        for (int i=0; i<str.length(); i++){
            String v = str.substring(i, i+1);
            if (!v.equals(" ")) {set.add(v);}
        }
        return Collections.unmodifiableSet(set);
    }

    /**
     * Asks user for a map from Strings to Things.
     * Reads the key->value pairs in the form   a=x b=y c=z
     * Returns the Map
     */
    public Map<String,Thing> askMapST(String prompt){
        UI.println(prompt + " (in the form:  a=x b=y c=z");
        String str = UI.askString("map:");
        Map<String,Thing> ans = new HashMap<String, Thing>();
        String[] pairs = str.trim().split(" ");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length==2){
                ans.put(kv[0], new Thing(kv[1]));
            }
        }
        return Collections.unmodifiableMap(ans); 
    }

    /**
     * Asks user for a map from Strings to Lists of Things.
     * Reads the key->value pairs in the form   a=xyz b=yu c=uvwxy
     * Returns the Map
     */
    public Map<String,List<Thing>> askMapSLT(String prompt){
        UI.println(prompt + " (in the form: a=xyz b=yu c=uvwxy");
        String str = UI.askString("map:");
        Map<String,List<Thing>> map = new HashMap<String,List<Thing>>();
        String[] pairs = str.trim().split(" ");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            List<Thing> values = new ArrayList<Thing>();
            if (kv.length==2){
                for (int i=0; i<kv[1].length();i++){
                    values.add(new Thing(kv[1].substring(i, i+1)));
                }
                map.put(kv[0], values);
            }
        }
        return Collections.unmodifiableMap(map); 
    }

    /**  
     * Asks user for a map from Things to Items.
     * Reads the key->value pairs in the form   x=m y=n z=p
     * Returns the Map
     */
    public Map<Thing,Item> askMapTI(String prompt){
        UI.println(prompt);
        UI.println("Enter Map<Thing,Item> in the form:  x=m y=n z=p");
        String str = UI.askString("map:");
        Map<Thing,Item> map = new HashMap<Thing,Item>();
        String[] pairs = str.trim().split(" ");
        for (String pair : pairs) {
            String[] kv = pair.split("= ");
            if (kv.length==2){
                map.put(new Thing(kv[0]), new Item(kv[1]));
            }
        }
        return Collections.unmodifiableMap(map); 
    }

    /** Initialise mapST with the map in the comment for lookupAll() */
    public void initMapST(){
        Map<String,Thing> map = new HashMap<String,Thing>();
        map.put("a",new Thing("X"));
        map.put("b", new Thing("U"));
        map.put("d", new Thing("W"));
        map.put("e", new Thing("U"));
        map.put("f", new Thing("Z"));
        mapST =  Collections.unmodifiableMap(map);
    }

    /** Initialise mapSLT with the map in the comment for lookupAllInLists() */
    public void initMapSLT(){
        Map<String,List<Thing>> map = new HashMap<String,List<Thing>>();
        map.put("a", List.of(new Thing("X"), new Thing("Y"), new Thing("Z")));
        map.put("b", List.of(new Thing("U"), new Thing("V")));
        map.put("c", List.of(new Thing("V"), new Thing("W"), new Thing("X")));
        mapSLT = Collections.unmodifiableMap(map);
    }

    /** Initialise mapTI with the map in the comment for transitiveLookup() */
    public void initMapTI(){
        Map<Thing,Item> map = new HashMap<Thing,Item>();
        map.put(new Thing("Y"), new Item("M"));
        map.put(new Thing("U"), new Item("P"));
        map.put(new Thing("W"), new Item("Q"));
        map.put(new Thing("Z"), new Item("N"));
        map.put(new Thing("T"), new Item("M"));
        mapTI =  Collections.unmodifiableMap(map);
    }

    public void reportMaps(){
        UI.clearText();
        UI.println("Current maps:");
        UI.println(" mapST:  "+mapST);
        UI.println(" mapSLT: "+mapSLT);
        UI.println(" mapTI:  "+mapTI);        
    }

    //================================================
    // DO NOT CHANGE ANYTHING BELOW THIS LINE!
    // IF YOU CHANGE THIS TO MAKE YOUR CODE COMPILE,
    // THE AUTOMATED MARKING WILL FAIL AND GIVE YOU ZERO

    public void checkCompile(){
        List<Thing> lT1 = lookupAll(new HashMap<String,Thing>(), new HashSet<String>());
        List<Thing> lT2 = lookupAllInLists(new HashMap<String,List<Thing>>(), new HashSet<String>());
        Set<Item> si = transitiveLookup(new HashMap<String,List<Thing>>(), new HashMap<Thing,Item>(), "");
        Set<String> ss = inverseLookup(new HashMap<String,Thing>(), new HashMap<Thing,Item>(), new Item(""));
        Map<Thing,Integer> m =constructCounts(new ArrayList<Thing>());
    }

}
