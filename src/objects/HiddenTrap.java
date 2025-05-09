package objects;

import pt.iscte.poo.utils.Point2D;

public class HiddenTrap extends Trap {

	private boolean activate = false;
	
	public HiddenTrap(Point2D position) {
		super(position);
		this.setName("Wall");
	}
	
	public void activate(Boolean bool) {
		activate = bool;
	}
	
	public void updateHiddenTrap() {
		if(activate) {
			setName("Trap");
		}
	}

}
