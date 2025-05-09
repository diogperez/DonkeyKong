package objects;

import java.util.List;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Bomb extends Consumable implements Interactable, TickRelated{
	
	private boolean wasPlaced = false;
	private boolean wasPicked = false;
	private boolean exploded = false;
	private int tickCounter = 0;
	
	public Bomb(Point2D position) {
		super("Bomb", position, 0);
	}
	
	public boolean wasPlaced() {
		return wasPlaced;
	}
	
	public void place(Boolean bool, Point2D position) {
		wasPlaced = bool;
		if(wasPlaced) {
			setPosition(position);
			getCurrentRoom().addElementToGame(this);
		}
	}
	
	public void pick(Boolean bool) {
		wasPicked = bool;
	}
	
	public void explode() {
		if(exploded) return;
		exploded = true;
		Room room = getCurrentRoom();
		Point2D bombPosition = getPosition();
		List<Point2D> neighbours = bombPosition.getNeighbourhoodPoints();
		neighbours.add(bombPosition);  //Para o caso do jump man ficar na posicao da bomba apos esta ser plantada
		List<GameElement> elements = room.getGameElements();
		
		for(int i = 0; i < neighbours.size(); i++) {
			Point2D position = neighbours.get(i);
			for(int j = 0; j < elements.size(); j++) {
				GameElement e = elements.get(j);
				if(e.getPosition().equals(position)) {
					interact(e);
				}
			}
		}
		room.removeElementFromGame(this); 
	}
	
	@Override
	public void update() {
		if(wasPlaced && wasPicked) {
			if(tickCounter >= 5) {
				explode();
			} else {				
				tickCounter++;
			}
		}
	}
	
	@Override
	public void interact(GameElement element) {
		if( !((element instanceof Stairs) || element instanceof Wall || element instanceof Floor)) {
			if(element instanceof Character) {
				Character character = (Character) element; 
				character.decreaseHealth(character.getHealth()); //Tira toda a vida do character
			} else if (element instanceof Consumable) {
				getCurrentRoom().removeElementFromGame(element);
			}		
		}
	}


}
