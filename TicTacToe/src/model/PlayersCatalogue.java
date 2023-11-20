package model;

import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;



public class PlayersCatalogue implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyList<Player> totalPlayers;
	private Player[] p;
	
		
	/**
	 * Constructs a new array of Players with default 10 size
	 *  */
	public PlayersCatalogue() {
		this.totalPlayers = new MyList<Player>();
		this.p = new Player[1];
	}
	
	
	/**
	 * Returns the player in the given index
	 * @return Player
	 *  */
	public Player getPlayer(int index) {
		return this.totalPlayers.get(index);
	}
	
	//add a player to the catalog
	@SuppressWarnings("unchecked")
	public void addPlayer(Player player) {
		this.totalPlayers.addExpandable(player);
	}
	
	
	/**
	 * Returns an array of players names
	 * @return String[]
	 *  */
	public String[] getPlayerNames() {
		Player[] players = this.totalPlayers.toArray(this.p);
		String[] names = new String [this.totalPlayers.getSize()];
		int counter=0;
		
		for(Player player : players) {
			names[counter++]=player.getName();
		}
		
		return names;
	}
	
	/**
	 * finds the player in list with this name
	 *  @return Player
	 *  */
	public Player findPlayerByName(String name) {
		/*Get an array with all the players, traverse it
		 *until you find a player with this name. 
		 *  */
		Player[] players = this.totalPlayers.toArray(this.p);
		for(Player player : players) {
			if(player.getName().equals(name))
				return player;
		}
		//if you don't find a player with this name, return null.
		return null;
	}
	
	/**
	 * Hall Of Fame method.Takes as parameter an integer n,
	 * and returns a list with the top n players.
	 * @param int size how many players do you want on this list
	 * @return MyList<Player> 
	 *  */
	public MyList<Player> findHallOfFame(int size){
		//check if the size is on bounds.
		if(size<=0 || size>this.totalPlayers.getSize())
			throw new IllegalArgumentException("Input parameter should be between 0-" + this.totalPlayers.getSize());
		
		MyList<Player> topPlayersList = new MyList<>(size);
		
		Player[] players = this.totalPlayers.toArray(this.p);
		/*
		 * The addStatic method will automatically compare the players,
		 * and put in the list only the top 'Size' players.They will be compared
		 * with the compareTo method.
		 *  */
		for(Player pl : players)
			topPlayersList.addStatic(pl);
		
		return topPlayersList;
	}
	
	//method that serialize the data and stores the players inside a file
	public void storePlayers() {	
		ObjectOutputStream os = null;
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream("tuctactoe.ser");//create the file to store the players			
			os = new ObjectOutputStream(fos);
			Player[] players = this.totalPlayers.toArray(this.p);
			

			for(Player player : players) {
				os.writeObject(player);
			}
			System.out.println("All players stored successfully....");
		} 
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());//finds out if something went wrong when the file was created
		}
		catch (IOException e) {			
			e.printStackTrace();//prints the Stack trace to find the path of the problem
		}
		finally {
			try {os.close(); fos.close();}catch (Exception e) {//it is important to close all the streams
			}
		}		
	}
	
	//method that deserialize the byte code from a file
	//Then it stores the player inside the ArrayList o Players
	public void loadPlayers() {
		ObjectInputStream is = null;
		FileInputStream fis = null;
		
		try {
			
			fis = new FileInputStream("tuctactoe.ser");
			is=new ObjectInputStream(fis);
			
			while(fis.available()>0) {
				Player player = (Player)is.readObject();
				this.totalPlayers.addExpandable(player);
				
			}
			
			System.out.println("Loaded "+this.totalPlayers.getSize()+" players");
			System.out.println("*********************************************");
		} 
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());//finds out if something went wrong when we tried to access the file
		}
		catch (IOException e) {
			e.printStackTrace();//prints the path of the problem
		}
		catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		finally {
			try {is.close(); fis.close();}catch (Exception e) {//it is important to close all the streams
			}
		}
	}


	/**********************************************GETTERS & SETTERS************************************************/
	
	public MyList<Player> getTotalPlayers() {
		return totalPlayers;
	}
	
	
}
