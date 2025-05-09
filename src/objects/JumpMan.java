package objects;

import java.util.List;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class JumpMan extends Character {
	
	private Bomb heldBomb;
	private boolean isBombBeingHeld = false;
	private Hammer hammer;
	private boolean hasHammer = true;
	
	public JumpMan(Point2D position) {
		super("JumpMan", position, false);
	}
	
	public Bomb getBomb() {
		return heldBomb;
	}
	
	public void setBomb(Bomb bomb) {
		heldBomb = bomb;
	}
	
	public boolean isBombBeingHeld() {
		return isBombBeingHeld;
	}
	
	public void setBombBeingHeld(Boolean bool) {
		isBombBeingHeld = bool;
	}
	
	public boolean hasHammer() {
		return hasHammer;
	}
	
	public void throwHammer() {
		if(hasHammer) {
			hammer = new Hammer(getPosition());
			getCurrentRoom().addElementToGame(hammer);
			hasHammer = false;
		}
	}
	
	public void throwFireBall() {
		FireBall fb = new FireBall(getPosition());
		getCurrentRoom().addElementToGame(fb);
	}
	
	@Override
	public void decreaseHealth(int value) {
		super.decreaseHealth(value);
		if(getHealth() <= 0) GameEngine.getInstance().jumpManDied();
	}
	
	@Override
	public void move(Direction direction) {
		if(direction == null) return;
		Point2D newPosition = getPosition().plus(direction.asVector());
		if(newPosition.getX() < 0 || newPosition.getX() >= 10 || newPosition.getY() < 0 || newPosition.getY() >= 10) return;
		
		boolean canMove = true;
		boolean onStairs = false;
		boolean aboveStairs = false;
		
		Room room = getCurrentRoom();
		List<GameElement> elements = room.getGameElements();
		for(int i = 0; i < elements.size(); i++) {
			GameElement e = elements.get(i);
			
			if(e.getPosition().equals(newPosition)) {
				
				//Impede o JumpMan de atravessar blocos solidos, ou a bomba caso esta tenha sido plantada
				if(e instanceof Block && ((Block) e).isSolid() || (e instanceof Bomb && ((Bomb) e).wasPlaced())) {
					canMove = false;		
					break;
				} 
				
				interact(e);
				
				//Se for consumable e removido do jogo apos a interacao (com a excepcao da bomba plantada)
				if(e instanceof Consumable && !((e instanceof Bomb) && ((Bomb)e).wasPlaced())) {
					room.removeElementFromGame(e);
				} 
				
				//Se for character, verifica a vida deste apos cada interacao, matando-o quando a sua vida e 0
				if (e instanceof Character) {
					canMove = false; 
					break;
				} 	
			} 
			
			//Para lidar com as traps
			if(e instanceof Trap && (newPosition.isAbove(e.getPosition()) ||
									 newPosition.isOnLeft(e.getPosition()) ||	
									 newPosition.isOnRight(e.getPosition())	)) {
				interact(e);
			}
				
			//Logica do movimento nas escadas
			if((e instanceof Stairs) && e.samePosition(this)) {
				onStairs = true;
			}
			if((e instanceof Stairs) && this.isAbove(e)) {
				aboveStairs = true;
			}
		}
		
		//Regras de movimento 
		if(canMove) {				
			//Movimento horizontal
			if((direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT))) {
				setPosition(newPosition);
						
			//Movimento para subir as escadas
			} else if ((direction.equals(Direction.UP)) && onStairs) {
				setPosition(newPosition);
						
			//Movimento para descer as escadas
			} else if ((direction.equals(Direction.DOWN) && (onStairs || aboveStairs))) {
				setPosition(newPosition);
			}
		}
	}
	
	
	@Override
	public void interact(GameElement element) {
		//Interacao DonkeyKong
		if(element instanceof DonkeyKong) {
			DonkeyKong dk = (DonkeyKong) element;
			dk.decreaseHealth(getAttack());
			if(dk.getHealth() > 0) ImageGUI.getInstance().setStatusMessage("JumpMan attacked DonkeyKong! Life " + dk.getHealth() + "/" + dk.getInitialHealth());
			
		//Interacao com a trap	
		} else if (element instanceof Trap){
			if(((Trap) element) instanceof HiddenTrap){
				HiddenTrap ht = (HiddenTrap) element;
				ht.activate(true);
				ht.updateHiddenTrap();
			}
			decreaseHealth(((Trap) element).getDamage());
			ImageGUI.getInstance().setStatusMessage("JumpMan was hit by a trap! Life " + getHealth() + "/" + getInitialHealth());
		
		//Interacao com a banana	
		} else if (element instanceof Banana) {
			ImageGUI.getInstance().setStatusMessage("JumpMan destroyed a banana!");
			
		//Interacao com a carne
		} else if (element instanceof Meat) {
			Meat meat = (Meat) element;
			meat.consume();
			if(meat.getName().equals("GoodMeat")) increaseHealth(meat.getEffect());
			else if (meat.getName().equals("BadMeat")) decreaseHealth(meat.getEffect());
			ImageGUI.getInstance().setStatusMessage("JumpMan picked up good meat! Life " + getHealth() + "/" + getInitialHealth());
		
		//Interacao com a espada
		} else if (element instanceof Sword) {
			increaseAttack(((Sword) element).getEffect());
			ImageGUI.getInstance().setStatusMessage("JumpMan caught the sword! Damage level has increased by 15 and is now " + getAttack() + "!");
			
		//Interacao com a princesa
		} else if (element instanceof Princess) {
			ImageGUI.getInstance().showMessage("You won!", "You rescued the Princess! Congratulations!");
			((Princess) element).setObjective(true);
			
		//Interacao com o bat (sofre dano)
		} else if (element instanceof Bat) {
			Bat bat = (Bat) element;
			decreaseHealth(bat.getAttack());
			ImageGUI.getInstance().setStatusMessage("JumpMan killed the bat! Life " + getHealth() + "/" + getInitialHealth());
			bat.kill();

		//Interacao com a porta (passa para o proximo nivel)	
		} else if (element instanceof Door) {
			getCurrentRoom().generateNextLevel();
				
		//Interacao para apanhar a bomba	
		} else if (element instanceof Bomb) {
			Bomb bomb = (Bomb) element;
			if(!bomb.wasPlaced()) {
				bomb.pick(true);
				heldBomb = bomb;
				isBombBeingHeld = true;
				ImageGUI.getInstance().setStatusMessage("JumpMan picked up the bomb!");
				getCurrentRoom().removeElementFromGame(bomb);
			}
		}

	}

	public void placeBomb() {
		heldBomb.place(true, getPosition());
		heldBomb = null;
		isBombBeingHeld = false;
	}

	

}
