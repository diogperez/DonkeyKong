package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.*;
import objects.Character;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private JumpMan jumpMan;
	private Point2D jumpManInitialPosition;
	private Princess princess;
	private List<GameElement> elements; //Todos os elementos do jogo
	private List<GameElement> movingElements;  //Todos os elementos que se movem (implementam a interface Movable)
	private List<GameElement> items; //Todos os elementos que dependem dos ticks (implementam a interface TickRelated)
	private List<Point2D> wrongChars; //Lista para armazenar todas as posicoes do caracteres que estao no ficheiro mas que nao sao do jogo
	private String nextRoom; //Para armazenar a proxima room
	private int roomLevel;
	
	public Room() {
		elements = new ArrayList<>();
		movingElements = new ArrayList<>();
		items = new ArrayList<>();
		wrongChars = new ArrayList<>();
	}
	
	public JumpMan getJumpMan() {
		return jumpMan;
	}
	
	public Princess getPrincess() {
		return princess;
	}
	
	public List<GameElement> getGameElements() {
		return elements;
	}
	
	public List<GameElement> getMovingElements() {
		return movingElements;
	}
	
	public List<GameElement> getItems() {
		return items;
	}
	
	public int getRoomLevel() {
		return roomLevel;
	}
	
	public void load(int level) { 
		roomLevel = level;
		try (Scanner sc = new Scanner(new File("rooms/room" + level + ".txt"))){
			
			int fileLines = 10; //Linhas do ficheiro txt
			
			if(sc.hasNextLine()) {
				String aux = sc.nextLine();
				if(aux.startsWith("#")) {
					String[] parts = aux.split(";");
					nextRoom = parts[1];
				} else {
					nextRoom = null; //Nao ha next room, logo current room = room2
					generateRoomLine(aux, 0);
				}
			}
			
			int j = 0;
			if(nextRoom == null) {  //Se room = null significa que a primeira linha da room2 ja foi adicionada
				j = 1;
				fileLines--;
			}
		
			while(sc.hasNextLine()) {
				generateRoomLine(sc.nextLine(), j++);
				fileLines--;
			}
			
			if(fileLines != 0) {
				//Lanca a excepcao terminando a interface grafica e o programa
				ImageGUI.getInstance().dispose();
				throw new IllegalArgumentException("Ficheiro incompleto"); //Se o ficheiro tem linhas a mais ou a menos lanca excepcao
			}
		
			ImageGUI.getInstance().addImages(elements);
			ImageGUI.getInstance().update();
			
			
		} catch (FileNotFoundException e) {
			System.err.println("Ficheiro nao encontrado!\n" + 
                    		   "Por favor introduza o nome do ficheiro que pretende utilizar!\n" + 
                               "Por exemplo: room1.txt");
			Scanner consoleInput = new Scanner(System.in); //Permite escrever na consola
			String fileName = consoleInput.nextLine();
			consoleInput.close();
			int newLevel = Integer.parseInt(String.valueOf(fileName.charAt(4)));//Retira o level para se poder usar no load
			load(newLevel);
		}
		
		for(Point2D position: wrongChars) {
			System.err.println("Caractere invalido na posicao " + position + ". Foi substituido por Floor");
		}
		
	}
	
	
	//Metodo auxiliar para gerar uma linha da room com base num indice
	private void generateRoomLine(String line, int j) {
		/* E incrementado um caractere especial, &, para ser lido no default case e ser adicionado na lista 
		 de caracteres errados para posteriormente informar o utilizador da posicao destes caracteres */
		int expected = 10;
		char c = '&';
		if(line.length() < expected) {
			while(line.length() < expected) {
				line += c;
			}
		}
		
		for(int i = 0; i < line.length(); i++) {
			char element = line.charAt(i);
			Point2D position = new Point2D(i,j);
			
			switch(element) {
			case 'W': 
				elements.add(new Wall(position));
				break;
			case 't': 
				elements.add(new Trap(position));
				break;
			case 'S': 
				elements.add(new Stairs(position));
				break;
			case '0': 
				elements.add(new Door(position));
				break;
			case 'H':
				jumpMan = new JumpMan(position);			
				jumpManInitialPosition = position;
				elements.add(jumpMan);
				elements.add(new Floor(position)); //Para nao ficar espaco em branco
				break;
			case 'G': 
				DonkeyKong dk = new DonkeyKong(position);
				elements.add(dk);
				movingElements.add(dk);
				elements.add(new Floor(position)); 
				break;
			case 'P':
				princess = new Princess(position);
				elements.add(princess);
				elements.add(new Floor(position)); 
				break;
			case 'm': 
				Meat meat = new Meat(position);
				elements.add(meat);
				items.add(meat);
				elements.add(new Floor(position)); 
				break;
			case 's': 
				elements.add(new Sword(position));
				elements.add(new Floor(position)); 
				break;
			case 'B':
				Bat bat = new Bat(position);
				elements.add(bat);
				movingElements.add(bat);
				elements.add(new Floor(position)); 
				break;
			case 'h':
				elements.add(new HiddenTrap(position));
				break;
			case 'b':
				Bomb bomb = new Bomb(position);
				elements.add(bomb);
				items.add(bomb);
				elements.add(new Floor(position));
				break;
			case ' ':
				elements.add(new Floor(position));
				break;
			//Preenche com floor todos os outros characteres que nao sao do jogo, e adiciona-os a lista wrongchars
			default:
				elements.add(new Floor(position));
				wrongChars.add(position); 
				break;
			}
		}
	}
	
	
	public void moveJumpMan(int key) { 
		Direction d = Direction.directionFor(key);
		if(d == null) return; 
		jumpMan.move(d);
	}
	
	
	//Metodo para verificar a gravidade de todos os characters
	public void checkGravity() {
		for(GameElement e : elements) {
			if (e instanceof Character) {
				((Character) e).applyGravity();
			}
		}
	}
	
	
	//Metodo para fazer mover todos os elementos que implementam a interface Movable, com excepcao do jump man
	public void moveElements() {	
		for(int i = 0; i < movingElements.size(); i++) {
			GameElement e = movingElements.get(i);
			if(e instanceof Movable) {
				((Movable) e).move(null);
			}	
		}
	}
	
	
	public void updateItems() {
		for(int i = 0; i < items.size(); i++) {
			GameElement e = items.get(i);
			if(e instanceof TickRelated) {
				((TickRelated) e).update();
			}
		}
	}	
	
	
	public void addElementToGame(GameElement element) {
		elements.add(element);
		if(element instanceof Movable && !(element instanceof JumpMan)) {
			movingElements.add(element);
		}
		ImageGUI.getInstance().addImage(element);
	}
	
	
	public void removeElementFromGame(GameElement element) {
		elements.remove(element);			
		movingElements.remove(element);
		ImageGUI.getInstance().removeImage(element);
	}
		
	
	public void respawnJumpMan() {
		removeElementFromGame(jumpMan);
		jumpMan = new JumpMan(jumpManInitialPosition);
		jumpMan.setHealth(100);
		addElementToGame(jumpMan);
		ImageGUI.getInstance().setStatusMessage("JumpMan respawned! Lives remaining: " + GameEngine.getInstance().getLives());
	}
	
	
	public void generateNextLevel() {
		//Guarda atributos do atual jump man
		int health = jumpMan.getHealth();
		int attack = jumpMan.getAttack();
		Bomb bomb = jumpMan.getBomb();
		Boolean b = jumpMan.isBombBeingHeld();
		
		elements.clear();
		movingElements.clear();
		ImageGUI.getInstance().clearImages();	
		
		load(roomLevel + 1);

		//Da overwrite dos atributos do novo jump man com os do antigo
		jumpMan.setHealth(health);
		jumpMan.setAttack(attack);
		jumpMan.setBomb(bomb);
		jumpMan.setBombBeingHeld(b);
	}
	
	
	
	
}