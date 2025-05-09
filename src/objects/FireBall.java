package objects;

import java.util.List;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class FireBall extends Consumable implements Interactable, Movable {
	
	private static final int DAMAGE = 50;
	
	public FireBall(Point2D position) {
		super("Fire", position, DAMAGE);
	}


	@Override
	public void move(Direction direction) {
		Point2D newPosition = getPosition().plus(Direction.LEFT.asVector());
		
		if(newPosition.getX() >= 10 || newPosition.getX() <0) {
			getCurrentRoom().removeElementFromGame(this);
			return;
		}
		
		List<GameElement> elements = getCurrentRoom().getGameElements();
		for(int i = 0; i < elements.size(); i++) {
			GameElement e = elements.get(i);
			if(e instanceof DonkeyKong && (e.getPosition().equals(newPosition) || e.getPosition().equals(getPosition()))) {
				interact(e);
				getCurrentRoom().removeElementFromGame(this); //Remove o Fire apos a interacao com o Donkey Kong
				return;
 			}		
		}
		
		setPosition(newPosition);
		
	}

	@Override
	public void interact(GameElement element) {
		DonkeyKong dk = (DonkeyKong) element;
		dk.decreaseHealth(DAMAGE);
		if(dk.getHealth() > 0) ImageGUI.getInstance().setStatusMessage("Donkey Kong was struck by Fire! " +  dk.getHealth() + "/" + dk.getInitialHealth());
		getCurrentRoom().removeElementFromGame(this);
		
	}

}
