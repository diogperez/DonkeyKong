package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile {
	
	private String name;
	private Point2D position;
	private final int layer;
	private Room currentRoom;
	
	public GameElement(String name, Point2D position, int layer) {
		this.name = name;
		this.position = position;
		this.layer = layer;
		this.currentRoom = GameEngine.getInstance().getCurrentRoom();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public int getLayer() {
		return layer;
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public int getX() {
		return position.getX();  
	}
	
	public int getY() {
		return position.getY();
	}
	
	public void setPosition(Point2D newPosition) {  
		position = newPosition;
	}
	
	public boolean samePosition(GameElement e) {  
		return position.equals(e.getPosition());
	}
	
	public boolean isAbove(GameElement e) {
		return position.isAbove(e.getPosition());   
	}
	
	public String toString() {
		return name + ";" + position + ";" + layer;
	}
}
