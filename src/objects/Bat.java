package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Bat extends Character{
	
	private static final int HEALTH = 10;
	
	public Bat(Point2D position) {
		super("Bat", position, true);
		setHealth(HEALTH);
	}

	@Override
	public void move(Direction direction) {
		Direction d = null;
		Room room = super.getCurrentRoom();
		
		boolean canMove = true;
		
		for(GameElement e : room.getGameElements()){
			if(e instanceof Stairs && this.isAbove(e)) {
				d = Direction.DOWN;
				break;
			}
		}
		
		if(d == null) {
			if(Math.random() > 0.5) d = Direction.LEFT;
			else d = Direction.RIGHT;
		}
		
		Point2D newPosition = getPosition().plus(d.asVector());
		if(newPosition.getX() < 0 || newPosition.getX() >= 10 || newPosition.getY() < 0 || newPosition.getY() >= 10) return;
		
		for(GameElement e : room.getGameElements()) {
			if(e.getPosition().equals(newPosition)) {
				//Impede de atravessar blocos solidos
	            if (e instanceof Block && ((Block) e).isSolid() || (e instanceof Bomb && ((Bomb) e).wasPlaced())) {
	                canMove = false; 
	                break;
	            
	            //Impede de atravessar outros Characters, e caso seja o JumpMan interage com ele    
	            } else if (e instanceof Character) {
	                canMove = false; 
	                if(e instanceof JumpMan) {
	                	interact(e);
	                }
	                break;
	            } 
			}
		}
		
		if(canMove) setPosition(newPosition);
	}

	@Override
	public void interact(GameElement element) {
		if(element instanceof JumpMan) {
			JumpMan jm = (JumpMan) element;
			jm.decreaseHealth(this.getAttack());
			if(jm.getHealth() > 0) ImageGUI.getInstance().setStatusMessage("JumpMan was attacked by Bat! Life " + jm.getHealth() + "/" + jm.getInitialHealth());
			getCurrentRoom().removeElementFromGame(this);
		}
		
	}

}
