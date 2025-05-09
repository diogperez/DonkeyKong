package objects;

import java.util.List;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Hammer extends Consumable implements Interactable, Movable {

	private static final int DAMAGE = 100;
	
	public Hammer(Point2D position) {
		super("Hammer", position, DAMAGE);
	}
	
	@Override
	public void move(Direction direction) {
		Point2D newPosition = getPosition().plus(Direction.UP.asVector());
		
		if(newPosition.getY() < 0) {
			getCurrentRoom().removeElementFromGame(this);
			return;
		}
		
		List<GameElement> elements = getCurrentRoom().getGameElements();
		for(int i = 0; i < elements.size(); i++) {
			GameElement e = elements.get(i);
			if(e instanceof DonkeyKong && e.getPosition().equals(newPosition)) {
				interact(e);
				getCurrentRoom().removeElementFromGame(this); 
				return;
 			}		
		}
		
		setPosition(newPosition);
		
	}

	@Override
	public void interact(GameElement element) {
		if(element instanceof DonkeyKong) {
			DonkeyKong dk = (DonkeyKong) element;
			dk.decreaseHealth(dk.getHealth());
			getCurrentRoom().removeElementFromGame(this); //Remove-se quando atinge o DonkeyKong
		}
		
	}

}
