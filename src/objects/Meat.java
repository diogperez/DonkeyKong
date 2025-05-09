package objects;

import pt.iscte.poo.utils.Point2D;

public class Meat extends Consumable implements TickRelated{

	private static final int BOOST = 100;
	private static final int NEGATIVE_BOOST = 10;
	private boolean wasConsumed = false;
	private int tickCounter = 0;
	
	public Meat(Point2D position) {
		super("GoodMeat", position, BOOST);
	}
	
	@Override
	public void update() {
		if(!wasConsumed) {
			if(tickCounter >= 5) {
				setName("BadMeat");
				setEffect(NEGATIVE_BOOST);
			} else {		
				tickCounter++;
			}
		}
	}
	
	public void consume() {
		wasConsumed = true;
	}

}
