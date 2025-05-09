package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Princess extends Character {
	
	private boolean objective;
	
	public Princess(Point2D position) {
		super("Princess", position, false);
		this.objective = false;
	}
	
	public boolean getObjective() {
		return objective;
	}
	
	public void setObjective(boolean bool) {
		objective = bool;
	}
	
	@Override
	public void move(Direction direction) {	
	}

	@Override
	public void interact(GameElement element) {
	}
}
