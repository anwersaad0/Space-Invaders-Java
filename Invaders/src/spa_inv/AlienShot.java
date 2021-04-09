package spa_inv;

public class AlienShot extends Sprite {
	
	private final int SHOT_SPEED = 6;
	private final int GROUND_LEVEL = 260;
	
	public AlienShot(int x, int y) {
		super(x, y);
		initShot();
	}
	
	private void initShot() {
		loadImg("src/resources/AlienShot.png");
		getSprDimensions();
	}
	
	public void move() {
		y += SHOT_SPEED;
		
		if (y >= GROUND_LEVEL - 8) {
			seen = false;
		}
	}

}