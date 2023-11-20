package model;

import java.io.Serializable;   
import java.util.Arrays;
import java.util.Iterator;

/**
 * A mutable array for the total Players.
 * This list is our data structure for the
 * players catalog. 
 *  */

public class MyList<T extends Comparable<T>> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object[] array;
	private int size;
	
	
	public MyList() {
		/*The array of Players */
		
		this.array = new Object[10];
		
		/*The size of the list */
		this.size=0;
	}
	
	/**
	 * this constructor will be used for HoF,the most recent games
	 * and the best games for a player
	 * @param the initial capacity of the list
	 */
	public MyList(int capacity) {
		/*instantiates a new array and initializes the capacity of it*/
		this.array = new Object[capacity];
		
		/*initialize the size of the array*/
		this.size=0;
	}
	
	/**
	 * method that adds an objects inside the list
	 * also it can be expandable,it will be used for example
	 * to make the PlayersCatalogue
	 * @param player
	 */
	public void addExpandable(T player) {
		//checks if the array is full
		if(size == array.length) {
			Object[] newArray = new Object[array.length*2];
			//creates a new array with double capacity
			for(int i=0; i<this.array.length;i++) {
				newArray[i] = this.array[i];
				/*copies all the data from the initial array
				  to the new one*/
			}
			//the "old" reference points to the new one which has double capacity
			this.array = newArray ;
		}
		
		this.array[size++]=player;//We add the player to the available position
								  //increase the size meter		
	}
	
	/**
	 * method that adds an object inside the list.
	 * it can't be growable,also it puts the objects
	 * in hierarhical order, it will be used to make the 
	 * Hall of Fame , the last best games of a player
	 * @param T item
	 */
	public void addStatic(T item) {
		for (int i = 0; i < this.capacity(); i++) {
			T current = this.get(i);
			int newSize=0;
			if (current == null) {
				this.array[i] = item;
				size++;
				return;	
			} 
			else {
				if (item.compareTo(current) < 0) { //if item>this
					// item should be placed here...
					
					// starting from the end, move each element one position down
					for (int j=this.capacity()-1; j>i;j--) {
						array[j]=array[j-1];						
						if (array[j]!=null && newSize==0) { // update newSize if it was not updated....
							newSize=j+1;
						}
					}
					//put the item in the i position;
					array[i]=item;
					size= newSize;
					return;
				}
			}		
		}
		// System.out.println("Item not added");
	}	

	public int getSize() {
		return this.size;
	}
	
	public int capacity() {
		return array.length;
	}

	/**
	 * This method returns the player of the given position
	 * @param position index
	 * @return the item in the specific position
	 */
	@SuppressWarnings("unchecked")
	public T get(int i) {
		checkAccessBounds(i);
		return (T)this.array[i]; 
	}
	
	
	/**
	 * Check if key belongs to the list
	 * @param  T key
	 * @return true if the object is inside the list,false if not
	 */
	public boolean contains(T key) {
		for(int i=0; i<array.length; i++)
			if(array[i] == key)
				return true;
		return false;
	}
	
	public boolean isEmpty() {
		return this.getSize() == 0;
	}
	
	/**
	 * Method to return an array with all the players.
	 * Get as parameter the type of the array. 
	 * @param T[] type.The type of the array which will be returned
	 * @return T[]
	 *  */
	@SuppressWarnings("unchecked")
	public T[] toArray(T[] type) {
		return (T[]) Arrays.copyOf(this.array,size,type.getClass());
		//return this.array;
	}
	
	//checks if the requested position is in bounds
	private void checkAccessBounds(int i) {
		if (i < 0 || i > this.capacity())
			throw new ArrayIndexOutOfBoundsException(i + " is not a valid index");
	}
	
	/**
	 * Clears the list.
	 *  */
	public void clear() {
		this.array = new Object[array.length];
		this.size = 0;
	}
	
	/**
	 * removes an object at a specific place inside the list
	 * then left shifts the rest of the objects to fill the gap
	 * @param the position i of the list
	 * @return returns the object that removed
	 */
	public T remove(int i) {
		checkAccessBounds(i);
		T dead = this.get(i);
		for (int j = i; j < this.size; j++) {
			// System.out.println("j= " + j);
			if (j+1 < this.capacity()) {
				array[j]= array[j+1];
			}else {
				array[j]=null;
			}
			
		}
		size--;
		return dead;
	}
		
	/**
	 * This method sorts the array in reverse
	 * this will help us because we want to take the 10 better
	 * players based the score each one has
	 * so it will be easier to have it sorted in reverse
	 */
	/*public void reverseSort() {
		T temp=null;
		
		for(int i=0; i<this.size;i++) {
			for(int j=0;j<this.size;j++) {
				if(array[i].getScore()>array[j].getScore()) {
					temp=array[i];
					array[i]=array[j];
					array[j]=temp;				
				}
				else {
					continue;
				}
			}
		}
	}*/
	
}
