/**
 * @(#)GameItem.java
 *
 *
 * @author
 * @version 1.00 2010/5/21
 */
package cantro.entity;

public class GameItem implements Comparable<Object>{

	//Has an ID, a price, and a name.
	public int ID;
	public int price;
	public String name;
	public int counter;

    public GameItem(int i, String n, int p) {
    	ID = i;
    	name = n;
    	price = p;
    	counter = 0;
    }

    public GameItem(int i, String n, int p, int c) {
    	ID = i;
    	name = n;
    	price = p;
    	counter = c;
    }

    //So the arraylist can be sorted through!
    public int compareTo(Object o)
    {
    	GameItem i = (GameItem)o;
    	return i.ID - ID;
    }
}