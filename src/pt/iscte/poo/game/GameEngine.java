package pt.iscte.poo.game;

import java.awt.event.KeyEvent;

import objects.Princess;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
	
	private static final int LIVES = 3;
	
	private static GameEngine INSTANCE; //Singleton
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private int lives = LIVES; //Numero de vidas do Jump Man
	private String username; //Nome escolhido pelo utilizador
	private Stats stats;
	
	
	private GameEngine() {
		currentRoom = new Room();
		stats = new Stats();
	}
	
	public static GameEngine getInstance() { 
        if (INSTANCE == null)
            INSTANCE = new GameEngine();
        return INSTANCE;
    }
	
	public void start() {
		username = ImageGUI.getInstance().askUser("Escreva o seu nome");
		currentRoom.load(0);  
		ImageGUI.getInstance().update();	
	}
	
	public void end() {
		ImageGUI.getInstance().dispose();
		ImageGUI.getInstance().showMessage("Top 10 times of all time:", stats.updateHighscores(new Player(username, lastTickProcessed)));
		System.exit(0);
	}
	

	@Override
	public void update(Observed source) {
		if(win()) end();
		//Movimento do Jump Man pelo input do user
		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
			//System.out.println("Keypressed " + k);
			if (Direction.isDirection(k)) {
				//System.out.println("Direction: " + Direction.directionFor(k));
				currentRoom.moveJumpMan(k);
			}
			
			if(k == KeyEvent.VK_B && currentRoom.getJumpMan().isBombBeingHeld()) {
				currentRoom.getJumpMan().placeBomb();
			}
			
			if(k == KeyEvent.VK_H && currentRoom.getJumpMan().hasHammer()) {
				currentRoom.getJumpMan().throwHammer();
			}
			
			if(k == KeyEvent.VK_F) {
				currentRoom.getJumpMan().throwFireBall();
			}
		}
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
			currentRoom.checkGravity();   //Verifica a gravidade a cada tick do jogo
			currentRoom.moveElements();   //Move todos os elementos do jogo que se devem mover, com excepcao do jump man
			currentRoom.updateItems();
		}
		ImageGUI.getInstance().update();
	}

	private void processTick() {
		System.out.println("Tic Tac : " + lastTickProcessed);
		lastTickProcessed++;
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int setLives(int lives) {
		return this.lives = lives;
	}
	
	public String getName() {
		return username;
	}

	public void jumpManDied() {
		lives--;
		if(lives > 0) {
			currentRoom.respawnJumpMan();
		} else {
			ImageGUI.getInstance().showMessage("Game Restarted!", "Game as been Restarted!!\n" + "Lives remaining: " + getLives());
			restart();
		}
	}
	
	private void restart() {
		currentRoom.getGameElements().clear();
		currentRoom.getMovingElements().clear();
		currentRoom.getItems().clear();
		ImageGUI.getInstance().clearImages();
		setLives(LIVES);
		currentRoom.load(0);
	}
	
	private boolean win() {
		Princess princess = currentRoom.getPrincess();
		if(princess != null) {
			return currentRoom.getPrincess().getObjective() == true;			
		}
		return false;
	}
}
