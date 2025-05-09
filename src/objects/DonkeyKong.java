package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class DonkeyKong extends Character {
	
	private double baseBananaPercentage = 0.3; //Percentagem de spawn de cada banana no inicio do jogo
	
	public DonkeyKong(Point2D position) {
		super("DonkeyKong", position, false);
	}
	
	@Override
	public void move(Direction direction) {
		Room room = getCurrentRoom();			
		Point2D newPosition = getNewPosition(room, getPosition());
		if(newPosition.getX() < 0 || newPosition.getX() >= 10 || newPosition.getY() < 0 || newPosition.getY() >= 10) return;
		
		
		boolean canMove = true;
		for(GameElement e : room.getGameElements()) {
			if(e.getPosition().equals(newPosition)) {
				//Impede de atravessar blocos solidos
	            if (e instanceof Block && ((Block) e).isSolid() || e instanceof Door || (e instanceof Bomb && ((Bomb) e).wasPlaced())) {
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
		
		double bananaPercentage = baseBananaPercentage + (0.1 * room.getRoomLevel()); //Aumenta o valor do spawn rate consoante o nivel
		spawnBanana(bananaPercentage);
	}
	
	
	@Override
	public void interact(GameElement element) {  
		if(element instanceof JumpMan) {
			JumpMan jm = (JumpMan) element;
			jm.decreaseHealth(getAttack());
			if(jm.getHealth() > 0) ImageGUI.getInstance().setStatusMessage("DonkeyKong attacks JumpMan! Life: " + jm.getHealth() + "/" + jm.getInitialHealth());
		}
	}
	
	//Metodo para gerar uma banana na posicao do DonkeyKong
	private void spawnBanana(double percentage) {
		Room room = super.getCurrentRoom();
		if(Math.random() < percentage) {
			Banana banana = new Banana(getPosition());
			room.getGameElements().add(banana);
			room.getMovingElements().add(banana);
			ImageGUI.getInstance().addImage(banana);
		}
	}
	
	
	private Point2D getNewPosition(Room room, Point2D position) {
		Direction d = null;
		int jumpManX = room.getJumpMan().getX();
		int level = room.getRoomLevel();
		
		double random = Math.random();
		// Nivel 0: movimento aleatorio
		if(level == 0) {
			if(random > 0.5) d = Direction.RIGHT;
			else d = Direction.LEFT;
		
		// Nivel 1: varia entre movimento aleatorio e movimento direcionado ao jump man
		} else if(level == 1) {
			if(random < 0.3) d = Direction.LEFT;
			if(0.3 <= random && random < 0.6) d = Direction.RIGHT;
			if(random >= 0.6) {
				if(position.getX() > jumpManX) {
					d = Direction.LEFT;
				} else {
					d = Direction.RIGHT;
				}
			}
		
		// Nivel 2+: movimento direcionado ao jump man
		} else {
			if(position.getX() > jumpManX) {
				d = Direction.LEFT;
			} else {
				d = Direction.RIGHT;
			}
		}
		
		return position.plus(d.asVector());
	}
}
