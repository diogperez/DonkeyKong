package pt.iscte.poo.game;

public class Player implements Comparable<Player>{
	
	private String name;
	private int time;
	
	public Player(String name, int time) {
		this.name = name;
		this.time = time;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public int compareTo(Player p) {
		return this.getTime() - p.getTime();
	}
	
	public String toString() {
		return name + ":" + time;
	}

}
