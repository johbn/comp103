// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2020T2, Assignment 3
 * Name:
 * Username:
 * ID:
 */

/** A Item is an object that has a pair of values.
 * Items can be printed, compared and used in hashed collections.
*/

public class Item implements Comparable<Item>{
    private String iden;
    private int num;

    public Item(String s){
        if(s==null||s.length()==0){
            iden = "-";
        }
        else{
            iden = s.toUpperCase();
        }
        num = iden.hashCode();
    }

    public boolean equals(Object other){
        if (other==null){return false;}
        if (! (other instanceof Item)) {return false;}
        Item otherItem = (Item)other;
        return this.iden.equals(otherItem.iden);
    }

    public int compareTo(Item other){
        return this.iden.compareTo(other.iden);
    }

    public int hashCode(){
        return iden.hashCode()*this.num;
    }

    /** toString returns iden */
    public String toString(){
        return iden;
    }


}
