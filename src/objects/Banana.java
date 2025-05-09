package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

import java.util.List;

import pt.iscte.poo.gui.*;


public class Banana extends Consumable implements Interactable, Movable { 
	
	private static final int DAMAGE = 10; //Banana por default tira 10 de vida
	
	public Banana(Point2D position) {
		super("Banana", position, DAMAGE);
	}


	@Override
	public void move(Direction direction) {
		Point2D newPosition = getPosition().plus(Direction.DOWN.asVector());
			
		//Remove a banana do jogo caso saia da GUI
		if(newPosition.getY() >= 10) {
			getCurrentRoom().removeElementFromGame(this);
			return;
		}
				
		List<GameElement> elements = getCurrentRoom().getGameElements();
		for(int i = 0; i < elements.size(); i++) {
			GameElement e = elements.get(i);
			if(e instanceof JumpMan && e.getPosition().equals(newPosition)) {
				interact(e);
				getCurrentRoom().removeElementFromGame(this); //Remove a banana apos a interacao com o JumpMan
				return;
 			}		
		}
				
		//Caso contrario, banana move-se para baixo
		setPosition(newPosition);
			
	}
	
	
	@Override
	public void interact(GameElement element) {
		if(element instanceof JumpMan) {
			JumpMan jm = (JumpMan) element;
			jm.decreaseHealth(DAMAGE);
			if(jm.getHealth() > 0) ImageGUI.getInstance().setStatusMessage("JumpMan was hit by a banana! " +  + jm.getHealth() + "/" + jm.getInitialHealth());
			ImageGUI.getInstance().removeImage(this);
		}
	}

}
