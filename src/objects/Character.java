package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class Character extends GameElement implements Movable, Interactable {
	
	private int health;
	private int attack; 
	private final boolean canFly;
	
	private static final int HEALTH = 100;
	private static final int ATTACK = 10;
	
	private static final int LAYER = 1;
	
	public Character(String name, Point2D position, Boolean canFly) {
		super(name, position, LAYER);  
		this.health = HEALTH;
		this.attack = ATTACK;
		this.canFly = canFly;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getInitialHealth() {
		return HEALTH;
	}
	
	public void setHealth(int value) {
		health = value;
	}
	
	public void setAttack(int value) {
		attack = value;
	}
	
	public void increaseHealth(int value) {
		if (health == HEALTH) {
			return;
		} else if (health + value > HEALTH) {
			health = HEALTH;
		} else {
			health += value;
		}
	}
	
	public void decreaseHealth(int value) {
		if(health - value < 0) {
			health = 0;
		} else {
			health -= value;			
		}
		
		if (health == 0) kill();
	}
	
	public void increaseAttack(int value) {
		attack += value;
	}
	
	
	public void applyGravity() {
		if(!canFly) {
			boolean hasSupport = false;
			for(GameElement e : getCurrentRoom().getGameElements()) {
				if(this.isAbove(e) && (e instanceof Block && ((Block) e).isSolid() || e.getName().equals("Stairs")) ) {
					hasSupport = true;
					break;
				}
			}
			if(!hasSupport) {
				Point2D newPosition = getPosition().plus(Direction.DOWN.asVector());
				setPosition(newPosition);
			}
		}
	}
	
	
	public void kill() {
		ImageGUI.getInstance().setStatusMessage(getName() + " has been killed!");
		getCurrentRoom().removeElementFromGame(this);
	}
	
	public String toString() {
		return super.toString() + "; health=" + health + "; attack=" + attack; 
	}

}
